package banco.projeto.monetario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import banco.projeto.monetario.domain.Conta;
import jakarta.transaction.Transactional;

public interface ContaRepository extends JpaRepository<Conta, Long>{

    @Query("SELECT c.saldo FROM Conta c WHERE c.usuario.id = :usuarioId")
    Double findSaldoByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT c.id FROM Conta c WHERE c.usuario.id = :usuarioId")
    Long findIdByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT c FROM Conta c WHERE c.codigoTransferencia = :codigoTransferencia")
    Conta findByCodigoTransferencia(@Param("codigoTransferencia") Long numeroConta);

    @Transactional
    @Modifying
    @Query("UPDATE Conta c SET c.saldo = c.saldo - :valor WHERE c.id = :id")
    void debitar(@Param("id") Long id, @Param("valor") double valor);

    @Transactional
    @Modifying
    @Query("UPDATE Conta c SET c.saldo = c.saldo + :valor WHERE c.codigoTransferencia = :codigo")
    void creditar(@Param("codigo") Long codigo, @Param("valor") double valor);

    @Transactional
    @Modifying
    @Query("UPDATE Conta c SET c.saldo = c.saldo - :valor WHERE c.id = :contaId")
    void saque(@Param("contaId") Long contaId, @Param("valor") double valor);

    @Transactional
    @Modifying
    @Query("UPDATE Conta c SET c.saldo = c.saldo + :valor WHERE c.id = :contaId")
    void deposito(@Param("contaId") Long contaId, @Param("valor") double valor);

    @Transactional
    @Modifying
    @Query("Update Conta c SET c.saldo = c.saldo + :valor WHERE c.id = :contaId")
    void emprestimo(@Param("contaId") Long contaId, @Param("valor") double valor);
}
