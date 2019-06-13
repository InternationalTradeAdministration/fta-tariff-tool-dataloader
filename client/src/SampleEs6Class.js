export class SampleEs6Class {

    constructor(hello, world) {
        this.hello = hello;
        this.world = world;
    }

    get message() {
        return this.hello + ' ' + this.world;
    }

}
