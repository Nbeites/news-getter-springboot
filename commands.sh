#!/bin/bash

#Execute Tests
mvn test

#Execute Spring application
CMD="java -Djava.security.egd=file:/dev/./urandom -jar news-0.0.1-SNAPSHOT.jar"
$CMD &
SERVICE_PID=$!


#Wait for Spring execution
wait "$SERVICE_PID"

