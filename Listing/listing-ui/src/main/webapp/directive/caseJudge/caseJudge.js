/**
 * @ngdoc directive
 * @name ccsApp.directive:caseJudge
 * @function
 *
 * @description
 * This directive (caseNote) does something. <-- Edit this.
 *
 * @example
  <example module="ccsApp">
    <file name="index.html">
      <caseJudge></caseJudge>
    </file>
  </example>
 */
angular.module('ccsApp').directive('caseJudge', function () {
    return {
        restrict: 'AE',
        replace: true,
        scope: {
            case: '='
        },
        templateUrl: 'directive/caseJudge/caseJudge.html',
        controller: 'JudgeController',
        link: function () {

        }
    };
}).controller('JudgeController', function ($scope, $modal, $timeout, CaseService, SharedService, JudgeService) {

    $scope.allocateJudge = function() {
        $scope.showAllocateJudge = true;
    };

    $scope.saveJudge = function (crestCaseNumber, caseRelated) {
        var params = {
            'crestCaseNumber'   : crestCaseNumber,
            'courtCenter'       : SharedService.courtCenter,
            'judicialOfficer'   : caseRelated.judgeSelected
        };

        CaseService.allocateJudge(params).then(function () {
            $scope.case.getCaseDetail(crestCaseNumber);
            $scope.showAllocateJudge = false;
            $scope.allocateJudgeMessage = 'Judge has been allocated...';
        }, function (error) {
            $scope.allocateJudgeMessage = error.message;
        }).finally(function () {
            $timeout(function () {
                $scope.allocateJudgeMessage = null;
            }, SharedService.timerDelay);
        });
    };

    $scope.deallocateJudge = function(crestCaseNumber) {
    	var params = {
                'crestCaseNumber'   : crestCaseNumber,
                'courtCenter'       : SharedService.courtCenter
            };

            CaseService.deallocateJudge(params).then(function () {
                $scope.case.getCaseDetail(crestCaseNumber);
                $scope.showAllocateJudge = false;
                $scope.allocateJudgeMessage = 'Judge has been deallocated...';
            }, function (error) {
                $scope.allocateJudgeMessage = error.message;
            }).finally(function () {
                $timeout(function () {
                    $scope.allocateJudgeMessage = null;
                }, SharedService.timerDelay);
            });
    };

    var getJudges = function () {
    	JudgeService.getListOfJudges().then(function (response) {
			$scope.listOfJudges = response;
		});
    };

    $scope.init = function () {
    	getJudges();
    };
});
