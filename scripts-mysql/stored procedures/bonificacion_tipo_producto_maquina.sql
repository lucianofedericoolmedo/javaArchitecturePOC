DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `bonificacion_tipo_producto_maquina`(IN param_maquina CHAR(50), IN param_bonificacion CHAR(50), IN param_fechadesde DATETIME, IN param_fechahasta DATETIME)
BEGIN
	SELECT tipos.valor,
		productividad.cantidad AS producido,
		item.cantidad_produccion_requerida AS cantidad_requerida,
		(productividad.cantidad * 100 / item.cantidad_produccion_requerida) AS porcentaje_requerido_producido,
		item.porcentaje_bonificacion_total AS porcentaje_item
	FROM bonificaciones bonificacion
	JOIN bonificacion_tipo_producto_items item 
		ON bonificacion.id = item.bonificacion 
	JOIN tipo_producto
		ON item.tipo_producto = tipo_producto.tipo_id
	JOIN tipos
		ON tipo_producto.tipo_id = tipos.id
	LEFT JOIN (
		SELECT producto.tipo_producto_id AS tipo_producto, SUM(sin_agrupar.cantidad) AS cantidad FROM (
			SELECT item_entrega.cantidad as cantidad,
				producto
			FROM items_remito_entrega item_entrega
			WHERE maquina = param_maquina
			AND (item_entrega.fecha_creacion BETWEEN param_fechadesde AND param_fechahasta)

			UNION ALL

			SELECT item_stock.cantidad as cantidad,
				producto
			FROM items_remito_stock item_stock
			WHERE maquina = param_maquina
			AND (item_stock.fecha_creacion BETWEEN param_fechadesde AND param_fechahasta)

			UNION ALL

			SELECT item_alquiler.cantidad as cantidad,
				producto
			FROM items_alquiler item_alquiler
			WHERE maquina = param_maquina
			AND (item_alquiler.fecha_creacion BETWEEN param_fechadesde AND param_fechahasta)

			UNION ALL

			SELECT item_camion.cantidad as cantidad,
				producto
			FROM items_remito_entrega_camion item_camion
			WHERE maquina = param_maquina
			AND (item_camion.fecha_creacion BETWEEN param_fechadesde AND param_fechahasta)

			UNION ALL

			SELECT movimiento_stock.cantidad as cantidad,
				producto
			FROM movimientos_stocks movimiento_stock
			WHERE maquina = param_maquina
			AND (movimiento_stock.fecha_creacion BETWEEN param_fechadesde AND param_fechahasta)
			AND movimiento_stock.tipo_movimiento = 'INGRESO_LINEA_DE_PRODUCCION'
		) AS sin_agrupar
		JOIN productos producto
			ON producto.id = sin_agrupar.producto
		GROUP BY producto.tipo_producto_id
	) AS productividad
		ON item.tipo_producto = productividad.tipo_producto
	WHERE bonificacion.id = param_bonificacion
	ORDER BY tipos.valor;
END$$
DELIMITER ;

