package com.saims.erpm.service;

import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.BancoModel;

public interface BancoService {
	
	BancoModel addDatos(DatosDtoRequest datosDtoRequest);

}
