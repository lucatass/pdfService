package com.yoiki.pdfservice.generator.dto;

import java.util.List;

public record RemitoDto(
        String fecha,
        Cliente remitente,
        Cliente destinatario,
        String tipoPago,
        String remito,
        String cartaPorte,
        String bultos,
        String descripcion,
        Seguro seguro,
        ContraReembolso contraReembolso,
        List<DetalleProductos> detalleProductos,
        String cae
) {
        public record Cliente(
                String nombre,
                String direccion,
                String provincia,
                String iva,
                String cuit
        ) {}

        public record Seguro(
                String valorDeclarado,
                String coeficiente,
                String seguro
        ) {}

        public record ContraReembolso(
                String coeficiente,
                String importe,
                String comision
        ) {}

        public record DetalleProductos(
                String producto,
                String unidad,
                String cantidad,
                String precio,
                String total,
                String descripcion
        ) {}
}
