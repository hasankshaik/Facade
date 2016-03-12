/**
 * @ngdoc directive
 * @name ccsApp.directive:caseView
 * @function
 *
 * @description
 * This directive (caseView) does something. <-- Edit this.
 *
 * @example
  <example module="ccsApp">
    <file name="index.html">
      <caseView></caseView>
    </file>
  </example>
 */
angular.module('ccsApp').directive('caseView', function () {
    return {
        restrict: 'AE',
        replace: true,
        scope: {
            caseNo: '='
        },
        templateUrl: 'directive/caseView/caseView.html',
        controller: 'ViewCaseController',
        link: function () {

        }
    };
}).controller('ViewCaseController', function ($scope, $modal, $timeout, CaseService, SharedService) {

	var getCaseDetail = function (crestCaseNumber) {
        CaseService.getCaseDetail(crestCaseNumber).then(function (response) {
            $scope.crestData          = response;
            $scope.dateOfSending      = SharedService.dateToString($scope.crestData.dateOfSending);
            $scope.dateOfCommittal    = SharedService.dateToString($scope.crestData.dateOfCommittal);
            $scope.showCase = true;
        }, function (error) {
            if(error.message !== null) {
                $scope.caseError = error.message;
            }
            $scope.showCase = false;
        }).finally(function () {
            $timeout(function () {
                $scope.caseError = null;
                $scope.hearingNameMessage = null;
            }, SharedService.timerDelay);
        });
    };
    
    $scope.$on('caseNumber', function(event, crestCaseNumber) {
        getCaseDetail(crestCaseNumber);
    });

    $scope.closeCase = function () {
        $scope.showCase = false;
    };


    
});
