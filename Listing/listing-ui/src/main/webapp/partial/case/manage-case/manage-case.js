/**
 * @ngdoc function
 * @name ccsApp.controller:CaseCreateController
 * @description
 * # CaseCreateController
 * Controller of the ccsApp app
 */

(function () {

    'use strict';

    angular
        .module('ccsApp')
        .controller('ManageCaseController', ManageCaseController);

    function ManageCaseController($scope, $timeout, $modal, CourtService, CaseService, SharedService, JudgeService) {

        var self = this;

        self.selectedCourt = SharedService.courtCenter;

        self.getCourtCentres = function () {
            CourtService.getCourtCentres().then(function (courts) {
                self.courtLists = courts;
            }, function (error) {
                self.tempError = error;
            });
        };

        self.getReleaseDecision = function () {
            CaseService.getReleaseDecision().then(function (response) {
                self.releaseDecisionList = response.data;
            }, function (error) {
                self.tempError = error;
            });
        };

        self.getTicketingRequirement = function () {
            CaseService.getTicketingRequirement().then(function (response) {
                self.ticketingRequirementList = response.data;
            }, function (error) {
                self.tempError = error;
            });
        };

        self.getOffenceClass = function () {
            CaseService.getOffenceClass().then(function (response) {
                self.offenceClassList = response.data;
            }, function (error) {
                self.tempError = error;
            });
        };

        self.getTimeUnits = function () {
            CaseService.getTimeUnits().then(function (response) {
                self.trialEstimationUnitList = response.data;
            }, function (error) {
                self.tempError = error;
            });
        };

        self.getCaseDetail = function (crestCaseNumber) {
            CaseService.getCaseDetail(crestCaseNumber).then(function (response) {
                self.crestData = response;
                self.hiddenCrestCaseNumber = response.crestCaseNumber;
                self.dateOfSending = SharedService.dateToString(self.crestData.dateOfSending);
                self.dateOfCommittal = SharedService.dateToString(self.crestData.dateOfCommittal);
                self.showCaseDetail = true;
                self.showDefendant = true;
            }, function (error) {
                self.courtCaseMessage = error.message;
                self.showCaseDetail = false;
                self.showDefendant = false;
                self.crestData = null;
                return false;
            }).finally(function () {
                $timeout(function () {
                    self.courtCaseMessage = null;
                }, SharedService.timerDelay);
            });
        };

        var areEditable = function (crestCaseNumber, selectedCourt) {

            var errorPresent = false;

            if (self.hiddenCrestCaseNumber && (crestCaseNumber !== self.hiddenCrestCaseNumber)) {
                errorPresent = true;
                self.caseSaveMessage = 'Cannot Edit Crest Number';
                $timeout(function () {
                    self.caseSaveMessage = null;
                }, SharedService.timerDelay);
            }

            if (SharedService.courtCenter !== selectedCourt) {
                self.caseSaveMessage = (errorPresent) ? "Cannot Edit Court Center, Crest Number" : "Cannot Edit Court Center";
                errorPresent = true;
                $timeout(function () {
                    self.caseSaveMessage = null;
                }, SharedService.timerDelay);
            }

            return errorPresent;
        };

        self.saveCase = function (crestCaseNumber, selectedCourt) {

            var errorFlag = areEditable(crestCaseNumber, selectedCourt);
            if (!errorFlag) {
                var params = {
                    'crestCaseNumber': self.crestData.crestCaseNumber,
                    'leadDefendant': self.crestData.leadDefendant,
                    'numberOfDefendent': self.crestData.numberOfDefendent,
                    'mostSeriousOffence': self.crestData.mostSeriousOffence,
                    'trialEstimate': self.crestData.trialEstimate,
                    'offenceClass': self.crestData.offenceClass,
                    'releaseDecisionStatus': self.crestData.releaseDecisionStatus,
                    'ticketingRequirement': self.crestData.ticketingRequirement,
                    'dateOfSending': self.dateOfSending,
                    'dateOfCommittal': self.dateOfCommittal,
                    'courtCenter': SharedService.courtCenter,
                    'trialEstimateUnit': self.crestData.trialEstimateUnit,
                    'judicialOfficer': self.crestData.judicialOfficer
                };

                CaseService.saveCase(params).then(function () {
                    self.caseSaveMessage = 'Case has been Saved...';
                    self.hiddenCrestCaseNumber = crestCaseNumber;
                    self.showDefendant = true;
                }, function (error) {
                    self.caseSaveMessage = error.message;
                    self.showDefendant = false;
                }).finally(function () {
                    $timeout(function () {
                        self.caseSaveMessage = null;
                    }, SharedService.timerDelay);
                });
            }
        };

        self.addCase = function (crestCaseNumber) {

            self.hiddenCrestCaseNumber = crestCaseNumber;

            CaseService.addCase(crestCaseNumber).then(function () {
                self.showCaseDetail = false;
                self.courtCaseMessage = "Case already exist.";
                return false;

            }, function (error) {
                if (error.message === null) {
                    self.showCaseDetail = false;
                    self.courtCaseMessage = "Case already exist.";
                    return false;
                }
                self.showCaseDetail = true;
                self.showDefendant = false;

                //Setting Default
                self.crestData.trialEstimateUnit = self.trialEstimationUnitList[0];
                self.crestData.offenceClass = self.offenceClassList[0];
                self.crestData.releaseDecisionStatus = self.releaseDecisionList[0];
                self.crestData.ticketingRequirement = self.ticketingRequirementList[0];
                self.dateOfSending = null;
                self.dateOfCommittal = null;

            }, function (error) {
                self.tempError = error;
            }).finally(function () {
                $timeout(function () {
                    self.courtCaseMessage = null;
                }, SharedService.timerDelay);
            });
        };

        self.resetCaseFields = function () {
            self.showCaseDetail = false;
            self.showDefendant = false;
            self.crestData = null;
        };

        self.getJudges = function () {
            JudgeService.getListOfJudges().then(function (response) {
                self.listOfJudges = response;
            });
        };

        self.init = function () {
            self.getCourtCentres();
            self.getReleaseDecision();
            self.getTicketingRequirement();
            self.getOffenceClass();
            self.getTimeUnits();
            self.getJudges();
        };

    }
}());
