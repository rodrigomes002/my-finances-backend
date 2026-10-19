package com.myfinances.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myfinances.entity.ArquivoProcessado;

public interface ArquivoProcessadoRepository extends JpaRepository<ArquivoProcessado, Long> {

    boolean existsByHashSha256(String hashSha256);
}
