DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `resumen_productividad_maquina`(IN fdesde DATETIME, IN fhasta DATETIME, IN maquinaid CHAR(50), IN diferenciaminutos DOUBLE)
BEGIN
 	SELECT SUM(datos_recolectados.cantidad) AS cantidad_producida, 
 	SUM(datos_recolectados.cantidad) * 100 / (maquina.cantidad_por_hora / 60 * diferenciaminutos) AS porcentaje_sobre_capacidad_producida
	FROM (
		SELECT item_entrega.cantidad AS cantidad,
			item_entrega.maquina AS maquina
		FROM items_remito_entrega item_entrega
		WHERE item_entrega.fecha_actualizacion BETWEEN fdesde AND fhasta
		AND item_entrega.maquina = maquinaid
		AND item_entrega.state = 2

		UNION ALL

		SELECT item_stock.cantidad AS cantidad,
			item_stock.maquina AS maquina
		FROM items_remito_stock item_stock
		WHERE item_stock.fecha_actualizacion BETWEEN fdesde AND fhasta
		AND item_stock.maquina = maquinaid
		AND item_stock.state = 2

		UNION ALL

		SELECT item_alquiler.cantidad AS cantidad,
			item_alquiler.maquina AS maquina
		FROM items_alquiler item_alquiler
		WHERE item_alquiler.fecha_actualizacion BETWEEN fdesde AND fhasta
		AND item_alquiler.maquina = maquinaid
		AND item_alquiler.state = 2

		UNION ALL

		SELECT item_camion.cantidad AS cantidad,
			item_camion.maquina AS maquina
		FROM items_remito_entrega_camion item_camion
		WHERE item_camion.fecha_actualizacion BETWEEN fdesde AND fhasta
		AND item_camion.maquina = maquinaid
		AND item_camion.state = 2

		UNION ALL

		SELECT movimiento_stock.cantidad AS cantidad,
			movimiento_stock.maquina AS maquina
		FROM movimientos_stocks movimiento_stock
		WHERE movimiento_stock.fecha_actualizacion BETWEEN fdesde AND fhasta
		AND movimiento_stock.maquina = maquinaid
		AND movimiento_stock.tipo_movimiento = 'INGRESO_LINEA_DE_PRODUCCION'
		
	) AS datos_recolectados
	JOIN maquinas maquina
		ON datos_recolectados.maquina = maquina.id
	GROUP BY maquina.cantidad_por_hora;
END$$
DELIMITER ;

