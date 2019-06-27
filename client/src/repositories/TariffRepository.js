const axios = require('axios')

export default class TariffRepository {
  async _getTariffs(countryCode, stagingBasketId, tariffLine, page, size) {
    let tariffsResponse = await axios.get('/api/tariffs', {
      params: {
        countryCode,
        stagingBasketId,
        tariffLine,
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

  async _getStagingBaskets(countryCode, productTypeId) {
    let stagingBasketsResponse = await axios.get('/api/staging_baskets', {
      params: {
        countryCode,
        productTypeId
      }
    });
    return stagingBasketsResponse.data;
  }
}
