import { SampleEs6Class } from '../SampleEs6Class.js'

describe('assertions', () => {

    it('asserts with chai on a sinon spy', () => {
        const coolSpy = sinon.spy();
        cool(coolSpy);
        expect(coolSpy.called).to.equal(true);
    })

    it('asserts on an es6 class', () => {
        const sampleEs6Class = new SampleEs6Class('hello', 'world');
        expect(sampleEs6Class.message).to.equal('hello world');
    })
})

function cool(somethingCool) {
    somethingCool();
}
