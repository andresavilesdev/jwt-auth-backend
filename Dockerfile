FROM eclipse-temurin:21.0.3_9-jdk

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline

COPY src/ src
RUN ./mvnw clean package -DskipTests

ENTRYPOINT ["java", "-jar", "target/demo-jwt-0.0.1-SNAPSHOT.jar"]
