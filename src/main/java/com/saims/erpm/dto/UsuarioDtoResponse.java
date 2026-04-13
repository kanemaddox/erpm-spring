package com.saims.erpm.dto;

import lombok.Data;

@Data
public class UsuarioDtoResponse {
	private Long id;
	private String usuario;
	private String password;
	private Boolean estado;
	private String fechaCreacion;
	private String fechaActualizacion;
}
