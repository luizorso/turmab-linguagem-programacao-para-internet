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
            PerfilRepository perfilRepository) {
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
            List<Perfil> perfisSelecionados
                    = perfilRepository.findAllById(uuids);

            for (Perfil perfil : perfisSelecionados) {
                usuario.adicionarPerfil(perfil);
            }
        }

        // 4. Salva o usuário (o CascadeType.ALL cuidará da tabela associativa)
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listAll() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario listById(UUID id) {
        return usuarioRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException(
                        "Usuário não encontrado para o ID: " + id));
    }

    /*
    update inteligente de perfis: Na edição, limpamos a lista atual 
   (usuario.getPerfis().clear()) e adicionamos novamente apenas os IDs que 
   o usuário marcou no formulário multi-seleção, garantindo que adições e 
   remoções de perfis sejam refletidas corretamente ao atualizar o cadastro.
     */
    @Transactional
    public Usuario update(UUID id, UsuarioDTO dto) {
        // 1. Busca no banco se já existe algum usuário com o e-mail informado
        var usuarioExistente = usuarioRepository.findByEmail(dto.email());

        if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(id)) {
            throw new IllegalArgumentException("Este e-mail já está sendo utilizado por outro usuário!");
        }

        // 2. Busca o usuário atual no banco
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        // 3. Atualiza os dados básicos
        usuario.setNome(dto.nome());
        usuario.setLogin(dto.login());
        usuario.setSenha(dto.senha()); // Ideal: Aplicar a criptografia de senha aqui
        usuario.setEmail(dto.email());
        usuario.setStatus(dto.status());

        // 4. ATUALIZAÇÃO SEGURA DOS PERFIS (Evita problemas de sincronização do Hibernate)
        // Em vez de dar .clear() direto na lista, usamos um método utilitário na própria entidade
        usuario.removerTodosPerfis();

        // 5. Adiciona os novos perfis selecionados na tela
        if (dto.perfisIds() != null && !dto.perfisIds().isEmpty()) {
            List<UUID> uuids = dto.perfisIds().stream()
                    .map(UUID::fromString)
                    .toList();

            List<Perfil> perfisSelecionados = perfilRepository.findAllById(uuids);

            for (Perfil perfil : perfisSelecionados) {
                usuario.adicionarPerfil(perfil);
            }
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deleteById(UUID id) {
        Usuario usuario = listById(id);
        usuarioRepository.delete(usuario);
    }

}
