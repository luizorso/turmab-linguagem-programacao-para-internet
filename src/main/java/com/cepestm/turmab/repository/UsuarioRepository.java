
package com.cepestm.turmab.repository;

import com.cepestm.turmab.entity.Usuario;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UsuarioRepository extends  JpaRepository<Usuario, UUID>{
    
}
