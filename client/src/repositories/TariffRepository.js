const axios = require('axios')

export default class TariffRepository {
  async _getTariffs (countryCode, stagingBasketId, tariffLine, page, size) {
    let tariffsResponse = await axios.get('/api/tariffs', {
      params: {
        countryCode,
        stagingBasketId,
        tariffLine,
        page: page - 1,
        size,
        sort: 'tariffLine,desc'
      }
    })

    return {
      totalPages: tariffsResponse.data.totalPages,
      tariffs: tariffsResponse.data.content
    }
  }

  async _getTariff (tariffId) {
    let tariffResponse = await axios.get('/api/tariff', {
      params: {
        tariffId
      }
    })

    return tariffResponse.data
  }

  async _getCountries () {
    let countriesResponse = await axios.get('/api/countries')
    return countriesResponse.data
  }

  async _getAllStagingBaskets () {
    let stagingBasketsResponse = await axios.get('/api/staging_baskets/all')
    return stagingBasketsResponse.data
  }

  async _getAllProductTypes () {
    let productTypesResponse = await axios.get('/api/product_types/all')
    return productTypesResponse.data
  }

  async _getStagingBaskets (countryCode, productTypeId) {
    let stagingBasketsResponse = await axios.get('/api/tariff/staging_baskets', {
      params: {
        countryCode,
        productTypeId
      }
    })
    return stagingBasketsResponse.data
  }

  async _saveTariffs (countryCode, csv) {
    const saveResponse = await axios.put('/api/tariffs/save', { csv }, {
      params: {
        countryCode
      }
    })
    return saveResponse.data
  }
}
