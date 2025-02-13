package banco.projeto.monetario.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import banco.projeto.monetario.DTO.UsuarioAtualizarDTO;
import banco.projeto.monetario.DTO.UsuarioDTO;
import banco.projeto.monetario.domain.Usuario;
import banco.projeto.monetario.request.TrocarSenhaPutRequestBody;
import banco.projeto.monetario.request.UsuarioLoginRequestBody;
import banco.projeto.monetario.request.UsuarioPostRequestBody;
import banco.projeto.monetario.request.UsuarioPutRequestBody;
import banco.projeto.monetario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpSession;

//Controller da classe usuario
@RestController
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping("/pegar_nome")
    public ResponseEntity<String> pegarNome(HttpSession session){
        Long userId = (Long) session.getAttribute("userId");
        return new ResponseEntity<>(usuarioService.pegarNome(userId), HttpStatus.OK);
    }

    @GetMapping("/pegar_informacoes")
    public ResponseEntity<?> pegarInformacoes(HttpSession session){

        Long userId = (Long) session.getAttribute("userId");

        Usuario usuario = usuarioService.pegarInformacoes(userId);
        
        UsuarioDTO dto = new UsuarioDTO(
            usuario.getNome(),
            usuario.getNomeCompleto(),
            usuario.getEmail(),
            usuario.getDataNascimento(),
            usuario.getSenha(),
            usuario.getCpf(),
            usuario.getCep()
        );

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody UsuarioPostRequestBody usuarioPostRequestBody){
        return new ResponseEntity<>(usuarioService.salvarUsuario(usuarioPostRequestBody), HttpStatus.OK);
    }

    @PostMapping("/logar")
    public ResponseEntity<String> logar(@RequestBody UsuarioLoginRequestBody usuarioLoginRequestBody, HttpSession session) {
        boolean loginValido = usuarioService.validarLogin(usuarioLoginRequestBody);

        if (loginValido) {
            Long userId = usuarioService.buscarIdPorEmail(usuarioLoginRequestBody.getEmail());
            session.setAttribute("userId", userId);
            return ResponseEntity.ok("Login realizado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos");
        }
    }

    @PutMapping("atualizar_informacoes")
    public ResponseEntity<UsuarioAtualizarDTO> atualizarInformacoes(@RequestBody UsuarioPutRequestBody usuarioPutRequestBody, HttpSession session){
        Long userId = (Long) session.getAttribute("userId");

        Usuario usuario = usuarioService.atualizarInformacoes(usuarioPutRequestBody, userId);

        UsuarioAtualizarDTO dto = new UsuarioAtualizarDTO(
            usuario.getNome(),
            usuario.getNomeCompleto(),
            usuario.getEmail(),
            usuario.getDataNascimento(),
            usuario.getCpf(),
            usuario.getCep()
        );

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("trocar_senha")
    public ResponseEntity<Usuario> trocarSenha(@RequestBody TrocarSenhaPutRequestBody trocarSenhaPutRequestBody, HttpSession session){
        System.out.println("VAI COMEÇAR AQUI");

        Long userId = (Long) session.getAttribute("userId");


        return new ResponseEntity<Usuario>(usuarioService.trocarSenha(trocarSenhaPutRequestBody, userId), HttpStatus.OK);
    }

    @PostMapping("deslogar")
    public ResponseEntity<String> deslogar(HttpSession session){
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body("Usuário deslogado com sucesso!");
    }


}
