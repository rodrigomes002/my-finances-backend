package com.myfinances.dto;

import java.math.BigDecimal;

public class ResumoLancamentos {

    private final BigDecimal receitas;
    private final BigDecimal despesas;
    private final BigDecimal saldoTotal;

    public ResumoLancamentos(BigDecimal receitas, BigDecimal despesas, BigDecimal saldoTotal) {
        this.receitas = receitas;
        this.despesas = despesas;
        this.saldoTotal = saldoTotal;
    }

    public BigDecimal getReceitas() {
        return receitas;
    }

    public BigDecimal getDespesas() {
        return despesas;
    }

    public BigDecimal getSaldoTotal() {
        return saldoTotal;
    }
}
