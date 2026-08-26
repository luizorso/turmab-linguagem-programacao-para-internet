
package com.cepestm.turmab.service;

import com.cepestm.turmab.dto.PerfilDTO;
import com.cepestm.turmab.entity.Perfil;
import com.cepestm.turmab.repository.PerfilRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PerfilService {
    private final PerfilRepository rep;
    
    public PerfilService(PerfilRepository rep){
        this.rep = rep;
        
    }
    // converte o DTO para Entidade
    @Transactional
    public Perfil savedByDTO(PerfilDTO dto){
         // executa o INSERT no banco
        Perfil perfil = rep.save(dto.toPerfil());
        
        return perfil;
        
    }
    //Retorna todos os registros da tabela e armazena
    //em um objeto List
    public List<Perfil> listAll(){
        return rep.findAll();
        
    }
    
    
    /*
        findById(id)> retorna um objeto do tipo optional <Perfil>.
        Usamos .orElseThrow para lançar uma exceção amigável
        caso o Id buscado não exista no banco de dados.
    */
    public Perfil listById(UUID id){
        return rep.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Perfil não encontrado para o ID: " + id));
    }
    
    @Transactional
    public Perfil update(UUID id, PerfilDTO dto){
        //Busca o perfil existente no banco de dados
        Perfil perfil = listById(id);
        //Atualiza os dados da entidade com as informações
        //recebidas no DTO.
        perfil.setNome(dto.nome());
        return rep.save(perfil);
    }
    
    @Transactional
    public void deleteById(UUID id){
        Perfil perfil = listById(id);
        rep.delete(perfil);
    }
    
    
    
}
