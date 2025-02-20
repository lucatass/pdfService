package com.yoiki.pdfservice.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PDFService {

    @Value("${pdf.template.directory}")
    private String templateDirectory;
    private final TemplateEngine templateEngine;
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public byte[] generarPDF(TipoDocumento tipoDocumento, Map<String, Object> datos) {
        var dto = convertirADto(tipoDocumento, datos);
        var datosMap = convertirDtoAMap(dto);

        log.debug("Generando PDF para {} con datos procesados: {}",
                tipoDocumento.name(), datosMap);

        return generarDesdeTemplate(tipoDocumento.getTemplate(), datosMap);
    }

    public byte[] generarDesdeTemplate(String templateNombre, Map<String, Object> datos) {
        try {
            var contexto = new Context();
            contexto.setVariables(datos);

            // Renderizar el HTML con Thymeleaf
            var htmlContent = templateEngine.process(templateDirectory + templateNombre, contexto);

            return convertirHtmlAPdf(htmlContent);
        } catch (Exception e) {
            throw new PDFGenerationException("Error al generar el PDF", e);
        }
    }

    private Object convertirADto(TipoDocumento tipoDocumento, Map<String, Object> datos) {
        try {
            return objectMapper.convertValue(datos, tipoDocumento.getDtoClass());
        } catch (Exception e) {
            log.error("Error convirtiendo datos a DTO para {}: {}", tipoDocumento.name(), e.getMessage(), e);
            throw new PDFGenerationException("Error al mapear los datos a "
                    + tipoDocumento.getDtoClass().getSimpleName(), e);
        }
    }

    private Map<String, Object> convertirDtoAMap(Object dto) {
        try {
            Map<String, Object> originalMap = objectMapper.convertValue(dto, new TypeReference<>() {
            });
            log.debug("📌 DTO convertido a Map para Thymeleaf: {}", originalMap);
            return Collections.unmodifiableMap(originalMap);
        } catch (Exception e) {
            log.error("❌ Error convirtiendo DTO a Map: {}", e.getMessage(), e);
            throw new PDFGenerationException("Error al mapear DTO a estructura de datos", e);
        }
    }

    private byte[] convertirHtmlAPdf(String htmlContent) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.toStream(outputStream);
            builder.withHtmlContent(htmlContent, null);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new PDFGenerationException("Error generando el PDF", e);
        }
    }
}
