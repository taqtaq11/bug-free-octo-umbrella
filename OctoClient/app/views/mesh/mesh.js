/**
 * Created by Денис on 29.05.2016.
 */
angular.module('octoApp.mesh', ['ngRoute', 'octoApp.user', 'ngOnErrorImg']).
config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/mesh', {
        templateUrl: 'views/mesh/mesh.html',
        controller: 'meshCtrl'
    });
}]).
factory('LoadMeshService', function ($http, $rootScope) {
    var loadMeshService = {};

    loadMeshService.load = function (id) {
        return $http({
            method: 'GET',
            url: $rootScope.REST_API_PATHS.MESHES + '/' + id
        })
            .then(function (res) {
                return res.data;
            });
    };

    return loadMeshService;
}).
factory('LoadMeshCommentsService', function ($http, $rootScope, $q) {
    var loadMeshCommentsService = {};

    loadMeshCommentsService.load = function (ids) {
        var loaders = [];

        angular.forEach(ids, function(value, key) {
            this.push(
                $http({
                    method: 'GET',
                    url: $rootScope.REST_API_PATHS.COMMENTS + '/' + value
                })
            );
        }, loaders);

        //TODO: загрузка автора комментария
        return $q.all(loaders).then(function (ret) {
            var unpackedRet = [];

            angular.forEach(ret, function(value, key) {
                unpackedRet.push(value.data);
            }, unpackedRet);

            unpackedRet.sort(function(a, b) {
                return a.timestamp > b.timestamp;
            });
            return unpackedRet;
        });
    };

    return loadMeshCommentsService;
}).
factory('AddCommentToMeshService', function ($http, $rootScope, Session) {
    var addCommentToMeshService = {};

    addCommentToMeshService.add = function (id, message) {
        return $http({
            method: 'POST',
            url: $rootScope.REST_API_PATHS.MESHES + '/' + id + '/comments',
            data: $.param({
                token: Session.token,
                message: message
            }),
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
            .then(function (res) {
                return res.data;
            });
    };

    return addCommentToMeshService;
}).
controller('meshCtrl',
    function($scope, $rootScope, $route,
             LoadMeshService, LoadUserService,
             LoadMeshCommentsService, AddCommentToMeshService,
             Session) {
        $scope.mesh = {};
        $scope.author = {};
        $scope.comments = [];
        $scope.newComment;
        $scope.currentUser = null;

        Session.getUserInfo(function(userInfo) {
            $scope.currentUser = userInfo;
        });

        LoadMeshService.load($route.current.params.id).then(function(mesh) {
            $scope.mesh = mesh;
            LoadUserService.load($scope.mesh.author).then(function (user) {
                $scope.user = user;
            });

            LoadMeshCommentsService.load(mesh.comments).then(function (comments) {
                $scope.comments = comments;
            });

        $scope.addCommentToMesh = function(message) {
            AddCommentToMeshService.add($route.current.params.id, message).then(function(res) {
                $scope.comments.push({
                    message: message,
                    user: $scope.currentUser
                });
            });
        }
    });
});