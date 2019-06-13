import React from 'react';
import ReactTestUtils from 'react-dom/test-utils';
import App from './../App'

describe('components', () => {

  it('should render', () => { 
    var component = <App />;
    let result = ReactTestUtils.renderIntoDocument(component);
    var renderedMyComponent = ReactTestUtils.findRenderedDOMComponentWithClass(result, 'App');
    expect(renderedMyComponent.innerHTML).to.contain('Learn React'); 
  });

});
 