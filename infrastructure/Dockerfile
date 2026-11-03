FROM maven:3.9.16-eclipse-temurin-17

WORKDIR /test
COPY entrypoint.sh .
COPY internal-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["./entrypoint.sh"]
