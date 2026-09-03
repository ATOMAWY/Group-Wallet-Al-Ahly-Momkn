package com.alahlymomkn.group.policy;

import com.alahlymomkn.common.enums.GroupRole;
import com.alahlymomkn.group.entity.GroupMember;
import com.alahlymomkn.group.repo.GroupMemberRepository;

public interface RoleAssignmentPolicy {

    boolean supports(GroupRole targetRole);

    void apply(GroupMember requester, GroupMember target, GroupMemberRepository repo);
}
