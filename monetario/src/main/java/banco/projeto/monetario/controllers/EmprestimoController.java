package banco.projeto.monetario.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import banco.projeto.monetario.DTO.EmprestimoDTO;
import banco.projeto.monetario.domain.Emprestimo;
import banco.projeto.monetario.request.EmprestimoPostRequestBody;
import banco.projeto.monetario.request.EmprestimoPutRequestBody;
import banco.projeto.monetario.service.EmprestimoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @GetMapping("/pegar_jurosEValor")
    public ResponseEntity<List<Double>> pegarJurosEValor(HttpSession session){
        
        Long userId = (Long) session.getAttribute("userId");
        return new ResponseEntity<>(emprestimoService.pegarValorEJuros(userId), HttpStatus.OK);
        
    }

    @PostMapping("/enviar_emprestimo")
    public ResponseEntity<EmprestimoDTO> enviarEmprestimo(@RequestBody EmprestimoPostRequestBody emprestimoPostRequestBody, HttpSession session){

        Long userId = (Long) session.getAttribute("userId");
        Emprestimo emprestimo = emprestimoService.saveEmprestimo(emprestimoPostRequestBody, userId);

        EmprestimoDTO dto = new EmprestimoDTO(
            emprestimo.getValor(),
            emprestimo.getSituacao(),
            emprestimo.getDataEmprestimo(),
            emprestimo.getJuros(),
            emprestimo.getMotivo()
        );

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/pagar_emprestimo")
    public ResponseEntity<String> pagarEmprestimo(@RequestBody EmprestimoPutRequestBody emprestimoPutRequestBody, HttpSession session){
        
        Long userId = (Long) session.getAttribute("userId");
        String resultado = emprestimoService.processarPagamento(emprestimoPutRequestBody, userId);

        if (resultado.equals("Empréstimo não encontrado!")) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resultado);
    }

}
