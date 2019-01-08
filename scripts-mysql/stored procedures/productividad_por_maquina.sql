DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `productividad_por_maquina`(IN param_maquina CHAR(50), IN fechadesde DATETIME, IN fechahasta DATETIME, IN horasdiferencia DOUBLE)
BEGIN
	SELECT producto.nombre,
	       SUM(productividad.cantidad),
	       SUM(productividad.cantidad) * 100 / (maquina.cantidad_por_hora * horasdiferencia),
	       SUM(productividad.cantidad) * producto.peso
	FROM (

		SELECT item_entrega.cantidad as cantidad,
			maquina,
			producto
		FROM items_remito_entrega item_entrega
		WHERE maquina = param_maquina
		AND (item_entrega.fecha_creacion BETWEEN fechadesde AND fechahasta)

		UNION ALL

		SELECT item_stock.cantidad as cantidad,
			maquina,
			producto
		FROM items_remito_stock item_stock
		WHERE maquina = param_maquina
		AND (item_stock.fecha_creacion BETWEEN fechadesde AND fechahasta)

		UNION ALL

		SELECT item_alquiler.cantidad as cantidad,
			maquina,
			producto
		FROM items_alquiler item_alquiler
		WHERE maquina = param_maquina
		AND (item_alquiler.fecha_creacion BETWEEN fechadesde AND fechahasta)

		UNION ALL

		SELECT item_camion.cantidad as cantidad,
			maquina,
			producto
		FROM items_remito_entrega_camion item_camion
		WHERE maquina = param_maquina
		AND (item_camion.fecha_creacion BETWEEN fechadesde AND fechahasta)

		UNION ALL

		SELECT movimiento_stock.cantidad as cantidad,
			maquina,
			producto
		FROM movimientos_stocks movimiento_stock
		WHERE maquina = param_maquina
		AND (movimiento_stock.fecha_creacion BETWEEN fechadesde AND fechahasta)
		AND movimiento_stock.tipo_movimiento = 'INGRESO_LINEA_DE_PRODUCCION'

	) AS productividad
	JOIN productos producto
		ON productividad.producto = producto.id
	JOIN maquinas maquina
		ON productividad.maquina = maquina.id
	GROUP BY maquina.id, 
		 maquina.cantidad_por_hora,
		 producto.nombre,
		 producto.peso;
END$$
DELIMITER ;

