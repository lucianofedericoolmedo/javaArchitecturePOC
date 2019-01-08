-- Function: facturaciones_alquileres_csv(character varying, character varying, character varying, character varying, character varying, character varying)

-- DROP FUNCTION facturaciones_alquileres_csv(character varying, character varying, character varying, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION facturaciones_alquileres_csv(fdesde character varying, fhasta character varying, cliente character varying, producto character varying, cantidadFacturadaDistinta double precision, archivo character varying, separador character varying)
  RETURNS void AS
$BODY$
    BEGIN
	EXECUTE ('COPY (
			SELECT TO_CHAR(fa.fecha_facturacion, ''DD/MM/YYYY'') AS fecha, 
				alq.numero AS comprobante_alquiler, 
				(
					CASE WHEN (rr.numero IS NOT NULL) THEN rr.numero
					WHEN (fa.generado_manualmente) THEN ''GENERADO MANUALMENTE''
					WHEN (fa.generado_manualmente IS FALSE) THEN ''FACTURACIÓN MISMO DÍA ALQUILER''
					END
				) AS facturado_por, 
				producto.nombre AS producto, 
				fa.cantidad_devuelta, 
				fa.cantidad_restante, 
				fa.cantidad_facturada 
			FROM facturaciones_alquileres fa
			JOIN items_alquiler ia ON fa.item_alquiler = ia.id
			JOIN alquileres alq ON ia.remito = alq.id
			JOIN productos producto ON ia.producto = producto.id
			LEFT JOIN remitos_retiro rr ON fa.remito_devolucion = rr.id
			WHERE fa.fecha_facturacion BETWEEN ' || quote_literal(fdesde) || ' AND ' || quote_literal(fhasta) || '
			AND alq.cliente = ' || quote_literal(cliente) || '
			AND (' || quote_literal(producto) || ' = '''' OR ia.producto = ' || quote_literal(producto) || ')
			AND fa.cantidad_facturada != ' || cantidadFacturadaDistinta || '
			ORDER BY fa.fecha_facturacion DESC
		) TO ' || quote_literal(archivo) || ' With CSV DELIMITER ' || quote_literal(separador) || ' HEADER;'
	);
    END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION facturaciones_alquileres_csv(character varying, character varying, character varying, character varying, character varying, character varying)
  OWNER TO postgres;

