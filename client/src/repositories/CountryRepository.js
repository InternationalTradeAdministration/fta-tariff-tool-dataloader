const axios = require('axios')

export default class CountryRepository {
  async _getCountries () {
    let countriesResponse = await axios.get('/api/countries')
    return countriesResponse.data
  }
}
