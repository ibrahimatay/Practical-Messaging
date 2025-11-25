# Kafka File Watcher Connector

This project is a custom Apache Kafka Source Connector that monitors a specified directory for file changes and publishes the content and metadata of changed files to a Kafka topic.

## Features

- **File Monitoring**: Watches a configured directory for file modifications.
- **Metadata Extraction**: Captures file metadata such as file name, path, and modification time.
- **Configurable Polling**: Allows customization of the polling interval for checking file changes.
- **Single Task**: Designed to run as a single task to monitor a specific directory.

## Prerequisites

- Java 21 or higher
- Gradle
- Apache Kafka (for running the connector in a Connect cluster)

## Configuration

The connector can be configured with the following properties:

| Property | Description | Default | Importance |
| :--- | :--- | :--- | :--- |
| `file.watcher.dir` | The absolute path of the directory to watch for file changes. | (Required) | HIGH |
| `topic` | The Kafka topic to publish the file data to. | (Required) | HIGH |
| `poll.interval.ms` | The interval in milliseconds at which the connector polls for changes. | `1000` | LOW |

## Build

To build the project and create the connector JAR, run:

```bash
./gradlew build
```

The resulting JAR file will be located in the `build/libs` directory.

## Usage

### Running Locally (for development/testing)

The project includes a `ConnectorRunner` for running the connector locally without a full Kafka Connect cluster setup. You can run it using the Gradle task:

```bash
./gradlew runRunner
```

### Deploying to Kafka Connect

1.  Build the project using `./gradlew build`.
2.  Copy the generated JAR file from `build/libs` to your Kafka Connect plugin path.
3.  Restart your Kafka Connect worker.
4.  Create a new connector instance using the REST API or configuration file.

**Example Connector Configuration (JSON):**

```json
{
  "name": "file-watcher-connector",
  "config": {
    "connector.class": "com.ibrahimatay.kafka.connect.filewatcher.FileWatcherSourceConnector",
    "tasks.max": "1",
    "file.watcher.dir": "/path/to/watched/directory",
    "topic": "file-changes-topic",
    "poll.interval.ms": "2000"
  }
}
```

## Development

- **Source Code**: Located in `src/main/java`.
- **Main Connector Class**: `com.ibrahimatay.kafka.connect.filewatcher.FileWatcherSourceConnector`.
- **Configuration Class**: `com.ibrahimatay.kafka.connect.filewatcher.FileWatcherConfig`.

## License

[Add License Information Here]
