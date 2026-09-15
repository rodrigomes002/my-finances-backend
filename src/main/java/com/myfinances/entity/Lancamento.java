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
    Integer id;
    
    LocalDate data;
    
    String descricao;

    BigDecimal valor;

    public Lancamento(LocalDate data, String descricao, BigDecimal valor) {
        this.data = data;
        this.descricao = descricao;
        this.valor = valor;
    }
}