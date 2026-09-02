package com.alahlymomkn.group;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "group_details")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}