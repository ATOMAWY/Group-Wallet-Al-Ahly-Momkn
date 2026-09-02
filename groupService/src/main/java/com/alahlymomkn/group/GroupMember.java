package com.alahlymomkn.group;

import com.alahlymomkn.common.GroupRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_members")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long groupId;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private GroupRole role;
}