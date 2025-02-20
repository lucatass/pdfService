package com.yoiki.pdfservice.generator;

import com.yoiki.pdfservice.generator.request.PDFRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pdf")
@RequiredArgsConstructor
public class PDFGenerationController {

    private final PDFService pdfService;

    @PostMapping("/generar")
    public ResponseEntity<byte[]> generarPDF(@RequestBody PDFRequest request) {
        try {
            var tipoDocumento = TipoDocumento.valueOf(request.tipoDocumento().toUpperCase());
            byte[] pdfBytes = pdfService.generarPDF(tipoDocumento, request.datos());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition", "inline; filename="
                            + tipoDocumento.name().toLowerCase() + ".pdf")
                    .body(pdfBytes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(("Tipo de documento no válido: "
                    + request.tipoDocumento()).getBytes());
        }
    }
}
