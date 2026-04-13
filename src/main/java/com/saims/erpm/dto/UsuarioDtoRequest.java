package com.saims.erpm.dto;

import lombok.Data;

@Data
public class UsuarioDtoRequest {
	private String usuario;
	private String password;
	private Boolean estado;
}
