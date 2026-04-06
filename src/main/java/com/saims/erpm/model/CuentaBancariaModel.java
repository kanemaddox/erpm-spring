package com.saims.erpm.model;

// Manejo de fechas
import java.time.LocalDate;

import com.saims.erpm.base.StatusEntity;
import com.saims.erpm.enums.EstadoCuenta;
// Enums
import com.saims.erpm.enums.TipoCuenta;

// Importaciones JPA
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
// Validaciones
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
// Lombok
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 📌 Clase: CuentaBancaria
 * 
 * 🧾 Descripción:
 * Representa una cuenta bancaria dentro del sistema ERP.
 * 
 * 🚀 Mejoras aplicadas:
 * - Relación con PersonaModel (en vez de ID suelto)
 * - Uso de ENUM para estado
 * - Validaciones de campos
 * - Auditoría automática
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "cuentabancaria")
public class CuentaBancariaModel extends StatusEntity{
	/**
	 * La clase StatusEntity invoca el atributo estado
	 * y luego los atributos ID creacion y actualizacion por herencia 
	 */

    /**
     * 🔢 Número de cuenta bancaria
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = "numero", nullable = false, length = 50)
    private String numero;

    /**
     * 🏦 Tipo de cuenta
     */
    @NotBlank
    @Column(name = "tipo_cuenta", nullable = false)
    private String tipoCuenta;

    /**
     * 🏦 Relación con banco
     */
    @ManyToOne
    @JoinColumn(name = "id_banco", nullable = false)
    private BancoModel banco;

    /**
     * 👤 Relación con persona
     */
    @ManyToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private PersonaModel persona;

}