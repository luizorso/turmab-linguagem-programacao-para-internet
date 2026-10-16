
package com.cepestm.turmab.service;

import com.cepestm.turmab.dto.UsuarioDTO;
import com.cepestm.turmab.entity.Usuario;
import com.cepestm.turmab.exception.ResourceNotFoundException;
import com.cepestm.turmab.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service

public class UsuarioService {
    
   public final UsuarioRepository repository; 
   
   public UsuarioService(UsuarioRepository repository){
       this.repository = repository;
   }
   
   @Transactional
   public Usuario saveByDTO(UsuarioDTO dto){
       Usuario usuario = repository.save(dto.toUsuario());
       return usuario;
   }
   
   @Transactional(readOnly = true)
   public List<Usuario> listAll(){
       return repository.findAll();
   }
   
   @Transactional(readOnly = true)
   public Usuario listById(UUID id){
      return repository.findById(id).orElseThrow(()->
        new ResourceNotFoundException(
                "Usuário não encontrado para o ID: " +id));
  }
   
   @Transactional
   public Usuario update(UUID id, UsuarioDTO dto){
       Usuario usuario = listById(id);
       usuario.setNome(dto.nome());
       usuario.setLogin(dto.login());
       //Ideal: Aplicar a função has aqui se utilizar o Spring Security
       usuario.setSenha(dto.senha());
       usuario.setEmail(dto.email());
       usuario.setStatus(dto.status());
       return repository.save(usuario);
       
   }
   
   @Transactional
   public void deleteById(UUID id){
       Usuario usuario = listById(id);
       repository.delete(usuario);
   }
   
   
   
}
