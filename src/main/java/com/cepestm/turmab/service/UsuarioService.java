
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
       // Verifica se já existe um usuário cadastrado com este e-mail
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail!");
        }
       // Se não existir, prossegue com a conversão e o salvamento
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
       //1. Busca no banco se já existe algum usuário com o e-mail informado
       
       var usuarioExistente = repository.findByEmail(dto.email());
       
       /*2. Se encontrou um usuário com esse e-mail e o ID desse usuário é
       diferente do ID que estamos editando 
       */
       if(usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(id)){
           throw new IllegalArgumentException("Este e-mail já está sendo utilizado por outro usuário!");
       }
       
       /*
       !usuarioExistente.get().getId().equals(id): Esta é a checagem de segurança principal. 
       Ela garante que se o e-mail encontrado pertencer ao próprio usuário que está na tela 
       editando seus dados, a alteração é permitida. Se pertencer a outro usuário cadastrado, 
       a exceção de e-mail duplicado é lançada com sucesso!
       */
               
       
       // 3. Se passou pela validação, busca o usuário atual, atualiza os dados e salva
       Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
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
