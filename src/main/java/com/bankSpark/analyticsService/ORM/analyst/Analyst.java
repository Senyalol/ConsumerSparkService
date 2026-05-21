package com.bankSpark.analyticsService.ORM.analyst;

import com.bankSpark.analyticsService.ORM.inviteToken.InviteToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "analyst")
public class Analyst {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analyst_id_gen")
    @SequenceGenerator(name = "analyst_id_gen", sequenceName = "analyst_analyst_id_seq", allocationSize = 1)
    @Column(name = "analyst_id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "token_id", nullable = false)
    private InviteToken token;

    @Column(name = "login", nullable = false, length = 50)
    private String login;

    @Column(name = "password", nullable = false, length = 500)
    private String password;

    @ColumnDefault("'ANALYST'")
    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}