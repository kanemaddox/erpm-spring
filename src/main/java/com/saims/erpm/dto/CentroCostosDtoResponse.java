package com.saims.erpm.dto;

import lombok.Data;

@Data
public class CentroCostosDtoResponse {

	private Long id;
	private String nombre;
	private String codigo;
	private String prefijo;
	private Boolean estado;
	private String fecha_creacion;
	private String fecha_actualizacion;
}
