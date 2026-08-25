
package com.cepestm.turmab.dto;

import com.cepestm.turmab.entity.*;
import java.util.UUID;


public record PerfilDTO(UUID id,String nome){
    
  public PerfilDTO(String nome){
      this(null,nome);
  }
    /* Este método converte um objeto do tipo perfilDTO para
    um objeto perfil.
  */
    public Perfil toPerfil(){
        Perfil perfil = new Perfil();
        perfil.setId(id);
        perfil.setNome(nome);
        return perfil;
        
    }
    
}
