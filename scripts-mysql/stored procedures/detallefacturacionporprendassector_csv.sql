DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `detallefacturacionporprendassector_csv`(IN fdesde CHAR(25), IN fhasta CHAR(25), cliente CHAR(50), mostrarpuedeutilizarrfid BOOLEAN, archivo CHAR(100), separador CHAR(5))
BEGIN 
	SET @innerQuery = CONCAT("
		(SELECT rrrs.nombre AS producto,SUM(rrrs.cantidad) AS cantidad, rrrs.sector AS sector, producto.puede_tener_rfid FROM (
			SELECT productos.id, productos.nombre, SUM(items_remito_retiro.cantidad) AS cantidad, sectores.nombre as sector
			FROM remitos_retiro
			INNER JOIN items_remito_retiro
				ON remitos_retiro.id = items_remito_retiro.remito
				AND (items_remito_retiro.tipo_item IS NULL OR items_remito_retiro.tipo_item != 'ALQUILER')
				AND (remitos_retiro.es_autogenerado_por_diferencia_de_stock != TRUE 
						OR items_remito_retiro.tipo_item IS NULL)
			LEFT JOIN sectores
				ON items_remito_retiro.sector = sectores.id
			INNER JOIN productos
				ON items_remito_retiro.producto = productos.id
			WHERE remitos_retiro.fecha_creacion BETWEEN '", fdesde, "' AND '", fhasta, "'
			AND remitos_retiro.cliente = '", cliente, "'
			GROUP BY productos.id, productos.nombre, sectores.nombre

			UNION ALL
				 
			SELECT productos.id, productos.nombre, SUM(items_remito_stock.cantidad) AS cantidad, '' as sector 
			FROM remitos_stock
			INNER JOIN items_remito_stock
				ON remitos_stock.id = items_remito_stock.remito
			INNER JOIN productos
				ON items_remito_stock.producto = productos.id
			WHERE remitos_stock.fecha_creacion BETWEEN '", fdesde, "' AND '", fhasta, "'
			AND remitos_stock.cliente = '", cliente, "'
			GROUP BY productos.id, productos.nombre

			UNION ALL

			SELECT productos.id, productos.nombre, SUM(items_ajustes_de_stock.cantidad) AS cantidad, '' as sector 
			FROM ajustes_de_stock
			INNER JOIN items_ajustes_de_stock
				ON ajustes_de_stock.id = items_ajustes_de_stock.remito
			INNER JOIN productos
				ON items_ajustes_de_stock.producto = productos.id
			INNER JOIN motivos_de_ajuste
				ON motivos_de_ajuste.id = items_ajustes_de_stock.motivo_ajuste
				AND motivos_de_ajuste.id = 'e9247af9-5c71-c0c0-d2ba-aceb89446c25'
			WHERE ajustes_de_stock.fecha_creacion BETWEEN '", fdesde, "' AND '", fhasta, "'
			AND ajustes_de_stock.cliente = '", cliente, "'
			GROUP BY productos.id, productos.nombre

			UNION ALL

			SELECT productos.id, productos.nombre, SUM(items_ajustes_de_stock.cantidad) AS cantidad, '' as sector  
			FROM ajustes_de_stock
			INNER JOIN items_ajustes_de_stock
				ON ajustes_de_stock.id = items_ajustes_de_stock.remito
			INNER JOIN productos
				ON items_ajustes_de_stock.producto = productos.id
			INNER JOIN motivos_de_ajuste
				ON motivos_de_ajuste.id = items_ajustes_de_stock.motivo_ajuste
				AND motivos_de_ajuste.id = '1cbdcb23-a85a-c5a7-7ba7-8798db499648'
			WHERE ajustes_de_stock.fecha_creacion BETWEEN '", fdesde, "' AND '", fhasta, "'
			AND ajustes_de_stock.cliente = '", cliente, "'
			GROUP BY productos.id, productos.nombre

			UNION ALL

			SELECT productos.id, productos.nombre, 0 - SUM(items_ajustes_de_stock.cantidad) AS cantidad, '' as sector  
			FROM ajustes_de_stock
			INNER JOIN items_ajustes_de_stock
				ON ajustes_de_stock.id = items_ajustes_de_stock.remito
			INNER JOIN productos
				ON items_ajustes_de_stock.producto = productos.id
			INNER JOIN motivos_de_ajuste
				ON motivos_de_ajuste.id = items_ajustes_de_stock.motivo_ajuste
				AND motivos_de_ajuste.id = 'f8d29aad-439f-1204-18b1-cb8a92e64486'
			WHERE ajustes_de_stock.fecha_creacion BETWEEN '", fdesde, "' AND '", fhasta, "'
			AND ajustes_de_stock.cliente = '", cliente, "'
			GROUP BY productos.id, productos.nombre

			UNION ALL

			SELECT productos.id, productos.nombre, 0 - SUM(items_ajustes_de_stock.cantidad) AS cantidad, '' as sector  
			FROM ajustes_de_stock
			INNER JOIN items_ajustes_de_stock
				ON ajustes_de_stock.id = items_ajustes_de_stock.remito
			INNER JOIN productos
				ON items_ajustes_de_stock.producto = productos.id
			INNER JOIN motivos_de_ajuste
				ON motivos_de_ajuste.id = items_ajustes_de_stock.motivo_ajuste
				AND motivos_de_ajuste.id = '53c2efb2-2640-598e-dd77-391cdabbd7ce'
			WHERE ajustes_de_stock.fecha_creacion BETWEEN '", fdesde, "' AND '", fhasta, "'
			AND ajustes_de_stock.cliente = '", cliente, "'
			GROUP BY productos.id, productos.nombre
		) AS rrrs
		JOIN productos producto
			ON producto.id = rrrs.id
		GROUP BY rrrs.id, rrrs.nombre, rrrs.sector, producto.puede_tener_rfid
		ORDER BY rrrs.nombre,rrrs.sector
		)
	");

	SET @stmt = CASE WHEN mostrarpuedeutilizarrfid
		THEN CONCAT("
			SELECT 'producto', 'cantidad', 'sector', 'puede_tener_rfid'
			UNION ALL
			SELECT producto, 
			cantidad, 
			sector,
			(CASE WHEN puede_tener_rfid 
				THEN 'SI' 
				ELSE 'NO' 
			END) AS puede_tener_rfid 
			FROM ", @innerQuery, " AS result
			INTO OUTFILE '", archivo, "'
			FIELDS TERMINATED BY ','
			ENCLOSED BY '\"'
			LINES TERMINATED BY '\n';
		")
		ELSE CONCAT("
			SELECT 'producto', 'cantidad', 'sector'
			UNION ALL
			SELECT producto, 
			cantidad,
			sector
			FROM ", @innerQuery, " AS result
			INTO OUTFILE '", archivo, "'
			FIELDS TERMINATED BY ','
			ENCLOSED BY '\"'
			LINES TERMINATED BY '\n';
		")
		END;

	PREPARE stmt FROM @stmt;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

END$$
DELIMITER ;

