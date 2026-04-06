package com.saims.erpm.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.SucursalAreaCentroCostosDao;
import com.saims.erpm.model.AreaModel;
import com.saims.erpm.model.CentroCostosModel;
import com.saims.erpm.model.SucursalAreaCentroCostosModel;
import com.saims.erpm.model.SucursalModel;
import com.saims.erpm.service.SucursalAreaCentroCostosService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SucursalAreaCentroCostosServiceImpl implements SucursalAreaCentroCostosService {
	private final SucursalAreaCentroCostosDao sucursalAreaCentroCostosDao;
	private final ModelMapper modelMapper;
	
	@Transactional
	public SucursalAreaCentroCostosModel addDatos(SucursalModel sucursalModel,AreaModel areaModel,CentroCostosModel centroCostosModel) {
		
		SucursalAreaCentroCostosModel sucursalAreaCentroCostosModel = this.sucursalAreaCentroCostosDao.findBySucursal_IdAndArea_IdAndCentrocostos_Id(sucursalModel.getId(),areaModel.getId(),centroCostosModel.getId())
				.orElseGet(()->{
					SucursalAreaCentroCostosModel nuevo = new SucursalAreaCentroCostosModel();
					nuevo.setArea(areaModel);
					nuevo.setSucursal(sucursalModel);
					nuevo.setCentrocostos(centroCostosModel);
					return (this.sucursalAreaCentroCostosDao.save(nuevo));
				});
		
		return(sucursalAreaCentroCostosModel);
	}

}
