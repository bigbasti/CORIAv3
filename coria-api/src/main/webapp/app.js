'use strict';

/*global angular */
/*global alert */

angular.module('coria.services', []);
angular.module('coria.components', ['ngResource']);

var app = angular.module('coria', [
    'ngSanitize'
    , 'ngAria'
    , 'ngRoute'
    , 'ngMessages'
    , 'ui.bootstrap'
    , 'ngResource'
    , 'coria.components'
    , 'smart-table'
    , 'cp.ngConfirm'
    // , 'app.directives'
    // , 'app.filters'
    // , 'app.services'
    // , 'fmTimepicker'
//    , 'ngDialog'
//    , 'ngCookies'
]);

var mod = app
    .config ( ['$routeProvider', '$httpProvider',
        function ($routeProvider, $httpProvider) {
            $routeProvider
                // .when('/', { redirectTo: '/login' })
                .when('/', { template: "<home-index></home-index>" })
                .when('/modules', { template: "<modules-overview></modules-overview>" })
                .when('/datasets', { template: "<datasets-overview></datasets-overview>" })
                .when('/datasets/upload', { template: "<datasets-upload></datasets-upload>" })
                .when('/datasets/export', { template: "<datasets-export></datasets-export>" })
                .when('/datasets/merge', { template: "<datasets-merge></datasets-merge>" })
                .when('/datasets/:datasetid', { template: "<datasets-details></datasets-details>" })
                .otherwise({ template: "<h2>Not yet implemented</h2>"});
        }])
    .config(['$compileProvider',
        function ($compileProvider) {
            $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|tel|file|blob):/);
        }]);

mod.factory('myHttpInterceptor', ['$rootScope', '$q', '$location', '$injector', 'errorDataService', 'globalSettings', '$timeout',
    function ($rootScope,   $q,   $location,   $injector,   errorDataService,   globalSettings,   $timeout) {
        var delayWaitAn;
        return {

        };
    }]);

mod.config(['$httpProvider', function ($httpProvider) {
    //$httpProvider.interceptors.push('myHttpInterceptor');
}]);


mod.config(['$httpProvider', function($httpProvider) {
    //initialize get if not there
    if (!$httpProvider.defaults.headers.get) {
        $httpProvider.defaults.headers.get = {};
    }

    //disable IE ajax request caching
    $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
    // extra
    $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
    $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
}]);