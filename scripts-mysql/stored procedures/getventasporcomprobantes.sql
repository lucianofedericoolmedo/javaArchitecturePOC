DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `getventasporcomprobantes`(IN param_cliente CHAR(50), IN fechadesde CHAR(25), IN fechahasta CHAR(25))
BEGIN 
	SELECT ventas.clienteid, 
		ventas.clienterazonsocial, 
		ventas.comprobantetipo, 
		ventas.numerocomprobante, 
		ventas.fecha, 
		ventas.productoid, 
		ventas.productonombre, 
		SUM(ventas.cantidad) AS cantidad FROM (
		SELECT 'REMITO RETIRO' AS comprobantetipo,
			remitos_retiro.numero AS numerocomprobante,
			remitos_retiro.fecha_creacion AS fecha,
			remitos_retiro.cliente AS clienteid,
			clientes.razon_social AS clienterazonsocial,
			productos.id AS productoid,
			productos.nombre AS productonombre,
			SUM(items_remito_retiro.cantidad) AS cantidad 
		FROM remitos_retiro
		INNER JOIN items_remito_retiro
			ON remitos_retiro.id = items_remito_retiro.remito
		INNER JOIN clientes
			ON remitos_retiro.cliente = clientes.id 
		INNER JOIN productos
			ON items_remito_retiro.producto = productos.id
		WHERE (remitos_retiro.cliente = param_cliente)
		AND (remitos_retiro.fecha_creacion BETWEEN fechadesde AND fechahasta)
		GROUP BY remitos_retiro.numero,
			 remitos_retiro.fecha_creacion,
			 remitos_retiro.cliente,
			 productos.id,
			 productos.nombre,
			 clientes.razon_social
			 
		UNION 
		
		SELECT 'REMITO STOCK' AS comprobantetipo,
			remitos_stock.numero AS numerocomprobante,
			remitos_stock.fecha_creacion AS fecha,
			remitos_stock.cliente AS clienteid,
			clientes.razon_social AS clienterazonsocial,
			productos.id AS productoid,
			productos.nombre AS productonombre,
			SUM(items_remito_stock.cantidad) AS cantidad 
		FROM remitos_stock
		INNER JOIN items_remito_stock
			ON remitos_stock.id = items_remito_stock.remito
		INNER JOIN clientes
			ON remitos_stock.cliente = clientes.id 
		INNER JOIN productos
			ON items_remito_stock.producto = productos.id
		WHERE (remitos_stock.cliente = param_cliente)
		AND (remitos_stock.fecha_creacion BETWEEN fechadesde AND fechahasta)
		GROUP BY remitos_stock.numero,
			 remitos_stock.fecha_creacion,
			 remitos_stock.cliente,
			 productos.id,
			 productos.nombre,
			 clientes.razon_social

		UNION 
		
		SELECT 'AJUSTE DE STOCK INCR. RR' AS comprobantetipo,
			ajustes_de_stock.numero AS numerocomprobante,
			ajustes_de_stock.fecha_creacion AS fecha,
			ajustes_de_stock.cliente AS clienteid,
			clientes.razon_social AS clienterazonsocial,
			productos.id AS productoid,
			productos.nombre AS productonombre,
			SUM(items_ajustes_de_stock.cantidad) AS cantidad 
		FROM ajustes_de_stock
		INNER JOIN items_ajustes_de_stock
			ON ajustes_de_stock.id = items_ajustes_de_stock.remito
		INNER JOIN motivos_de_ajuste
			ON motivos_de_ajuste.id = items_ajustes_de_stock.motivo_ajuste
			AND motivos_de_ajuste.id = 'e9247af9-5c71-c0c0-d2ba-aceb89446c25'
		INNER JOIN clientes
			ON ajustes_de_stock.cliente = clientes.id 
		INNER JOIN productos
			ON items_ajustes_de_stock.producto = productos.id
		WHERE (ajustes_de_stock.cliente = param_cliente)
		AND (ajustes_de_stock.fecha_creacion BETWEEN fechadesde AND fechahasta)
		GROUP BY ajustes_de_stock.numero,
			 ajustes_de_stock.fecha_creacion,
			 ajustes_de_stock.cliente,
			 productos.id,
			 productos.nombre,
			 clientes.razon_social

		UNION 
		
		SELECT 'AJUSTE DE STOCK INCR. RE' AS comprobantetipo,
			ajustes_de_stock.numero AS numerocomprobante,
			ajustes_de_stock.fecha_creacion AS fecha,
			ajustes_de_stock.cliente AS clienteid,
			clientes.razon_social AS clienterazonsocial,
			productos.id AS productoid,
			productos.nombre AS productonombre,
			SUM(items_ajustes_de_stock.cantidad) AS cantidad 
		FROM ajustes_de_stock
		INNER JOIN items_ajustes_de_stock
			ON ajustes_de_stock.id = items_ajustes_de_stock.remito
		INNER JOIN motivos_de_ajuste
			ON motivos_de_ajuste.id = items_ajustes_de_stock.motivo_ajuste
			AND motivos_de_ajuste.id = '1cbdcb23-a85a-c5a7-7ba7-8798db499648'
		INNER JOIN clientes
			ON ajustes_de_stock.cliente = clientes.id 
		INNER JOIN productos
			ON items_ajustes_de_stock.producto = productos.id
		WHERE (ajustes_de_stock.cliente = param_cliente)
		AND (ajustes_de_stock.fecha_creacion BETWEEN fechadesde AND fechahasta)
		GROUP BY ajustes_de_stock.numero,
			 ajustes_de_stock.fecha_creacion,
			 ajustes_de_stock.cliente,
			 productos.id,
			 productos.nombre,
			 clientes.razon_social

		UNION 
		
		SELECT 'AJUSTE DE STOCK DECR. RR' AS comprobantetipo,
			ajustes_de_stock.numero AS numerocomprobante,
			ajustes_de_stock.fecha_creacion AS fecha,
			ajustes_de_stock.cliente AS clienteid,
			clientes.razon_social AS clienterazonsocial,
			productos.id AS productoid,
			productos.nombre AS productonombre,
			0 - SUM(items_ajustes_de_stock.cantidad) AS cantidad 
		FROM ajustes_de_stock
		INNER JOIN items_ajustes_de_stock
			ON ajustes_de_stock.id = items_ajustes_de_stock.remito
		INNER JOIN motivos_de_ajuste
			ON motivos_de_ajuste.id = items_ajustes_de_stock.motivo_ajuste
			AND motivos_de_ajuste.id = 'f8d29aad-439f-1204-18b1-cb8a92e64486'
		INNER JOIN clientes
			ON ajustes_de_stock.cliente = clientes.id 
		INNER JOIN productos
			ON items_ajustes_de_stock.producto = productos.id
		WHERE (ajustes_de_stock.cliente = param_cliente)
		AND (ajustes_de_stock.fecha_creacion BETWEEN fechadesde AND fechahasta)
		GROUP BY ajustes_de_stock.numero,
			 ajustes_de_stock.fecha_creacion,
			 ajustes_de_stock.cliente,
			 productos.id,
			 productos.nombre,
			 clientes.razon_social

		UNION 
		
		SELECT 'AJUSTE DE STOCK DECR. RE' AS comprobantetipo,
			ajustes_de_stock.numero AS numerocomprobante,
			ajustes_de_stock.fecha_creacion AS fecha,
			ajustes_de_stock.cliente AS clienteid,
			clientes.razon_social AS clienterazonsocial,
			productos.id AS productoid,
			productos.nombre AS productonombre,
			0 - SUM(items_ajustes_de_stock.cantidad) AS cantidad 
		FROM ajustes_de_stock
		INNER JOIN items_ajustes_de_stock
			ON ajustes_de_stock.id = items_ajustes_de_stock.remito
		INNER JOIN motivos_de_ajuste
			ON motivos_de_ajuste.id = items_ajustes_de_stock.motivo_ajuste
			AND motivos_de_ajuste.id = '53c2efb2-2640-598e-dd77-391cdabbd7ce'
		INNER JOIN clientes
			ON ajustes_de_stock.cliente = clientes.id 
		INNER JOIN productos
			ON items_ajustes_de_stock.producto = productos.id
		WHERE (ajustes_de_stock.cliente = param_cliente)
		AND (ajustes_de_stock.fecha_creacion BETWEEN fechadesde AND fechahasta)
		GROUP BY ajustes_de_stock.numero,
			 ajustes_de_stock.fecha_creacion,
			 ajustes_de_stock.cliente,
			 productos.id,
			 productos.nombre,
			 clientes.razon_social
	 ) AS ventas
	GROUP BY ventas.clienteid, 
		 ventas.clienterazonsocial, 
		 ventas.comprobantetipo, 
		 ventas.numerocomprobante, 
		 ventas.fecha, 
		 ventas.productoid, 
		 ventas.productonombre;
END$$
DELIMITER ;

