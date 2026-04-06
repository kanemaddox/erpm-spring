package com.saims.erpm.dto;

import lombok.Data;

@Data
public class PersonaDtoResponse {
	
	/**
	 * Datos para el Json
	 */
	private Long id;
	private String idp;
	private String nombre;
	private String paterno;
	private String materno;
	private String email;

}
