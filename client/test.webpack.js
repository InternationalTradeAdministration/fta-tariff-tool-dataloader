import "@babel/polyfill";
var context = require.context('./src/integration_tests', true, /integration.js$/);
context.keys().forEach(context);
