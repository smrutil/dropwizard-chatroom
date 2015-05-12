'use strict';

/* https://github.com/angular/protractor/blob/master/docs/toc.md */

describe('my app', function() {

  browser.get('index.html');

  it('should automatically redirect to /chat when location hash/fragment is empty', function() {
    expect(browser.getLocationAbsUrl()).toMatch("/chat");
  });


  describe('chat', function() {

    beforeEach(function() {
      browser.get('index.html#/chat');
    });


    it('should render chat when user navigates to /chat', function() {
      expect(element.all(by.css('[ng-view] p')).first().getText()).
        toMatch(/partial for view 1/);
    });

  });

});
