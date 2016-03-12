angular.module('ccsApp').controller('BaseController', function ($scope, $timeout, $location, FileService) {

	var getLogoutUrl = function() {
		FileService.getLogoutUrl().then(function (url){
			$scope.logOutUrl = url;
		});
	};
	
	$scope.init = function () {
		getLogoutUrl();
	};
	
});