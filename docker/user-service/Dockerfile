# Build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY ../../user-service/pom.xml .
RUN mvn dependency:go-offline
COPY ../../user-service/src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app
COPY --from=build /build/target/user-service-*.jar /app/

EXPOSE 8088


CMD java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005  user-service-${JAR_VERSION}.jar