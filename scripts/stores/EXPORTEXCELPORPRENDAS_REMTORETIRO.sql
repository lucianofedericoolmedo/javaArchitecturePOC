-- Function: detallefacturacionporprendas(character varying, character varying, character varying, boolean, character varying, character varying)

-- DROP FUNCTION detallefacturacionporprendas(character varying, character varying, character varying, boolean, character varying, character varying);

CREATE OR REPLACE FUNCTION detallefacturacionporprendas(fdesde character varying, fhasta character varying, cliente character varying, mostrarpuedeutilizarrfid boolean, archivo character varying, separador character varying)
  RETURNS void AS
$BODY$
    DECLARE
	query TEXT;
    BEGIN
	query := format('(SELECT datos.id, datos.nombre, SUM(datos.cantidad) AS cantidad, producto.puede_tener_rfid FROM (
			SELECT productos.id, productos.nombre, SUM(items_remito_retiro.cantidad) AS cantidad 
			FROM remitos_retiro
			INNER JOIN items_remito_retiro
				ON remitos_retiro.id = items_remito_retiro.remito
				AND (items_remito_retiro.tipo_item IS NULL OR items_remito_retiro.tipo_item != ''ALQUILER'')
				AND (remitos_retiro.es_autogenerado_por_diferencia_de_stock != TRUE OR items_remito_retiro.tipo_item IS NULL)
			INNER JOIN productos
				ON items_remito_retiro.producto = productos.id
			WHERE remitos_retiro.fecha_creacion BETWEEN %1$L AND %2$L
			AND remitos_retiro.cliente = %3$L
			GROUP BY productos.id, productos.nombre 

			UNION ALL 

			SELECT productos.id, productos.nombre, SUM(facturaciones_alquileres.cantidad_facturada) AS cantidad 
			FROM facturaciones_alquileres
			INNER JOIN items_alquiler
				ON facturaciones_alquileres.item_alquiler = items_alquiler.id
			INNER JOIN alquileres
				ON items_alquiler.remito = alquileres.id
			INNER JOIN productos
				ON facturaciones_alquileres.producto = productos.id
			WHERE facturaciones_alquileres.fecha_facturacion BETWEEN %1$L AND %2$L
			AND alquileres.cliente = %3$L
			GROUP BY productos.id, productos.nombre
			   
			UNION ALL
			     
			SELECT productos.id, productos.nombre, SUM(items_remito_stock.cantidad) AS cantidad 
			FROM remitos_stock
			INNER JOIN items_remito_stock
				ON remitos_stock.id = items_remito_stock.remito
			INNER JOIN productos
				ON items_remito_stock.producto = productos.id
			WHERE remitos_stock.fecha_creacion BETWEEN %1$L AND %2$L
			AND remitos_stock.cliente = %3$L
			GROUP BY productos.id, productos.nombre

			UNION ALL

			SELECT productos.id, productos.nombre, SUM(items_ajustes_de_stock.cantidad) AS cantidad 
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
			GROUP BY productos.id, productos.nombre

			UNION ALL

			SELECT productos.id, productos.nombre, SUM(items_ajustes_de_stock.cantidad) AS cantidad 
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
			GROUP BY productos.id, productos.nombre

			UNION ALL

			SELECT productos.id, productos.nombre, 0 - SUM(items_ajustes_de_stock.cantidad) AS cantidad 
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
			GROUP BY productos.id, productos.nombre

			UNION ALL

			SELECT productos.id, productos.nombre, 0 - SUM(items_ajustes_de_stock.cantidad) AS cantidad 
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
			GROUP BY productos.id, productos.nombre
		) AS datos
		JOIN productos producto
			ON datos.id = producto.id
		GROUP BY datos.id, datos.nombre, producto.puede_tener_rfid)',
		fdesde, fhasta, cliente
	);
	IF $4 THEN 
		EXECUTE format('COPY (
					SELECT nombre, 
					cantidad, 
					CASE WHEN puede_tener_rfid = TRUE THEN ''SI'' ELSE ''NO'' END AS puede_tener_rfid 
					FROM '||query||' AS result 
					ORDER BY result.nombre
				) TO %1$L With CSV DELIMITER %2$L HEADER;', archivo, separador);
	ELSE 
		EXECUTE format('COPY (
					SELECT nombre, 
					cantidad FROM '||query||' AS result 
					ORDER BY result.nombre
				) TO %1$L With CSV DELIMITER %2$L HEADER;', archivo, separador);
	END IF;
    END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION detallefacturacionporprendas(character varying, character varying, character varying, boolean, character varying, character varying)
  OWNER TO postgres;

