
package com.cepestm.turmab.dto;

import com.cepestm.turmab.entity.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;


public record PerfilDTO(
        UUID id,
        @NotBlank(message = "O nome é obrigatório!")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres!")
        String nome){
    
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
