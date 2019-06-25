const axios = require('axios')

export default class TariffRepository {
  async _getTariffs(countryCode, productTypeId, stagingBasketId, page, size) {
    let tariffsResponse = await axios.get('/api/tariffs', {
      params: {
        countryCode,
        productTypeId,
        stagingBasketId,
        page: page - 1,
        size,
        sort: 'tariffLine,desc'
      }
    });

    return {
      totalPages: tariffsResponse.data.totalPages,
      tariffs: tariffsResponse.data.content
    }
  }

  async _getCountries() {
    let countriesResponse = await axios.get('/api/countries');
    return countriesResponse.data;
  }

  async _getProductTypes(countryCode) {
    let productTypesResponse = await axios.get('/api/product_types', {
      params: {
        countryCode
      }
    });
    return productTypesResponse.data;
  }

  async _getStagingBaskets() {
    let stagingBasketsResponse = await axios.get('/api/staging_baskets');
    return stagingBasketsResponse.data;
  }
}
