DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `detallefacturacionporprendas_remitoentrega_csv`(IN fdesde CHAR(25), IN fhasta CHAR(25), cliente CHAR(50), mostrarpuedeutilizarrfid BOOLEAN, archivo CHAR(75), separador CHAR(5))
BEGIN 
	SET @innerQuery = CONCAT("
		(SELECT rrrs.nombre AS producto,SUM(rrrs.cantidad) AS cantidad, producto.puede_tener_rfid FROM (
			SELECT productos.id, productos.nombre, SUM(items_remito_entrega.cantidad) AS cantidad 
			FROM remitos_entrega
			INNER JOIN items_remito_entrega
				ON remitos_entrega.id = items_remito_entrega.remito
			INNER JOIN productos
				ON items_remito_entrega.producto = productos.id
			WHERE remitos_entrega.fecha_creacion BETWEEN '", fdesde, "' AND '", fhasta, "'
			AND remitos_entrega.cliente = '", cliente, "'
			GROUP BY productos.id, productos.nombre 

			UNION ALL
			     
			SELECT productos.id, productos.nombre, SUM(items_remito_stock.cantidad) AS cantidad 
			FROM remitos_stock
			INNER JOIN items_remito_stock
				ON remitos_stock.id = items_remito_stock.remito
			INNER JOIN productos
				ON items_remito_stock.producto = productos.id
			WHERE remitos_stock.fecha_creacion BETWEEN '", fdesde, "' AND '", fhasta, "'
			AND remitos_stock.cliente = '", cliente, "'
			GROUP BY productos.id, productos.nombre

			UNION ALL

			SELECT productos.id, productos.nombre, SUM(items_ajustes_de_stock.cantidad) AS cantidad 
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

			SELECT productos.id, productos.nombre, SUM(items_ajustes_de_stock.cantidad) AS cantidad 
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

			SELECT productos.id, productos.nombre, 0 - SUM(items_ajustes_de_stock.cantidad) AS cantidad 
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

			SELECT productos.id, productos.nombre, 0 - SUM(items_ajustes_de_stock.cantidad) AS cantidad 
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

			UNION ALL

			SELECT productos.id, productos.nombre, SUM(items_remito_retiro.cantidad) AS cantidad
			FROM remitos_retiro
			INNER JOIN items_remito_retiro 
				ON remitos_retiro.id = items_remito_retiro.remito
			INNER JOIN productos
				ON items_remito_retiro.producto = productos.id
			WHERE remitos_retiro.fecha_creacion BETWEEN '", fdesde, "' AND '", fhasta, "'
			AND remitos_retiro.cliente = '", cliente, "'
			AND items_remito_retiro.reposicion = TRUE
			GROUP BY productos.id, productos.nombre
		) AS rrrs
		JOIN productos producto
			ON rrrs.id = producto.id
		GROUP BY rrrs.id,rrrs.nombre, producto.puede_tener_rfid
		ORDER BY rrrs.nombre ASC
		)
	");

	SET @stmt = CASE WHEN mostrarpuedeutilizarrfid
		THEN CONCAT("
			SELECT 'producto', 'cantidad', 'puede_tener_rfid'
			UNION ALL
			SELECT nombre AS producto, 
			cantidad, 
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
			SELECT 'producto', 'cantidad'
			UNION ALL
			SELECT nombre AS producto, 
			cantidad
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

