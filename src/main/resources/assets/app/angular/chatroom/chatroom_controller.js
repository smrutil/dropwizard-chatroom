'use strict';

angular.module('chatApp.chatroom')
    .controller('ChatRoomCtrl', function ($scope, $http, ChatRoom, User, Message) {

        $scope.currentRoom = {"name": "default"};
        $scope.messages = [];
        $scope.inputText = "";
        $scope.user = User.current();

        $scope.login = function () {
            /*var user = {};
            user.username= $scope.user.name;"
            user.password = "secret";
            console.log("user >> "+user)*/
            $scope.user = User.login("username="+$scope.user.name+"&password=secret");
            $scope.user = User.current();
        };

        $scope.changeNickName = function () {
            User.update({}, $scope.user).$promise.then(function() {
                $scope.user = User.current();
            });
        };

        $scope.setCurrentRoom = function (room) {
            $scope.currentRoom = room;
            $scope.fetchAllMsg();
        };

        $scope.addRoom = function () {
            var currentRoom = {"name": $scope.currentRoom.name};
            ChatRoom.save({}, currentRoom).$promise.then(function() {
                $scope.currentRoom = currentRoom;
                $scope.refreshRoom();
            });
        };

        $scope.updateRoom = function () {
            ChatRoom.update({}, $scope.currentRoom).$promise.then(function() {
                $scope.refreshRoom();
            });
        };

        $scope.deleteRoom = function () {
            ChatRoom.delete({}, $scope.currentRoom).$promise.then(function() {
                $scope.refreshRoom();
            });
        };

        $scope.refreshRoom = function () {
            var currentRoom = $scope.currentRoom;
            ChatRoom.query().$promise.then(function(rooms){
                $scope.rooms = rooms;
                $scope.currentRoom  = $scope.rooms.filter(function(room){
                    return currentRoom.name == room.name;
                })[0];
                /*if(rooms.indexOf(currentRoom) == -1){
                    currentRoom = $scope.rooms[0];
                }*/
                //$scope.currentRoom = currentRoom;
                $scope.fetchAllMsg();
            });
        };

        $scope.refreshMsg = function () {
            $scope.fetchMsgFromLastSeen();
        };

        $scope.submitMsg = function () {
            var message = {"id": $scope.currentRoom.id, "content": $scope.inputText}
            //var message = new Message({"content": $scope.inputText});
            Message.save(message).$promise.then(function() {
                $scope.fetchMsgFromLastSeen();
            });
            $scope.inputText = "";
        };

        $scope.fetchAllMsg = function () {
            $scope.messages = [];
            var params = {"id": $scope.currentRoom.id};
            Message.query({}, params).$promise.then(function(messages) {
                $scope.addMsg(messages);
            });
        };

        $scope.fetchMsgFromLastSeen = function () {
            var params = {"id": $scope.currentRoom.id};
             Message.fromLastSeen({}, params).$promise.then(function(messages){
                $scope.addMsg(messages);
            });
        };

        $scope.addMsg = function (messages) {
            if (messages instanceof Array) {
                angular.forEach(messages, function(message) {
                    $scope.messages.push(message);
                });
            }else {
                $scope.messages.push(messages);
            }
        };

        $scope.refreshRoom();
    });
