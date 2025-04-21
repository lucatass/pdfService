package com.yoiki.pdfservice.generator;

import com.yoiki.pdfservice.generator.dto.HojaRepartoDto;
import com.yoiki.pdfservice.generator.dto.HojaRutaDto;
import com.yoiki.pdfservice.generator.dto.RemitoDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public enum TipoDocumento {

    REMITO("remito.html", RemitoDto.class),
    HOJARUTA("hoja_ruta.html", HojaRutaDto.class),
    HOJAREPARTO("hoja_reparto.html", HojaRepartoDto.class),
    FACTURA("factura.html", Map.class),
    RECIBO("recibo.html", Map.class);

    private final String template;
    private final Class<?> dtoClass;
}
