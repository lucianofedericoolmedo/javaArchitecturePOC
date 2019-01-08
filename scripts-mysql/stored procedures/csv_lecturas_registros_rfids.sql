DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `csv_lecturas_registros_rfids`(codigo_rfid CHAR(24), archivo CHAR(75), separador CHAR(5))
BEGIN 
	SET @st1 = CONCAT("
		SELECT lectura.fecha_creacion AS FECHA, 
				lugar.nombre AS LUGAR, 
				producto.nombre AS PRODUCTO, 
				lectura.traza AS TRAZA
		FROM lecturas_registros_rfid lectura
		JOIN lugares_lectura lugar
			ON lectura.lugar = lugar.id
		JOIN registros_rfid registro
			ON lectura.codigo = registro.codigorfid
			AND lectura.traza = registro.traza
		JOIN productos producto
			ON producto.id = registro.producto
		WHERE lectura.codigo = ?
		ORDER BY lectura.fecha_creacion DESC
		INTO OUTFILE ?
		FIELDS TERMINATED BY ','
		ENCLOSED BY '\"'
		LINES TERMINATED BY '\n';
	");
	PREPARE stmt FROM @st1;
	SET @codigo = codigo_rfid;
	SET @archivo = archivo;
	EXECUTE stmt USING @codigo, @archivo;
	DEALLOCATE PREPARE stmt;

END$$
DELIMITER ;

