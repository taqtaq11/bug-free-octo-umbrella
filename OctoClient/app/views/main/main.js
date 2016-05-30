/**
 * Created by Денис on 29.05.2016.
 */
'use strict';

angular.module('octoApp.main', ['ngRoute']).
config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/main', {
        templateUrl: 'views/main/main.html',
        controller: 'mainCtrl'
    });
}]).
factory('LoadMeshesService', function ($http, $rootScope, Session) {
    var loadMeshesService = {};

    loadMeshesService.load = function (query) {
        return $http({
            method: 'GET',
            url: $rootScope.REST_API_PATHS.MESHES,
            data: $.param({'query': query}),
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
            .then(function (res) {
                return res.data;
            });
    };

    return loadMeshesService;
}).
controller('mainCtrl', function($scope, $rootScope, $location, LoadMeshesService) {
    $scope.meshes = [];

    $scope.openMeshView = function(id) {
        $location.path($rootScope.ROUTES.MESH.replace(':id', id));
    };

    LoadMeshesService.load().then(function(meshes) {
        $scope.meshes = meshes;
    });
});