FROM amazoncorretto:17.0.6
LABEL maintainer=ivan.vakhrushev
ARG JAR_FILE
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
