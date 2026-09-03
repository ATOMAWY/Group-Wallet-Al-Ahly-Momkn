package com.alahlymomkn.group.service;

import com.alahlymomkn.common.enums.GroupRole;
import com.alahlymomkn.common.exceptions.AccessDeniedException;
import com.alahlymomkn.common.exceptions.ResourceNotFoundException;
import com.alahlymomkn.group.entity.Group;
import com.alahlymomkn.group.entity.GroupMember;
import com.alahlymomkn.group.policy.RoleAssignmentPolicy;
import com.alahlymomkn.group.repo.GroupMemberRepository;
import com.alahlymomkn.group.repo.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository memberRepository;
    private final List<RoleAssignmentPolicy> roleAssignmentPolicies;

    @Transactional
    public Group createGroup(String groupName, Long creatorUserId) {
        Group group = groupRepository.save(Group.builder().name(groupName).build());

        GroupMember moderator = GroupMember.builder()
                .groupId(group.getId())
                .userId(creatorUserId)
                .roles(new HashSet<>(Set.of(GroupRole.MODERATOR)))
                .build();
        memberRepository.save(moderator);

        return group;
    }

    @Transactional
    public void assignRole(Long moderatorId, Long groupId, Long targetUserId, GroupRole newRole) {
        GroupMember requester = memberRepository.findByGroupIdAndUserId(groupId, moderatorId)
                .orElseThrow(() -> new AccessDeniedException("Only group moderators can change roles."));

        if (!requester.getRoles().contains(GroupRole.MODERATOR)) {
            throw new AccessDeniedException("Only group moderators can change roles.");
        }

        GroupMember target = memberRepository.findByGroupIdAndUserId(groupId, targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Target member not found in this group."));

        RoleAssignmentPolicy policy = roleAssignmentPolicies.stream()
                .filter(rolePolicy -> rolePolicy.supports(newRole))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No role assignment policy found for role: " + newRole));

        policy.apply(requester, target, memberRepository);
    }

    @Transactional
    public void addMember(Long moderatorId, Long groupId, Long newUserId) {
        GroupMember requester = memberRepository.findByGroupIdAndUserId(groupId, moderatorId)
                .orElseThrow(() -> new AccessDeniedException("Only group moderators can add members."));

        if (!requester.getRoles().contains(GroupRole.MODERATOR)) {
            throw new AccessDeniedException("Only group moderators can add members.");
        }

        if (memberRepository.findByGroupIdAndUserId(groupId, newUserId).isPresent()) {
            return;
        }

        GroupMember newMember = GroupMember.builder()
                .groupId(groupId)
                .userId(newUserId)
                .roles(new HashSet<>(Set.of(GroupRole.MEMBER)))
                .build();

        memberRepository.save(newMember);
    }

    public void validateMemberAccess(Long userId, Long groupId) {
        memberRepository.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new AccessDeniedException("User is not a member of this group."));
    }
}