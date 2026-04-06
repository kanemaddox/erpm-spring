package com.saims.erpm.service;

import com.saims.erpm.model.AreaModel;
import com.saims.erpm.model.CentroCostosModel;
import com.saims.erpm.model.SucursalAreaCentroCostosModel;
import com.saims.erpm.model.SucursalModel;

public interface SucursalAreaCentroCostosService {
	SucursalAreaCentroCostosModel addDatos(SucursalModel sucursalModel,AreaModel areaModel,CentroCostosModel centroCostosModel);

}
