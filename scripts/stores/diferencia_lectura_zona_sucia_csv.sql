-- Function: diferencia_lectura_zona_sucia_csv(character varying, character varying, character varying, character varying)

-- DROP FUNCTION diferencia_lectura_zona_sucia_csv(character varying, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION diferencia_lectura_zona_sucia_csv(remito_retiro_id character varying, lectura_control_id character varying, archivo character varying, separador character varying)
  RETURNS integer AS
$BODY$
    BEGIN
      EXECUTE('Copy (
		SELECT rr.numero AS numero_comprobante, producto.nombre AS producto, COUNT(DISTINCT items_rfids.item_rfid) AS cantidad_lectura_comprobante, COUNT(DISTINCT control_rfids.control_rfid) AS cantidad_lectura_control FROM (
			SELECT rfid AS item_rfid FROM items_comprobantes_rfids items_rfids
			WHERE items_rfids.item IN (
				SELECT id FROM items_remito_retiro irr
				WHERE irr.remito = ' || quote_literal(remito_retiro_id) || '
				AND irr.por_ausencia_en_lectura_control = FALSE
			)
		) AS items_rfids
		FULL JOIN (
			SELECT codigorfid AS control_rfid FROM lecturas_control_items
			WHERE lectura_control = ' || quote_literal(lectura_control_id) || '
		) AS control_rfids ON items_rfids.item_rfid = control_rfids.control_rfid
		JOIN (
			SELECT * FROM registros_rfid
			WHERE estado = ''ACTUAL''
		) AS registros_actuales ON ((items_rfids.item_rfid IS NOT NULL AND items_rfids.item_rfid = codigorfid) OR (control_rfids.control_rfid IS NOT NULL AND control_rfids.control_rfid = codigorfid))
		JOIN productos producto ON registros_actuales.producto = producto.id
		JOIN remitos_retiro rr ON rr.id = ' || quote_literal(remito_retiro_id) || '
		GROUP BY rr.numero, producto.nombre
		ORDER BY producto.nombre ASC
	) To '|| quote_literal(archivo) ||' With CSV DELIMITER '|| quote_literal(separador) ||' HEADER;');
      RETURN 1;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION diferencia_lectura_zona_sucia_csv(character varying, character varying, character varying, character varying)
  OWNER TO postgres;

