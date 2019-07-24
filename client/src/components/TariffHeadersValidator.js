
export class TariffHeadersValidator {
  constructor(headers) {
    this.headers = headers
  }

  _validate() {
    let valid = true
    let errorMessages = []

    if (this.headers.indexOf(null) != -1) {
      valid = false
      errorMessages.push('There are null headers, please remove them and try again.')
      return { valid, errorMessages }
    }

    let headerCounts = {}
    this.headers.forEach(header => { headerCounts[header] = (headerCounts[header] || 0) + 1 })
    let duplicateHeaders = Object.keys(headerCounts).filter(x => headerCounts[x] > 1)
    if (duplicateHeaders.length > 0) {
      valid = false
      errorMessages.push('There are duplicate headers, please remove them and try again: ' + duplicateHeaders)
    }

    let requiredHeaderExistance = {}
    this.requiredHeaders().forEach(reqHeader => { requiredHeaderExistance[reqHeader] = this.headers.indexOf
      (reqHeader) })
    let missingHeaders = Object.keys(requiredHeaderExistance).filter(x => requiredHeaderExistance[x] === -1)
    if (missingHeaders.length > 0) {
      valid = false
      missingHeaders.forEach(header => {
        errorMessages.push('The following header is missing, please add it and try again: ' + header)
      })
    }

    let rateFields = this.headers.filter(header => { if (header.match('^Y') || header.match('^Alt_')) { return header } })
    if (rateFields.length === 0) {
      valid = false
      errorMessages.push('No headers meet the minimum requirements to be a rate field. (ex. Alt_2002, Y2004, Year_1, Year_1_Alt)')
    }

    return { valid, errorMessages }
  }

  requiredHeaders() {
    return [
      'ID',
      'TL',
      'TL_Desc',
      'Sector_Code',
      'Final_Year',
      'TRQ_Quota',
      'TRQ_Note',
      'Tariff_Eliminated',
      'PartnerName',
      'ReporterName',
      'PartnerStartYear',
      'ReporterStartYear',
      'QuotaName',
      'Rule_Text',
      'HS6',
      'HS6_Desc',
      'StagingBasket',
      'ProductType',
      'Base_Rate_Alt',
      'Base_Rate',
      'Link_Url',
      'Link_Text'
    ]
  }
}
