package com.bankSpark.analyticsService.ORM.inviteToken;

import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "invite_tokens")
public class InviteToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invite_tokens_id_gen")
    @SequenceGenerator(name = "invite_tokens_id_gen", sequenceName = "invite_tokens_token_id_seq", allocationSize = 1)
    @Column(name = "token_id", nullable = false)
    private Integer id;

    @Column(name = "token", nullable = false)
    private String token;

    @ColumnDefault("'ANALYST'")
    @Column(name = "role", length = 50)
    private String role;

    @ColumnDefault("false")
    @Column(name = "used")
    private Boolean used;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToOne(mappedBy = "token")
    private Analyst analyst;

}