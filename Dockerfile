FROM openjdk:17-oracle
COPY ./target/demo-0.0.1-SNAPSHOT.jar Gateway.jar
CMD ["java","-jar","Gateway.jar"]
