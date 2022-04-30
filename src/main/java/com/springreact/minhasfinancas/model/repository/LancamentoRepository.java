package com.springreact.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springreact.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
