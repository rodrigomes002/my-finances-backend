package com.myfinances.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity 
@Table(name = "lancamentos")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private LocalDate data;
    
    private String descricao;

    private BigDecimal valor;

    protected Lancamento() {
    }

    public Lancamento(LocalDate data, String descricao, BigDecimal valor) {
        this.data = data;
        this.descricao = descricao;
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }
}