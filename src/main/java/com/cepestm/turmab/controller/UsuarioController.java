
package com.cepestm.turmab.controller;

import com.cepestm.turmab.dto.UsuarioDTO;
import com.cepestm.turmab.entity.Usuario;
import com.cepestm.turmab.service.PerfilService;
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
    private final UsuarioService usuarioService;
    private final PerfilService perfilService;
    
    //Injeção de Dependência recomendada pelo navegador
    public UsuarioController(UsuarioService usuarioService, PerfilService perfilService){
        this.usuarioService = usuarioService;
        this.perfilService = perfilService;
    }
    
    /*
    Foi adicionado todosPerfis no método /novo: Como o <select multiple> de 
    perfis agora é exibido tanto no cadastro quanto na edição, a listagem de 
    todos os perfis cadastrados precisa estar presente no Model quando 
    criamos um usuário novo.
    */
    
    @GetMapping("/novo")
    public String cadastrarUsuario(Model model){
       model.addAttribute("titulo", "Cadastro de Usuário");
          
       // Instancia o Record DTO usando o construtor utilitário (sem ID)
        model.addAttribute("usuarioDTO", new UsuarioDTO("", "", "", "", true));
        
        // Adicionado para o <select multiple> funcionar no cadastro novo
        model.addAttribute("todosPerfis", perfilService.listAll());
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
public String salvarUsuario(@Valid @ModelAttribute("usuarioDTO") 
        UsuarioDTO dto,
        BindingResult bindingResult,
        RedirectAttributes redAtributos,
        Model model) {
    
    /*
    1. Validação de Campos: Se o usuário preencher algo errado de acordo com 
    as anotações do DTO, o código impede o salvamento, mantém os dados e 
    devolve para a tela exibir as mensagens.
    */
    
    /* Foi adicionado todosPerfis nos blocos de erro (hasErrors e catch): Se a 
    validação falhar, o Spring retorna para a view cadastrarUsuario. 
    Se o todosPerfis não for reenviado ao Model nessas falhas, a página quebrará.
    
    */
    if (bindingResult.hasErrors()) {
        model.addAttribute("titulo", dto.id() != null ? 
                "Editar Usuário" : "Cadastro de Usuário");
        model.addAttribute("usuarioDTO", dto);
        
        // Garante que os perfis não sumam se a validação falhar
        model.addAttribute("todosPerfis", perfilService.listAll());
        
        return "cadastrarUsuario";
    }
    
    try {
        /*
        2. Regra de Negócio: Identifica se é uma atualização (ID preenchido) 
        ou um novo cadastro (ID nulo) e aciona o método correto do service.
        */
        if (dto.id() != null) {
            usuarioService.update(dto.id(), dto);
            redAtributos.addFlashAttribute("mensagem", 
                    "Dados do usuário atualizados com sucesso!");
        } else {
            usuarioService.saveByDTO(dto);
            redAtributos.addFlashAttribute("mensagem", 
                    "Dados do usuário cadastrados com sucesso!");
        }
    } catch (IllegalArgumentException e) {
        /*
        3. Tratamento de Exceção: Captura a validação de e-mail duplicado 
        lançada pelo Service e vincula o erro diretamente ao campo 'email' 
        do formulário Thymeleaf.
        */
        bindingResult.rejectValue("email", "error.usuarioDTO", e.getMessage());
        model.addAttribute("titulo", dto.id() != null ? 
                "Editar Usuário" : "Cadastro de Usuário");
        model.addAttribute("usuarioDTO", dto);
        /*Garante que os perfis não sumam em caso de exceção
        (ex: e-mail duplicado)
        */
        model.addAttribute("todosPerfis", perfilService.listAll());
        return "cadastrarUsuario"; 
    }
    
    return "redirect:/usuarios"; 
}

    
    @GetMapping
    public String listarUsuarios(Model model){
        List<Usuario> usuarios = usuarioService.listAll();
        model.addAttribute("usuarios", usuarios);
        return "listarUsuarios";
    }
    
    @PostMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable("id") UUID id,
        RedirectAttributes redAtributos){
        usuarioService.deleteById(id);
        redAtributos.addFlashAttribute("mensagem", 
                "Usuário excluído com sucesso!");
        return "redirect:/usuarios";
    }
    
   
    
     @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") UUID id, Model model) {
        Usuario usuario = usuarioService.listById(id);
        UsuarioDTO dto = new UsuarioDTO(usuario); 
        
        // ADICIONE ESTA LINHA:
        System.out.println(">>> CONTROLLER - Qtd PerfisIds no DTO: " + dto.perfisIds().size());
        
        model.addAttribute("titulo", "Editar Usuário");
        model.addAttribute("usuarioDTO", dto);
        
        //Usa o objeto perfilService que foi injetado pelo construtor
        model.addAttribute("todosPerfis", perfilService.listAll()); 
        
        return "cadastrarUsuario";
    }

    
    
}
