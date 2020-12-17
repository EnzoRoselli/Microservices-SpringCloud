FROM openjdk:11
VOLUME /tmp
EXPOSE 9100
ADD target/*.jar app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]