
package com.cepestm.turmab.service;

import com.cepestm.turmab.repository.PerfilRepository;
import com.cepestm.turmab.repository.UsuarioPerfilRepository;
import com.cepestm.turmab.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioPerfilService {
    private final UsuarioPerfilRepository usuarioPerfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    
    public UsuarioPerfilService(UsuarioPerfilRepository usuarioPerfilRepository,
        UsuarioRepository usuarioRepository,
        PerfilRepository perfilRepository){
        this.usuarioPerfilRepository = usuarioPerfilRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
    }
    
}
