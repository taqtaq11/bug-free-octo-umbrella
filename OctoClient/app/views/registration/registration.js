/**
 * Created by Денис on 29.05.2016.
 */
angular.module('octoApp.registration', ['ngRoute', 'ngCookies', 'octoApp.login']).
factory('RegistrationService', function ($http, $rootScope, Session) {
    var registrationService = {};

    registrationService.register = function (credentials) {
        return $http({
            method: 'POST',
            url: $rootScope.REST_API_PATHS.REGISTRATION,
            data: $.param(credentials),
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
            .then(function (res) {
                return res.data.user;
            });
    };

    return registrationService;
}).
controller('registrationCtrl', function($scope, $rootScope, $location, AUTH_EVENTS, AuthService, RegistrationService) {
    $scope.credentials = {};

    $scope.submit = function(credentials) {
        if ($scope.registrationForm.$valid) {
            RegistrationService.register(credentials).then(function(res) {
                var loginCredentials = {
                    login: credentials.login,
                    password: credentials.password
                };

                AuthService.login(loginCredentials).then(function (user) {
                    $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                    $location.path($rootScope.ROUTES.MAIN);
                }, function () {
                    $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
                });
            });
        }
    };
});