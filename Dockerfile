FROM maven:3.8.5-openjdk-17-slim AS build
COPY api /home/authboot/api
COPY app /home/authboot/app
COPY commons /home/authboot/commons
COPY core /home/authboot/core
COPY core-impl /home/authboot/core-impl
COPY infra /home/authboot/infra
COPY lombok.config /home/authboot
COPY pom.xml /home/authboot
RUN mvn -f /home/authboot/pom.xml clean package -DskipTests

FROM openjdk:17-jdk-slim
RUN addgroup --system thegroup && GroupId=`grep "thegroup" /etc/group|cut -d: -f3` && adduser --system theuser --gid $GroupId
USER theuser
WORKDIR /home/theuser
EXPOSE 8090
COPY --from=build /home/authboot/app/target/authboot-app-*.jar /home/theuser/app.jar
ENTRYPOINT ["java", "-Duser.timezone=\"America/Santiago\"","-Djava.security.egd=file:/dev/./urandom","-jar","/home/theuser/app.jar"]