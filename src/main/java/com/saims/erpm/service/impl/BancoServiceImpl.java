package com.saims.erpm.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.saims.erpm.dao.BancoDao;
import com.saims.erpm.dto.BancoDtoRequest;
import com.saims.erpm.dto.BancoDtoResponse;
import com.saims.erpm.dto.DatosDtoRequest;
import com.saims.erpm.model.BancoModel;
import com.saims.erpm.service.BancoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

/**
 * 📌 Clase: BancoServiceImpl
 * 
 * 🧾 Descripción:
 * Implementación del servicio para la gestión de bancos.
 * Contiene la lógica de negocio para registrar, obtener y listar bancos,
 * evitando duplicados mediante la validación por nombre.
 * 
 * 🔧 Tecnologías:
 * - Spring Boot (@Service, @Transactional)
 * - JPA (BancoDao)
 * - ModelMapper (conversión Entity ↔ DTO)
 */
@Service
@RequiredArgsConstructor
public class BancoServiceImpl implements BancoService {

    private final BancoDao bancoDao;
    private final ModelMapper modelMapper;
    
    // =========================
    // 🟢 CREATE
    // =========================    
    /**
     * 📌 Crea un banco evitando duplicados
     */
    @Transactional
    public BancoDtoResponse createBanco(BancoDtoRequest request) {

        String nombre = normalizarNombre(request.getNombre());

        if (existsByNombre(nombre)) {
            throw new RuntimeException("El área ya existe");
        }

        BancoModel banco = new BancoModel();
        banco.setNombre(nombre);
        banco.setEstado(true);
        
        this.bancoDao.save(banco);

        return this.modelMapper.map(banco, BancoDtoResponse.class);
    }

    /**
     * 📌 Método: createOrGet
     * 
     * 🧾 Descripción:
     * Busca un banco por nombre. Si no existe, lo crea.
     * Este método es reutilizable para otras entidades (patrón "find or create").
     * 
     * @param nombre Nombre del banco
     * @return BancoModel existente o creado
     */
    @Transactional
    public BancoModel createOrGet(DatosDtoRequest request) {
        String nombreNormalizado = normalizarNombre(request.getBancoNombre());

        return this.bancoDao.findByNombre(nombreNormalizado)
                .orElseGet(() -> {
                    BancoModel nuevo = new BancoModel();
                    nuevo.setNombre(nombreNormalizado);
                    nuevo.setEstado(true);
                    return this.bancoDao.save(nuevo);
                });
    }

    /**
     * 📌 Método: getById
     * 
     * 🧾 Descripción:
     * Obtiene un banco por ID.
     * 
     * @param id ID del banco
     * @return BancoDtoResponse
     */
    @Transactional
    public BancoDtoResponse getById(Long id) {
        BancoModel banco = bancoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Banco no encontrado con ID: " + id));

        return this.modelMapper.map(banco, BancoDtoResponse.class);
    }

    /**
     * 📌 Método: getAll
     * 
     * 🧾 Descripción:
     * Lista todos los bancos.
     * 
     * @return Lista de BancoDtoResponse
     */
    @Transactional
    public List<BancoDtoResponse> getAll() {
    	return this.bancoDao.findAll().stream()
                .map(banco -> this.modelMapper.map(banco, BancoDtoResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * 📌 Método: getAllPaginated
     * 
     * 🧾 Descripción:
     * Lista bancos de forma paginada.
     * (Consistente con AreaServiceImpl)
     * 
     * @param pageable configuración de paginación
     * @return Page<BancoDtoResponse>
     */
    @Transactional
    public Page<BancoDtoResponse> getAllPaginated(Pageable pageable) {
    	return this.bancoDao.findAll(pageable)
                .map(banco -> this.modelMapper.map(banco, BancoDtoResponse.class));
    }

    /**
     * 📌 Método: findByNombre
     * 
     * 🧾 Descripción:
     * Busca un banco por nombre.
     * 
     * @param nombre nombre del banco
     * @return BancoDtoResponse
     */
    @Transactional
    public BancoDtoResponse findByNombre(String nombre) {
        String nombreNormalizado = normalizarNombre(nombre);

        BancoModel banco = bancoDao.findByNombre(nombreNormalizado)
                .orElseThrow(() -> new RuntimeException("Banco no encontrado: " + nombre));

        return this.modelMapper.map(banco, BancoDtoResponse.class);
    }

    /**
     * 📌 Método: delete
     * 
     * 🧾 Descripción:
     * Eliminación lógica (si tienes campo estado).
     * Si no, puedes cambiar a delete físico.
     * 
     * @param id ID del banco
     */
    @Transactional
    public void delete(Long id) {
        BancoModel banco = bancoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Banco no encontrado"));

        banco.setEstado(false); // requiere campo en BaseEntity
        this.bancoDao.save(banco);
    }

    /**
     * 📌 Método: updateNombre
     * 
     * 🧾 Descripción:
     * Actualiza el nombre de un banco.
     * 
     * @param id ID del banco
     * @param nuevoNombre nuevo nombre
     * @return BancoDtoResponse
     */
    @Transactional
    public BancoDtoResponse updateNombre(Long id, String nuevoNombre) {

        BancoModel banco = this.bancoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        String nombreNormalizado = normalizarNombre(nuevoNombre);

        if (existsByNombre(nombreNormalizado)) {
            throw new RuntimeException("Ya existe un área con ese nombre");
        }

        banco.setNombre(nombreNormalizado);

        this.bancoDao.save(banco);

        return this.modelMapper.map(banco, BancoDtoResponse.class);
    }
    
    /**
     * 📌 Actualización completa (usar con cuidado)
     */
    @Transactional
    public BancoDtoResponse updateFull(Long id, BancoDtoRequest request) {

        BancoModel banco = this.bancoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        String nombre = normalizarNombre(request.getNombre());

        if (!banco.getNombre().equals(nombre) && existsByNombre(nombre)) {
            throw new RuntimeException("Ya existe un área con ese nombre");
        }

        banco.setNombre(nombre);

        this.bancoDao.save(banco);

        return this.modelMapper.map(banco, BancoDtoResponse.class);
    }

    // =========================
    // 🔧 MÉTODOS PRIVADOS
    // =========================
    
    
    /**
     * 📌 Verifica existencia por nombre
     */
    public boolean existsByNombre(String nombre) {
        return this.bancoDao.findByNombre(nombre).isPresent();
    }

    /**
     * Normaliza nombres (mayúsculas + trim)
     */
    private String normalizarNombre(String nombre) {
        return nombre.toUpperCase().trim();
    }
}
