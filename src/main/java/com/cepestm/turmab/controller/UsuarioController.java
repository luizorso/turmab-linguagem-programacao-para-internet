
package com.cepestm.turmab.controller;

import com.cepestm.turmab.dto.UsuarioDTO;
import com.cepestm.turmab.entity.Usuario;
import com.cepestm.turmab.service.UsuarioService;
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
    
    /*
        A anotação @Valid deve ser colocada antes de @ModelAttribute. Isso
    informa ao Spring Boot para verificar as anotações de validação
    (@Not Blank, @Size e @Email) presentes na classe DTO
    */
    
    /*
    BindingResult: deve vir imediatamente após o objeto validadeo. Se você
    alterar a ordem ou colocar o Model entre eles, o Spring lança uma exceção.
    Ele captura as falhas de validação.
    */
    
    @PostMapping("/salvar")
    public String salvarUsuario(@Valid @ModelAttribute("usuarioDTO") UsuarioDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redAtributos,
            Model model){
        /*
        Se o usuário preencher algo errado, o código impede o salvamento no banco,
        mantém os dados digitados e devolve o fluxo para a view cadastrarUsuario
        para que os erros possam ser exibidos na tela.
        */
        if(bindingResult.hasErrors()){
            model.addAttribute("titulo","Cadastro de Usuário");
            model.addAttribute("usuarioDTO", dto); // Mantém os dados digitados na tela
            return "cadastrarUsuario";
        }
        
        try {
            // 2. Tenta salvar ou atualizar aplicando a regra do service
            if(dto.id() != null ){
                service.update(dto.id(), dto);
                redAtributos.addFlashAttribute("mensagem", 
                    "Dados do usuário atualizados com sucesso!");
            }else{
                service.saveByDTO(dto);
                redAtributos.addFlashAttribute("mensagem",
                    "Dados do usuário cadastrados com sucesso!");
            }
        } catch (IllegalArgumentException e) {
            // 3. Captura a exceção de e-mail duplicado lançada pelo Service
            // e vincula a mensagem diretamente ao campo 'email' na tela
            bindingResult.rejectValue("email", "error.usuarioDTO", e.getMessage());
            // Recarrega os atributos necessários para a view não quebrar
            model.addAttribute("titulo", "Cadastro de Usuário");
            model.addAttribute("usuarioDTO", dto);
            return "cadastrarUsuario"; 
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
