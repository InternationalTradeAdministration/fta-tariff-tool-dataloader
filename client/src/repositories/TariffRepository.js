const axios = require('axios')

export default class TariffRepository {
  async _getTariffs (countryCode, page, size) {
    let tariffsResponse = await axios.get('/api/tariffs', {
      params: {
        countryCode,
        page,
        size,
        sort: 'id,desc'
      }
    })

    return {
      totalPages: tariffsResponse.data.totalPages,
      tariffs: tariffsResponse.data.content
    }
  }
}
