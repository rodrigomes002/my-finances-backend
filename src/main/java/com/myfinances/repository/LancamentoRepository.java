package com.myfinances.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myfinances.entity.Lancamento;

@Repository 
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
    
}
