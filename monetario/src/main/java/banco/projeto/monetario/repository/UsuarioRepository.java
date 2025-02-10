package banco.projeto.monetario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import banco.projeto.monetario.domain.Usuario;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long>{

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u.id FROM Usuario u WHERE u.email = :email")
    Long findIdByEmail(@Param("email") String email);

    boolean existsByCpf(String cpf);
    boolean existsByCep(String cep);

    @Query("SELECT u.nome FROM Usuario u WHERE u.id = :id")
    String findNomeById(@Param("id") Long id);

    
    @Query("SELECT u.senha FROM Usuario u WHERE u.email = :email")
    String findPasswordByEmail(@Param("email") String email);

    Optional<Usuario> findById(Long userId);

}
