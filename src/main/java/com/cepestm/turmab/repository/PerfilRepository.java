
package com.cepestm.turmab.repository;

import com.cepestm.turmab.entity.Perfil;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends 
        JpaRepository<Perfil, UUID>{
    
}
