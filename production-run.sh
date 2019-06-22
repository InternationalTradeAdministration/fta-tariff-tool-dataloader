#!/usr/bin/env bash

export TERRAFREIGHTS_DB_URL="jdbc:mysql://localhost:3306/terrafreights?serverTimezone=UTC"
export TERRAFREIGHTS_DB_USERNAME="service"
export TERRAFREIGHTS_DB_PASSWORD="password"

java -jar build/libs/terra-freights-0.0.1-SNAPSHOT.jar --spring.profiles.active=production