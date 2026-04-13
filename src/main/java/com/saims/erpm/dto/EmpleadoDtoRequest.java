package com.saims.erpm.dto;

import com.saims.erpm.enums.TipoEmpleado;

import lombok.Data;

@Data
public class EmpleadoDtoRequest {
	private Long idCargo;
	private Long idPersona;
	private Long idProfesion;
	private Long idSucursal;
	private TipoEmpleado tipoEmpleado;
	private String idpJefe;
	private Boolean estado;
}
