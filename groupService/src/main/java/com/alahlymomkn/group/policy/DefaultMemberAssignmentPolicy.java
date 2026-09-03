package com.alahlymomkn.group.policy;

import com.alahlymomkn.common.enums.GroupRole;
import com.alahlymomkn.group.entity.GroupMember;
import com.alahlymomkn.group.repo.GroupMemberRepository;
import org.springframework.stereotype.Component;

@Component
public class DefaultMemberAssignmentPolicy implements RoleAssignmentPolicy {

    @Override
    public boolean supports(GroupRole targetRole) {
        return GroupRole.MEMBER.equals(targetRole);
    }

    @Override
    public void apply(GroupMember requester, GroupMember target, GroupMemberRepository repo) {
        target.getRoles().add(GroupRole.MEMBER);
        repo.save(target);
    }
}
