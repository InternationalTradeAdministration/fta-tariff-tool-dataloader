# ITA Tariff Tool
A tool that helps maintain Tariff Rates

## Local Development
Steps to run this application on you local for development purposes

**Prerequisites** 
 - Java 8
 - Gradle
 - NPM (`cd client` then `npm install`)

**Backend** 
 - `gradle bootRun`, http://localhost:8080

**Frontend** `cd client && npm start`, http://localhost:8081

## Running Tests

**Backend** `./gradlew test`

**Frontend**  `cd client && npm test`

## Production Build
```./production-build.sh```

## Production Deployment
 - The following URLs must be accessible from the network where this app is running:
    - https://s3.amazonaws.com/tariffs/20190502/Singapore.csv
    - https://s3.amazonaws.com/tariffs/20190502/Peru.csv
    - https://s3.amazonaws.com/tariffs/20190502/Panama.csv
    - https://s3.amazonaws.com/tariffs/20190502/Oman.csv
    - https://s3.amazonaws.com/tariffs/20190502/Nicaragua.csv
    - https://s3.amazonaws.com/tariffs/20190502/Morocco.csv
    - https://tariffs.s3.amazonaws.com/20190617/Mexico+USMCA.csv
    - https://s3.amazonaws.com/tariffs/20190502/Honduras.csv
    - https://s3.amazonaws.com/tariffs/20190502/Guatemala.csv
    - https://s3.amazonaws.com/tariffs/20190502/El+Salvador.csv
    - https://s3.amazonaws.com/tariffs/20190502/Dominican+Republic.csv
    - https://s3.amazonaws.com/tariffs/20190502/Costa+Rica.csv
    - https://s3.amazonaws.com/tariffs/20190502/Colombia.csv
    - https://s3.amazonaws.com/tariffs/20190430/Korea.csv
    - https://s3.amazonaws.com/tariffs/20190502/Australia.csv
    - https://s3.amazonaws.com/tariffs/20190502/Bahrain.csv
    - https://tariffs.s3.amazonaws.com/20190617/Canada+USMCA.csv
    - https://s3.amazonaws.com/tariffs/20190502/Chile.csv
 - When the application initially starts, it will seed the database with tariff data; this only happens once.
 - A MySql database must exist on the network; populate connection parameters in the following environment variables:
    - TARIFFTOOL_DB_URL: Database Connection URL
    - TARIFFTOOL_DB_USERNAME: Username with permissions to execute DDL and DML sql statements
    - TARIFFTOOL_DB_PASSWORD: Password
 - Azure AD is configured for authentication; users must be part of the EDSP group to alter tariff rates.
 - `java -jar build/libs/terra-freights-0.0.1-SNAPSHOT.jar --spring.profiles.active=production`
 - See `./production-run.sh` for further guidance
 