
package com.cepestm.turmab.repository;


import com.cepestm.turmab.entity.Usuario;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID>{
    // O Spring Data JPA já fornece automaticamente:// O Spring Data JPA já fornece automaticamente:// O Spring Data JPA já fornece automaticamente:// O Spring Data JPA já fornece automaticamente:// O Spring Data JPA já fornece automaticamente:// O Spring Data JPA já fornece automaticamente:
        // - save(Usuario usuario)    // - save(Usuario usuario)    // - save(Usuario usuario)    // - save(Usuario usuario)    // - save(Usuario usuario)
        // - findById(UUID id) -> Retorna Optional<Usuario>    // - findById(UUID id) -> Retorna Optional<Usuario>    // - findById(UUID id) -> Retorna Optional<Usuario>    // - findById(UUID id) -> Retorna Optional<Usuario>
        // - findAll() -> Retorna List<Usuario>    // - findAll() -> Retorna List<Usuario>    // - findAll() -> Retorna List<Usuario>
        // - delete(Usuario usuario) / deleteById(UUID id)    // - delete(Usuario usuario) / deleteById(UUID id)
        
    
}
