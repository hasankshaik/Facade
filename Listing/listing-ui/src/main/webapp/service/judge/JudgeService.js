/**
 * @ngdoc service
 * @name ccsApp.JudgeService
 *
 * @description
 * # JudgeService
 * Service in the ccsApp app.
 */
angular.module('ccsApp').factory('JudgeService', function($http, $q, ENV) {

	return{
		getListOfJudges : function() {
			var q = $q.defer();
			$http.get(ENV.apiEndpoint + "rest/adminservice/judicialofficers")
			.success(function(response) {
				q.resolve(response.data);
			})
			.error(function(error) {
				q.reject(error);
			});
			return q.promise;
		},

		addJudge : function(params) {
			var q = $q.defer();
			$http({
				url: ENV.apiEndpoint + "rest/adminservice/judicialofficers", 
				method: 'POST',
				data: params,
				headers: {'Content-Type': 'application/json; charset=utf-8'}
			}).success(function (response) {
				q.resolve(response);
			}).error(function(error) {
				q.reject(error);
			});
			return q.promise;
		},
		
		getJudicialOfficerType: function () {
			var q = $q.defer();
			$http.get(ENV.apiEndpoint + "rest/listing/enums/judicialofficertype")
			.success(function (response) {
				q.resolve(response);
			})
			.error(function (error) {
				q.reject(error);
			});
			return q.promise;
		},
		
		getJudicialOfficerTickets: function () {
			var q = $q.defer();
			$http.get(ENV.apiEndpoint + "rest/listing/enums/judicialtickets")
			.success(function (response) {
				q.resolve(response);
			})
			.error(function (error) {
				q.reject(error);
			});
			return q.promise;
		},

		allocateJudge : function(params) {
			var q = $q.defer();
			$http({
				url: ENV.apiEndpoint + "rest/adminservice/allocatejudgeinroom", 
				method: 'POST',
				data: params,
				headers: {'Content-Type': 'application/json; charset=utf-8'}
			}).success(function (response) {
				q.resolve(response);
			}).error(function (error){
				q.reject(error);
			});
			return q.promise;
		},

		deallocateJudge : function(params) {
			var q = $q.defer();
			$http({
				url: ENV.apiEndpoint + "rest/adminservice/deallocatejudgeinroom", 
				method: 'POST',
				data: params,
				headers: {'Content-Type': 'application/json; charset=utf-8'}
			}).success(function (response) {
				q.resolve(response);
			}).error(function (error){
				q.reject(error);
			});
			return q.promise;
		},
		
		manageBlocks : function(params) {
			var q = $q.defer();
			$http({
				url: ENV.apiEndpoint + "rest/adminservice/manageblocks", 
				method: 'POST',
				data: params,
				headers: {'Content-Type': 'application/json; charset=utf-8'}
			}).success(function (response) {
				q.resolve(response);
			}).error(function (error){
				q.reject(error);
			});
			return q.promise;
		},

		deleteBlocks : function(params) {
			var q = $q.defer();
			$http({
				url: ENV.apiEndpoint + "rest/adminservice/manageblocks", 
				method: 'DELETE',
				data: params,
				headers: {'Content-Type': 'application/json; charset=utf-8'}
			}).success(function (response) {
				q.resolve(response);
			}).error(function (error){
				q.reject(error);
			});
			return q.promise;
		}
	};

});
