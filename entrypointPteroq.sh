#!/bin/bash
cd /home/container

echo "Get latest server.jar"
rm server.jar
cp /home/java/server.jar server.jar

echo "Java version"
java --version

# Replace Startup Variables
MODIFIED_STARTUP=`eval echo $(echo ${STARTUP} | sed -e 's/{{/${/g' -e 's/}}/}/g')`
echo ":/home/container$ ${MODIFIED_STARTUP}"

# Run the Server
${MODIFIED_STARTUP}