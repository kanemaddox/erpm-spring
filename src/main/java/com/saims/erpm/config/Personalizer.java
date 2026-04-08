package com.saims.erpm.config;

public class Personalizer {
	
	public String normalizer(String value) {
        return value != null ? value.toUpperCase().trim() : null;
    }
	
	public String normalizerLower(String value) {
        return value != null ? value.toLowerCase().trim() : null;
    }
	
	public String normalizerName(String value) {
		return value !=null ? name(value):null;
	}
	private String name(String value) {
		String name = value.trim();
		return (name.substring(0, 1).toUpperCase()+ name.substring(1).toLowerCase());
	}
	
	/**
     * 📌 Genera el prefijo de la sucursal
     * ✔ Toma los primeros 2 caracteres del código
     */
    public String generatePrefijo(String codigo) {
        return (codigo != null && codigo.length() >= 2) 
                ? codigo.substring(0, 2) 
                : codigo;
    }

}
