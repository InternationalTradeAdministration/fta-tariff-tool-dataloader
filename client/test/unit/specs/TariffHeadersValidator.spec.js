import { TariffHeadersValidator } from "./../../../src/components/TariffHeadersValidator";

describe('TariffHeadersValidator', () => {
  it('Duplicate headers are not valid', async () => {
    var headers = ["ok", "ok"];
    const tariffHeadersValidator = new TariffHeadersValidator(headers);
    const results = tariffHeadersValidator._validate();
    expect(results.valid).to.equal(false);
    expect(results.errorMessages.length).to.equal(24);
  })

  it('Null headers are not valid', async () => {
    var headers = ["ok", null];
    const tariffHeadersValidator = new TariffHeadersValidator(headers);
    const results = tariffHeadersValidator._validate();
    expect(results.valid).to.equal(false);
    expect(results.errorMessages.length).to.equal(1);
  })

  it('Missing required headers is not valid', async () => {
    var headers = ["hello", "world", "Y2012"];
    const tariffHeadersValidator = new TariffHeadersValidator(headers);
    const results = tariffHeadersValidator._validate();
    expect(results.valid).to.equal(false);
    expect(results.errorMessages.length).to.equal(22);
  })

  it('Missing headers with a rate fields is not valid', () => {
    var headers = [
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
    ];
    const tariffHeadersValidator = new TariffHeadersValidator(headers);
    const results = tariffHeadersValidator._validate();
    expect(results.valid).to.equal(false);
    expect(results.errorMessages.length).to.equal(1);
  })

  it('Valid headers contain all required fields', async () => {
    var headers = [
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
      'Link_Text',
      'Y2012'
    ];
    const tariffHeadersValidator = new TariffHeadersValidator(headers);
    const results = tariffHeadersValidator._validate();
    expect(results.valid).to.equal(true);
    expect(results.errorMessages.length).to.equal(0);
  })
})
