package com.myfinances.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myfinances.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Integer> {

    List<Lancamento> findByDataGreaterThanEqualAndDataLessThan(LocalDate inicio, LocalDate fim);
}
