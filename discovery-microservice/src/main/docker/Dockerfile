FROM openjdk:8
VOLUME /tmp
ADD discovery-microservice-0.0.1.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 1111
CMD ["java", "-jar", "app.jar"]