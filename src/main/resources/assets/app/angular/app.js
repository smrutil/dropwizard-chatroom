'use strict';

/** app level module which depends on services and controllers */
angular.module('chatApp', [
    'ngRoute',
    'ngResource',
    'chatApp.chatroom',
    'chatApp.user'
]).config(['$routeProvider', function($routeProvider) {
    $routeProvider.otherwise({redirectTo: '/chatroom'});
}]);