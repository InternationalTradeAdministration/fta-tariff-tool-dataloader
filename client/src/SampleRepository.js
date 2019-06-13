export class SampleRepository {

    async _getGreece() {
        const response = await fetch('http://api.worldbank.org/countries/gr');
        const body = await response.text()
        return body;
    }

}
