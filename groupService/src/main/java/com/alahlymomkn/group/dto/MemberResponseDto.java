package com.alahlymomkn.group.dto;

import com.alahlymomkn.common.enums.GroupRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private Long groupId;
    private Long userId;
    private Set<GroupRole> roles;
}
