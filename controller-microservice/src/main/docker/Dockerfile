FROM openjdk:8
VOLUME /tmp
ADD logic-microservice-0.0.1.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 2222
CMD ["java", "-jar", "app.jar"]
