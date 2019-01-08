-- Function: csv_lecturas_registros_rfids(character varying, character varying, character varying)

-- DROP FUNCTION csv_lecturas_registros_rfids(character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION csv_lecturas_registros_rfids(codigo_rfid character varying, archivo character varying, separador character varying)
  RETURNS void AS
$BODY$
    DECLARE
	query TEXT;
    BEGIN
	query := format('SELECT lectura.fecha_creacion AS FECHA, lugar.nombre AS LUGAR, producto.nombre AS PRODUCTO, lectura.traza AS TRAZA
				FROM lecturas_registros_rfid lectura
				JOIN lugares_lectura lugar
					ON lectura.lugar = lugar.id
				JOIN registros_rfid registro
					ON lectura.codigo = registro.codigorfid
					AND lectura.traza = registro.traza
				JOIN productos producto
					ON producto.id = registro.producto
				WHERE lectura.codigo = %1$L
				ORDER BY lectura.fecha_creacion DESC',
		codigo_rfid
	);
	EXECUTE format('COPY ('||query||') TO %1$L With CSV DELIMITER %2$L HEADER;', archivo, separador);
    END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION csv_lecturas_registros_rfids(character varying, character varying, character varying)
  OWNER TO postgres;

