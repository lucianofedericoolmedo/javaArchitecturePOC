DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `codigos_sin_lectura_desde`(IN param_fdesde DATETIME, IN param_lugarid CHAR(50))
BEGIN
	SELECT codigorfid as codigo, ultimas_lecturas.fecha_creacion as fecha_creacion, p.nombre as nombre, ll.nombre as lugar FROM (
		SELECT lrr.codigo, lrr.fecha_creacion, lrr.lugar FROM lecturas_registros_rfid lrr
		INNER JOIN (
			SELECT subLrr.codigo, MAX(subLrr.fecha_creacion) AS max_fecha FROM lecturas_registros_rfid subLrr
			WHERE (param_lugarid = '' OR subLrr.lugar = param_lugarid)
			GROUP BY subLrr.codigo
		) AS maximas
		ON lrr.codigo = maximas.codigo
		AND lrr.fecha_creacion = maximas.max_fecha
		AND maximas.max_fecha < param_fdesde
	) AS ultimas_lecturas
	JOIN registros_rfid rr ON codigorfid = ultimas_lecturas.codigo AND rr.estado = 'ACTUAL'
	JOIN productos p ON rr.producto = p.id
	JOIN lugares_lectura ll ON ultimas_lecturas.lugar = ll.id
	ORDER BY ultimas_lecturas.fecha_creacion DESC;
END$$
DELIMITER ;

