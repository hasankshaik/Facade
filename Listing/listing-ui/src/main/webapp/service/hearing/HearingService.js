/**
 * @ngdoc service
 * @name ccsApp.HearingService
 *
 * @description
 * # HearingService
 * Service in the ccsApp app.
 */

(function () {

    'use strict';

    angular.module('ccsApp').factory('HearingService', function ($http, $q, ENV) {

        return {
            showScheduledHearingDetails: function (hearing) {
                var q = $q.defer();
                $http.get(ENV.apiEndpoint + "rest/listing/hearings/" + hearing.hearingKey)
                .success(function (response) {
                    q.resolve(response.data);
                })
                .error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            getUnlistedHearings: function () {
                var q = $q.defer();
                $http.get(ENV.apiEndpoint + "rest/listing/hearings/unlisted")
                .success(function (response) {
                    q.resolve(response.data);
                })
                .error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            getSessionBlockType: function () {
                var q = $q.defer();
                $http.get(ENV.apiEndpoint + "rest/listing/enums/sessionblocktype")
                .success(function (response) {
                    q.resolve(response);
                })
                .error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            getAnnualSittingsDays: function (courtCenter, financialYearSelected) {
                var q = $q.defer();
                $http.get(ENV.apiEndpoint + "rest/sitting/annual-actual-sitting-days/" + courtCenter + "/year/" + financialYearSelected)
                .success(function (response) {
                    q.resolve(response.data);
                })
                .error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            getMonthlyActualSittings: function (courtCentre, financialYearSelected) {
                var q = $q.defer();
                $http.get(ENV.apiEndpoint + "rest/sitting/monthly-actual-sitting-days/" + courtCentre + "/" + financialYearSelected)
                .success(function (response) {
                    q.resolve(response.data);
                }).error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            getHearingSlots: function (courtCentre, hearing, overbook) {
                var q = $q.defer();
                $http.get(ENV.apiEndpoint + "rest/listing/hearing/slots/" + courtCentre + "/" + hearing.hearingKey + "/" + hearing.trialEstimate + "/" + overbook)
                .success(function (response) {
                    q.resolve(response.data);
                }).error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            listHearing: function (params) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/hearings/list",
                    method: 'POST',
                    data: params,
                    headers: { 'Content-Type': 'application/json; charset=utf-8' }
                }).success(function (response) {
                    q.resolve(response.data);
                }).error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            updateHearing: function (params) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/hearings/relist",
                    method: 'POST',
                    data: params,
                    headers: { 'Content-Type': 'application/json; charset=utf-8' }
                }).success(function (response) {
                    q.resolve(response.data);
                }).error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            unListHearing: function (hearingKey, boolean) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/hearings/" + hearingKey + "/" + boolean,
                    method: 'DELETE',
                    headers: { 'Content-Type': 'application/json; charset=utf-8' }
                }).success(function (response) {
                    q.resolve(response);
                }).error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            updatePcmhHearing: function (params) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/hearings/pcmhaction",
                    method: 'POST',
                    data: params,
                    headers: { 'Content-Type': 'application/json; charset=utf-8' }
                }).success(function (response) {
                    q.resolve(response);
                }).error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            getPchmHeringsForAction: function () {
                var q = $q.defer();
                $http.get(ENV.apiEndpoint + "rest/listing/hearings/foraction")
                .success(function (response) {
                    q.resolve(response.data);
                }).error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            editEstimate: function (params) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/hearings",
                    method: 'POST',
                    data: params,
                    headers: { 'Content-Type': 'application/json; charset=utf-8' }
                }).success(function (response) {
                    q.resolve(response);
                }).error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            }
        };
    });
}());

