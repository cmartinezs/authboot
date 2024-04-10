FROM maven:3.9.6-amazoncorretto-21-al2023 AS build
COPY api /home/authboot/api
COPY app /home/authboot/app
COPY commons /home/authboot/commons
COPY core /home/authboot/core
COPY infra /home/authboot/infra
COPY lombok.config /home/authboot
COPY pom.xml /home/authboot
RUN mvn -f /home/authboot/pom.xml clean package -DskipTests

FROM amazoncorretto:21-alpine-jdk
RUN addgroup -S authgroup && adduser -S authuser -G authgroup
USER authuser
WORKDIR /home/authuser
EXPOSE 8090
COPY --from=build /home/authboot/app/target/authboot-app-*.jar /home/authuser/app.jar
ENTRYPOINT ["java", "-Duser.timezone=\"America/Santiago\"","-Djava.security.egd=file:/dev/./urandom","-jar","/home/authuser/app.jar"]