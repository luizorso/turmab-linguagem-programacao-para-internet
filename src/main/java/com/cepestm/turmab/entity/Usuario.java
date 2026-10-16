
package com.cepestm.turmab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name="nome", nullable= false, length = 100)
    private String nome;
    
    @Column(name="login", nullable = false, length = 100)
    private String login;
    
    @Column(name="senha", nullable = false, length= 256)
    private String senha;
    
    @Column(name="email", nullable = false, unique = true, length= 150)
    private String email;
    
    @Column(name="status", nullable = false, columnDefinition="boolean default true")
    private boolean status = true;
            
     
    
    
    
    
}
