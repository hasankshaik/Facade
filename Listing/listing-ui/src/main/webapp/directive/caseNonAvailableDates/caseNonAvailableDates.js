/**
 * @ngdoc directive
 * @name ccsApp.directive:caseNonAvailableDates
 * @function
 *
 * @description This directive (caseNonAvailableDates) does something. <-- Edit
 *              this.
 *
 * @example <example module="ccsApp"> <file name="index.html"> <caseNote></caseNote>
 *          </file> </example>
 */
angular.module('ccsApp').directive('caseNonAvailableDates', function () {
    return {
        restrict: 'AE',
        replace: true,
        scope: {
            case: '='
        },
        templateUrl: 'directive/caseNonAvailableDates/caseNonAvailableDates.html',
        controller: 'NonAvailableDatesController',
        link: function () {

        }
    };
}).controller('NonAvailableDatesController', function ($scope, $modal, $timeout, CaseService, SharedService) {

    $scope.addNonAvailableDate = function() {
        $scope.notAvailabilityReason ='';
        $scope.notAvailabilityStartDate ='';
        $scope.notAvailabilityEndDate ='';
        $scope.showAddNonAvailableDate = true;
    };

    $scope.editNonAvailabileDate = function (nonAvailableDate, crestCaseNumber) {
    	 var params ={
                 'reason'        : nonAvailableDate.reason,
                 'startDate'     : nonAvailableDate.startDateStr,
                 'endDate'       : nonAvailableDate.endDateStr,
                 'crestCaseNumber'   : crestCaseNumber,
                 'id'  : nonAvailableDate.id

           };

        CaseService.editNonAvailabileDate(params).then(function () {
            $scope.case.getCaseDetail(crestCaseNumber);
            $scope.showAddNonAvailableDate = false;
            $scope.updateNonAvailableDateMessage = 'Non availability date has been Updated...';
        }, function (error) {
            $scope.updateNonAvailableDateMessage = error.message;
        }).finally(function () {
            $timeout(function () {
                $scope.updateNonAvailableDateMessage = null;
            }, SharedService.timerDelay);
        });
    };

    $scope.deleteNonAvailabileDate = function(nonAvailabileId,crestCaseNumber) {
        var msg = "Do you want to delete this non available date ?";

        $modal.open({
            templateUrl:    'partial/alertDialog/alertDialog.html',
            controller:     'RemoveNonAvailableDateCtrl',
            resolve: {
                bodyMessage     : function() { return msg; },
                headerMessage   : function() { return "Non available date deleted"; },
                nonAvailabileDateId : function() { return nonAvailabileId; },
                crestCaseNumber : function() { return crestCaseNumber; }
            }
        }).result.then(function () {

        }, function (message) {
            if (message && message.indexOf("deleted") < 0) {
                $scope.defendantUpdatedMessage = message;
                $scope.case.getCaseDetail(crestCaseNumber);
            }
            $timeout(function () {
                $scope.defendantUpdatedMessage = null;
            }, SharedService.timerDelay);
        });
    };

    $scope.saveNonAvailableDate = function (crestCaseNumber) {
        var params ={
                'reason'        : $scope.notAvailabilityReason,
                'startDate'     : $scope.notAvailabilityStartDate,
                'endDate'       : $scope.notAvailabilityEndDate,
                'crestCaseNumber'   : crestCaseNumber
          };

        CaseService.saveNonAvailableDate(params).then(function () {
            $scope.case.getCaseDetail(crestCaseNumber);
            $scope.showAddNonAvailableDate = false;
            $scope.nonAvailableDateSaveMessage = 'Non available date has been added...';
        }, function (error) {
            $scope.nonAvailableDateSaveMessage = error.message;
        }).finally(function () {
            $timeout(function () {
                $scope.nonAvailableDateSaveMessage = null;
            }, SharedService.timerDelay);
        });
    };

});
