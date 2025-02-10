package banco.projeto.monetario.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import banco.projeto.monetario.DTO.OperacoesDTO;
import banco.projeto.monetario.domain.Operacoes;
import banco.projeto.monetario.request.OperacoesPostRequestBody;
import banco.projeto.monetario.service.OperacoesService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OperacoesController {

    private final OperacoesService operacoesService;

    @PostMapping("/save_operacao")
    public ResponseEntity<OperacoesDTO> saveOperacao(@RequestBody OperacoesPostRequestBody operacoesPostRequestBody, HttpSession session){

        Long userId = (Long) session.getAttribute("userId");
        Operacoes operacoes = operacoesService.saveOperacoes(operacoesPostRequestBody, userId);

        OperacoesDTO dto = new OperacoesDTO(
            operacoes.getTipo(),
            operacoes.getDataOperacao(),
            operacoes.getValor()
        );

        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

}
