package com.saims.erpm.service;

import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.dto.DatosDtoResponse;

public interface DatosService {
	
	DatosDtoResponse AddDatos(DatosDtoRequest datosDtoRequest);

}
