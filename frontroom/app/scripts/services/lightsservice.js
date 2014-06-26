'use strict';

var base_url = 'http://'+document.location.hostname

angular.module('roomsWebApp')
    .service('Lightservice', ['$rootScope', 'Lightstate', 'Lightcontrol', function ($rootScope, Lightstate, Lightcontrol) {

        var states = new Object()
        var brightness = new Object()
        var warmth = new Object()
        var commands ={switch_on: switch_on,
            change_brightness: change_brightness,
            change_warmth: change_warmth
        }

        var stompClient

        connect()

        Lightstate.query(function (response) {
            angular.forEach(response, function (state) {
                states[state.key] = state
                var br = state.value.brightness
                if (!br) {
                    console.log("Brightness: " + br)
                    brightness[state.key] = -1
                } else {
                    brightness[state.key] = br
                }
                var w = state.value.warmth
                if (!w) {
                    warmth[state.key] = -1
                } else {
                    warmth[state.key] = state.value.warmth
                }
            });

        });

        this.getStates = function () {
            return states;
        }

        this.getBrightness = function() {
            return brightness;
        }

        this.getWarmth = function() {
            return warmth;
        }




        this.getCommands = function() {
            return commands;
        }

        function connect() {
            var socket = new SockJS('http://localhost:8084/rooms');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/light_change', function (greeting) {
                    var light_msg = JSON.parse(greeting.body)
                    lightChanged(JSON.parse(greeting.body));
                });
            });
        }

        function lightChanged(state) {
            $rootScope.$apply(function () {
                var existing_state = states [state.key]
                if (existing_state) {
                    existing_state.value.on = state.value.on
                } else {
                    states[state.key] = state
                }
            })

        }

        function switch_on(lightname, on) {
            if ( on ) {
                Lightcontrol.turn_on({light_name: lightname})
            } else {
                Lightcontrol.turn_off({light_name: lightname})
            }
        }

        function change_brightness(lightname, newVal) {
            Lightcontrol.set_brightness({light_name: lightname, brightness: newVal})
        }

        function change_warmth(lightname, newVal) {
            Lightcontrol.set_warmth({light_name: lightname, warmth: newVal})
        }

    }]);
