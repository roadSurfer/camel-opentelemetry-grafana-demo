FROM openjdk:11-jdk-slim
ADD /target/camel-opentelemetry-grafana-demo-1.0.0-SNAPSHOT.jar observable-service.jar
ADD /agent/opentelemetry-javaagent-all-1.3.0.jar /etc/agent/opentelemetry-javaagent-all.jar
EXPOSE 8080
ENTRYPOINT ["java", "-javaagent:/etc/agent/opentelemetry-javaagent-all.jar" , "-jar", "observable-service.jar"]
