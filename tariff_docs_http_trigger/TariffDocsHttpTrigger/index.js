const axios = require('axios')
const tariffDocsUrl = process.env.TARIFF_DOCS_URL
const accessTokenUrl = process.env.ACCESS_TOKEN_URL
const clientId = process.env.CLIENT_ID
const clientSecret = process.env.CLIENT_SECRET

module.exports = async function (context, req) {
  context.log('JavaScript HTTP trigger function processed a request.')

  const accessToken = await getAccessToken()
  const tariffDocsResponse = await getTariffDocs(accessToken)

  if (tariffDocsResponse.data) {
    context.res = {
      body: tariffDocsResponse.data,
      headers: {
        'Content-Type': 'application/json'
      }
    }
  } else {
    context.res = {
      status: 400,
      body: 'An error occured when trying to retireve tariff documents'
    }
  }
}

const getTariffDocs = async (accessToken) => {
  const response = await axios({
    method: 'GET',
    url: tariffDocsUrl,
    ContentType: 'application/json',
    headers: {
      Authorization: 'Bearer ' + accessToken,
      CacheControl: 'no-cache'
    },
    data: {
      query: "$filter=Publication eq 'FTA Publication'"
    }
  }).catch(err => {
    console.log(err.message)
  })
  return response
}

const getAccessToken = async () => {
  const params = {
    grant_type: 'client_credentials',
    client_id: clientId,
    client_secret: clientSecret,
    resource: clientId
  }

  const tokenResponse = await axios(
    {
      method: 'POST',
      url: accessTokenUrl,
      ContentType: 'application/x-www-form-urlencoded',
      data: buildHttpQuery(params)
    }).catch(err => {
    console.log(err.message)
  })

  return tokenResponse.data.access_token
}

const buildHttpQuery = (params) => {
  if (typeof params === 'undefined' || typeof params !== 'object') {
    params = {}
    return params
  }

  var query = ''
  var index = 0

  for (var i in params) {
    index++
    var param = i
    var value = params[i]
    if (index === 1) {
      query += param + '=' + value
    } else {
      query += '&' + param + '=' + value
    }
  }

  return query
}
