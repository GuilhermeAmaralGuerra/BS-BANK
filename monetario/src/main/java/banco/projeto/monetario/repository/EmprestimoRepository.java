package banco.projeto.monetario.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import banco.projeto.monetario.domain.Emprestimo;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long>{

    @Query("SELECT e FROM Emprestimo e WHERE e.conta.id = :contaId")
    List<Emprestimo> findEmprestimosByContaId(@Param("contaId") Long contaId);

    @Query("SELECT e.juros FROM Emprestimo e WHERE e.conta.id = :contaId AND (e.situacao = 'pendente' OR e.situacao = 'pago_parcialmente')")
    Double findJurosByContaId(@Param("contaId") Long contaId);

    @Query("SELECT e.valor FROM Emprestimo e WHERE e.conta.id = :contaId AND (e.situacao = 'pendente' OR e.situacao = 'pago_parcialmente')")
    Double findValorByContaId(@Param("contaId") Long contaId);

    @Query("SELECT e.valorPago FROM Emprestimo e WHERE e.conta.id = :contaId AND (e.situacao = 'pendente' OR e.situacao = 'pago_parcialmente')")
    Double findValorPagoByContaId(@Param("contaId") Long contaId);

    @Query("SELECT e.dataEmprestimo FROM Emprestimo e WHERE e.conta.id = :contaId AND (e.situacao = 'pendente' OR e.situacao = 'pago_parcialmente')")
    LocalDateTime findDataEmprestimoByContaId(@Param("contaId") Long contaId);

    @Query("SELECT e.id FROM Emprestimo e WHERE e.conta.id = :contaId AND (e.situacao = 'pendente' OR e.situacao = 'pago_parcialmente')")
    Optional<Long> findIdEmprestimoNaoPagoByContaID(@Param("contaId") Long contaId);

    //@Query("SELECT e FROM Emprestimo e WHERE e.conta.id = :contaID AND (e.situacao = 'pendente' OR e.situacao = 'pago_parcialmente')")
    //ptional<Emprestimo> findEmprestimoNaoPagoByContaID(@Param("contaId") Long contaId);
}
