FROM adoptopenjdk:11-jre-hotspot
WORKDIR .
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/tmp/app.jar"]
