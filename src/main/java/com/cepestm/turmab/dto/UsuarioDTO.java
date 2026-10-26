package com.cepestm.turmab.dto;

import com.cepestm.turmab.entity.Usuario;
import com.cepestm.turmab.entity.UsuarioPerfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record UsuarioDTO(
    UUID id,
    
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome não deve exceder 100 caracteres")
    String nome,
    
    @NotBlank(message = "O login é obrigatório")
    @Size(max = 100, message = "O login não deve exceder 100 caracteres")
    String login,
    
    @NotBlank(message = "A senha é obrigatória")
    @Size(max = 256, message = "A senha não deve exceder 256 caracteres")
    String senha,
    
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Insira um e-mail válido")
    @Size(max = 150, message = "O e-mail não deve exceder 150 caracteres")
    String email,
    
    boolean status,
    
    // Adicionado o campo para que o Thymeleaf consiga ler na view (caso precise)
    List<UsuarioPerfil> perfis,
    
    // Campo adicionado para receber os IDs selecionados no <select multiple> do formulário
    /*
    Adicionado List<String> perfisIds: Este componente é o responsável por fazer o binding 
    direto com o atributo th:field="*{perfisIds}" que definimos no elemento 
    <select multiple> do Thymeleaf.
    */
   // Alterado de List<UUID> para List<String> para corresponder aos valores textuais do HTML
    List<String> perfisIds
) {
    
    // Construtor utilitário para cadastros novos (Cria a lista vazia automaticamente)
    public UsuarioDTO(String nome, String login, String senha, String email, boolean status) {
        this(null, nome, login, senha, email, status, new ArrayList<>(), new ArrayList<>());
    }
    
    // Construtor de conversão (Utilizado no método editarUsuario do seu Controller)
    /* Atualização dos Construtores: Como o record exige que todos os parâmetros do 
    cabeçalho principal sejam informados, ajustamos os construtores secundários 
    (this(...)) para passar valores padrão (como new ArrayList<>()) para os campos 
    perfis e perfisIds.
    
    */
  // CONSTRUTOR DE CONVERSÃO ATUALIZADO PARA EDIÇÃO
    /* Permite que os perfis previamentes atribuidos aos usuários sejam exibidos
     no select múltiplo ao abrir a tela de edição
     
    */
// CONSTRUTOR DE CONVERSÃO PARA EDIÇÃO
  // CONSTRUTOR DE CONVERSÃO PARA EDIÇÃO (Padrão Java seguro, sem preview)
    public UsuarioDTO(Usuario usuario) {
        this(
            usuario.getId(),
            usuario.getNome(),
            usuario.getLogin(),
            usuario.getSenha(),
            usuario.getEmail(),
            usuario.isStatus(),
            usuario.getPerfis(), 
            extrairIdsPerfis(usuario.getPerfis())
        );
        // ADICIONE ESTA LINHA:
    System.out.println(">>> DTO CRIADO PARA: " + usuario.getNome() + " | IDs dos Perfis: " + extrairIdsPerfis(usuario.getPerfis()));
    }
    
    // Método auxiliar para tratar a extração dos IDs sem violar as regras de construtor do Java
    private static List<String> extrairIdsPerfis(List<UsuarioPerfil> perfis) {
        if (perfis == null) {
            return new ArrayList<>();
        }
        return perfis.stream()
                     .map(up -> up.getPerfil().getId().toString()) // Converte o UUID para String
                     .toList();
    }
    
    /*
    O método toUsuario() serve para converter (ou mapear) um objeto do tipo atual 
    (geralmente um DTO ou Form) em uma entidade do tipo Usuario
    */
    public Usuario toUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setLogin(login);
        usuario.setSenha(senha);
        usuario.setEmail(email);
        usuario.setStatus(status);
        // O relacionamento em cascata é gerenciado via métodos auxiliares
        return usuario;
    }
    
    
}
