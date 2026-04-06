package com.saims.erpm.dto;

import lombok.Data;

@Data
public class CuentaBancariaDtoRequest {
	private String numero;
	private String tipoCuenta;
	private boolean estado;
	private Long id_persona;
	private Long id_banco;

}
