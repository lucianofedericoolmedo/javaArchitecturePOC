DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `generar_archivo_codigos_sin_lectura_desde_con_externa`(IN fdesde DATETIME, IN lugarid CHAR(50), archivo CHAR(100), separador CHAR(5))
BEGIN 
	SET @stmt = CONCAT("
		SELECT 'codigo', 'fecha_creacion', 'producto', 'lugar', 'lugar_acceso'
		UNION ALL
		SELECT * FROM (
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
							AND maximas_interna.max_fecha < '", fdesde, "'

						UNION ALL

						-- Consulto maximas lecturas externas
						SELECT CONCAT('300ED89F3350', lecturaExterna.codigo), lecturaExterna.fecha_lectura AS fecha 
						FROM lecturas_registros_rfid_externa lecturaExterna
						INNER JOIN (
							SELECT subLecturaExterna.codigo, MAX(subLecturaExterna.fecha_lectura) AS max_fecha 
							FROM lecturas_registros_rfid_externa subLecturaExterna
							GROUP BY subLecturaExterna.codigo
						) AS maximas_externa
							ON lecturaExterna.codigo = maximas_externa.codigo
							AND lecturaExterna.fecha_lectura = maximas_externa.max_fecha
							AND maximas_externa.max_fecha < '", fdesde, "'
					) AS united_lecturas
					GROUP BY united_lecturas.codigo
				) AS maximas_lecturas
				INNER JOIN (
					-- Consulto maximas lecturas internas
					SELECT lrr.codigo, lrr.fecha_creacion AS fecha, ll.nombre AS lugar, '' AS lugar_acceso, ll.id AS lugar_id
					FROM lecturas_registros_rfid lrr
					JOIN lugares_lectura ll
						ON lrr.lugar = ll.id
					WHERE ('", lugarid, "' = '' OR lrr.lugar = '", lugarid, "')

					UNION ALL

					-- Consulto maximas lecturas externas
					SELECT CONCAT('300ED89F3350', lecturaExterna.codigo), lecturaExterna.fecha_lectura AS fecha, lecturaExterna.ubicacion AS lugar, lecturaExterna.acceso AS lugar_acceso, '16350eab-b644-9e18-1e88-80fe82828017' AS lugar_id -- id de zona limpia cliente
					FROM lecturas_registros_rfid_externa lecturaExterna
					WHERE ('", lugarid, "' = '' OR '", lugarid, "' = '16350eab-b644-9e18-1e88-80fe82828017') -- id de zona limpia cliente
				) AS info_maximas_lecturas
					ON maximas_lecturas.codigo = info_maximas_lecturas.codigo
					AND maximas_lecturas.fecha = info_maximas_lecturas.fecha
				WHERE ('", lugarid, "' = '' OR info_maximas_lecturas.lugar_id = '", lugarid, "')
			) AS ultimas_lecturas
			JOIN registros_rfid rr 
				ON codigorfid = ultimas_lecturas.codigo 
				AND rr.estado = 'ACTUAL'
			JOIN productos p 
				ON rr.producto = p.id
			ORDER BY ultimas_lecturas.fecha DESC
		) AS result
		INTO OUTFILE '", archivo, "'
		FIELDS TERMINATED BY '", separador, "'
		ENCLOSED BY '\"'
		LINES TERMINATED BY '\n';
	");

	PREPARE stmt FROM @stmt;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;

