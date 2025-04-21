package com.yoiki.pdfservice.generator.dto;

import java.util.List;

public record HojaRutaDto(
        String id,
        String numero,
        String fecha,
        String salida,
        String llegada,
        boolean cerrada,
        String origen,
        String destino,
        String transporte,
        String personal,
        String camion,
        List<RemitoDto> remitoDtos
) {
}
