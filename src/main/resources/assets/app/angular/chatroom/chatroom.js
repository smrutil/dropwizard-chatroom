'use strict';

angular.module('chatApp.chatroom', ['ngRoute', 'chatApp.common', 'chatApp.user', 'chatApp.message'])
    .config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/chatroom', {
            templateUrl: 'chatroom/chatroom.html',
            controller: 'ChatRoomCtrl'
        });
    }]);
