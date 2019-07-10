import { utils } from 'xlsx'

export class TariffWorksheetValidator {
  constructor (sheet) {
    this.sheet = sheet
  }

  _validate () {
    let valid = true
    let headers = []
    let errorMessages = []
    let nullHeaderCount = 0

    var range = utils.decode_range(this.sheet['!ref'])
    for (var colNum = range.s.c; colNum < range.e.c; colNum++) {
      const cell = this.sheet[utils.encode_cell({ r: 0, c: colNum })]
      if (!cell) {
        nullHeaderCount++
        continue
      }
      headers.push(cell.v)
    }

    if (nullHeaderCount > 0) {
      valid = false
      errorMessages.push('There are null headers, please remove them and try again.')
    }

    let headerCounts = {}
    headers.forEach(header => { headerCounts[header] = (headerCounts[header] || 0) + 1 })
    let duplicateHeaders = Object.keys(headerCounts).filter(x => headerCounts[x] > 1)
    if (duplicateHeaders.length > 0) {
      valid = false
      errorMessages.push('There are duplicate headers, please remove them and try again: ' + duplicateHeaders)
    }

    let requiredHeaderExistance = {}
    this.requiredHeaders().forEach(reqHeader => { requiredHeaderExistance[reqHeader] = headers.includes(reqHeader) })
    let missingHeaders = Object.keys(requiredHeaderExistance).filter(x => requiredHeaderExistance[x] === false)
    if (missingHeaders.length > 0) {
      valid = false
      missingHeaders.forEach(header => {
        errorMessages.push('The following header is missing, please add it and try again: ' + header)
      })
    }

    let rateFields = headers.filter(header => { if (header.match('^Y') || header.match('^Alt_')) { return header } })
    if (rateFields.length === 0) {
      valid = false
      missingHeaders.push('No headers meet the minimum requirements to be a rate field. (ex. Alt_2002, Y2004, Year_1, Year_1_Alt)')
    }

    return { valid, errorMessages }
  }

  requiredHeaders () {
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
      'StagingBasketId',
      'StagingBasket',
      'Product_Type',
      'ProductType',
      'Base_Rate_Alt',
      'Base_Rate',
      'Link_Url',
      'Link_Text'
    ]
  }
}
