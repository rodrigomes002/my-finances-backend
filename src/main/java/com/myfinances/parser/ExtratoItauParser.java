package com.myfinances.parser;

import java.util.*;
import java.util.regex.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExtratoItauParser {

    public static class Lancamento {
        LocalDate data;
        String descricao;
        BigDecimal valor;

        Lancamento(LocalDate data, String descricao, BigDecimal valor) {
            this.data = data;
            this.descricao = descricao;
            this.valor = valor;
        }

        @Override
        public String toString() {
            return data + " | " + descricao + " | R$ " + valor;
        }
    }

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Regex: data completa no início de cada lançamento
    private static final Pattern SPLIT_PATTERN = Pattern.compile("(?=\\d{2}/\\d{2}/\\d{4}\\s)");

    // Regex: valor no formato brasileiro (com sinal opcional), no FINAL do bloco
    private static final Pattern VALOR_PATTERN = Pattern.compile("(-?\\d{1,3}(?:\\.\\d{3})*,\\d{2})$");

    // Regex: data no início do bloco
    private static final Pattern DATA_PATTERN = Pattern.compile("^(\\d{2}/\\d{2}/\\d{4})\\s+");

    public static List<Lancamento> parse(String textoCompleto) {
        List<Lancamento> lancamentos = new ArrayList<>();

        String[] blocos = SPLIT_PATTERN.split(textoCompleto);

        for (String bloco : blocos) {
            bloco = bloco.trim();
            if (bloco.isEmpty()) continue;

            Matcher dataMatcher = DATA_PATTERN.matcher(bloco);
            Matcher valorMatcher = VALOR_PATTERN.matcher(bloco);

            if (dataMatcher.find() && valorMatcher.find()) {
                LocalDate data = LocalDate.parse(dataMatcher.group(1), DATE_FORMAT);
                String valorStr = valorMatcher.group(1)
                        .replace(".", "")   // remove separador de milhar
                        .replace(",", "."); // vírgula decimal -> ponto
                BigDecimal valor = new BigDecimal(valorStr);

                // Descrição = o que sobra entre a data e o valor
                String descricao = bloco.substring(dataMatcher.end(), valorMatcher.start()).trim();

                lancamentos.add(new Lancamento(data, descricao, valor));
            }
        }

        return lancamentos;
    }
}