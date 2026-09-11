package com.crmdexter.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity @Table(name = "dashboards")
public class Dashboard {
    @Id @GeneratedValue(strategy = GenerationType.UUID) @JdbcTypeCode(SqlTypes.UUID) @Column(name = "id", columnDefinition = "uuid") private UUID id;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @UpdateTimestamp @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @JdbcTypeCode(SqlTypes.UUID) @Column(name = "proyecto_id", nullable = false, columnDefinition = "uuid") private UUID proyectoId;
    @JdbcTypeCode(SqlTypes.UUID) @Column(name = "dataset_procesado_id", columnDefinition = "uuid") private UUID datasetProcesadoId;
    @JdbcTypeCode(SqlTypes.UUID) @Column(name = "created_by", nullable = false, columnDefinition = "uuid") private UUID createdBy;
    @Column(nullable = false, length = 180) private String nombre;
    @Column(length = 500) private String descripcion;
    @Column(nullable = false) private boolean activo = true;
    protected Dashboard() {}
}
