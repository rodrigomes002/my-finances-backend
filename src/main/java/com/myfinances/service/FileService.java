package com.myfinances.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.myfinances.entity.Lancamento;
import com.myfinances.parser.ExtratoItauParser;
import com.myfinances.repository.LancamentoRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final LancamentoRepository lancamentoRepository;

    public void processFile(MultipartFile file) {
        try(PDDocument document = Loader.loadPDF(file.getBytes())){

            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(document);

            List<Lancamento> lancamentos = ExtratoItauParser.parse(texto);

            for (Lancamento lancamento : lancamentos) {
                lancamentoRepository.save(lancamento);
            }

            log.info("Texto extraído do PDF: {}", texto.substring(0, Math.min(texto.length(), 100))); // Loga os primeiros 100 caracteres do texto extraído

        }catch (Exception e){
            log.error("Erro ao processar o arquivo: {}", e.getMessage());
        }
    }
}
