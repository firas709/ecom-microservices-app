# Build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY ../../api-gateway/pom.xml .
RUN mvn dependency:go-offline
COPY ../../api-gateway/src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app
COPY --from=build /build/target/api-gateway-*.jar /app/

EXPOSE 8088


CMD java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005   api-gateway-${JAR_VERSION}.jar