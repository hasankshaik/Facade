/**
 * @ngdoc function
 * @name ccsApp.controller:SittingController
 * @description
 * # SittingController
 * Controller of the ccsApp app
 */
(function () {

    'use strict';

    angular.module('ccsApp').controller('SittingController', function ($scope, $filter, $q, $timeout, $location, FinancialYearService, SittingsService, SharedService) {
        $scope.courtName = SharedService.courtName;
        $scope.courtCenter = $location.search()['courtCenter'] || "Birmingham";
        $scope.financialYears = FinancialYearService.getYears();
        $scope.selectedFinancialYear = 'Select an year';
        $scope.monthlyActuals = [];

        var calculateCumulativeVar = function () {
            $scope.cumulativeVar = [];
            for (var i = 0; i < ($scope.monthList.length) ; i++) {
                var date = $scope.monthList[i];
                var month = $filter('date')(date, 'MMM');
                var variance = $scope.monthlyActuals[month] - ($scope.sittingTarget.monthlyTargets[month] || 0);
                var previousMonth = new Date();
                previousMonth.setDate(1);
                previousMonth.setMonth(date.getMonth() - 1);
                var preMonthString = $filter('date')(previousMonth, 'MMM');
                $scope.cumulativeVar[month] = ($scope.cumulativeVar[preMonthString] || 0) + (variance || 0);
            }
        };

        $scope.getSittings = function (year) {
            if (year !== null) {
                return $q.all([
                        SittingsService.getSittings($scope.courtCenter, $scope.selectedFinancialYear),
                        SittingsService.getAnnualSittingsDays($scope.courtCenter, $scope.selectedFinancialYear),
                        SittingsService.getMonthlyActualSittings($scope.courtCenter, $scope.selectedFinancialYear),
                        FinancialYearService.populateMonths($scope.selectedFinancialYear)
                ]).then(function (data) {
                    $scope.sittingTarget = data[0];
                    $scope.annualActualSittingDays = data[1];
                    $scope.monthlyActuals = data[2];
                    $scope.monthList = data[3];
                    $scope.yearSelected = true;
                    calculateCumulativeVar();
                });
            } else {
                $scope.yearSelected = false;
            }
        };

        var setSittingDays = function (annualTarget, monthlyTargets) {
            SittingsService.setSittingDays($scope.courtCenter, $scope.selectedFinancialYear, annualTarget, monthlyTargets)
            .then(function () {
                $scope.getSittings($scope.selectedFinancialYear);
                $scope.sittingSavedMessage = 'Saved...';
            }, function (error) {
                $scope.sittingSavedMessage = error.message;
            }).finally(function () {
                $timeout(function () {
                    $scope.sittingSavedMessage = null;
                }, 10000);
            });
        };

        $scope.setAnnualSittingDays = function () {
            setSittingDays($scope.sittingTarget.annualTarget, null);
        };

        $scope.setMonthlySittingDays = function () {
            setSittingDays($scope.sittingTarget.annualTarget, $scope.sittingTarget.monthlyTargets);
        };

    }).directive('formCleaner', function () {
        return {
            scope: true,
            require: '^form',
            link: function (scope) {
                scope.clean = function () {
                    scope.innerForm.$setPristine();
                };

                scope.$watch('innerForm.$dirty', function (isDirty) {
                    scope.innerForm.$setValidity('name', !isDirty);
                });
            }
        };
    });
}());
