
package com.cepestm.turmab.controller;

import com.cepestm.turmab.dto.PerfilDTO;
import com.cepestm.turmab.entity.Perfil;
import com.cepestm.turmab.service.PerfilService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PerfilController{
    
 
    private final PerfilService service;
    
    public PerfilController(PerfilService service){
        this.service = service;
        
    }
    
    @GetMapping("/cadastrarPerfil")
    public String cadastrarPerfil(Model model){
        model.addAttribute("titulo", "Cadastro de Perfil");
        model.addAttribute("perfilDTO", new PerfilDTO(""));
        return "cadastrarPerfil";
    }
    
    @PostMapping("/cadastrarPerfil")
    public String salvarPerfil(@ModelAttribute("perfilDTO")
            PerfilDTO dto, 
            RedirectAttributes redAtributos){
        if(dto.id() != null){
            service.update(dto.id(), dto);
            redAtributos.addFlashAttribute
                    ("mensagem", "Perfil atualizado com sucesso!" );
            
        }else{
            service.savedByDTO(dto);
            redAtributos.addFlashAttribute
                    ("mensagem", "Perfil salvo com sucesso!" );
          
        }
        return "redirect:/perfis";
    }
    
    @GetMapping("/perfis")
    public String listarPerfis(Model model){
        /* Busca todos os objetos perfis e armazena
        na Lista Perfis
        */
        List<Perfil> perfis = service.listAll();
        //Adiciona a lista perfis ao objeto model 
        model.addAttribute("perfis", perfis);
        return "listarPerfis";
    }
    
    @PostMapping("/perfis/excluir/{id}")
    public String excluirPerfil(@PathVariable("id")UUID id,
            RedirectAttributes redAtributos){
        service.deleteById(id);
        redAtributos.addFlashAttribute(
                "mensagem", 
                "Perfil excluído com sucesso!");
        return "redirect:/perfis";
        
    }
    @GetMapping("/perfis/editar/{id}")
    public String editarPerfil(@PathVariable("id") UUID id, 
           Model model){
        Perfil perfil = service.listById(id);
        PerfilDTO dto = 
                new PerfilDTO(perfil.getId(), perfil.getNome());
        
        model.addAttribute("titulo", "Editar Perfil");
        model.addAttribute("perfilDTO", dto);
        return "cadastrarPerfil";
        
    }
    
  
    
  
    
}
