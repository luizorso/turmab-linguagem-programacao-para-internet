
package com.cepestm.turmab.dto;

import com.cepestm.turmab.entity.*;


public record PerfilDTO(String nome) {
    public Perfil toPerfil(){
        Perfil perfil = new Perfil();
        perfil.setNome(nome);
        return perfil;
        
    }
    
}
