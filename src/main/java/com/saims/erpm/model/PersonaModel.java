package com.saims.erpm.model;


import com.saims.erpm.base.BaseEntity;

// Importaciones de JPA
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
// Importaciones de validación
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// Importaciones de Lombok
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 📌 Clase: PersonaModel
 * 
 * 🧾 Descripción:
 * Representa la entidad "persona" en la base de datos.
 * Almacena la información básica de una persona dentro del sistema ERP.
 * 
 * 🚀 Mejoras implementadas:
 * ✔ Validaciones (Bean Validation)
 * ✔ Restricciones en base de datos
 * ✔ Auditoría (fechas automáticas)
 * ✔ Preparada para escalabilidad
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "persona")
public class PersonaModel extends BaseEntity{

    /**
     * 🪪 Documento de identidad (CI / RUT)
     */
    @NotBlank(message = "El documento de identidad es obligatorio")
    @Size(min = 5, max = 20)
    @Column(name = "idp", nullable = false, length = 20, unique = true)
    private String idp;

    /**
     * 👤 Nombre de la persona
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * 👤 Apellido paterno
     */
    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 100)
    @Column(name = "paterno", length = 100)
    private String paterno;

    /**
     * 👤 Apellido materno
     */
    @Size(max = 100)
    @Column(name = "materno", length = 100)
    private String materno;

    /**
     * 📧 Correo electrónico
     */
    @Email(message = "Debe ser un correo electrónico válido")
    @Size(max = 150)
    @Column(name = "email", length = 150, unique = true)
    private String email;
}