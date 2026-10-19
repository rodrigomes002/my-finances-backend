package com.myfinances.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.myfinances.entity.Lancamento;
import com.myfinances.repository.LancamentoRepository;

@SpringBootTest
@AutoConfigureMockMvc
class LancamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LancamentoRepository lancamentoRepository;

    @Test
    void deveRetornarTodosOsLancamentos() throws Exception {
        when(lancamentoRepository.findAll()).thenReturn(List.of(
                new Lancamento(LocalDate.of(2024, 1, 1), "Mercado", new BigDecimal("-150.00"))));

        mockMvc.perform(get("/lancamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].data").value("2024-01-01"))
                .andExpect(jsonPath("$[0].descricao").value("Mercado"))
                .andExpect(jsonPath("$[0].valor").value(-150.00));
    }

    @Test
    void deveCalcularReceitasDespesasESaldoTotal() throws Exception {
        when(lancamentoRepository.findAll()).thenReturn(List.of(
                new Lancamento(LocalDate.of(2024, 1, 1), "Salário", new BigDecimal("2500.00")),
                new Lancamento(LocalDate.of(2024, 1, 2), "Mercado", new BigDecimal("-180.50")),
                new Lancamento(LocalDate.of(2024, 1, 3), "Conta", new BigDecimal("-320.00")),
                new Lancamento(LocalDate.of(2024, 1, 4), "Reembolso", new BigDecimal("50.00"))));

        mockMvc.perform(get("/lancamentos/resumo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.receitas").value(2550.00))
                .andExpect(jsonPath("$.despesas").value(500.50))
                .andExpect(jsonPath("$.saldoTotal").value(2049.50));
    }

    @Test
    void deveFiltrarLancamentosPorMes() throws Exception {
        when(lancamentoRepository.findByDataGreaterThanEqualAndDataLessThan(
                LocalDate.of(2024, 2, 1), LocalDate.of(2024, 3, 1)))
                .thenReturn(List.of(
                        new Lancamento(LocalDate.of(2024, 2, 1), "Aluguel", new BigDecimal("-900.00")),
                        new Lancamento(LocalDate.of(2024, 2, 29), "Salário", new BigDecimal("3000.00"))));

        mockMvc.perform(get("/lancamentos").param("mes", "2024-02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].data").value("2024-02-01"))
                .andExpect(jsonPath("$[1].data").value("2024-02-29"));

        verify(lancamentoRepository).findByDataGreaterThanEqualAndDataLessThan(
                LocalDate.of(2024, 2, 1), LocalDate.of(2024, 3, 1));
    }

    @Test
    void deveCalcularResumoSomenteDoMesInformado() throws Exception {
        when(lancamentoRepository.findByDataGreaterThanEqualAndDataLessThan(
                LocalDate.of(2024, 12, 1), LocalDate.of(2025, 1, 1)))
                .thenReturn(List.of(
                        new Lancamento(LocalDate.of(2024, 12, 1), "Salário", new BigDecimal("2500.00")),
                        new Lancamento(LocalDate.of(2024, 12, 31), "Contas", new BigDecimal("-400.00"))));

        mockMvc.perform(get("/lancamentos/resumo").param("mes", "2024-12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.receitas").value(2500.00))
                .andExpect(jsonPath("$.despesas").value(400.00))
                .andExpect(jsonPath("$.saldoTotal").value(2100.00));

        verify(lancamentoRepository).findByDataGreaterThanEqualAndDataLessThan(
                LocalDate.of(2024, 12, 1), LocalDate.of(2025, 1, 1));
    }

    @Test
    void deveRejeitarMesComFormatoInvalido() throws Exception {
        mockMvc.perform(get("/lancamentos").param("mes", "2024-13"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void devePermitirRequisicoesCorsDeQualquerOrigem() throws Exception {
        mockMvc.perform(options("/lancamentos")
                        .header("Origin", "https://aplicacao-exemplo.com")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "https://aplicacao-exemplo.com"))
                .andExpect(header().string("Access-Control-Allow-Methods", "GET"));
    }
}
