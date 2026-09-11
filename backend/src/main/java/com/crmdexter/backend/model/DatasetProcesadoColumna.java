package com.crmdexter.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Map;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity @Table(name = "dataset_procesado_columnas")
public class DatasetProcesadoColumna {
    @Id @GeneratedValue(strategy = GenerationType.UUID) @JdbcTypeCode(SqlTypes.UUID) @Column(columnDefinition = "uuid") private UUID id;
    @JdbcTypeCode(SqlTypes.UUID) @Column(name = "dataset_procesado_id", nullable = false, columnDefinition = "uuid") private UUID datasetProcesadoId;
    @Column(nullable = false, length = 150) private String nombre;
    @Column(name = "tipo_dato", nullable = false, length = 30) private String tipoDato;
    @Column(nullable = false) private int posicion;
    @JdbcTypeCode(SqlTypes.JSON) @Column(name = "estadisticas_json", columnDefinition = "json") private Map<String, Object> estadisticasJson;
    protected DatasetProcesadoColumna() {}
}
