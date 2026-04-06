package com.saims.erpm.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.erpm.model.SucursalAreaCentroCostosModel;

public interface SucursalAreaCentroCostosDao extends JpaRepository<SucursalAreaCentroCostosModel,Long> {

	@Query(value = "select * from sucursal_area_centrocostos where id_sucursal=? and id_area=? and id_centrocostos=?",nativeQuery=true)
	SucursalAreaCentroCostosModel getSucursalAreaCentroCostos(Long idSucursal, Long idArea, Long idCentroCostos);
	
	Optional<SucursalAreaCentroCostosModel> findBySucursal_IdAndArea_IdAndCentrocostos_Id(Long sucursalId, Long areaId, Long centrocostosId);
}
