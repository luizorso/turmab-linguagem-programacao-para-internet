
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
@Table(name="perfil")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name="nome", nullable=false, length=64)
    private String nome;
    
    //Relacionamento com a tabela associativa UsuarioPerfil
    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioPerfil> usuarios = new ArrayList<>();
    
    /* Atributo usuarios: Adicionado o mapeamento mappedBy = "perfil", que espelha 
    o relacionamento definido na entidade intermediária UsuarioPerfil.
    
    
    */
    
}
