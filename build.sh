#!/bin/bash

./gradlew clean build
cd build/distributions
rm -rf redis-0.1
tar xvf redis-0.1.tar
mv redis-0.1/lib/redis-0.1.jar .
docker-compose up --build -d

