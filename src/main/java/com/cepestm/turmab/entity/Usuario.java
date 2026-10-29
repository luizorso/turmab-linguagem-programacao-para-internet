package com.cepestm.turmab.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "login", nullable = false, unique = true, length = 100)
    private String login;

    @Column(name = "senha", nullable = false, length = 256)
    private String senha;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "status", nullable = false, columnDefinition = "boolean default true")
    private boolean status = true;

    //Relacionamento com a tabela associativa UsuarioPerfil
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioPerfil> perfis = new ArrayList<>();

    /*
    Atributo perfis: Inserida a lista mapeada por mappedBy = "usuario", indicando que o 
    controle do relacionamento está no atributo usuario da classe UsuarioPerfil. 
    O uso de cascade = CascadeType.ALL e orphanRemoval = true garante que os vínculos 
    sejam salvos ou removidos automaticamente junto com o usuário.
    
     */
    public void adicionarPerfil(Perfil perfil) {
        UsuarioPerfil usuarioPerfil = new UsuarioPerfil();
        usuarioPerfil.setUsuario(this); // 'this' associa este usuário atual ao vínculo
        usuarioPerfil.setPerfil(perfil); // associa o perfil recebido ao vínculo
        this.perfis.add(usuarioPerfil);  // adiciona o vínculo na lista que o JPA monitora
    }

    public void removerTodosPerfis() {
        if (this.perfis != null) {
            // Criamos uma cópia da lista para limpar os relacionamentos reversos sem quebrar o laço de repetição do Java
            List<UsuarioPerfil> copiaPerfis = new ArrayList<>(this.perfis);
            for (UsuarioPerfil up : copiaPerfis) {
                up.setUsuario(null);
                up.setPerfil(null);
            }
            // Agora limpamos a lista original monitorada pelo JPA para ativar o orphanRemoval
            this.perfis.clear();
        }
    }

}
