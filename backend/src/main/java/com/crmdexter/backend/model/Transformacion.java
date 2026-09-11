package com.crmdexter.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity @Table(name = "transformaciones")
public class Transformacion {
    @Id @GeneratedValue(strategy = GenerationType.UUID) @JdbcTypeCode(SqlTypes.UUID) @Column(name = "id", columnDefinition = "uuid") private UUID id;
    @CreationTimestamp @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @UpdateTimestamp @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @JdbcTypeCode(SqlTypes.UUID) @Column(name = "dataset_id", nullable = false, columnDefinition = "uuid") private UUID datasetId;
    @JdbcTypeCode(SqlTypes.UUID) @Column(name = "dataset_columna_id", columnDefinition = "uuid") private UUID datasetColumnaId;
    @JdbcTypeCode(SqlTypes.UUID) @Column(name = "ejecucion_id", columnDefinition = "uuid") private UUID ejecucionId;
    @JdbcTypeCode(SqlTypes.UUID) @Column(name = "created_by", nullable = false, columnDefinition = "uuid") private UUID createdBy;
    @Column(name = "tipo_transformacion", nullable = false, length = 40) private String tipoTransformacion;
    @JdbcTypeCode(SqlTypes.JSON) @Column(name = "configuracion_json", columnDefinition = "json") private Map<String, Object> configuracionJson;
    @Column(name = "orden_ejecucion", nullable = false) private int ordenEjecucion = 1;
    @Column(name = "filas_afectadas", nullable = false) private long filasAfectadas;
    @Column(nullable = false, length = 20) private String estado = "PENDIENTE";
    protected Transformacion() {}
}
