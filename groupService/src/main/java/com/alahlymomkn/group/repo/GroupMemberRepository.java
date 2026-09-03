package com.alahlymomkn.group.repo;

import com.alahlymomkn.common.enums.GroupRole;
import com.alahlymomkn.group.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByGroupIdAndUserId(Long groupId, Long userId);

    @Query("select gm from GroupMember gm join gm.roles r where gm.groupId = :groupId and r = :role")
    Optional<GroupMember> findByGroupIdAndRole(@Param("groupId") Long groupId, @Param("role") GroupRole role);

    @Query("select gm from GroupMember gm join gm.roles r where gm.groupId = :groupId and gm.userId = :userId and r = :role")
    Optional<GroupMember> findByGroupIdAndUserIdAndRole(@Param("groupId") Long groupId,
                                                      @Param("userId") Long userId,
                                                      @Param("role") GroupRole role);
}