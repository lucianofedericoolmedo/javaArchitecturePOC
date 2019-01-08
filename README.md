# Proof of concept

## Configurar tomcat 8.

Una forma de hacerlo es con una instancia de docker.


> docker run -it -d --name 't8-j8' -p 8081:8080 -e JAVA_OPTS='-Xmx1g' tomcat:8.0-jre8-alpine

### Configurar (en la instancia docker seria)
### en /usr/local/tomcat/conf/web.xml

<context-param>
   <description>Enable Spring Profile for this environment</description>
   <param-name>spring.profiles.active</param-name>
   <param-value>localhost</param-value>
 </context-param>



Luego correr 

> sh build-dev.sh (en linux)

o 

> sudo service tomcat8 stop && sudo rm -rf /var/lib/tomcat8/webapps/* /var/lib/tomcat8/webapps/CONSOLIDAR.war && mvn clean package && sudo cp target/CONSOLIDAR.war /var/lib/tomcat8/webapps && sudo service tomcat8 restart


---------------------------------

### Autenticar un usuario:

> curl 'http://localhost:8080/CONSOLIDAR/services/auth/login' --data-binary '{"username":"admin","password":"123456"}'  -H 'Content-Type: application/json;charset=UTF-8'

### Crear una "localidad" (entidad basica) , no requiere autenticacion

> curl 'http://localhost:8080/CONSOLIDAR/services/localidades' --data-binary '{"valor":"example","codigo":"unCodigo"}'  -H 'Content-Type: application/json;charset=UTF-8' 

Consultar localidades creadas (paginadas)

> curl 'http://localhost:8080/CONSOLIDAR/services/localidades?&page=0&pageSize=10'


?&page=0&pageSize=10

---------------------------------

## Perfiles de la aplicación

Al momento de *construir* o *empaquetar* la aplicación, se podrán seleccionar distintos perfiles los cuales podrían contener distintas variables de configuración del contexto de la aplicación; por ejemplo, direcciones de distintas bases de datos, propiedades utilizables dentro de la aplicación, etc.

Estos perfiles deben ser creados y configurados dentro del directorio `src/main/resources/profiles/{nombre-perfil}`.
Dentro de esta carpeta se podrán agregar los archivos de configuración `{nombre archivo configuración}.properties` o `log4j2.xml`, por ejemplo.

Dentro del archivo `pom.xml` se deben agregar, en la sección `profiles`, los distintos perfiles que se deseen utilizar. 

~~~
	<profile>
	  <id>{id-perfil}</id>
	  <!-- Si el perfil debe ser utilizado por defecto -->
	  <activation>
	    <activeByDefault>true</activeByDefault>
	  </activation>
	  <properties>
	    <profile.name>{nombre-perfil}</profile.name>
	  </properties>
	</profile>
~~~

El nombre asignado a `{id-perfil}` es el que se utilizará al momento de construir la aplicación, de la manera `mvn clean package -P{id-perfil}`; mientras que el nombre asignado a `{nombre-perfil}` es el que se utilizará para encontrar, y que debe ser exactamente igual que el nombre del directorio del perfil con los archivos mencionado previamente. 


## Lints

### Maven Checkstyle

La arquitectura posee revisión de estilo de código de maven-checkstyle-plugin con [Checkstyle](http://checkstyle.sourceforge.net/). Por defecto, el build de la aplicación sera exitoso aunque se encuentren errores de estilo de código aunque podrá ser configurado para que, en caso de encontrar errores de estilo, el build falle. Para conseguir esto se debe modificar la configuración de la ejecución del plugin con id `<id>checkstyle</id>`.

Para omitir la verificación de estilo de código de la aplicación se debe ejecutar el ciclo de maven de la manera `mvn clean package -Dcheckstyle.skip` o asignar la fase de ejecución a `<phase>none</phase>` (por defecto deshabilitado, cambiar fase a `validate` si se quiere realizar una revisión de estilo).

También se podrá realizar un reporte externo de los resultados de la revisión de estilos. Para esto se debe ejectuar `mvn site`, lo que generará la carpeta `target/site` donde se deberá levantar el archivo `checkstyle.html` para ver el reporte.

### SonarQube

Para utilizar revisión de estilo de código con SonarQube se debe descargar y descomprimir el paquete con el servidor del mismo desde el sitio de [SonarQube](https://www.sonarqube.org/downloads/).

Para levantar el servidor debemos ejecutar `bin\windows-x86-xx\StartSonar.bat` (Windows) o `bin/[OS]/sonar.sh console` (Linux) según corresponda. Por defecto el servidor corre en el puerto 9000.

Luego, se debe configurar, en el caso que sea necesario, la dirección del servidor en el archivo `pom.xml`, en el perfil con id *sonar*. Por defecto la dirección será `localhost:9000`.

Para analizar el proyecto maven en el servidor SonarQube se debe ejecutar el comando `mvn sonar:sonar`.
