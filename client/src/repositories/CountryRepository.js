const axios = require('axios')

export default class CountryRepository {
  async _getTariffs () {
    let countriesResponse = await axios.get('/api/countries')
    return countriesResponse.data
  }
}
