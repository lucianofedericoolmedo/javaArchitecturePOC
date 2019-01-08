sh impresoras-productivo.sh
sh servicios-productivo.sh
sudo service tomcat7 stop && sudo rm -rf /var/lib/tomcat7/webapps/* /var/lib/tomcat7/webapps/consolidar.war && mvn clean package -Pgrunt-build,graylog-prod && sudo cp target/consolidar.war /var/lib/tomcat7/webapps && sudo service tomcat7 restart
