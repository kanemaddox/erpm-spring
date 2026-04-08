package com.saims.erpm.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.CentroCostosDao;
import com.saims.erpm.dto.AreaDtoRequest;
import com.saims.erpm.dto.AreaDtoResponse;
import com.saims.erpm.dto.CentroCostosDtoRequest;
import com.saims.erpm.dto.CentroCostosDtoResponse;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.AreaModel;
import com.saims.erpm.model.CentroCostosModel;
import com.saims.erpm.service.CentroCostosService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Implementación del servicio para la gestión de Centros de Costos.
 * 
 * Proporciona operaciones para la creación, búsqueda, actualización y
 * gestión de centros de costos dentro del sistema.
 * 
 * @author 
 */
@Service
@RequiredArgsConstructor
public class CentroCostosServiceImpl implements CentroCostosService {

    private final CentroCostosDao centroCostosDao;
    private final ModelMapper modelMapper;

    /**
     * Crea un nuevo Centro de Costos o lo obtiene si ya existe.
     * 
     * Reglas de negocio:
     * - El código se normaliza (mayúsculas y sin espacios).
     * - Si ya existe un registro con ese código, se retorna.
     * - Si no existe, se crea un nuevo registro.
     *
     * @param datosDtoRequest Datos de entrada que contienen código y nombre del centro de costos.
     * @return CentroCostosModel existente o recién creado.
     */
    @Transactional
    public CentroCostosModel createOrGet(DatosDtoRequest datosDtoRequest) {

        String codigoCentroCostos = normalize(datosDtoRequest.getCodigoCentroCostos());
        String nombreCentroCostos = normalize(datosDtoRequest.getNombreCentroCostos());

        return this.centroCostosDao.findByCodigo(codigoCentroCostos)
                .orElseGet(() -> {
                    CentroCostosModel nuevo = new CentroCostosModel();
                    nuevo.setCodigo(codigoCentroCostos);
                    nuevo.setNombre(nombreCentroCostos);
                    nuevo.setPrefijo(generatePrefijo(codigoCentroCostos));
                    nuevo.setEstado(true);
                    return this.centroCostosDao.save(nuevo);
                });
    }

    /**
     * Crea un nuevo Centro de Costos.
     *
     * @param request DTO con los datos del centro de costos.
     * @return DTO de respuesta.
     */
    @Transactional
    public CentroCostosDtoResponse createCentroCostos(CentroCostosDtoRequest request) {

        String codigo = normalize(request.getCodigo());

        // Validación: evitar duplicados
        if (this.centroCostosDao.findByCodigo(codigo).isPresent()) {
            throw new RuntimeException("El centro de costos ya existe con código: " + codigo);
        }

        CentroCostosModel centroCosto = new CentroCostosModel();
        centroCosto.setCodigo(codigo);
        centroCosto.setNombre(normalize(request.getNombre()));
        centroCosto.setPrefijo(generatePrefijo(codigo));
        centroCosto.setEstado(true);

        this.centroCostosDao.save(centroCosto);

        return mapToDto(centroCosto);
    }

    /**
     * Obtiene un Centro de Costos por su ID.
     */
    @Transactional
    public CentroCostosDtoResponse getById(Long id) {
        CentroCostosModel centroCosto = this.centroCostosDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Centro de costos no encontrado con ID: " + id));

        return this.mapToDto(centroCosto);
    }

    /**
     * Lista todos los centros de costos.
     */
    @Transactional
    public List<CentroCostosDtoResponse> getAll() {
        return this.centroCostosDao.findAll()
                .stream()
                .map(centroCosto -> this.mapToDto(centroCosto))
                .toList();
    }

    /**
     * Lista paginada de centros de costos.
     */
    @Transactional
    public Page<CentroCostosDtoResponse> getAllPaginated(Pageable pageable) {
        return centroCostosDao.findAll(pageable)
                .map(entity -> this.mapToDto(entity));
    }

    /**
     * Busca un centro de costos por su código.
     */
    @Transactional
    public CentroCostosDtoResponse findByCodigo(String codigo) {
        CentroCostosModel centroCostos = centroCostosDao.findByCodigo(normalize(codigo))
                .orElseThrow(() -> new RuntimeException("No existe centro de costos con código: " + codigo));

        return this.mapToDto(centroCostos);
    }

    /**
     * Actualiza el nombre de un centro de costos.
     */

    @Transactional
    public CentroCostosDtoResponse updateNombre(CentroCostosDtoResponse response) {
        CentroCostosModel centroCostos = this.centroCostosDao.findById(response.getId())
                .orElseThrow(() -> new RuntimeException("Centro de costos no encontrado"));

        centroCostos.setNombre(normalize(response.getNombre()));
        centroCostosDao.save(centroCostos);

        return this.mapToDto(centroCostos);
    }
    
    @Transactional
    public CentroCostosDtoResponse updateFull(CentroCostosDtoResponse response) {

        CentroCostosModel centroCostos = this.centroCostosDao.findById(response.getId())
                .orElseThrow(() -> new RuntimeException("Centro de Costos no encontrada"));

        String nombre = normalize(response.getNombre());

        if (!centroCostos.getNombre().equals(nombre) && existsByNombre(nombre)) {
            throw new RuntimeException("Ya existe un Centro de Costos con ese nombre");
        }

        centroCostos.setNombre(nombre);
        centroCostos.setCodigo(normalize(response.getCodigo()));
        centroCostos.setPrefijo(this.generatePrefijo(response.getCodigo()));
        centroCostos.setEstado(response.getEstado());

        this.centroCostosDao.save(centroCostos);

        return this.mapToDto(centroCostos);
    }

    /**
     * Elimina un centro de costos por ID.
     */
    @Transactional
    public void delete(Long id) {
    	CentroCostosModel centroCostos = this.centroCostosDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Centro de Costos no encontrada"));

        centroCostos.setEstado(false); // 🔥 soft delete

        this.centroCostosDao.save(centroCostos);
    }

    // =========================
    // 🧠 MÉTODOS AUXILIARES
    // =========================

    /**
     * Normaliza textos:
     * - Convierte a mayúsculas
     * - Elimina espacios en blanco al inicio y final
     */
    private String normalize(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }

    /**
     * Genera el prefijo a partir del código.
     * Ejemplo: "CC01" -> "CC"
     */
    private String generatePrefijo(String codigo) {
        if (codigo == null || codigo.length() < 2) {
            return codigo;
        }
        return codigo.substring(0, 2);
    }
    
    /**
     * 📌 Convierte Entity → DTO
     */
    private CentroCostosDtoResponse mapToDto(CentroCostosModel centroCostosModel) {
    	
        return this.modelMapper.map(centroCostosModel, CentroCostosDtoResponse.class);
    }
    
    public boolean existsByNombre(String nombre) {
        return this.centroCostosDao.findByNombre(nombre).isPresent();
    }
}