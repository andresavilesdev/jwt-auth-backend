# Imagen modelo
FROM eclipse-temurin:21.0.3_9-jdk

# Definir directorio raiz de nuestro contenedor
WORKDIR /root

# Copiar y pegar archivos dentro del contenedor
COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root

# Descargar las dependencias
RUN ./mvnw dependency:go-offline

# Copiar codigo fuente dentro del contenedor
COPY ./src /root/src

# Construir nuestra aplicacion
RUN ./mvnw clean install -DskipTests

# Levantar nuestra app cuando el contenedor inicie
ENTRYPOINT ["java","-jar","/root/target/demo-jwt-0.0.1-SNAPSHOT.jar"]