DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `diferencia_lectura_zona_limpia_csv`(IN remito_entrega_id CHAR(25), IN lectura_control_id CHAR(50), IN archivo CHAR(250), IN separador CHAR(50))
BEGIN 
	SET @stmt = CONCAT("
		SELECT 'numero_comprobante', 'producto', 'cantidad_cargada_remito', 'cantidad_lectura_control'
		UNION ALL
		SELECT re.numero AS numero_comprobante, producto.nombre AS producto, SUM(items_entrega.cantidad) AS cantidad_cargada_remito, SUM(items_control.cantidad) AS cantidad_lectura_control FROM (
			SELECT ire.producto, SUM(cantidad) AS cantidad FROM items_remito_entrega ire
			WHERE ire.remito = '", remito_entrega_id, "'
			AND state = '2'
			GROUP BY ire.producto
		) AS items_entrega
		FULL JOIN (
			SELECT registros_actuales.producto, COUNT(DISTINCT lci.codigorfid) AS cantidad FROM lecturas_control_items lci
			JOIN (
				SELECT * FROM registros_rfid
				WHERE estado = 'ACTUAL'
			) AS registros_actuales ON lci.codigorfid = registros_actuales.codigorfid
			WHERE lectura_control = '", lectura_control_id, "'
			GROUP BY registros_actuales.producto
		) AS items_control ON items_entrega.producto = items_control.producto
		JOIN productos producto ON items_entrega.producto = producto.id
		JOIN remitos_entrega re ON re.id = '", remito_entrega_id, "'
		GROUP BY re.numero, producto.nombre
		ORDER BY producto.nombre ASC
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

