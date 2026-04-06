package com.saims.erpm.service;

import java.util.List;

import com.saims.erpm.dto.CuentaBancariaDtoRequest;
import com.saims.erpm.dto.CuentaBancariaDtoResponse;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.BancoModel;
import com.saims.erpm.model.CuentaBancariaModel;
import com.saims.erpm.model.PersonaModel;

public interface CuentaBancariaService {
	
	CuentaBancariaModel addDatos(PersonaModel personaModel,BancoModel bancoModel,DatosDtoRequest datosDtoRequest);
	CuentaBancariaDtoResponse addCuentaBancaria(CuentaBancariaDtoRequest cuentaBancariaDtoRequest);
	CuentaBancariaDtoResponse getCuentaBancaria(Long id);
	List<CuentaBancariaDtoResponse>getCuentasBancarias(Long id_persona);
	List<CuentaBancariaDtoResponse>getCuentasBancarias();

}
