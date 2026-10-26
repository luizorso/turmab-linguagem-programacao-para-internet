
package com.cepestm.turmab.repository;

import com.cepestm.turmab.entity.UsuarioPerfil;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, UUID> {
    // Busca todos os vínculos de perfil associados a um determinado usuário
    List<UsuarioPerfil> findByUsuarioId(UUID usuarioId);
    // Busca todos os usuários que possuem um determinado perfil
    List<UsuarioPerfil> findByPerfilId(UUID perfilId);
    
    // verifica se um vínculo específico já existe, evitando registros duplicados no banco:
    boolean existsByUsuarioIdAndPerfilId(UUID usuarioId, UUID perfilId);
}
