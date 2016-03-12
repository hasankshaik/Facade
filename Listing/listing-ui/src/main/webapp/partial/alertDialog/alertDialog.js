(function () {

    'use strict';

    angular.module('ccsApp')
        .controller('AlertdialogCtrl', function ($scope, $timeout, $modalInstance, HearingService,
            SharedService, hearingKey, headerMessage, bodyMessage) {

            $scope.headerMessage = headerMessage;
            $scope.bodyMessage = bodyMessage;
            $scope.save = function () {
                HearingService.unListHearing(hearingKey, false).then(function () {
                    $modalInstance.dismiss("Hearing deleted");
                }, function (error) {
                    $modalInstance.dismiss("Error: " + error.errorMessage);
                }).finally(function () {
                    $timeout(function () {
                        self.hearingNameMessage = null;
                    }, SharedService.timerDelay);
                });
            };
        }).controller('UnlistHearingCtrl', function (
                $scope,
                $modalInstance,
                HearingService,
                SharedService,
                bodyMessage,
                hearingKey) {

            $scope.bodyMessage = bodyMessage;

            $scope.save = function () {
                HearingService.unListHearing(hearingKey, true).then(function () {
                    $modalInstance.dismiss("Hearing unlisted");
                }, function (error) {
                    $modalInstance.dismiss("Error: " + error.errorMessage);
                });
            };
        }).controller('RemoveDefendantCtrl', function (
            $scope,
            $modalInstance,
            CaseService,
            SharedService,
            bodyMessage,
            headerMessage,
            personFullName,
            crestCaseNumber) {

            $scope.headerMessage = headerMessage;

            $scope.bodyMessage = bodyMessage;

            $scope.save = function () {
                CaseService.deleteDefendant(crestCaseNumber, personFullName).then(function () {
                    $modalInstance.dismiss("Defendant deleted");
                }, function (error) {
                    $modalInstance.dismiss("Error: " + error.errorMessage);
                });
            };
        }).controller('RemoveNoteCtrl', function (
            $scope,
            $modalInstance,
            CaseService,
            SharedService,
            bodyMessage,
            headerMessage,
            description,
            crestCaseNumber) {

            $scope.headerMessage = headerMessage;

            $scope.bodyMessage = bodyMessage;

            $scope.save = function () {
                CaseService.deleteNote(crestCaseNumber, description).then(function () {
                    $modalInstance.dismiss("Note deleted");
                }, function (error) {
                    $modalInstance.dismiss("Error: " + error.errorMessage);
                });
            };
        }).controller('RemoveLinkedCaseCtrl', function (
            $scope,
            $modalInstance,
            CaseService,
            SharedService,
            bodyMessage,
            headerMessage,
            crestLinkedNumber,
            crestCaseNumber,
            courtCenter) {

            $scope.headerMessage = headerMessage;

            $scope.bodyMessage = bodyMessage;

            $scope.save = function () {
                CaseService.deleteLinkedCase(courtCenter, crestCaseNumber, crestLinkedNumber).then(function () {
                    $modalInstance.dismiss("Case unlinked");
                }, function (error) {
                    $modalInstance.dismiss("Error: " + error.errorMessage);
                });
            };
        }).controller('RemoveNonAvailableDateCtrl', function (
            $scope,
            $modalInstance,
            CaseService,
            bodyMessage,
            headerMessage,
            nonAvailabileDateId) {

            $scope.headerMessage = headerMessage;

            $scope.bodyMessage = bodyMessage;

            $scope.save = function () {
                CaseService.deleteNonAvailabileDate(nonAvailabileDateId).then(function () {
                    $modalInstance.dismiss("Deleted non avaialable date");
                }, function (error) {
                    $modalInstance.dismiss("Error: " + error.errorMessage);
                });
            };
        });

}());
