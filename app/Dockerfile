FROM amazoncorretto:21-alpine-jdk
RUN addgroup -S authgroup && adduser -S authuser -G authgroup
USER authuser
WORKDIR /home/authuser
EXPOSE 8090
COPY ../config /home/authuser/config
COPY target/authboot-app-*.jar /home/authuser/app.jar
ENTRYPOINT ["java", "-Duser.timezone=\"America/Santiago\"","-Djava.security.egd=file:/dev/./urandom","-jar","/home/authuser/app.jar"]