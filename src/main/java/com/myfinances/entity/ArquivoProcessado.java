package com.myfinances.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "arquivos_processados")
public class ArquivoProcessado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hash_sha256", nullable = false, unique = true, length = 64)
    private String hashSha256;

    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @Column(name = "processado_em", nullable = false)
    private LocalDateTime processadoEm;

    protected ArquivoProcessado() {
    }

    public ArquivoProcessado(String hashSha256, String nomeArquivo, LocalDateTime processadoEm) {
        this.hashSha256 = hashSha256;
        this.nomeArquivo = nomeArquivo;
        this.processadoEm = processadoEm;
    }
}
