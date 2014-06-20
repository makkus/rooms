'use strict';

angular
  .module('roomsWebApp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
        'vr.directives.slider'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
