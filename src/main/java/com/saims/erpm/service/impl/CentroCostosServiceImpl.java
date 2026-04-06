package com.saims.erpm.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.CentroCostosDao;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.CentroCostosModel;
import com.saims.erpm.service.CentroCostosService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CentroCostosServiceImpl implements CentroCostosService {
	
	private final CentroCostosDao centroCostosDao;
	private final ModelMapper modelMapper;
	
	@Transactional
	public CentroCostosModel addDatos(DatosDtoRequest datosDtoRequest) {
		/**
		 * registro de Centro de costos
		 * primero verificamos si existe  mediante el codigo del centro de costos
		 * si existe 
		 * 		no realizamos nada 
		 * caso contrario 
		 * 		creamos una nuevo centro de costos
		 */
		
		String codigoCentroCostos = datosDtoRequest.getCodigoCentroCostos().toUpperCase().trim();
				
		CentroCostosModel centroCostosModel = this.centroCostosDao.findByCodigo(codigoCentroCostos)
				.orElseGet(()->{
					CentroCostosModel nuevo = new CentroCostosModel();
					nuevo.setCodigo(codigoCentroCostos);
					nuevo.setNombre(datosDtoRequest.getNombreCentroCostos().toUpperCase().trim());
					nuevo.setPrefijo(codigoCentroCostos.substring(0, 2));
					return(this.centroCostosDao.save(nuevo));
				});
		
		return (centroCostosModel);
	}
	
	

}
