FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package
FROM tomcat:9.0.123-jdk17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/pet-adoption.war /usr/local/tomcat/webapps/ROOT.war
ENV UPLOAD_DIR=/var/lib/pet-adoption/uploads
EXPOSE 8080
