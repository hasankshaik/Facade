/**
 * @ngdoc service
 * @name ccsApp.CaseService
 *
 * @description
 * # CaseService
 * Service in the ccsApp app.
 */
(function () {

    'use strict';

    angular.module('ccsApp').factory('CaseService', function ($http, $q, ENV) {

        return {
            getCaseDetail: function (courtCaseCrestNumber) {
                var q = $q.defer();
                $http.get(ENV.apiEndpoint + "rest/listing/case/" + courtCaseCrestNumber)
                .success(function (response) {
                    q.resolve(response.data);
                })
                .error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            getReleaseDecision: function () {
                var q = $q.defer();
                $http.get(ENV.apiEndpoint + "rest/listing/enums/releasedecision")
                .success(function (response) {
                    q.resolve(response);
                })
                .error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            getTicketingRequirement: function () {
                var q = $q.defer();
                $http.get(ENV.apiEndpoint + "rest/listing/enums/ticketingrequirement")
                .success(function (response) {
                    q.resolve(response);
                })
                .error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            getOffenceClass: function () {
                var q = $q.defer();
                $http.get(ENV.apiEndpoint + "rest/listing/enums/offenceclass")
                .success(function (response) {
                    q.resolve(response);
                })
                .error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            getTimeUnits: function () {
                var q = $q.defer();
                $http.get(ENV.apiEndpoint + "rest/listing/enums/timeunits")
                .success(function (response) {
                    q.resolve(response);
                })
                .error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            getCustodyStatus: function () {
                var q = $q.defer();
                $http.get(ENV.apiEndpoint + "rest/listing/enums/custodystatus")
                .success(function (response) {
                    q.resolve(response);
                })
                .error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            addCase: function (crestCaseNumber) {
                var q = $q.defer();
                $http.get(ENV.apiEndpoint + "rest/listing/case/" + crestCaseNumber)
                .success(function (response) {
                    q.resolve(response);
                })
                .error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            saveCase: function (params) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/casesRelated",
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

            editDefendant: function (params) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/casesRelated/Defendent",
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

            saveDefendant: function (params) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/casesRelated/Defendent",
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

            deleteDefendant: function (crestCaseNumber, personFullName) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/casesRelated/personInCase/" + crestCaseNumber + "/" + personFullName,
                    method: 'DELETE',
                    headers: { 'Content-Type': 'application/json; charset=utf-8' }
                }).success(function (response) {
                    q.resolve(response);
                }).error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            allocateJudge: function (params) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/casesRelated/allocatejudge",
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

            deallocateJudge: function (params) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/casesRelated/deallocatejudge",
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

            saveNote: function (params) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/casesRelated/casenote",
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

            deleteNote: function (crestCaseNumber, description) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/casesRelated/casenote/" + crestCaseNumber + "/" + description,
                    method: 'DELETE',
                    headers: { 'Content-Type': 'application/json; charset=utf-8' }
                }).success(function (response) {
                    q.resolve(response);
                }).error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            editNonAvailabileDate: function (params) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/casesRelated/notavailabledate",
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

            saveNonAvailableDate: function (params) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/casesRelated/notavailabledate",
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

            deleteNonAvailabileDate: function (param) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/casesRelated/notavailabledate/" + param,
                    method: 'DELETE',
                    headers: { 'Content-Type': 'application/json; charset=utf-8' }
                }).success(function (response) {
                    q.resolve(response);
                }).error(function (error) {
                    q.reject(error);
                });
                return q.promise;
            },

            saveLinkedCase: function (params) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/casesRelated/linkedcase",
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

            deleteLinkedCase: function (courtCenter, crestCaseNumber, crestLinkedNumber) {
                var q = $q.defer();
                $http({
                    url: ENV.apiEndpoint + "rest/listing/casesRelated/linkedcase/" + courtCenter + "/" + crestCaseNumber + "/" + crestLinkedNumber,
                    method: 'DELETE',
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
