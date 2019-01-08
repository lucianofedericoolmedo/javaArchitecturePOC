-- Function: codigos_sin_lectura_desde_con_externa(timestamp without time zone, character varying)

-- DROP FUNCTION codigos_sin_lectura_desde_con_externa(timestamp without time zone, character varying);

CREATE OR REPLACE FUNCTION codigos_sin_lectura_desde_con_externa(IN fdesde timestamp without time zone, IN lugarid character varying)
  RETURNS TABLE(codigo character varying, fecha timestamp without time zone, producto character varying, lugar character varying, lugar_acceso character varying) AS
$BODY$
BEGIN
 RETURN QUERY 
 	SELECT ultimas_lecturas.codigo, ultimas_lecturas.fecha, p.nombre as producto, ultimas_lecturas.lugar, ultimas_lecturas.lugar_acceso FROM (
		SELECT maximas_lecturas.codigo, maximas_lecturas.fecha, info_maximas_lecturas.lugar, info_maximas_lecturas.lugar_acceso FROM (
			SELECT united_lecturas.codigo, MAX(united_lecturas.fecha) AS fecha FROM (
				-- Consulto maximas lecturas internas
				SELECT lrr.codigo, lrr.fecha_creacion AS fecha 
				FROM lecturas_registros_rfid lrr
				INNER JOIN (
					SELECT subLrr.codigo, MAX(subLrr.fecha_creacion) AS max_fecha 
					FROM lecturas_registros_rfid subLrr
					GROUP BY subLrr.codigo
				) AS maximas_interna
					ON lrr.codigo = maximas_interna.codigo
					AND lrr.fecha_creacion = maximas_interna.max_fecha
					AND maximas_interna.max_fecha <  $1

				UNION ALL

				-- Consulto maximas lecturas externas
				SELECT ('300ED89F3350' || lecturaExterna.codigo), lecturaExterna.fecha_lectura AS fecha 
				FROM lecturas_registros_rfid_externa lecturaExterna
				INNER JOIN (
					SELECT subLecturaExterna.codigo, MAX(subLecturaExterna.fecha_lectura) AS max_fecha 
					FROM lecturas_registros_rfid_externa subLecturaExterna
					GROUP BY subLecturaExterna.codigo
				) AS maximas_externa
					ON lecturaExterna.codigo = maximas_externa.codigo
					AND lecturaExterna.fecha_lectura = maximas_externa.max_fecha
					AND maximas_externa.max_fecha < $1
			) AS united_lecturas
			GROUP BY united_lecturas.codigo
		) AS maximas_lecturas
		INNER JOIN (
			-- Consulto maximas lecturas internas
			SELECT lrr.codigo, lrr.fecha_creacion AS fecha, ll.nombre AS lugar, '' AS lugar_acceso, ll.id AS lugar_id
			FROM lecturas_registros_rfid lrr
			JOIN lugares_lectura ll
				ON lrr.lugar = ll.id
			WHERE ($2 = '' OR lrr.lugar = $2)

			UNION ALL

			-- Consulto maximas lecturas externas
			SELECT ('300ED89F3350' || lecturaExterna.codigo), lecturaExterna.fecha_lectura AS fecha, lecturaExterna.ubicacion AS lugar, lecturaExterna.acceso AS lugar_acceso, '16350eab-b644-9e18-1e88-80fe82828017' AS lugar_id -- id de zona limpia cliente
			FROM lecturas_registros_rfid_externa lecturaExterna
			WHERE ($2 = '' OR $2 = '16350eab-b644-9e18-1e88-80fe82828017') -- id de zona limpia cliente
		) AS info_maximas_lecturas
			ON maximas_lecturas.codigo = info_maximas_lecturas.codigo
			AND maximas_lecturas.fecha = info_maximas_lecturas.fecha
		WHERE ($2 = '' OR info_maximas_lecturas.lugar_id = $2)
	) AS ultimas_lecturas
	JOIN registros_rfid rr 
		ON codigorfid = ultimas_lecturas.codigo 
		AND rr.estado = 'ACTUAL'
	JOIN productos p 
		ON rr.producto = p.id
	ORDER BY ultimas_lecturas.fecha DESC;
END; $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION codigos_sin_lectura_desde_con_externa(timestamp without time zone, character varying)
  OWNER TO postgres;

