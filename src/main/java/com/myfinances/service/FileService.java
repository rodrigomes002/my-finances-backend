package com.myfinances.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.myfinances.entity.ArquivoProcessado;
import com.myfinances.entity.Lancamento;
import com.myfinances.exception.ArquivoJaProcessadoException;
import com.myfinances.repository.ArquivoProcessadoRepository;
import com.myfinances.parser.ExtratoItauParser;
import com.myfinances.repository.LancamentoRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final LancamentoRepository lancamentoRepository;
    private final ArquivoProcessadoRepository arquivoProcessadoRepository;

    @Transactional
    public void processFile(MultipartFile file) {
        final byte[] conteudo;
        final String hash;
        try {
            conteudo = file.getBytes();
            hash = calcularSha256(conteudo);
        } catch (IOException e) {
            throw new IllegalArgumentException("Não foi possível ler o arquivo enviado.", e);
        }

        if (arquivoProcessadoRepository.existsByHashSha256(hash)) {
            throw new ArquivoJaProcessadoException();
        }

        try (PDDocument document = Loader.loadPDF(conteudo)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(document);

            List<Lancamento> lancamentos = ExtratoItauParser.parse(texto);
            for (Lancamento lancamento : lancamentos) {
                lancamentoRepository.save(Objects.requireNonNull(lancamento));
            }
            try {
                arquivoProcessadoRepository.saveAndFlush(
                        criarArquivoProcessado(hash, file.getOriginalFilename()));
            } catch (DataIntegrityViolationException e) {
                throw new ArquivoJaProcessadoException();
            }
            log.info("Arquivo processado com sucesso: {}", file.getOriginalFilename());
        } catch (IOException e) {
            throw new IllegalArgumentException("Não foi possível processar o arquivo enviado.", e);
        }
    }

    @NonNull
    private ArquivoProcessado criarArquivoProcessado(String hash, String nomeArquivo) {
        return new ArquivoProcessado(hash, nomeArquivo, LocalDateTime.now());
    }

    private String calcularSha256(byte[] conteudo) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(conteudo);
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo SHA-256 indisponível.", e);
        }
    }
}
