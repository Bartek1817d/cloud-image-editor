FROM openjdk:8
VOLUME /tmp
ADD view-microservice-0.0.1.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
