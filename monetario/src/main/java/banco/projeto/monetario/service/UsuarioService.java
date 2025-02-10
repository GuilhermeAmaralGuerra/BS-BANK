package banco.projeto.monetario.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import banco.projeto.monetario.DTO.CepValidadoDTO;
import banco.projeto.monetario.domain.Usuario;
import banco.projeto.monetario.exception.CriptografiaException;
import banco.projeto.monetario.exception.DadoJaExisteException;
import banco.projeto.monetario.exception.UsuarioException;
import banco.projeto.monetario.exception.ValidacaoException;
import banco.projeto.monetario.repository.UsuarioRepository;
import banco.projeto.monetario.request.UsuarioLoginRequestBody;
import banco.projeto.monetario.request.UsuarioPostRequestBody;
import banco.projeto.monetario.request.UsuarioPutRequestBody;
import banco.projeto.monetario.utils.CriptografiaUtils;
import banco.projeto.monetario.utils.DescriptografiaUtils;
import banco.projeto.monetario.utils.ValidacaoUtils;
import banco.projeto.monetario.utils.VerificacaoUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final ContaService contaService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional(rollbackFor = Exception.class)
    public Usuario saveUsuario(UsuarioPostRequestBody usuarioPostRequestBody){

        if (usuarioRepository.existsByCpf(usuarioPostRequestBody.getCpf())) {
            throw new DadoJaExisteException("CPF já cadastrado no sistema: " + usuarioPostRequestBody.getCpf());
        }
        
        if (usuarioRepository.existsByCep(usuarioPostRequestBody.getCep())) {
            throw new DadoJaExisteException("CEP já cadastrado no sistema: " + usuarioPostRequestBody.getCep());
        }        

        if (!ValidacaoUtils.validarCPF(usuarioPostRequestBody.getCpf())) {
            throw new ValidacaoException("CPF inválido");
        }

        if (!validarCepViaCep(usuarioPostRequestBody.getCep())) {
            throw new ValidacaoException("CEP inválido!");
        }

        String senhaCodificada = CriptografiaUtils.codificarSenha(usuarioPostRequestBody.getSenha());

        String cpfCriptografado = null;
        String cepCriptografado = null;

        try {
            cpfCriptografado = CriptografiaUtils.criptografarCepCpf(usuarioPostRequestBody.getCpf());
            cepCriptografado = CriptografiaUtils.criptografarCepCpf(usuarioPostRequestBody.getCep());
        } catch (CriptografiaException e) {
            throw e;
        }

        Usuario usuario = Usuario.builder()
                            .nome(usuarioPostRequestBody.getNome())
                            .nomeCompleto(usuarioPostRequestBody.getNomeCompleto())
                            .email(usuarioPostRequestBody.getEmail())
                            .senha(senhaCodificada)
                            .cpf(cpfCriptografado)
                            .dataNascimento(usuarioPostRequestBody.getDataNascimento())
                            .cep(cepCriptografado)
                            .build();

        Usuario usuarioCriado = usuarioRepository.save(usuario);

        contaService.saveConta(usuarioPostRequestBody);

        return usuarioCriado;
    }

    public boolean validarLogin(UsuarioLoginRequestBody usuarioLoginRequestBody) {
        String senhaCodificada = usuarioRepository.findPasswordByEmail(usuarioLoginRequestBody.getEmail());
    
        if (senhaCodificada != null && VerificacaoUtils.verificarSenha(usuarioLoginRequestBody.getSenha(), senhaCodificada)) {
            return true;
        } else {
            return false;
        }
    }

    public Long buscarIdPorEmail(String email){
        Long userId = usuarioRepository.findIdByEmail(email);
        return userId;
    }
    
    public String pegarNome(Long id){
        String nome = usuarioRepository.findNomeById(id);
        return nome;
    }

    public boolean validarCepViaCep(String cep){
        String url = String.format("https://viacep.com.br/ws/%s/json/", cep);
        ResponseEntity<CepValidadoDTO> resposta = restTemplate.getForEntity(url, CepValidadoDTO.class);
    
        if (resposta.getBody() != null) {
            if (resposta.getBody().getErro() == null) {
                return true;
            } else {
                throw new ValidacaoException("CEP inexistente!");
            }
        }  else {
            throw new ValidacaoException("CEP inexistente!");
        }
    }

    public Usuario pegarInformacoes(Long userId){

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(userId);

        if (usuarioOptional.isEmpty()) {
            
            throw new UsuarioException("Usuário não encontrado"); 
        }

        Usuario usuario = usuarioOptional.get();

        String cpfDescriptografado = DescriptografiaUtils.descriptografarCepCef(usuario.getCpf());
        String cepDescriptografado = DescriptografiaUtils.descriptografarCepCef(usuario.getCep());

        usuario.setCpf(cpfDescriptografado);
        usuario.setCep(cepDescriptografado);

        return usuario;
    }   

    @Transactional
    public Usuario atualizarInformacoes(UsuarioPutRequestBody usuarioPutRequestBody, Long userId){

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(userId);

        if (usuarioOptional.isEmpty()) {
            
            throw new UsuarioException("Usuário não encontrado"); 
        }

        if (usuarioRepository.existsByCpf(usuarioPutRequestBody.getCpf())) {
            throw new DadoJaExisteException("CPF já cadastrado no sistema: " + usuarioPutRequestBody.getCpf());
        }
        
        if (usuarioRepository.existsByCep(usuarioPutRequestBody.getCep())) {
            throw new DadoJaExisteException("CEP já cadastrado no sistema: " + usuarioPutRequestBody.getCep());
        }        

        if (!ValidacaoUtils.validarCPF(usuarioPutRequestBody.getCpf())) {
            throw new ValidacaoException("CPF inválido");
        }

        if (!validarCepViaCep(usuarioPutRequestBody.getCep())) {
            throw new ValidacaoException("CEP inválido!");
        } 

        String cpfCriptografado = null;
        String cepCriptografado = null;
        
        try {
            cpfCriptografado = CriptografiaUtils.criptografarCepCpf(usuarioPutRequestBody.getCpf());
            cepCriptografado = CriptografiaUtils.criptografarCepCpf(usuarioPutRequestBody.getCep());
        } catch (CriptografiaException e) {
            throw e;
        }

        Usuario usuario = usuarioOptional.get();

        usuario.setNome(usuarioPutRequestBody.getNome());
        usuario.setNomeCompleto(usuarioPutRequestBody.getNomeCompleto());
        usuario.setEmail(usuarioPutRequestBody.getEmail());
        usuario.setDataNascimento(usuarioPutRequestBody.getDataNascimento());
        usuario.setCpf(cpfCriptografado);
        usuario.setCep(cepCriptografado);

        return usuario;
    }

}
