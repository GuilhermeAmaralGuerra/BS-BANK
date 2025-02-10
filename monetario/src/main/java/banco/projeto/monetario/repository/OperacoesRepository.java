package banco.projeto.monetario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import banco.projeto.monetario.domain.Operacoes;

public interface OperacoesRepository extends JpaRepository<Operacoes, Long>{

    

}
