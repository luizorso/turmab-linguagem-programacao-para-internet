
package com.cepestm.turmab.controller;

import com.cepestm.turmab.dto.PerfilDTO;
import com.cepestm.turmab.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    
  
    
}
