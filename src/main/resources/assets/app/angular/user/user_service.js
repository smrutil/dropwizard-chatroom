'use strict';

angular.module('chatApp.user')
    .factory('User', function($resource) {
        return $resource('/api/user/:id',{id:'@id'},{
            "update": {method: "PUT"},
            'login': {url: '/api/user/login', method: 'POST', isArray:false, headers: {'Content-Type': 'application/x-www-form-urlencoded'}},
            'current': {url: '/api/user/current', method: 'GET', headers:{'Accept': 'application/json'}}
        });
    })/*.service('ChatRoomService', function (UserRepository) {
        var getRooms = function () {
            return UserRepository.list();
        };
        return { getRooms: getRooms };
    })*/;