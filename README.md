# Terra Freights
A tool that helps maintain Tariff Rates

## Local Development
Steps to run this application on you local for development purposes

**Prerequisites** 
 - Java 8
 - NPM (`cd client` then `npm install`)

**Backend** `./gradlew bootRun`, http://localhost:8080

**Frontend** `cd client && npm start`, http://localhost:3000

## Running Tests

**Backend** `./gradlew test`

**Frontend**  `cd client`
 - `npm start` : Unit & Integration tests
 - `npm run contracts` : Contract tests (these require the Backend to be running locally)

## Production Build
```build.sh```
