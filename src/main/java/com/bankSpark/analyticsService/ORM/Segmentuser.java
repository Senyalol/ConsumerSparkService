package com.bankSpark.analyticsService.ORM;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "segmentusers")
public class Segmentuser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "segmentusers_id_gen")
    @SequenceGenerator(name = "segmentusers_id_gen", sequenceName = "segmentusers_segment_id_seq", allocationSize = 1)
    @Column(name = "segment_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private com.bankSpark.analyticsService.User user;

    @Column(name = "segment", nullable = false, length = 20)
    private String segment;

    @Column(name = "r_minutes", nullable = false)
    private Double rMinutes;

    @Column(name = "f", nullable = false)
    private Long f;

    @Column(name = "m", nullable = false)
    private Double m;

    @Column(name = "updated_at", nullable = false)
    private Long updatedAt;

}