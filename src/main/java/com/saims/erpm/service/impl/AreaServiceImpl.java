package com.saims.erpm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

//import org.hibernate.internal.util.collections.AbstractPagedArray.Page;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Service;

import com.saims.erpm.dao.AreaDao;
import com.saims.erpm.dto.AreaDtoRequest;
import com.saims.erpm.dto.AreaDtoResponse;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.AreaModel;
import com.saims.erpm.service.AreaService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {

    private final AreaDao areaDao;
    private final ModelMapper modelMapper;

    // =========================
    // 🟢 CREATE
    // =========================

    /**
     * 📌 Crea un área evitando duplicados
     */
    @Transactional
    public AreaDtoResponse createArea(AreaDtoRequest request) {

        String nombre = normalizarNombre(request.getNombre());

        if (existsByNombre(nombre)) {
            throw new RuntimeException("El área ya existe");
        }

        AreaModel area = new AreaModel();
        area.setNombre(nombre);
        area.setEstado(true);

        this.areaDao.save(area);

        return this.modelMapper.map(area, AreaDtoResponse.class);
    }

    /**
     * 📌 Uso interno o cargas masivas
     */
    @Transactional
    public AreaModel createOrGet(DatosDtoRequest request) {

        String nombre = normalizarNombre(request.getNombreArea());

        return this.areaDao.findByNombre(nombre)
                .orElseGet(() -> {
                    AreaModel nueva = new AreaModel();
                    nueva.setNombre(nombre);
                    nueva.setEstado(true);
                    return this.areaDao.save(nueva);
                });
    }

    // =========================
    // 🔵 READ
    // =========================

    /**
     * 📌 Obtiene un área por ID
     */
    @Transactional
    public AreaDtoResponse getById(Long id) {

        AreaModel area = this.areaDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada con ID: " + id));

        return this.modelMapper.map(area, AreaDtoResponse.class);
    }

    /**
     * 📌 Lista todas las áreas activas
     */
    @Transactional
    public List<AreaDtoResponse> getAllActive() {

        return this.areaDao.findAll().stream()
                .filter(AreaModel::getEstado) // 🔥 solo activos
                .map(area -> this.modelMapper.map(area, AreaDtoResponse.class))
                .collect(Collectors.toList());
    }
    
    /**
     * 📌 Lista todas las áreas
     */
    @Transactional
    public List<AreaDtoResponse> getAll() {

        return this.areaDao.findAll().stream()
                .map(area -> this.modelMapper.map(area, AreaDtoResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * 📌 Búsqueda por nombre
     */
    @Transactional
    public AreaDtoResponse findByNombre(String nombre) {

        AreaModel area = this.areaDao.findByNombre(normalizarNombre(nombre))
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        return this.modelMapper.map(area, AreaDtoResponse.class);
    }

    /**
     * 📌 Paginación (frontend)
     */
    @Transactional
    public Page<AreaDtoResponse> getAllPaginated(Pageable pageable) {
        return this.areaDao.findAll(pageable)
                .map(area -> this.modelMapper.map(area, AreaDtoResponse.class));
    }

    // =========================
    // 🟡 UPDATE
    // =========================

    /**
     * 📌 Actualización parcial (RECOMENDADO)
     */
    @Transactional
    public AreaDtoResponse updateNombre(Long id, String nuevoNombre) {

        AreaModel area = this.areaDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        String nombreNormalizado = normalizarNombre(nuevoNombre);

        if (existsByNombre(nombreNormalizado)) {
            throw new RuntimeException("Ya existe un área con ese nombre");
        }

        area.setNombre(nombreNormalizado);

        this.areaDao.save(area);

        return modelMapper.map(area, AreaDtoResponse.class);
    }

    /**
     * 📌 Actualización completa (usar con cuidado)
     */
    @Transactional
    public AreaDtoResponse updateFull(Long id, AreaDtoRequest request) {

        AreaModel area = this.areaDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        String nombre = normalizarNombre(request.getNombre());

        if (!area.getNombre().equals(nombre) && existsByNombre(nombre)) {
            throw new RuntimeException("Ya existe un área con ese nombre");
        }

        area.setNombre(nombre);

        this.areaDao.save(area);

        return this.modelMapper.map(area, AreaDtoResponse.class);
    }

    // =========================
    // 🔴 DELETE (SOFT DELETE)
    // =========================

    /**
     * 📌 Eliminación lógica
     */
    @Transactional
    public void delete(Long id) {

        AreaModel area = this.areaDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        area.setEstado(false); // 🔥 soft delete

        this.areaDao.save(area);
    }

    // =========================
    // 🧠 MÉTODOS AUXILIARES
    // =========================

    /**
     * 📌 Verifica existencia por nombre
     */
    public boolean existsByNombre(String nombre) {
        return this.areaDao.findByNombre(nombre).isPresent();
    }

    /**
     * 📌 Normaliza texto (clave para evitar duplicados)
     */
    private String normalizarNombre(String nombre) {
        return nombre.toUpperCase().trim();
    }
}