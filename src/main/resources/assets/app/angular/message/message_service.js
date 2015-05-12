'use strict';

angular.module('chatApp.message')
    .factory('Message', function($resource) {
        return $resource('/api/message/chatRoom/:id',{id: '@id'},{
            'save': {url: '/api/message/chatRoom/:id', params:{id:'@id'}, method: 'POST',  headers:{'Accept': 'application/json'}},
            'fromLastSeen': {url: '/api/message/chatRoom/:id/fromLastSeen', params:{id:'@id'}, method: 'GET', isArray:true, headers:{'Accept': 'application/json'}}
        });
    })/*.service('ChatRoomService', function (UserRepository) {
        var getRooms = function () {
            return UserRepository.list();
        };
        return { getRooms: getRooms };
    })*/;