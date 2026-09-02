
package com.cepestm.turmab.controller;

import com.cepestm.turmab.dto.PerfilDTO;
import com.cepestm.turmab.entity.Perfil;
import com.cepestm.turmab.service.PerfilService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PerfilController{
    
    @Autowired
    private PerfilService service;
    
    @GetMapping("/cadastrarPerfil")
    public String cadastrarPerfil(Model model){
        model.addAttribute("titulo", "Cadastro de Perfil");
        model.addAttribute("perfilDTO", new PerfilDTO(""));
        return "cadastrarPerfil";
    }
    
    @PostMapping("cadastrarPerfil")
    public String salvarPerfil(PerfilDTO dto, 
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
        
        List<Perfil> perfis = service.listAll();
        return "listarPerfis";
    }
    
  
    
}
