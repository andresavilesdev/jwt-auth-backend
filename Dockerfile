FROM eclipse-temurin:21.0.3_9-jdk

WORKDIR /root

COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root

# ✅ Dar permisos de ejecución
RUN chmod +x ./mvnw

# Descargar las dependencias
RUN ./mvnw dependency:go-offline

COPY ./src /root/src

RUN ./mvnw clean install -DskipTests

# Levantar nuestra app cuando el contenedor inicie
ENTRYPOINT ["java","-jar","/root/target/demo-jwt-0.0.1-SNAPSHOT.jar"]