/**
 * @ngdoc function
 * @name ccsApp.controller:ViewCaseController
 * @description
 * # ViewCaseController
 * Controller of the ccsApp app
 */
(function () {

    'use strict';

    angular.module('ccsApp').controller('CaseViewController', CaseViewController);

    function CaseViewController($scope, $timeout, $modal, SharedService, CaseService, HearingService) {

        var self = this;
        self.getCaseDetail = function (crestCaseNumber) {
            CaseService.getCaseDetail(crestCaseNumber).then(function (response) {
                self.crestData = response;
                self.dateOfSending = SharedService.dateToString(self.crestData.dateOfSending);
                self.dateOfCommittal = SharedService.dateToString(self.crestData.dateOfCommittal);
                self.showCase = true;
            }, function (error) {
                if (error.message !== null) {
                    self.caseError = error.message;
                }
                self.showCase = false;
            }).finally(function () {
                $timeout(function () {
                    self.caseError = null;
                    self.hearingNameMessage = null;
                }, SharedService.timerDelay);
            });
        };

        self.createHearing = function (hearing, crestData) {
            var crestCaseNumber,
                successMessage,
                isValid = true,
                params = {};

            if (typeof hearing === 'string' && crestData) {
                crestCaseNumber = crestData.crestCaseNumber;
                successMessage = 'Hearing has been created';

                params = {
                    "hearingName": hearing,
                    "hearingKey": "",
                    "hearingCase": crestData.crestCaseNumber,
                    "trialEstimate": crestData.trialEstimate,
                    "hearingType": "TRIAL",
                    "bookingStatus": "NOTBOOKED",
                    "active": true
                };

            } else if (typeof hearing === 'object') {
                crestCaseNumber = hearing.hearingCase;
                successMessage = 'Hearing has been updated';

                if (hearing.hearingNote != null && hearing.hearingNote.length > 50) {
                    successMessage = 'Annotation exceeds maximum field length';
                    isValid = false;
                }

                params = {
                    "hearingName": hearing.hearingName,
                    "hearingKey": hearing.hearingKey,
                    "hearingCase": hearing.hearingCase,
                    "trialEstimate": hearing.trialEstimate,
                    "bookingType": hearing.hearingBookingTypeSelected,
                    "hearingNote": hearing.hearingNote,
                    "active": true
                };
            }
            if (isValid) {
                HearingService.editEstimate(params).then(function () {
                    if (typeof hearing === 'string') {
                        self.getCaseDetail(crestCaseNumber);
                    }

                    self.hearingNameMessage = successMessage;
                    self.hearingName = '';
                }, function (error) {
                    self.hearingNameMessage = error.message;
                }).finally(function () {
                    $timeout(function () {
                        self.hearingNameMessage = null;
                    }, SharedService.timerDelay);
                });
            } else {
                self.hearingNameMessage = successMessage;
                $timeout(function () {
                    self.hearingNameMessage = null;
                }, SharedService.timerDelay);
            }
        };

        self.removeHearing = function (hearing) {
            var msg;
            if (hearing.bookingStatus === 'Unscheduled') {
                msg = "Do you want to delete this hearing?";
            } else if (hearing.bookingStatus === 'Scheduled') {
                msg = "This hearing is listed on " + hearing.startDate + ". Do you want to delete this hearing?";
            }
            $modal.open({
                templateUrl: 'partial/alertDialog/alertDialog.html',
                controller: 'AlertdialogCtrl',
                resolve: {
                    bodyMessage: function () { return msg; },
                    headerMessage: function () { return "Delete hearing"; },
                    hearingKey: function () { return hearing.hearingKey; }
                }
            }).result.then(function () {
            }, function (message) {
                if (message && message.indexOf("deleted") > 0) {
                    self.hearingNameMessage = message;
                    self.getCaseDetail(self.crestData.crestCaseNumber);
                } else {
                    self.hearingNameMessage = message;
                }
            });
        };
    }
}());
