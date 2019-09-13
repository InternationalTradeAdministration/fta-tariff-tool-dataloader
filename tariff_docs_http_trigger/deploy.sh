#!/usr/bin/env bash

#https://docs.microsoft.com/en-us/azure/azure-functions/functions-create-first-azure-function-azure-cli
npx standard --fix
az functionapp create --name tariff-docs-http-trigger --storage-account itadataservices --resource-group ITA_DataServices --consumption-plan-location eastus --runtime node
func azure functionapp publish tariff-docs-http-trigger
az functionapp config appsettings set \
  --name tariff-docs-http-trigger \
  --resource-group ITA_DataServices \
  --settings TARIFF_DOCS_URL=$TARIFF_DOCS_URL \
  ACCESS_TOKEN_URL=$TARIFF_DOCS_ACCESS_TOKEN_URL \
  CLIENT_ID=$TARIFF_DOCS_CLIENT_ID \
  CLIENT_SECRET=$TARIFF_DOCS_CLIENT_SECRET
