-- Function: diferencia_lectura_zona_sucia_csv(character varying, character varying, character varying, character varying)

-- DROP FUNCTION diferencia_lectura_zona_sucia_csv(character varying, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION items_rr_generados_por_lectura_control(remito_retiro_id character varying, archivo character varying, separador character varying)
  RETURNS void AS
$BODY$
    BEGIN
      EXECUTE('Copy (
		SELECT producto.nombre AS producto, SUM(item.cantidad) AS cantidad
		FROM items_remito_retiro item
		JOIN productos producto ON producto.id = item.producto
		WHERE item.por_ausencia_en_lectura_control = TRUE
		AND item.remito = ' || quote_literal(remito_retiro_id) || '
		GROUP BY producto.nombre
		ORDER BY producto.nombre ASC
	) To '|| quote_literal(archivo) ||' With CSV DELIMITER '|| quote_literal(separador) ||' HEADER;');
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION items_rr_generados_por_lectura_control(character varying, character varying, character varying)
  OWNER TO postgres;

