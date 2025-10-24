package com.example.blockrott.backend.entidades;


import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reporte_semanal", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "nombre_app", "semana_inicio"})
})
@Getter
@Setter
@NoArgsConstructor
public class ReporteSemanal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // bigserial PRIMARY KEY

    // Relacion uno a muchos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "nombre_app", nullable = false)
    private String nombreApp; // varchar(255)

    @Column(name = "semana_inicio", nullable = false)
    private LocalDate semanaInicio;

    @Column(name = "promedio_uso_seg", nullable = false)
    private Long promedioUsoSeg; //
}
