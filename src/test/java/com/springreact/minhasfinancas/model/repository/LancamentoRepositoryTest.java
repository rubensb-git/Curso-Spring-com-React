package com.springreact.minhasfinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.springreact.minhasfinancas.model.entity.Lancamento;
import com.springreact.minhasfinancas.model.enums.StatusLancamento;
import com.springreact.minhasfinancas.model.enums.TipoLancamento;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoRepositoryTest {
	
	@Autowired
	LancamentoRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveSalvarUmLancamento() {		
		
		Lancamento lancamento = criarLancamento();
		
		lancamento = repository.save(lancamento);
		
		Assertions.assertThat(lancamento.getId()).isNotNull();
		
	}
		
	@Test
	public void deveDeletarUmLancamento() {
		
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);
		
		lancamento = entityManager.find(Lancamento.class, lancamento.getId());
		
		repository.delete(lancamento);
		
		Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());
		
		Assertions.assertThat(lancamentoInexistente).isNull();		
		
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);
		
		lancamento.setAno(2018);
		lancamento.setDescricao("Teste Atualizar");
		lancamento.setStatus(StatusLancamento.CANCELADO);
		
		repository.save(lancamento);
		
		Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());
		
		Assertions.assertThat(lancamento.getAno()).isEqualTo(2018);
		Assertions.assertThat(lancamento.getDescricao()).isEqualTo("Teste Atualizar");
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
		
	}
	
	@Test
	public void deveBuscarUmLancamentoPorId() {
		
		Lancamento lancamento = criarLancamento();
		entityManager.persist(lancamento);
		
		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());
		
		Assertions.assertThat(lancamentoEncontrado.isPresent()).isTrue();
		
	}
	
	public static Lancamento criarLancamento() {
		
		return Lancamento.builder()
				.ano(2019)
				.mes(1)
				.descricao("Lançamento qualquer")
				.valor(BigDecimal.valueOf(10))
				.tipo(TipoLancamento.RECEITA)
				.status(StatusLancamento.PENDENTE)
				.dataCadastro(LocalDate.now())
				.build();				
		
	}

}
