package com.saims.erpm.dto;

import lombok.Data;

@Data
public class CargoDtoResponse {
	private Long id;
	private String nombre;
	private Boolean estado;
	private String fechaCreacion;
	private String fechaActualizacion;
}
