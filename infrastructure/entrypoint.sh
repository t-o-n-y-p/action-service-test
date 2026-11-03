#!/bin/sh
java -jar -Dsecret=$1 -Dmock=http://wiremock:8080/ internal-0.0.1-SNAPSHOT.jar
