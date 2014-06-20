'use strict';

angular.module('roomsWebApp')
  .factory('Lightstate', ['$resource', function ($resource) {
        var lightstate_query = $resource(
            "http://localhost:8084/rest/get/every/light")

        return lightstate_query
  }]);

