package banco.projeto.monetario.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

//Controller referente às páginas
@Controller
public class PagesController {

    @GetMapping("/login")
    public String login(){
        return "html/login";
    }

    @GetMapping("/cadastro")
    public String cadastro(){
        return "html/cadastrar";
    }

    @GetMapping("/principal")
    public String principal(HttpSession session){
            return "html/telaPrincipal";
    }

    @GetMapping("/conta")
    public String conta(HttpSession session){

        long userId = (Long) session.getAttribute("userId");
        System.out.println(userId);

        return "html/conta";
    }

    
}
