'use strict';

angular.module('roomsWebApp')

    .controller('MainCtrl', ["$scope", "Lightservice", function ($scope, Lightservice) {

        $scope.states = Lightservice.getStates()
        $scope.commands = Lightservice.getCommands()

        $scope.brightness = Lightservice.getBrightness()
        $scope.warmth = Lightservice.getWarmth()

        $scope.$watch('brightness', function (newvar, oldvar) {

            for (name in newvar) {
                var oldval = oldvar[name]
                var newval = newvar[name]

//                console.log("old "+ oldval)
//                console.log("new "+ newval)

                if (typeof oldval == 'undefined') {
                    console.log('ingore undefined')
                    break;
                }
                if (oldval < 0 && newvar[name] <= 0) {
                    console.log('ignore')
                    break;
                }

                if (oldval != newval) {
                    console.log(name + ": " + newval)
                    $scope.commands.change_brightness(name, newval)
                }
            }
        }, true)

        $scope.$watch('warmth', function (newvar, oldvar) {

            for (name in newvar) {
                var oldval = oldvar[name]
                var newval = newvar[name]

                if (typeof oldval == 'undefined') {
                    console.log('ingore undefined')
                    break;
                }
                if (oldval < 0 && newvar[name] <= 0) {
                    console.log('ignore')
                    break;
                }

                if (oldval != newval) {
                    console.log(name + ": " + newval)
                    $scope.commands.change_warmth(name, newval)
                }
            }
        }, true)


    }]);
