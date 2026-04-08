package com.saims.erpm.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.saims.erpm.model.SucursalAreaCentroCostosModel;

public interface SucursalAreaCentroCostosDao extends JpaRepository<SucursalAreaCentroCostosModel,Long> {

	//@Query(value = "select * from sucursal_area_centrocostos where id_sucursal=? and id_area=? and id_centrocostos=?",nativeQuery=true)
	//SucursalAreaCentroCostosModel getSucursalAreaCentroCostos(Long idSucursal, Long idArea, Long idCentroCostos);
	
	Optional<SucursalAreaCentroCostosModel> findBySucursal_IdAndArea_IdAndCentrocostos_Id(Long sucursalId, Long areaId, Long centrocostosId);
	
	@Query(value = "SELECT * FROM sucursal_area_centrocostos WHERE id_sucursal = ? AND id_area = ? AND (? IS NULL OR id_centrocostos = ?) AND (id_centrocostos IS NULL OR ? IS NOT NULL)",nativeQuery = true)
	Optional<SucursalAreaCentroCostosModel> getSucursalAreaCentroCostos(Long idSucursal, Long idArea, Long isCentroCostos);
	
	List<SucursalAreaCentroCostosModel>findBySucursalId(Long id_surusal);
	
	List<SucursalAreaCentroCostosModel>findByAreaId(Long id_area);
	
	List<SucursalAreaCentroCostosModel>findByCentrocostosId(Long id_centrocostos);
	
}
