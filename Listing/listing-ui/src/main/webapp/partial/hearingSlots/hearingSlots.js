(function () {

    'use strict';

    angular.module('ccsApp').controller('HearingslotsCtrl', HearingslotsCtrl);

    function HearingslotsCtrl($scope, $modalInstance, HearingService, courtCenter, hearing, overbook, selected) {

        $scope.selectedSlot = { slot: angular.toJson(selected), open: null, bookingType: null };

        HearingService.getHearingSlots(courtCenter, hearing, overbook).then(function (response) {
            $scope.hearingSlots = response;
        });

        $scope.navigate = function () {
            $scope.selectedSlot.open = 'navi';
            $modalInstance.close($scope.selectedSlot);
        };

        $scope.list = function () {
            $scope.selectedSlot.open = 'list';
            $modalInstance.close($scope.selectedSlot);
        };

        $scope.hitEnter = function (evt) {
            if (angular.equals(evt.keyCode, 13) && !(angular.equals($scope.name, null) || angular.equals($scope.name, ''))) {
                $scope.save();
            }
        };
    }

}());
