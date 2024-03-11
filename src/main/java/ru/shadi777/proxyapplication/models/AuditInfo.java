package ru.shadi777.proxyapplication.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Data
@Table(name = "audits")
public class AuditInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private OffsetDateTime offsetDateTime;

    private String endpoint;

    @ManyToOne
    private User user;

    private String ipAddress;

    private Integer responseStatus;
}
