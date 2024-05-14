#!/bin/bash
sdk use java 17.0.4-amzn
./mvnw clean install -DskipTests
#./mvnw clean install sonar:sonar -U -X -DskipTests
./mvnw -Pprod verify jib:dockerBuild -DskipTests
DATE_WITH_TIME=`date "+%Y%m%d.%H%M%S.%S"` #add %3N as we want millisecond too
IMAGE_TAG=awoedey2k/httpproxy:${DATE_WITH_TIME}
echo "version -> ${DATE_WITH_TIME}"
docker tag switchhttpproxy  ${IMAGE_TAG}
docker push ${IMAGE_TAG}
