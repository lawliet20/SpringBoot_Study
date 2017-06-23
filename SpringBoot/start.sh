rm -f .gateway.tpid
rm -f gateway.log

SPRING_OPTS=""
# SPRING_OPTS="${SPRING_OPTS} --eureka.client.serviceUrl.defaultZone=http://admin:GenieRegistry@118.187.50.42:8761/eureka/"
# SPRING_OPTS="${SPRING_OPTS} --spring.cloud.config.uri=http://admin:GenieRegistry@118.187.50.42:8761/config"
SPRING_OPTS="${SPRING_OPTS} --spring.profiles.active=prod,swagger"
SPRING_OPTS="${SPRING_OPTS} --server.port=8402"
# SPRING_OPTS="${SPRING_OPTS} --jhipster.cors.exposed-headers=X-Total-Count"


nohup java -jar gateway-0.0.1-SNAPSHOT.war $SPRING_OPTS > gateway.log 2>&1 &
echo $! > .gateway.tpid
