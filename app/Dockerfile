FROM openjdk:17-jdk-slim
RUN addgroup --system thegroup && GroupId=`grep "thegroup" /etc/group|cut -d: -f3` && adduser --system theuser --gid $GroupId
USER theuser
WORKDIR /home/theuser
EXPOSE 8090
COPY target/authboot-app-*.jar /home/theuser/app.jar
ENTRYPOINT ["java", "-Duser.timezone=\"America/Santiago\"","-Djava.security.egd=file:/dev/./urandom","-jar","/home/theuser/app.jar"]