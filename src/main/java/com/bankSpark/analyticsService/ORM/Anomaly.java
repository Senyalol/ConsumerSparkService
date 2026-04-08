package com.bankSpark.analyticsService.ORM;

import com.bankSpark.analyticsService.ORM.User;
import jakarta.persistence.*;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@Table(name = "anomaly")
public class Anomaly {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "anomaly_id_gen")
    @SequenceGenerator(name = "anomaly_id_gen", sequenceName = "anomaly_anomaly_id_seq", allocationSize = 1)
    @Column(name = "anomaly_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "event_time", nullable = false)
    private Long eventTime;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "sum", nullable = false)
    private Double sum;

    @Column(name = "avg_check", nullable = false)
    private Double avgCheck;

    @Column(name = "message", nullable = false, length = Integer.MAX_VALUE)
    private String message;

}