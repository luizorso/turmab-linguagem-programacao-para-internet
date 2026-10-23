
package com.cepestm.turmab.controller;

import com.cepestm.turmab.dto.UsuarioDTO;
import com.cepestm.turmab.entity.Usuario;
import com.cepestm.turmab.service.UsuarioService;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService service;
    
    //Injeção de Dependência recomendada pelo navegador
    public UsuarioController(UsuarioService service){
        this.service = service;
    }
    
    @GetMapping("/novo")
    public String cadastrarUsuario(Model model){
       model.addAttribute("titulo", "Cadastro de Usuário");
       model.addAttribute("usuarioDTO", 
               new Usuario(null, "", "", "","", true));
       return "cadastrarUsuario";
    }
    
    @PostMapping("/salvar")
    public String salvarUsuario(@ModelAttribute("usuarioDTO") UsuarioDTO dto,
                RedirectAttributes redAtributos){
        
        if(dto.id() != null ){
            service.update(dto.id(), dto);
            redAtributos.addFlashAttribute("mensagem", 
                    "Dados do usuários atualizados com sucesso!");
        }else{
            service.saveByDTO(dto);
            redAtributos.addFlashAttribute("mensagem",
                    "Dados do usuários cadastrados com sucesso!");
            
        }
       return "redirect:/usuarios"; 
    }
    
    @GetMapping
    public String listarUsuarios(Model model){
        List<Usuario> usuarios = service.listAll();
        model.addAttribute("usuarios", usuarios);
        return "listarUsuarios";
    }
    
    @PostMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable("id") UUID id,
        RedirectAttributes redAtributos){
        service.deleteById(id);
        redAtributos.addFlashAttribute("mensagem", "Usuário excluído com sucesso!");
        return "redirect:/usuarios";
    }
    
    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") UUID id,
        Model model){
        Usuario usuario = service.listById(id);
        UsuarioDTO dto = new UsuarioDTO(usuario);
        model.addAttribute("titulo", "Editar Usuário");
        model.addAttribute("usuarioDTO", dto);
        return "cadastrarUsuario";
    }
    
}
