package banco.projeto.monetario.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import banco.projeto.monetario.service.ContaService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

//Controller da classe conta
@RestController
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;

    @GetMapping("/pegar_saldo")
    public ResponseEntity<Double> pegarNome(HttpSession session){
        Long userId = (Long) session.getAttribute("userId");
        return new ResponseEntity<>(contaService.pegarSaldo(userId), HttpStatus.OK);
    }

}
