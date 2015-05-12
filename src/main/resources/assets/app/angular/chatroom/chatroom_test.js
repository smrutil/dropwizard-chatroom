'use strict';

describe('myApp.chatroom module', function() {

  beforeEach(module('chatApp.chatroom'));

  describe('chatroom controller', function(){

    it('should ....', inject(function($controller) {
      //spec body
      var chatRoomCtrl = $controller('ChatRoomCtrl');
      expect(chatRoomCtrl).toBeDefined();
    }));

  });
});