package com.alahlymomkn.group;

import com.alahlymomkn.common.GroupRole;
import com.alahlymomkn.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository memberRepository;
    private final WalletService walletService; // We will inject this to create the wallet

    @Transactional
    public Group createGroup(String groupName, Long creatorUserId) {
        // 1. Create the Group
        Group group = groupRepository.save(Group.builder().name(groupName).build());

        // 2. Create the Group Wallet (Call the other module!)
        walletService.createGroupWallet(group.getId());

        // 3. Make the creator the MODERATOR
        GroupMember moderator = GroupMember.builder()
                .groupId(group.getId())
                .userId(creatorUserId)
                .role(GroupRole.MODERATOR)
                .build();
        memberRepository.save(moderator);

        return group;
    }

    public void assignRole(Long moderatorId, Long groupId, Long targetUserId, GroupRole newRole) {
        // 1. Check if the person asking is actually the MODERATOR
        GroupMember requester = memberRepository.findByGroupIdAndUserId(groupId, moderatorId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (requester.getRole() != GroupRole.MODERATOR) {
            throw new RuntimeException("Access Denied: Only Moderators can change roles!");
        }

        // 2. Update the target user's role
        GroupMember target = memberRepository.findByGroupIdAndUserId(groupId, targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user is not in this group"));

        target.setRole(newRole);
        memberRepository.save(target);
    }
}