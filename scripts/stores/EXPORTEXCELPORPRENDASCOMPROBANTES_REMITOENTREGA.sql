-- Function: detallefacturacionporremitoentregaprendas_csv(character varying, character varying, character varying, boolean, character varying, character varying)

-- DROP FUNCTION detallefacturacionporremitoentregaprendas_csv(character varying, character varying, character varying, boolean, character varying, character varying);

CREATE OR REPLACE FUNCTION detallefacturacionporremitoentregaprendas_csv(fdesde character varying, fhasta character varying, cliente character varying, mostrarpuedeutilizarrfid boolean, archivo character varying, separador character varying)
  RETURNS void AS
$BODY$
    DECLARE
	query TEXT;
    BEGIN
	query := format('(SELECT datos.*, producto.puede_tener_rfid FROM (
			SELECT remitos_entrega.numero AS comprobante, remitos_entrega.fecha_creacion, productos.id AS producto_id, productos.nombre AS producto_nombre, SUM(items_remito_entrega.cantidad) AS cantidad
			FROM remitos_entrega
				INNER JOIN items_remito_entrega
					ON remitos_entrega.id = items_remito_entrega.remito
				INNER JOIN productos
					ON items_remito_entrega.producto = productos.id
			WHERE remitos_entrega.fecha_creacion BETWEEN %1$L AND %2$L
			AND remitos_entrega.cliente = %3$L
			GROUP BY remitos_entrega.numero, remitos_entrega.fecha_creacion,productos.id, productos.nombre

			UNION ALL 

			SELECT alquileres.numero AS comprobante, DATE(facturaciones_alquileres.fecha_facturacion) AS fecha_creacion, productos.id AS producto_id, productos.nombre AS producto_nombre, SUM(facturaciones_alquileres.cantidad_facturada) AS cantidad 
			FROM facturaciones_alquileres
				INNER JOIN items_alquiler
					ON facturaciones_alquileres.item_alquiler = items_alquiler.id
				INNER JOIN alquileres
					ON items_alquiler.remito = alquileres.id
				INNER JOIN productos
					ON facturaciones_alquileres.producto = productos.id
			WHERE facturaciones_alquileres.fecha_facturacion BETWEEN %1$L AND %2$L
			AND alquileres.cliente = %3$L
			GROUP BY alquileres.numero, DATE(facturaciones_alquileres.fecha_facturacion), productos.id, productos.nombre
			
			UNION ALL
			
			SELECT remitos_stock.numero, remitos_stock.fecha_creacion, productos.id, productos.nombre, SUM(items_remito_stock.cantidad) 
			FROM remitos_stock
				INNER JOIN items_remito_stock
					ON remitos_stock.id = items_remito_stock.remito
				INNER JOIN productos
					ON items_remito_stock.producto = productos.id
			WHERE remitos_stock.fecha_creacion BETWEEN %1$L AND %2$L
			AND remitos_stock.cliente = %3$L
			GROUP BY remitos_stock.numero, remitos_stock.fecha_creacion, productos.id, productos.nombre

			UNION ALL

			SELECT ajustes_de_stock.numero, ajustes_de_stock.fecha_creacion, productos.id, productos.nombre, SUM(items_ajustes_de_stock.cantidad) 
			FROM ajustes_de_stock
				INNER JOIN items_ajustes_de_stock
					ON ajustes_de_stock.id = items_ajustes_de_stock.remito
				INNER JOIN productos
					ON items_ajustes_de_stock.producto = productos.id
				INNER JOIN motivos_de_ajuste
					ON motivos_de_ajuste.id = items_ajustes_de_stock.motivo_ajuste
					AND motivos_de_ajuste.id = ''e9247af9-5c71-c0c0-d2ba-aceb89446c25''
			WHERE ajustes_de_stock.fecha_creacion BETWEEN %1$L AND %2$L
			AND ajustes_de_stock.cliente = %3$L
			GROUP BY ajustes_de_stock.numero, ajustes_de_stock.fecha_creacion, productos.id, productos.nombre

			UNION ALL

			SELECT ajustes_de_stock.numero, ajustes_de_stock.fecha_creacion, productos.id, productos.nombre, SUM(items_ajustes_de_stock.cantidad) 
			FROM ajustes_de_stock
				INNER JOIN items_ajustes_de_stock
					ON ajustes_de_stock.id = items_ajustes_de_stock.remito
				INNER JOIN productos
					ON items_ajustes_de_stock.producto = productos.id
				INNER JOIN motivos_de_ajuste
					ON motivos_de_ajuste.id = items_ajustes_de_stock.motivo_ajuste
					AND motivos_de_ajuste.id = ''1cbdcb23-a85a-c5a7-7ba7-8798db499648''
			WHERE ajustes_de_stock.fecha_creacion BETWEEN %1$L AND %2$L
			AND ajustes_de_stock.cliente = %3$L
			GROUP BY ajustes_de_stock.numero, ajustes_de_stock.fecha_creacion, productos.id, productos.nombre

			UNION ALL

			SELECT ajustes_de_stock.numero, ajustes_de_stock.fecha_creacion, productos.id, productos.nombre, 0 -SUM(items_ajustes_de_stock.cantidad) 
			FROM ajustes_de_stock
				INNER JOIN items_ajustes_de_stock
					ON ajustes_de_stock.id = items_ajustes_de_stock.remito
				INNER JOIN productos
					ON items_ajustes_de_stock.producto = productos.id
				INNER JOIN motivos_de_ajuste
					ON motivos_de_ajuste.id = items_ajustes_de_stock.motivo_ajuste
					AND motivos_de_ajuste.id = ''f8d29aad-439f-1204-18b1-cb8a92e64486''
			WHERE ajustes_de_stock.fecha_creacion BETWEEN %1$L AND %2$L
			AND ajustes_de_stock.cliente = %3$L
			GROUP BY ajustes_de_stock.numero, ajustes_de_stock.fecha_creacion, productos.id, productos.nombre

			UNION ALL

			SELECT ajustes_de_stock.numero, ajustes_de_stock.fecha_creacion, productos.id, productos.nombre, 0 -SUM(items_ajustes_de_stock.cantidad) 
			FROM ajustes_de_stock
				INNER JOIN items_ajustes_de_stock
					ON ajustes_de_stock.id = items_ajustes_de_stock.remito
				INNER JOIN productos
					ON items_ajustes_de_stock.producto = productos.id
				INNER JOIN motivos_de_ajuste
					ON motivos_de_ajuste.id = items_ajustes_de_stock.motivo_ajuste
					AND motivos_de_ajuste.id = ''53c2efb2-2640-598e-dd77-391cdabbd7ce''
			WHERE ajustes_de_stock.fecha_creacion BETWEEN %1$L AND %2$L
			AND ajustes_de_stock.cliente = %3$L
			GROUP BY ajustes_de_stock.numero, ajustes_de_stock.fecha_creacion, productos.id, productos.nombre

			UNION ALL

			SELECT remitos_retiro.numero AS comprobante, remitos_retiro.fecha_creacion, productos.id AS producto_id, productos.nombre AS producto_nombre, SUM(items_remito_retiro.cantidad) AS cantidad
			FROM remitos_retiro
				INNER JOIN items_remito_retiro
					ON remitos_retiro.id = items_remito_retiro.remito
				INNER JOIN productos
					ON items_remito_retiro.producto = productos.id
			WHERE remitos_retiro.fecha_creacion BETWEEN %1$L AND %2$L
			AND remitos_retiro.cliente = %3$L
			AND items_remito_retiro.reposicion = TRUE
			GROUP BY remitos_retiro.numero, remitos_retiro.fecha_creacion,productos.id, productos.nombre
		) AS datos
		JOIN productos producto
			ON datos.producto_id = producto.id
		)',
		fdesde, fhasta, cliente
	);
	IF $4 THEN 
		EXECUTE format('COPY (
					SELECT comprobante, 
					TO_CHAR(fecha_creacion, ' || quote_literal('DD/MM/YYYY') || ') AS fecha, 
					producto_nombre AS producto, 
					cantidad, 
					CASE WHEN puede_tener_rfid = TRUE THEN ''SI'' ELSE ''NO'' END AS puede_tener_rfid 
					FROM '||query||' AS result
					ORDER BY comprobante, producto_nombre
				) TO %1$L With CSV DELIMITER %2$L HEADER;', archivo, separador);
	ELSE 
		EXECUTE format('COPY (
					SELECT comprobante, 
					TO_CHAR(fecha_creacion, ' || quote_literal('DD/MM/YYYY') || ') AS fecha, 
					producto_nombre AS producto, 
					cantidad 
					FROM '||query||' AS result
					ORDER BY comprobante, producto_nombre
				) TO %1$L With CSV DELIMITER %2$L HEADER;', archivo, separador);
	END IF;
    END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION detallefacturacionporremitoentregaprendas_csv(character varying, character varying, character varying, boolean, character varying, character varying)
  OWNER TO postgres;

