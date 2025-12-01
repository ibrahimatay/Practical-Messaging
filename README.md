# Practical Messaging
[![.github/workflows/maven.yml](https://github.com/ibrahimatay/Practical-Messaging/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/ibrahimatay/Practical-Messaging/actions/workflows/maven.yml)

This project includes various messaging patterns and application examples using Apache Kafka and Java.

It aims to demonstrate how messaging systems can be integrated and utilized in real-world scenarios, providing a comprehensive guide for developers working with distributed systems.

|                               Sample                                |                                                                                                    Description                                                                                                     |
|:-------------------------------------------------------------------:|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Hello world](/hello-world/src/main/java/com/ibrahimatay/Main.java) | This Spring Boot application sends a "Hello world!" message to Kafka every 5 seconds and listens to these messages with a consumer. The required Kafka topic is automatically created when the application starts. |
|   [File Watcher, Apache Kafka connector](/filewatcher-connecter/)   |This project is a custom Apache Kafka Source Connector that monitors a specified directory for file changes and publishes the content and metadata of changed files to a Kafka topic.|

## Tools
- [KafkIO](https://www.kafkio.com/)

## Notes

- [Apache Kafka - Confluence](https://cwiki.apache.org/confluence/display/KAFKA/Index)
- [Apache Kafka - Github Mirror](https://github.com/apache/kafka)
- [Open-Source Web UI for Apache Kafka Management](https://github.com/provectus/kafka-ui)
- [kafka-stack-docker-compose](https://github.com/conduktor/kafka-stack-docker-compose)
- [Spring for Apache Kafka](https://spring.io/projects/spring-kafka)
