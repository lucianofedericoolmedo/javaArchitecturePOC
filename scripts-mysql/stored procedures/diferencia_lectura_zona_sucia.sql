DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `diferencia_lectura_zona_sucia`(IN remito_retiro_id char(50), IN lectura_control_id char(50))
BEGIN 
	SELECT rr.numero AS numero_comprobante, producto.nombre AS producto, COUNT(DISTINCT rfids.item_rfid) AS cantidad_lectura_comprobante, COUNT(DISTINCT rfids.control_rfid) AS cantidad_lectura_control 
	FROM (
		SELECT rfid AS item_rfid, control_rfids.control_rfid FROM items_comprobantes_rfids items_rfids
		LEFT JOIN (
			SELECT codigorfid AS control_rfid FROM lecturas_control_items
			WHERE lectura_control = lectura_control_id
		) AS control_rfids 
			ON items_rfids.rfid = control_rfids.control_rfid
		WHERE items_rfids.item IN (
			SELECT id FROM items_remito_retiro irr
			WHERE irr.remito = remito_retiro_id
			AND irr.por_ausencia_en_lectura_control = FALSE
		)
		UNION ALL
		SELECT item_rfid, control_rfids.control_rfid FROM (
			SELECT rfid AS item_rfid FROM items_comprobantes_rfids items_rfids
			WHERE items_rfids.item IN (
				SELECT id FROM items_remito_retiro irr
				WHERE irr.remito = remito_retiro_id
				AND irr.por_ausencia_en_lectura_control = FALSE
			)
		) AS rfid_comprobante
		RIGHT JOIN (
			SELECT codigorfid AS control_rfid FROM lecturas_control_items
			WHERE lectura_control = lectura_control_id
		) AS control_rfids
			ON rfid_comprobante.item_rfid = control_rfids.control_rfid
		WHERE rfid_comprobante.item_rfid IS NULL
	) AS rfids
	JOIN (
		SELECT * FROM registros_rfid
		WHERE estado = 'ACTUAL'
	) AS registros_actuales 
		ON ((rfids.item_rfid IS NOT NULL AND rfids.item_rfid = codigorfid) 
			OR (rfids.control_rfid IS NOT NULL AND rfids.control_rfid = codigorfid))
	JOIN productos producto 
		ON registros_actuales.producto = producto.id
	JOIN remitos_retiro rr 
		ON rr.id = remito_retiro_id
	GROUP BY rr.numero, producto.nombre
	ORDER BY producto.nombre ASC;
END$$
DELIMITER ;

