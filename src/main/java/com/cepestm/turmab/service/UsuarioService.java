
package com.cepestm.turmab.service;

import com.cepestm.turmab.dto.UsuarioDTO;
import com.cepestm.turmab.entity.Perfil;
import com.cepestm.turmab.entity.Usuario;
import com.cepestm.turmab.exception.ResourceNotFoundException;
import com.cepestm.turmab.repository.PerfilRepository;
import com.cepestm.turmab.repository.UsuarioPerfilRepository;
import com.cepestm.turmab.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service

public class UsuarioService {
    
   private final UsuarioPerfilRepository usuarioPerfilRepository;
    
   private final UsuarioRepository usuarioRepository; 
   
   private final PerfilRepository perfilRepository;
   
  public UsuarioService(UsuarioPerfilRepository usuarioPerfilRepository,
        UsuarioRepository usuarioRepository,
        PerfilRepository perfilRepository){
        this.usuarioPerfilRepository = usuarioPerfilRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
    }
   
  /*     busca os perfis no banco utilizando 
    perfilRepository.findAllById(dto.perfisIds()) 
    com base nos IDs selecionados na interface e utiliza o método auxiliar 
    adicionarPerfil na entidade Usuario para persistir o relacionamento.
  */
   @Transactional
   public Usuario saveByDTO(UsuarioDTO dto) {
        // 1. Verifica se já existe um usuário cadastrado com este e-mail
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException(
                "Já existe um usuário cadastrado com este e-mail!");
        }
        
        // 2. Converte o DTO para Entidade básica
        Usuario usuario = dto.toUsuario();
        
        // Processa a vinculação dos perfis selecionados no <select multiple>
        if (dto.perfisIds() != null && !dto.perfisIds().isEmpty()) {
            List<UUID> uuids = dto.perfisIds().stream().map(UUID::fromString).toList();
            List<Perfil> perfisSelecionados = 
                    perfilRepository.findAllById(uuids);
          
            for (Perfil perfil : perfisSelecionados) {
                usuario.adicionarPerfil(perfil); 
            }
        }
        
        // 4. Salva o usuário (o CascadeType.ALL cuidará da tabela associativa)
        return usuarioRepository.save(usuario);
    }
   
   @Transactional(readOnly = true)
   public List<Usuario> listAll(){
       return usuarioRepository.findAll();
   }
   
   @Transactional(readOnly = true)
   public Usuario listById(UUID id){
      return usuarioRepository.findById(id).orElseThrow(()->
        new ResourceNotFoundException(
            "Usuário não encontrado para o ID: " +id));
  }
   /*
    update inteligente de perfis: Na edição, limpamos a lista atual 
   (usuario.getPerfis().clear()) e adicionamos novamente apenas os IDs que 
   o usuário marcou no formulário multi-seleção, garantindo que adições e 
   remoções de perfis sejam refletidas corretamente ao atualizar o cadastro.
   */
   @Transactional
   public Usuario update(UUID id, UsuarioDTO dto){
       //1. Busca no banco se já existe algum usuário com o e-mail informado
       
       var usuarioExistente = usuarioRepository.findByEmail(dto.email());
       
       /*2. Se encontrou um usuário com esse e-mail e o ID desse usuário é
       diferente do ID que estamos editando 
       */
       if(usuarioExistente.isPresent() && 
               !usuarioExistente.get().getId().equals(id)){
           throw new IllegalArgumentException(
                   "Este e-mail já está sendo utilizado por outro usuário!");
       }
       
       /*
       !usuarioExistente.get().getId().equals(id): Esta é a checagem de 
       segurança principal.  Ela garante que se o e-mail encontrado pertencer 
       ao próprio usuário que está na tela editando seus dados, a alteração 
       é permitida. Se pertencer a outro usuário cadastrado, a exceção 
       de e-mail duplicado é lançada com sucesso!
       */
               
       
       /*Se passou pela validação, busca o usuário atual, 
       atualiza os dados e salva */
       Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Usuário não encontrado!"));
       usuario.setNome(dto.nome());
       usuario.setLogin(dto.login());
       //Ideal: Aplicar a função has aqui se utilizar o Spring Security
       usuario.setSenha(dto.senha());
       usuario.setEmail(dto.email());
       usuario.setStatus(dto.status());
       /*Atualiza os perfis: Limpa os antigos e adiciona os novos 
       vindos do <select multiple> */
        usuario.getPerfis().clear(); 

        // Remove os vínculos atuais (com orphanRemoval=true, limpa o banco)
        
        if (dto.perfisIds() != null && !dto.perfisIds().isEmpty()) {
            List<UUID> uuids = dto.perfisIds().stream().map(UUID::fromString).toList();
            List<Perfil> perfisSelecionados = 
                    perfilRepository.findAllById(uuids);
            for (Perfil perfil : perfisSelecionados) {
                usuario.adicionarPerfil(perfil);
            }
        }
       return usuarioRepository.save(usuario);
       
   }
   
   @Transactional
   public void deleteById(UUID id){
       Usuario usuario = listById(id);
       usuarioRepository.delete(usuario);
   }
   
      
  
    

}
   
   
   

