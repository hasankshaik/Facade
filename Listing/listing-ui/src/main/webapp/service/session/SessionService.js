/**
 * @ngdoc service
 * @name ccsApp.manageSessionService
 *
 * @description
 * # manageSessionService
 * Service in the ccsApp app.
 */
angular.module('ccsApp').factory('SessionService', function($http, $q, ENV) {

  return { 
	  managesessions : function(params) {
		var q = $q.defer();
		$http({
			url: ENV.apiEndpoint + "rest/adminservice/managesessions", 
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
