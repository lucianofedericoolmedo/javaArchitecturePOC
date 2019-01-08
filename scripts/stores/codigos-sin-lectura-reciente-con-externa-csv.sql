-- Function: generar_archivo_codigos_sin_lectura_desde_con_externa(timestamp without time zone, character varying, character varying, character varying)

-- DROP FUNCTION generar_archivo_codigos_sin_lectura_desde_con_externa(timestamp without time zone, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION generar_archivo_codigos_sin_lectura_desde_con_externa(fdesde timestamp without time zone, lugarid character varying, archivo character varying, separador character varying)
  RETURNS integer AS
$BODY$
BEGIN
	EXECUTE(
		'Copy (
			SELECT * FROM codigos_sin_lectura_desde_con_externa(' || quote_literal(fdesde) || ', ' || quote_literal(lugarId) || ')
		) To ' || quote_literal(archivo) || ' With CSV DELIMITER ' || quote_literal(separador) || ' HEADER;'
	);
	RETURN 1;
END; $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION generar_archivo_codigos_sin_lectura_desde_con_externa(timestamp without time zone, character varying, character varying, character varying)
  OWNER TO postgres;

