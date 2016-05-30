/**
 * Created by Денис on 29.05.2016.
 */
angular.module('octoApp.user', ['ngRoute', 'octoApp.login']).
config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/user', {
        templateUrl: 'views/user/user.html',
        controller: 'userCtrl'
    });
}]).
factory('LoadUserService', function ($http, $rootScope) {
    var loadUserService = {};

    loadUserService.load = function (id) {
        return $http({
            method: 'GET',
            url: $rootScope.REST_API_PATHS.USERS + '/' + id
        })
            .then(function (res) {
                return res.data;
            });
    };

    return loadUserService;
}).
controller('userCtrl', function($scope, $rootScope, $route, LoadUserService) {
    $scope.user = {};

    LoadUserService.load($route.current.params.id).then(function(user) {
        $scope.user = user;
    });
});