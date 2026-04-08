package com.bankSpark.analyticsService.ORM;

import com.bankSpark.analyticsService.ORM.Anomaly;
import com.bankSpark.analyticsService.ORM.Segmentuser;
import jakarta.persistence.*;
import lombok.Data;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_user_id_seq", allocationSize = 1)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @OneToMany(mappedBy = "user")
    private Set<Anomaly> anomalies = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Segmentuser> segmentusers = new LinkedHashSet<>();

}