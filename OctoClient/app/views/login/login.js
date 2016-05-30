/**
 * Created by Денис on 29.05.2016.
 */
angular.module('octoApp.login', ['ngRoute', 'ngCookies', 'octoApp.user']).
constant('AUTH_EVENTS', {
    loginSuccess: 'auth-login-success',
    loginFailed: 'auth-login-failed',
    logoutSuccess: 'auth-logout-success',
    sessionTimeout: 'auth-session-timeout',
    notAuthenticated: 'auth-not-authenticated',
    notAuthorized: 'auth-not-authorized'
}).
constant('SESSION', {
    status: {
        opened: true,
        closed: false
    },
    cookie: {
        name: 'session',
        lifetime: 10 * 60000
    }
}).
run(['$rootScope', 'AUTH_EVENTS', 'SESSION', function ($rootScope, AUTH_EVENTS, SESSION) {
    $rootScope.AUTH_EVENTS = AUTH_EVENTS;
    $rootScope.SESSION = SESSION;
}]).
service('Session', function ($rootScope, $cookies, LoadUserService) {
    this.create = function (userId, token) {
        this.userId = userId;
        this.token = token;
        this.userInfo = null;

        var now = new Date();
        $cookies.put(
            $rootScope.SESSION.cookie.name,
            $rootScope.SESSION.status.opened,
            {
                expires: new Date(now.getTime() + $rootScope.SESSION.cookie.lifetime)
            }
        );
    };

    this.destroy = function () {
        this.userId = null;
        this.token = null;
        this.userInfo = null;
    };

    this.getUserInfo = function(callback) {
        var self = this;

        if (!self.userId) {
            callback(null);
        }

        if (self.userInfo) {
            callback(self.userInfo);
        }
        else {
            return LoadUserService.load(self.userId).then(function (user) {
                self.userInfo = user;
                callback(user);
            });
        }
    }
}).
factory('AuthService', function ($http, $rootScope, Session) {
    var authService = {};

    authService.login = function (credentials) {
        return $http({
            method: 'POST',
            url: $rootScope.REST_API_PATHS.LOGIN,
            data: $.param(credentials),
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
            .then(function (res) {
                Session.create(res.data.userId, res.data.token);
                return res.data.user;
            });
    };

    authService.logout = function() {
        return $http({
            method: 'POST',
            url: $rootScope.REST_API_PATHS.LOGOUT,
            data: $.param({
                token: Session.token
            }),
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
            .then(function (res) {
                Session.destroy();
            });
    };

    authService.isAuthenticated = function () {
        return !!Session.userId;
    };

    authService.isAuthorized = function () {
        return authService.isAuthenticated();
    };

    return authService;
}).
controller('loginCtrl', function($scope, $rootScope, $location, AUTH_EVENTS, AuthService) {
    $scope.credentials = {};

    $scope.submit = function(credentials) {
        if ($scope.loginForm.$valid) {
            AuthService.login(credentials).then(function (user) {
                $rootScope.$broadcast(AUTH_EVENTS.loginSuccess);
                $location.path($rootScope.ROUTES.MAIN);
            }, function () {
                $rootScope.$broadcast(AUTH_EVENTS.loginFailed);
            });
        }
    };
});