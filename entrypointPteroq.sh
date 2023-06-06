#!/bin/bash
cd /home/container || exit 1

echo "Java version"
java --version

java -jar /home/java/server.jar -Xms1000M -Xmx${SERVER_MEMORY}M -Dfile.encoding=UTF-8
