/**
 * @ngdoc directive
 * @name ccsApp.directive:caseHearing
 * @function
 *
 * @description
 * This directive (caseHearing) does something. <-- Edit this.
 *
 * @example
  <example module="ccsApp">
    <file name="index.html">
      <caseHearing></caseHearing>
    </file>
  </example>
 */
(function() {
    'use strict';
    angular
        .module('ccsApp')
        .directive('caseHearing', function () {
            return {
                restrict: 'AE',
                replace: true,
                scope: { case: '=' },
                controller:  'ListHearingController',
                templateUrl: 'directive/caseHearing/caseHearing.html',
                link: function () {
                }
            };
        })
        .controller('ListHearingController', function ($scope, $modal, $timeout, HearingService, CaseService, SharedService) {
            $scope.addHearing = function() {
                $scope.hearingName      = '';
                $scope.showAddHearing   = true;       
            };
            $scope.createHearing =  function (hearingName, caseObj) {
                var params = {
                    "hearingName":      hearingName, 
                    "hearingKey":       "", 
                    "hearingCase":      caseObj.crestCaseNumber, 
                    "trialEstimate":    caseObj.trialEstimate, 
                    "hearingType":      "TRIAL", 
                    "bookingStatus":    "NOTBOOKED", 
                    "active":           true
                };

                HearingService.editEstimate(params)
                .then(function (response) { return CaseService.getCaseDetail(caseObj.crestCaseNumber) })
                .then(function (response) {   
                    $scope.case.hearings = response.hearings;           
                    $scope.showAddHearing = false; 
                    $scope.hearingSaveMessage = 'Hearing has been created'; })
                .catch(function (error) {
                    $scope.hearingSaveMessage = error.message; })
                .finally(function () {
                    $timeout(function () {
                        $scope.hearingSaveMessage = null;
                    }, SharedService.timerDelay);
                });
            };

            $scope.editHearing =  function (hearing, crestCaseNumber) {
                var isValid = true;
                if(hearing.hearingNote !== null &&  hearing.hearingNote.length > 50) {
                     $scope.hearingUpdatedMessage = 'Please enter a valid annotation';
                     isValid = false;
                } else if(hearing.trialEstimate === null || hearing.trialEstimate === "") {
                     $scope.hearingUpdatedMessage = 'You can not remove the hearing estimation';
                     isValid = false;
                }
                if(isValid) {
                    var params = {
                        "hearingName":      hearing.hearingName, 
                        "hearingKey":       hearing.hearingKey, 
                        "hearingCase":      hearing.hearingCase, 
                        "trialEstimate":    hearing.trialEstimate, 
                        "bookingType":      hearing.hearingBookingTypeSelected, 
                        "hearingNote":      hearing.hearingNote, 
                        "active":           true
                    };
                    HearingService.editEstimate(params)
                    .then(function (response) { return CaseService.getCaseDetail(crestCaseNumber) })
                    .then(function (response) {   
                        $scope.case.hearings = response.hearings;           
                        $scope.showAddHearing = false; 
                        $scope.hearingUpdatedMessage = 'Hearing has been Updated...'; })
                    .catch(function (error) {
                        $scope.hearingUpdatedMessage = error.message; })
                    .finally(function () {
                        $timeout(function () {
                            $scope.hearingUpdatedMessage = null;
                        }, SharedService.timerDelay);
                    });
                } 
            };

            $scope.removeHearing = function(hearing, crestCaseNumber) {
                var msg;
                if (hearing.bookingStatus === 'Unscheduled') {
                     msg = "Do you want to delete this hearing?";
                } else if (hearing.bookingStatus === 'Scheduled') {
                     msg = "This hearing is listed on " + hearing.startDate +". Do you want to delete this hearing?";
                }
                
                $modal.open({
                    templateUrl:    'partial/alertDialog/alertDialog.html',
                    controller:     'AlertdialogCtrl', 
                    resolve: {
                        bodyMessage     : function() { return msg; },
                        headerMessage   : function() { return "Delete hearing"; }, 
                        hearingKey      : function() { return hearing.hearingKey; }
                    }
                }).result.then(function () {

                }, function (message) {
                    if (message && message.indexOf("deleted") > 0) {
                        $scope.hearingUpdatedMessage = message;
                        CaseService.getCaseDetail(crestCaseNumber).then(function (response) {
                            $scope.case.hearings = response.hearings;
                        });
                    }              
                    $timeout(function () {
                        $scope.hearingUpdatedMessage = null;
                    }, SharedService.timerDelay);
                });
            };
        });
})();