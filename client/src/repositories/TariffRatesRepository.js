const axios = require("axios");

export default class TarrifRatesRepository {

  async _getTariffs(country, page, size) {
    let tariffsResponse = await axios.get("/api/tariffs", {
      params: {
        country,
        page,
        size,
        sort: "id,desc"
      }
    });

    return {
      totalPages: tariffsResponse.data.totalPages,
      tariffs: tariffsResponse.data.content
    }
  }

}