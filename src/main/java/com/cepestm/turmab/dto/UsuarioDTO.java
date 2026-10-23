package com.cepestm.turmab.dto;

import com.cepestm.turmab.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;


public record UsuarioDTO(
    UUID id,
    @NotBlank(message = "O nome é obrigatório!")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres!")
    /*
    Aceita todas as letras maiúsculas, minúsculas, com acentos (como ã, é, ç, ó) comuns 
    na língua portuguesa e espaços entre os nomes, bloqueando números e caracteres 
    especiais indesejados.
    */
    @Pattern(
        regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$", 
        message = "O nome deve conter apenas letras e espaços!"
    )
    String nome,
    @NotBlank(message="O nome login do usuário é obrigatório!")
    @Size(min =8, max = 16, message = "O login do usuário deve ter entre 8 e 16 caracteres!")
    String login,
    @NotBlank(message="A senha é obrigatória!")
    @Size(min = 8, max = 256, message="A senha deve ter no mínimo 8 caracteres")
    /*
    A regex utilizada (^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&._-]).+$) 
    valida cada exigência através de "olhadas adiante" (lookaheads):

    ^(?=.*[a-z]): Garante que existe pelo menos uma letra minúscula de a a z.

    (?=.*[A-Z]): Garante que existe pelo menos uma letra maiúscula de A a Z.

    (?=.*\d): Garante que existe pelo menos um número (\d).

    (?=.*[@$!%*?&._-]): Garante que existe pelo menos um caractere especial 
    dentre os colchetes (você pode adicionar ou remover símbolos conforme a 
    política de segurança da sua aplicação).

    .+$: Garante que a string possui conteúdo válido após validar as regras acima 
    (o tamanho mínimo e máximo já está sendo controlado de forma limpa pelo 
    seu @Size).
    
    */
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._-]).+$",
        message = "A senha deve conter ao menos uma letra minúscula, uma maiúscula, um número e um caractere especial (@$!%*?&._-)!"
    )
    String senha,
   @NotBlank(message = "O e-mail é obrigatório!")
    @Email(message = "Informe um e-mail válido!")
    @Size(min = 6, max = 100, message = "O e-mail deve ter entre 6 e 100 caracteres!")
    String email,
   
   @NotNull(message = "O status é obrigatório!")
    Boolean status
    ) 
{
    //Construtor utilitário para cadastrar novos usuarios sem o ID
    
    public UsuarioDTO(String nome, String login, String senha, 
            String email, boolean status){
        this(null, nome, login, senha, email, status);
    }
    
    /*Construtor para Transformar Entidade em DTO Facilmente */
    
    public UsuarioDTO(Usuario usuario){
        this(
        usuario.getId(),
        usuario.getNome(),
        usuario.getLogin(),
        usuario.getSenha(),
        usuario.getEmail(),
        usuario.isStatus()
        );
    }
    
    public Usuario toUsuario(){
        Usuario u = new Usuario();
        u.setId(this.id);
        u.setNome(this.nome);
        u.setLogin(this.login);
        u.setSenha(this.senha);
        u.setEmail(this.email);
        u.setStatus(this.status);
        return u;
    }
    

}
