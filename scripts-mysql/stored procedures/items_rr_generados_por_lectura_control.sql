DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `items_rr_generados_por_lectura_control`(IN remito_retiro_id CHAR(50), IN archivo CHAR(100), separador CHAR(5))
BEGIN 
	SET @stmt = CONCAT("
		SELECT 'producto', 'cantidad'
		UNION ALL
		SELECT * FROM (
		SELECT producto.nombre AS producto, SUM(item.cantidad) AS cantidad
		FROM items_remito_retiro item
		JOIN productos producto ON producto.id = item.producto
		WHERE item.por_ausencia_en_lectura_control = TRUE
		AND item.remito = '", remito_retiro_id, "'
		GROUP BY producto.nombre
		ORDER BY producto.nombre ASC
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
