# ITA Tariff Tool
A tool that helps maintain Tariff Rates

## Local Development
Steps to run this application on you local for development purposes

**Prerequisites** 
 - Java 8
 - Gradle
 - NPM (`cd client` then `npm install`)

**Backend** 
 - `gradle bootRun `, http://localhost:8080

**Frontend** `cd client && npm start`, http://localhost:8081

## Running Tests

**Backend** `./gradlew test`

**Frontend**  `cd client && npm test`

## Production Build
```./production-build.sh```

## Production Deployment Notes
 - The following environment variables need to exist:
    - TARIFFTOOL_AZURE_OAUTH_CLIENT_ID: Active Directory Client ID
    - TARIFFTOOL_AZURE_OAUTH_CLIENT_SECRET: Active Directory Client Secret
    - TARIFFTOOL_AZURE_STORAGE_ACCOUNT: Data Lake Storage Account
    - TARIFFTOOL_AZURE_STORAGE_ACCOUNT_KEY: Data Lake Storage Account Key
 - Authentication with Active Directory is enabled; users must be part of the EDSP group to alter tariff rates.
 - `java -jar build/libs/terra-freights-0.0.1-SNAPSHOT.jar --spring.profiles.active=production`
 