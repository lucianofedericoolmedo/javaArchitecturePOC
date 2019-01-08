-- Function: diferencia_lectura_zona_sucia(character varying, character varying)

-- DROP FUNCTION diferencia_lectura_zona_sucia(character varying, character varying);

CREATE OR REPLACE FUNCTION diferencia_lectura_zona_sucia(IN remito_retiro_id character varying, IN lectura_control_id character varying)
  RETURNS TABLE(numero_comprobante character varying, producto character varying, cantidad_lectura_comprobante bigint, cantidad_lectura_control bigint) AS
$BODY$
    BEGIN
    RETURN QUERY
	SELECT rr.numero AS numero_comprobante, producto.nombre AS producto, COUNT(DISTINCT items_rfids.item_rfid) AS cantidad_lectura_comprobante, COUNT(DISTINCT control_rfids.control_rfid) AS cantidad_lectura_control FROM (
		SELECT rfid AS item_rfid FROM items_comprobantes_rfids items_rfids
		WHERE items_rfids.item IN (
			SELECT id FROM items_remito_retiro irr
			WHERE irr.remito = $1
			AND irr.por_ausencia_en_lectura_control = FALSE
		)
	) AS items_rfids
	FULL JOIN (
		SELECT codigorfid AS control_rfid FROM lecturas_control_items
		WHERE lectura_control = $2
	) AS control_rfids ON items_rfids.item_rfid = control_rfids.control_rfid
	JOIN (
		SELECT * FROM registros_rfid
		WHERE estado = 'ACTUAL'
	) AS registros_actuales ON ((items_rfids.item_rfid IS NOT NULL AND items_rfids.item_rfid = codigorfid) OR (control_rfids.control_rfid IS NOT NULL AND control_rfids.control_rfid = codigorfid))
	JOIN productos producto ON registros_actuales.producto = producto.id
	JOIN remitos_retiro rr ON rr.id = $1
	GROUP BY rr.numero, producto.nombre
	ORDER BY producto.nombre ASC;
    END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION diferencia_lectura_zona_sucia(character varying, character varying)
  OWNER TO postgres;

