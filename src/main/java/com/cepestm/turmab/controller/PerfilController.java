
package com.cepestm.turmab.controller;

import com.cepestm.turmab.dto.PerfilDTO;
import com.cepestm.turmab.entity.Perfil;
import com.cepestm.turmab.service.PerfilService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfis")
public class PerfilController{
    
    //Injeção de Dependência recomendada pelo navegador
    private final PerfilService service;
    
    public PerfilController(PerfilService service){
        this.service = service;
        
    }
    
    @GetMapping("/novo")
    public String cadastrarPerfil(Model model){
        model.addAttribute("titulo", "Cadastro de Perfil");
        model.addAttribute("perfilDTO", new PerfilDTO(""));
        return "cadastrarPerfil";
    }
    
    @PostMapping("/salvar")
    public String salvarPerfil(@Valid @ModelAttribute("perfilDTO")
            PerfilDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redAtributos,
            Model model){
        
        if(bindingResult.hasErrors()){
            model.addAttribute("titulo", "Cadastro de Perfil");
            model.addAttribute("perfilDTO", dto);
            return "cadastrarPerfil";
        }
        
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
    
    @GetMapping
    public String listarPerfis(Model model){
        /* Busca todos os objetos perfis e armazena
        na Lista Perfis
        */
        List<Perfil> perfis = service.listAll();
        //Adiciona a lista perfis ao objeto model 
        model.addAttribute("perfis", perfis);
        return "listarPerfis";
    }
    
    @PostMapping("/excluir/{id}")
    public String excluirPerfil(@PathVariable("id")UUID id,
        RedirectAttributes redAtributos){
        service.deleteById(id);
        redAtributos.addFlashAttribute(
                "mensagem", 
                "Perfil excluído com sucesso!");
        return "redirect:/perfis";
        
    }
    @GetMapping("/editar/{id}")
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
