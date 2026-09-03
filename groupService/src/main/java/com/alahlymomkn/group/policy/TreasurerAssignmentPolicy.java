package com.alahlymomkn.group.policy;

import com.alahlymomkn.common.enums.GroupRole;
import com.alahlymomkn.group.entity.GroupMember;
import com.alahlymomkn.group.repo.GroupMemberRepository;
import org.springframework.stereotype.Component;

@Component
public class TreasurerAssignmentPolicy implements RoleAssignmentPolicy {

    @Override
    public boolean supports(GroupRole targetRole) {
        return GroupRole.TREASURER.equals(targetRole);
    }

    @Override
    public void apply(GroupMember requester, GroupMember target, GroupMemberRepository repo) {
        target.getRoles().clear();
        target.getRoles().add(GroupRole.TREASURER);
        repo.save(target);
    }
}
