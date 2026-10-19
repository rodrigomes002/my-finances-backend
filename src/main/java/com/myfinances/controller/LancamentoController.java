package com.myfinances.controller;

import java.time.YearMonth;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myfinances.dto.ResumoLancamentos;
import com.myfinances.entity.Lancamento;
import com.myfinances.service.LancamentoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;

    @GetMapping
    public ResponseEntity<List<Lancamento>> listarTodos(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
        List<Lancamento> lancamentos = mes == null
                ? lancamentoService.listarTodos()
                : lancamentoService.listarPorMes(mes);
        return ResponseEntity.ok(lancamentos);
    }

    @GetMapping("/resumo")
    public ResponseEntity<ResumoLancamentos> calcularResumo(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
        ResumoLancamentos resumo = mes == null
                ? lancamentoService.calcularResumo()
                : lancamentoService.calcularResumo(mes);
        return ResponseEntity.ok(resumo);
    }
}
