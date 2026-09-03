package com.alahlymomkn.group.policy;

import com.alahlymomkn.common.enums.GroupRole;
import com.alahlymomkn.group.entity.GroupMember;
import com.alahlymomkn.group.repo.GroupMemberRepository;
import org.springframework.stereotype.Component;

@Component
public class ModeratorAssignmentPolicy implements RoleAssignmentPolicy {

    @Override
    public boolean supports(GroupRole targetRole) {
        return GroupRole.MODERATOR.equals(targetRole);
    }

    @Override
    public void apply(GroupMember requester, GroupMember target, GroupMemberRepository repo) {
        if (!requester.getUserId().equals(target.getUserId())) {
            requester.getRoles().remove(GroupRole.MODERATOR);
            if (requester.getRoles().isEmpty()) {
                requester.getRoles().add(GroupRole.MEMBER);
            }
            repo.save(requester);
        }

        target.getRoles().add(GroupRole.MODERATOR);
        repo.save(target);
    }
}
