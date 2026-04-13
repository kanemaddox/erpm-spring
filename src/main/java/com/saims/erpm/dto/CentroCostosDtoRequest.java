package com.saims.erpm.dto;

import lombok.Data;

@Data
public class CentroCostosDtoRequest {
	private String codigo;
	private String nombre;
	private String prefijo;
	private boolean estado;

}
