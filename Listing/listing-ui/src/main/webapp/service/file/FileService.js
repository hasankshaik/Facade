/**
 * @ngdoc service
 * @name ccsApp.FileService
 *
 * @description
 * # FileService
 * Service in the ccsApp app.
 */
angular.module('ccsApp').factory('FileService', function($http, $q, ENV) {

	return {
		uploadFile : function(file) {
			var q = $q.defer();
			var fd = new FormData();
			fd.append('file', file);
			$http({
				url: ENV.apiEndpoint + "rest/listing/upload", 
				method: 'POST',
				data: fd,
				transformRequest: angular.identity,
				headers: {'Content-Type': undefined}
			}).success(function (response) {
				q.resolve(response);
			}).error(function (error){
				q.reject(error);
			});
			return q.promise;
		},

		getProcessingStatus : function () {
			var q = $q.defer();
			$http.get(ENV.apiEndpoint + "rest/listing/crestdata/status")
			.success(function (response) {
				q.resolve(response.data);
			})
			.error(function (error) {
				q.reject(error);
			});
			return q.promise;
		},
		
		getLogoutUrl : function() {
			var q = $q.defer();
			$http.get(ENV.apiEndpoint + "rest/listing/logout")
			.success(function(response) {
				q.resolve(response);
			})
			.error(function (error) {
				q.reject(error);
			});
			return q.promise;
		}
	};
});
