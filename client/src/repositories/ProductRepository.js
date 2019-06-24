const axios = require('axios')

export default class ProductRepository {
  async _getTypes () {
    let productTypesResponse = await axios.get('/api/product_types')
    return productTypesResponse.data
  }
}
