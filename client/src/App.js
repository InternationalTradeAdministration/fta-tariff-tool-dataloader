import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';

class App extends Component {
  
  async componentWillMount() {
    const hello = await fetch('/hello')
    const helloText = await hello.text();
    console.log(helloText);
  }

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <p>
            Edit <code>src/App.js</code> and save to reload.
          </p>
          <a
            className="App-link"
            href="https://reactjs.org"
            target="_blank"
            rel="noopener noreferrer"
          >
            Learn React
          </a>
        </header>
      </div>
    );
  }
}

export default App;
