/**
 * @ngdoc service
 * @name ccsApp.SittingsService
 *
 * @description
 * # SittingsService
 * Service in the ccsApp app.
 */
angular.module('ccsApp').factory('SittingsService', function($http, $q, $rootScope, ENV) {

	return{
		getSittings : function(courtCenter, financialYearSelected) {
			var q = $q.defer();
			$http.get(ENV.apiEndpoint + "rest/sitting/annual-sitting-target/" + courtCenter + "/" + financialYearSelected)
			.success(function (response) {
				q.resolve(response.data);
			})
			.error(function (error) {
				q.reject(error);
			});
			return q.promise;
		},

		getAnnualSittingsDays : function(courtCenter, financialYearSelected) {
			var q = $q.defer();
			$http.get(ENV.apiEndpoint + "rest/sitting/annual-actual-sitting-days/" + courtCenter + "/year/" + financialYearSelected)
			.success(function(response) {
				q.resolve(response.data);
			})
			.error(function (error) {
				q.reject(error);
			});
			return q.promise;
		},

		populateAnnualActualSitting : function(courtCenter, date) {
			var q = $q.defer();
			$http.get(ENV.apiEndpoint + "rest/sitting/annual-actual-sitting-days/" + courtCenter + "/date/" + date)
			.success(function(response) {
				q.resolve(response.data);
			})
			.error(function (error) {
				q.reject(error);
			});
			return q.promise;
		},

		populateSittings : function(courtCenter, startDate, endDate) {
			var q = $q.defer();
			$http.get(ENV.apiEndpoint + "rest/listing/planner/" + courtCenter + "/daterange/" + startDate + "/" + endDate)
			.success(function(response) {
				$rootScope.$broadcast('planner', response);
				q.resolve(response.data);
			})
			.error(function (error) {
				q.reject(error);
			});
			return q.promise;
		},

		getMonthlyActualSittings : function(courtCenter, financialYearSelected) {
			var q = $q.defer();
			$http.get(ENV.apiEndpoint + "rest/sitting/monthly-actual-sitting-days/" + courtCenter + "/" + financialYearSelected)
			.success(function (response) {
				q.resolve(response.data);
			}).error(function (error) {
				q.reject(error);
			});
			return q.promise;
		},

		setSittingDays : function(courtCenter, selectedFinancialYear, annualTarget, monthlyTargets) {
			var q = $q.defer();
			$http({
				url: ENV.apiEndpoint + "rest/sitting/annual-sitting-target", 
				method: 'PUT',
				data: {courtCenter:courtCenter, financialYear:selectedFinancialYear, annualTarget:annualTarget, monthlyTargets:monthlyTargets},
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
