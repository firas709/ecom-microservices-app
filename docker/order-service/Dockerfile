# Build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY ../../order-service/pom.xml .
RUN mvn dependency:go-offline
COPY ../../order-service/src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app
COPY --from=build /build/target/order-service-*.jar /app/

EXPOSE 8088


CMD java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005  order-service-${JAR_VERSION}.jar