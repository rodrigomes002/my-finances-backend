package com.myfinances.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.myfinances.entity.Lancamento;
import com.myfinances.repository.LancamentoRepository;

@SpringBootTest 
@AutoConfigureMockMvc 
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean 
    private LancamentoRepository lancamentoRepository;

    @Test
    void deveFazerUploadDoExtrato() throws Exception {
        byte[] pdfBytes = criarPdfValido();

        MockMultipartFile file = new MockMultipartFile(
            "file",
            "extrato.pdf",
            "application/pdf",
            pdfBytes
        );

        when(lancamentoRepository.save(any(Lancamento.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(multipart("/files/upload")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Arquivo enviado com sucesso!"));

        verify(lancamentoRepository, atLeastOnce()).save(any(Lancamento.class));
    }

    private byte[] criarPdfValido() throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PDDocument documento = new PDDocument()) {

            PDPage page = new PDPage();
            documento.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(documento, page)) {
                content.beginText();
                PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                content.setFont(font, 12);
                content.newLineAtOffset(100, 700);
                content.showText("01/01/2024 Mercado -150,00");
                content.endText();
            }

            documento.save(baos);
            return baos.toByteArray();
        }
    }
}