
package com.cepestm.turmab.service;

import com.cepestm.turmab.dto.PerfilDTO;
import com.cepestm.turmab.entity.Perfil;
import com.cepestm.turmab.repository.PerfilRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PerfilService {
    private final PerfilRepository rep;
    
    public PerfilService(PerfilRepository rep){
        this.rep = rep;
        
    }
    // converte o DTO para Entidade
    @Transactional
    public Perfil savedByDTO(PerfilDTO dto){
         // executa o INSERT no banco
        Perfil perfil = rep.save(dto.toPerfil());
        
        return perfil;
        
    }
    
}
