package com.saims.erpm.dto;

import lombok.Data;

@Data
public class CuentaBancariaDtoRequest {
	private String numero;
	private String tipoCuenta;
	private boolean estado;
	private Long idPersona;
	private Long idBanco;

}
