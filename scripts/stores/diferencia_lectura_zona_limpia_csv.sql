-- Function: diferencia_lectura_zona_limpia_csv(character varying, character varying, character varying, character varying)

-- DROP FUNCTION diferencia_lectura_zona_limpia_csv(character varying, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION diferencia_lectura_zona_limpia_csv(remito_entrega_id character varying, lectura_control_id character varying, archivo character varying, separador character varying)
  RETURNS integer AS
$BODY$
    BEGIN
      EXECUTE('Copy (
		SELECT re.numero AS numero_comprobante, producto.nombre AS producto, SUM(items_entrega.cantidad) AS cantidad_cargada_remito, SUM(items_control.cantidad) AS cantidad_lectura_control FROM (
			SELECT ire.producto, SUM(cantidad) AS cantidad FROM items_remito_entrega ire
			WHERE ire.remito = ' || quote_literal(remito_entrega_id) || '
			AND state = ''2''
			GROUP BY ire.producto
		) AS items_entrega
		FULL JOIN (
			SELECT registros_actuales.producto, COUNT(DISTINCT lci.codigorfid) AS cantidad FROM lecturas_control_items lci
			JOIN (
				SELECT * FROM registros_rfid
				WHERE estado = ''ACTUAL''
			) AS registros_actuales ON lci.codigorfid = registros_actuales.codigorfid
			WHERE lectura_control = '  || quote_literal(lectura_control_id) ||  '
			GROUP BY registros_actuales.producto
		) AS items_control ON items_entrega.producto = items_control.producto
		JOIN productos producto ON items_entrega.producto = producto.id
		JOIN remitos_entrega re ON re.id = ' || quote_literal(remito_entrega_id) || '
		GROUP BY re.numero, producto.nombre
		ORDER BY producto.nombre ASC
	) To '|| quote_literal(archivo) ||' With CSV DELIMITER '|| quote_literal(separador) ||' HEADER;');
      RETURN 1;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION diferencia_lectura_zona_limpia_csv(character varying, character varying, character varying, character varying)
  OWNER TO postgres;

