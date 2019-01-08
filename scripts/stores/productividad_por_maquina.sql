-- Function: productividad_por_maquina(character varying, timestamp without time zone, timestamp without time zone, double precision)

-- DROP FUNCTION productividad_por_maquina(character varying, timestamp without time zone, timestamp without time zone, double precision);

CREATE OR REPLACE FUNCTION productividad_por_maquina(IN maquina character varying, IN fechadesde timestamp without time zone, IN fechahasta timestamp without time zone, IN horasdiferencia double precision)
  RETURNS TABLE(nombreproducto character varying, cantidadtotalproducida double precision, porcentajeproducido double precision, pesototalproducido double precision) AS
$BODY$
	SELECT producto.nombre,
	       SUM(productividad.cantidad),
	       SUM(productividad.cantidad) * 100 / (maquina.cantidad_por_hora * $4),
	       SUM(productividad.cantidad) * producto.peso
	FROM (

		SELECT item_entrega.cantidad as cantidad,
			maquina,
			producto
		FROM items_remito_entrega item_entrega
		WHERE (maquina = '' || $1 || '')
		AND (item_entrega.fecha_creacion BETWEEN $2::timestamp AND $3::timestamp)

		UNION ALL

		SELECT item_stock.cantidad as cantidad,
			maquina,
			producto
		FROM items_remito_stock item_stock
		WHERE (maquina = '' || $1 || '')
		AND (item_stock.fecha_creacion BETWEEN $2::timestamp AND $3::timestamp)

		UNION ALL

		SELECT item_alquiler.cantidad as cantidad,
			maquina,
			producto
		FROM items_alquiler item_alquiler
		WHERE (maquina = '' || $1 || '')
		AND (item_alquiler.fecha_creacion BETWEEN $2::timestamp AND $3::timestamp)

		UNION ALL

		SELECT item_camion.cantidad as cantidad,
			maquina,
			producto
		FROM items_remito_entrega_camion item_camion
		WHERE (maquina = '' || $1 || '')
		AND (item_camion.fecha_creacion BETWEEN $2::timestamp AND $3::timestamp)

		UNION ALL

		SELECT movimiento_stock.cantidad as cantidad,
			maquina,
			producto
		FROM movimientos_stocks movimiento_stock
		WHERE (maquina = '' || $1 || '')
		AND (movimiento_stock.fecha_creacion BETWEEN $2::timestamp AND $3::timestamp)
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

$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION productividad_por_maquina(character varying, timestamp without time zone, timestamp without time zone, double precision)
  OWNER TO postgres;

