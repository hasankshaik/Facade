/**
 * @ngdoc directive
 * @name ccsApp.directive:unscheduledHearing
 * @function
 *
 * @description
 * This directive (unscheduledHearing) does something. <-- Edit this.
 *
 * @example
  <example module="ccsApp">
    <file name="index.html">
      <unscheduledHearing></unscheduledHearing> OR
      <div data-unscheduled-hearing></div>
    </file>
  </example>
 */
angular.module('ccsApp')
    .controller('HearingController', function ($scope, $rootScope, HearingService) {

        var planner = $rootScope.$on('planner', function (event, data) {
            $scope.roomList = data;
        });

        $scope.$on('$destroy', planner);

        $scope.onUnScheduledHearingSelection = function () {
            $scope.selectedHearing=angular.copy($scope.selectedUnscheduledHearing);
            $scope.showUnScheduledHearingFlag = true;
            $scope.showEditEstimateFlag = true;
            $scope.editTrialEstimate = $scope.selectedUnscheduledHearing.trialEstimate;
            $scope.showHearingInfoFlag = true;
            $scope.showScheduledHearingFlag = false;
            $scope.clearMessages();
        };

        var getUnlistedHearings =  function() {
            HearingService.getUnlistedHearings().then(function (response) {
                $scope.unlistedHearings = response;
            });
        };

        var onload = function () {
            getUnlistedHearings();
        };

        onload();

    })
    .directive('unscheduledHearing', function () {
    return {
        restrict: 'AE',
        replace: true,
        scope: {}, // This directive has its own isolated scope so two-way data binding would only happen inside this directive
        templateUrl: 'directive/unscheduledHearing/unscheduledHearing.html',
        controller: 'HearingController',
        link: function () {

        }
    };
});
