const axios = require('axios')

export default class TariffRepository {
  async _getCountries () {
    let countriesResponse = await axios.get('/api/countries')
    return countriesResponse.data
  }

  async _saveTariffs (countryCode, csv) {
    const saveResponse = await axios.put('/api/tariffs/save', { csv }, {
      params: {
        countryCode
      }
    })
    return saveResponse.data
  }

  async _saveCountries (countries) {
    await axios.post('/api/countries/save', countries)
  }
}
