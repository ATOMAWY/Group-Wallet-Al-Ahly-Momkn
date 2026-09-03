package com.alahlymomkn.group.mapper;

import com.alahlymomkn.group.dto.GroupResponseDto;
import com.alahlymomkn.group.dto.MemberResponseDto;
import com.alahlymomkn.group.entity.Group;
import com.alahlymomkn.group.entity.GroupMember;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupResponseDto toResponseDto(Group group);
    MemberResponseDto toMemberResponseDto(GroupMember groupMember);
}
