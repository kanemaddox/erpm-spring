package com.saims.erpm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.BancoDao;
import com.saims.erpm.dao.CuentaBancariaDao;
import com.saims.erpm.dao.PersonaDao;
import com.saims.erpm.dto.CuentaBancariaDtoRequest;
import com.saims.erpm.dto.CuentaBancariaDtoResponse;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.BancoModel;
import com.saims.erpm.model.CuentaBancariaModel;
import com.saims.erpm.model.PersonaModel;
import com.saims.erpm.service.CuentaBancariaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CuentaBancariaServiceImpl implements CuentaBancariaService {
	private final CuentaBancariaDao cuentaBancariaDao;
	private final PersonaDao personaDao;
	private final BancoDao bancoDao;
	private final ModelMapper modelMapper;
	
	@Transactional
	public CuentaBancariaModel addDatos(PersonaModel personaModel,BancoModel bancoModel,DatosDtoRequest datosDtoRequest) {
		/**
		 * Registro para cuenta bancaria
		 * verificamos si persona tiene una cuenta bancaria 
		 * si exsite 
		 * 		verificamos si su cuenta bancaria es la misma
		 * 		si es la misma
		 * 			se queda como esta
		 * 		caso contrario
		 * 			agregamos una nueva cuenta bancaria y desabilitamos la ultima
		 * caso contrario		 * 
		 * 		Creamos la cuenta bancaria relasionada entre entidad banco y persona
		 */
		
		
		List<CuentaBancariaModel>cuentas = cuentaBancariaDao.getListaCuentaBancaria(personaModel.getId());
		CuentaBancariaModel cuentaBancariaModel = null;
		int j = 0;
		int k = 0;
		if(cuentas.size()>0) {
			for(int i=0; i<cuentas.size();i++) {
				cuentaBancariaModel = cuentas.get(i);
				if(cuentaBancariaModel.getBanco().getId().longValue() == bancoModel.getId().longValue()) {
					if(cuentaBancariaModel.getNumero().equals(datosDtoRequest.getNumero())) {
						j=i;
						k++;
					}
				}
				cuentas.get(i).setEstado(false);
				cuentaBancariaDao.save(cuentas.get(i));
			}
			if(k>0) {
				cuentaBancariaModel = cuentas.get(j);
				cuentaBancariaModel.setEstado(true);
				cuentaBancariaDao.save(cuentaBancariaModel);
			}
			
		}
		if(j==0 && k==0) {
			cuentaBancariaModel = new CuentaBancariaModel();
			cuentaBancariaModel.setNumero(datosDtoRequest.getNumero());
			cuentaBancariaModel.setTipoCuenta(datosDtoRequest.getTipoCuenta());
			cuentaBancariaModel.setBanco(bancoModel);
			cuentaBancariaModel.setPersona(personaModel);
			cuentaBancariaModel.setEstado(true);
			cuentaBancariaDao.save(cuentaBancariaModel);
		}
		
		return (cuentaBancariaModel);
	}
	
	@Transactional
	public CuentaBancariaDtoResponse addCuentaBancaria(CuentaBancariaDtoRequest cuentaBancariaDtoRequest) {
		PersonaModel personaModel= personaDao.getId(cuentaBancariaDtoRequest.getId_persona());
		BancoModel bancoModel = bancoDao.getId(cuentaBancariaDtoRequest.getId_banco());
		
		List<CuentaBancariaModel>cuentas = cuentaBancariaDao.getListaCuentaBancaria(personaModel.getId());
		CuentaBancariaModel cuentaBancariaModel = null;
		int j = 0;
		if(cuentas.size()>0) {
			for(int i=0; i<cuentas.size();i++) {
				cuentaBancariaModel = cuentas.get(i);
				if(cuentaBancariaModel.getBanco().getId().longValue() == bancoModel.getId().longValue()) {
					if(cuentaBancariaModel.getNumero().equals(cuentaBancariaDtoRequest.getNumero())) {
						j=i;
					}
				}
				cuentas.get(i).setEstado(false);
				cuentaBancariaDao.save(cuentas.get(i));
			}
			if(j>0) {
				cuentaBancariaModel = cuentas.get(j);
				cuentaBancariaModel.setEstado(true);
				cuentaBancariaDao.save(cuentaBancariaModel);
			}
			
		}
		if(j==0) {
			cuentaBancariaModel = new CuentaBancariaModel();
			cuentaBancariaModel.setNumero(cuentaBancariaDtoRequest.getNumero());
			cuentaBancariaModel.setTipoCuenta(cuentaBancariaDtoRequest.getTipoCuenta());
			cuentaBancariaModel.setBanco(bancoModel);
			cuentaBancariaModel.setPersona(personaModel);
			cuentaBancariaDao.save(cuentaBancariaModel);
		}
		
		return (this.modelMapper.map(cuentaBancariaModel, CuentaBancariaDtoResponse.class));
	}
	
	@Transactional
	public CuentaBancariaDtoResponse getCuentaBancaria(Long id) {
		CuentaBancariaModel cuentaBancariaModel = cuentaBancariaDao.getId(id);
		return (this.modelMapper.map(cuentaBancariaModel, CuentaBancariaDtoResponse.class));
	}
	
	@Transactional
	public List<CuentaBancariaDtoResponse>getCuentasBancarias(Long id_persona){
		List<CuentaBancariaDtoResponse>cuentas = this.cuentaBancariaDao.getListaCuentaBancaria(id_persona).stream()
				.map(cuenta -> this.modelMapper.map(cuenta, CuentaBancariaDtoResponse.class))
				.collect(Collectors.toList());
		return(cuentas);
	}
	
	@Transactional
	public List<CuentaBancariaDtoResponse>getCuentasBancarias(){
		List<CuentaBancariaDtoResponse>cuentas = this.cuentaBancariaDao.findAll().stream()
				.map(cuenta -> this.modelMapper.map(cuenta, CuentaBancariaDtoResponse.class))
				.collect(Collectors.toList());
		return(cuentas);
	}

}
