'use strict';

angular.module('roomsWebApp')
    .service('Lightcontrol', ['$resource', function Lightcontrol($resource) {

        var light_control = $resource(
            "http://localhost:8084/rest/:command/light/:light_name",
            {},
            {
                set_brightness: {
                    method: "POST",
                    isArray: true,
                    params: {
                        command: "set",
                        light_name: "@light_name",
                        brightness: "@brightness"
                    }
                },
                set_warmth: {
                    method: "POST",
                    isArray: true,
                    params: {
                        command: "set",
                        light_name: "@light_name",
                        warmth: "@warmth"
                    }
                },
                turn_on: {
                    method: "POST",
                    isArray: true,
                    params: {
                        command: "turn_on",
                        light_name: "@light_name"
                    }
                },
                turn_off: {
                    method: "POST",
                    isArray: true,
                    params: {
                        command: "turn_off",
                        light_name: "@light_name"
                    }
                }
            });

        return light_control
    }]);

