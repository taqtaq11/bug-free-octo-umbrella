'use strict';

// Declare app level module which depends on views, and components
angular.module('octoApp', [
    'ngRoute',
    'ngMaterial',
    'octoApp.main',
    'octoApp.login',
    'octoApp.mesh',
    'octoApp.user',
    'octoApp.registration'
]).
constant('ROUTES', function () {
    return {
        MAIN: '/main',
        MESH: '/mesh/:id',
        MESH_CREATE: '/mesh/create',
        USER: '/user/:id',
        REGISTRATION: '/registration',
        LOGIN: '/login'
    }
}()).
constant('REST_API_PATHS', function () {
    var REST_SERVICE_URL = 'http://localhost:8054/OctoService';
    var API_PREFIX = '/api';

    return {
        LOGIN: REST_SERVICE_URL + API_PREFIX + '/auth/signin',
        LOGOUT: REST_SERVICE_URL + API_PREFIX + '/auth/signout',
        REGISTRATION: REST_SERVICE_URL + API_PREFIX + '/users',
        MESHES: REST_SERVICE_URL + API_PREFIX + '/meshes',
        USERS: REST_SERVICE_URL + API_PREFIX + '/users',
        COMMENTS: REST_SERVICE_URL + API_PREFIX + '/comments'
    }
}()).
constant('DEFAULTS', function() {
    var IMAGES_DIRECTORY = '../../img';

    return {
        images: {
            imagesDirectory: IMAGES_DIRECTORY,
            defaultImagePath: IMAGES_DIRECTORY + '/default.png',
            defaultAvatarPath: IMAGES_DIRECTORY + '/no-avatar.png'
        }
    }
}()).
run(['$rootScope', 'ROUTES', 'REST_API_PATHS', 'DEFAULTS', function ($rootScope, ROUTES, REST_API_PATHS, DEFAULTS) {
    $rootScope.ROUTES = ROUTES;
    $rootScope.REST_API_PATHS = REST_API_PATHS;
    $rootScope.DEFAULTS = DEFAULTS;
}]).
config(['$locationProvider', '$routeProvider', 'ROUTES', function ($locationProvider, $routeProvider, ROUTES) {
    var pathToViews = '/views';

    $locationProvider.hashPrefix('!');

    $routeProvider.when(ROUTES.MAIN, {templateUrl: pathToViews + '/main/main.html', controller: 'mainCtrl'});
    $routeProvider.when(ROUTES.MESH, {templateUrl: pathToViews + '/mesh/mesh.html', controller: 'meshCtrl'});
    $routeProvider.when(ROUTES.MESH_CREATE, {templateUrl: pathToViews + '/mesh_create/mesh_create.html', controller: 'meshCreateCtrl'});
    $routeProvider.when(ROUTES.USER, {templateUrl: pathToViews + '/user/user.html', controller: 'userCtrl'});
    $routeProvider.when(ROUTES.REGISTRATION, {templateUrl: pathToViews + '/registration/registration.html', controller: 'registrationCtrl'});
    $routeProvider.when(ROUTES.LOGIN, {templateUrl: pathToViews + '/login/login.html', controller: 'loginCtrl'});
    $routeProvider.otherwise({redirectTo: ROUTES.MAIN});
}]).
controller('headerCtrl', function($scope, $rootScope, $cookies, AuthService) {
    var sessionCookie = $cookies.get($rootScope.SESSION.cookie.name);

    if (sessionCookie == $rootScope.SESSION.status.opened) {
        $rootScope.isAuthorized = true;
    }
    else {
        $rootScope.isAuthorized = false;
    }

    $scope.$on($rootScope.AUTH_EVENTS.loginSuccess, function(newValue, oldValue) {
        $rootScope.isAuthorized = true;
    });

    $scope.logout = function() {
        AuthService.logout().then(function(res) {
            $rootScope.isAuthorized = false;
        });
    }
});