import { SampleRepository } from './../SampleRepository.js'
import { SampleFakeRepository } from './../SampleFakeRepository.js'

describe('world bank countries repository', () => {

    const repositories = [
        { type: 'real', class: new SampleRepository() },
        { type: 'fake', class: new SampleFakeRepository() }
    ]

    repositories.map(repository => {
        it(repository.type + ' repository should return data for greece', async () => {
            const greece = await repository.class._getGreece();
            expect(greece).to.contain('Athens');
        })
    })

})
