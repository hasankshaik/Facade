/**
 * @ngdoc service
 * @name ccsApp.CourtService
 *
 * @description
 * # CourtService
 * Service in the ccsApp app.
 */
angular.module('ccsApp').factory('CourtService', function($http, $q, ENV) {

	return{
		getCourtCentres : function() {
			var q = $q.defer();
			$http.get(ENV.apiEndpoint + "rest/adminservice/courtcentres")
			.success(function (response) {
				q.resolve(response.data);
			})
			.error(function (error) {
				q.reject(error);
			});
			return q.promise;
		},

		getCourtRooms : function(courtCenter) {
			var q = $q.defer();
			$http.get(ENV.apiEndpoint + "rest/listing/courtrooms/centername/"+courtCenter)
			.success(function (response) {
				q.resolve(response.data);
			})
			.error(function (error) {
				q.reject(error);
			});
			return q.promise;
		},

		addCourtCentre : function(params) {
			var q = $q.defer();
			$http({
				url: ENV.apiEndpoint + "rest/adminservice/courtcentre/", 
				method: 'PUT',
				data: params,
				headers: {'Content-Type': 'application/json; charset=utf-8'}
			}).success(function (response) {
				q.resolve(response);
			}).error(function (error) {
				q.reject(error);
			});
			return q.promise;
		},

		addCourtRoom : function(params) {
			var q = $q.defer();
			$http({
				url: ENV.apiEndpoint + "rest/adminservice/courtrooms/", 
				method: 'POST',
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
