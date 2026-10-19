package com.myfinances.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.myfinances.entity.Lancamento;
import com.myfinances.repository.LancamentoRepository;
import com.myfinances.dto.ResumoLancamentos;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;

    public List<Lancamento> listarTodos() {
        return lancamentoRepository.findAll();
    }

    public List<Lancamento> listarPorMes(YearMonth mes) {
        LocalDate inicio = mes.atDay(1);
        return lancamentoRepository.findByDataGreaterThanEqualAndDataLessThan(inicio, inicio.plusMonths(1));
    }

    public ResumoLancamentos calcularResumo() {
        return calcularResumo(lancamentoRepository.findAll());
    }

    public ResumoLancamentos calcularResumo(YearMonth mes) {
        LocalDate inicio = mes.atDay(1);
        List<Lancamento> lancamentos = lancamentoRepository
                .findByDataGreaterThanEqualAndDataLessThan(inicio, inicio.plusMonths(1));
        return calcularResumo(lancamentos);
    }

    private ResumoLancamentos calcularResumo(List<Lancamento> lancamentos) {
        BigDecimal receitas = BigDecimal.ZERO;
        BigDecimal despesas = BigDecimal.ZERO;
        BigDecimal saldoTotal = BigDecimal.ZERO;

        for (Lancamento lancamento : lancamentos) {
            BigDecimal valor = lancamento.getValor();
            saldoTotal = saldoTotal.add(valor);

            if (valor.signum() > 0) {
                receitas = receitas.add(valor);
            } else if (valor.signum() < 0) {
                despesas = despesas.add(valor.abs());
            }
        }

        return new ResumoLancamentos(receitas, despesas, saldoTotal);
    }
}
