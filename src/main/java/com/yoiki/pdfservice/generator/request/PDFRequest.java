package com.yoiki.pdfservice.generator.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record PDFRequest(
        @NotBlank(message = "Tipo de documento no especificado")
        String tipoDocumento,
        @NotBlank(message = "Datos no especificados")
        Map<String, Object> datos
) {
}
