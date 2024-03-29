# FTA Tariff Tool Dataloader
A tool that helps validate and maintain a record of Tariff Rates for consumption by other users or systems.
The last file that is uploaded and passes validation will be the data referenced when the system is queried.

## Validation
File validation ensures that the first record of an Excel or Csv file has the appropriate headers:

- ```ID``` - value must be numeric
- ```TL```
- ```TL_Desc```
- ```Sector_Code```
- ```Final_Year``` - value must be numeric
- ```TRQ_Quota```
- ```TRQ_Note```
- ```Tariff_Eliminated```
- ```PartnerName```
- ```ReporterName```
- ```PartnerStartYear``` - value must be numeric
- ```ReporterStartYear``` - value must be numeric
- ```QuotaName```
- ```Rule_Text```
- ```HS6```
- ```HS6_Desc```
- ```StagingBasket```
- ```ProductType```
- ```Base_Rate``` value must be numeric
- ```Base_Rate_Alt```
- Rate fields naming conventions may be either:
    - ```Y<year>``` (ex: Y2008) value must be numeric
    - ```Alt_<year>``` (ex: Alt_2008)
    
    or
    - ```Year_<year>``` (ex: Year_2008) value must be numeric
    - ```Yea_<year>_Alt``` (ex: Year_2008_Alt)

Note: 
 - CSV files must have a comma (,) delimiter for columns
 - Rules of Origin links are procured from a SharePoint document library and merged with tariff rares based on the first 2 digits of the HS6 code. Links will be appended as follows:
    - ```link_url_<#>``` (ex: link_url_1, link_url_2)
    - ```link_text_<#>``` (ex: link_text_1, link_text_2)
 
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

## Build
```./build.sh```

## Deploy
```./deploy.sh```

## Production Deployment Notes
 - An Azure Blob Storage account needs to exist
 - An Azure AD App Registration needs to be made to procure an OAuth Client ID and Client Secret
 - The following environment variables need to exist:
    - AZURE_OAUTH_CLIENT_ID: Active Directory Client ID
    - AZURE_OAUTH_CLIENT_SECRET: Active Directory Client Secret
    - AZURE_STORAGE_ACCOUNT: Data Lake Storage Account
    - AZURE_STORAGE_ACCOUNT_KEY: Data Lake Storage Account Key
 - Authentication with Active Directory is enabled; users must be part of the EDSP group to alter tariff rates. 
