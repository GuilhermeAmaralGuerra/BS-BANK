package banco.projeto.monetario.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import banco.projeto.monetario.DTO.TransferenciaDTO;
import banco.projeto.monetario.domain.Transferencia;
import banco.projeto.monetario.request.TransferenciaPostRequestBody;
import banco.projeto.monetario.service.TransferenciaService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    @PostMapping("/save_transferir")
    public ResponseEntity<TransferenciaDTO> saveTransferencia(
        @RequestBody TransferenciaPostRequestBody transferenciaPostRequestBody, 
        HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Transferencia transferencia = transferenciaService.saveTransferencia(transferenciaPostRequestBody, userId);

        TransferenciaDTO dto = new TransferenciaDTO(
            transferencia.getValor(),
            transferencia.getData(),
            transferencia.getDescricao(),
            transferencia.getDestinatario()
        );
        
        return new ResponseEntity<>(dto, HttpStatus.OK);
}


}
