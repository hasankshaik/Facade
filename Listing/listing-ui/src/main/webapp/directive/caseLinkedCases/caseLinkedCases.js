/**
 * @ngdoc directive
 * @name ccsApp.directive:caseLinkedCases
 * @function
 *
 * @description
 * This directive (caseLinkedCases) does something. <-- Edit this.
 *
 * @example
  <example module="ccsApp">
    <file name="index.html">
      <caseLinkedCases></caseLinkedCases>
    </file>
  </example>
 */
angular.module('ccsApp').directive('caseLinkedCases', function () {
    return {
        restrict: 'AE',
        replace: true,
        scope: {
            case: '='
        },
        templateUrl: 'directive/caseLinkedCases/caseLinkedCases.html',
        controller: 'LinkedCaseController',
        link: function () {

        }
    };
}).controller('LinkedCaseController', function ($scope, $modal, $timeout, CaseService, SharedService) {

    $scope.addLinkedCase = function() {
        $scope.linkedCaseNumber ='';
        $scope.showAddLinkedCase = true;
    };

    $scope.saveLinkedCase = function (crestCaseNumber) {
        var params = {
            'crestCaseNumber'   : crestCaseNumber,
            'courtCenter'       : SharedService.courtCenter,
            'linkedCases'       : [{
                'crestCaseNumber'   : $scope.linkedCaseNumber
            }]
        };

        CaseService.saveLinkedCase(params).then(function () {
            $scope.case.getCaseDetail(crestCaseNumber);
            $scope.showAddLinkedCase = false;
            $scope.linkedCaseSaveMessage = 'Linked case has been added...';
        }, function (error) {
            $scope.linkedCaseSaveMessage = error.message;
        }).finally(function () {
            $timeout(function () {
                $scope.linkedCaseSaveMessage = null;
            }, SharedService.timerDelay);
        });
    };

    $scope.removeLinkedCase = function(linkedCase, crestCaseNumber) {

        var msg = "Do you want to unlink this case ?";

        $modal.open({
            templateUrl:    'partial/alertDialog/alertDialog.html',
            controller:     'RemoveLinkedCaseCtrl',
            resolve: {
                bodyMessage         : function() { return msg; },
                headerMessage       : function() { return "Delete linked case"; },
                crestLinkedNumber   : function() { return linkedCase.crestCaseNumber; },
                crestCaseNumber     : function() { return crestCaseNumber; },
                courtCenter         : function() { return SharedService.courtCenter; }
            }
        }).result.then(function () {

         }, function (message) {
             if (message && message.indexOf("unlinked") > 0) {
                $scope.linkedCaseDeleteMessage = message;
                $scope.case.getCaseDetail(crestCaseNumber);
             }
             $timeout(function () {
                $scope.linkedCaseDeleteMessage = null;
            }, SharedService.timerDelay);
        });
    };

});
