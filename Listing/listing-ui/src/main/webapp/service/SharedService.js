/**
 * @ngdoc service
 * @name ccsApp.SharedService
 *
 * @description
 * # SharedService
 * Service in the ccsApp app.
 */
angular.module('ccsApp').factory('SharedService', function($location, $filter) {

	var SharedService = {
		courtName: 'Birmingham Court',
		courtCenter:  $location.search()['courtCenter'] || "Birmingham",
		timerDelay: 10000,
		dateToString: function (dateParam) {
			return (dateParam === null || dateParam === 0) ? " " : $filter('date')(dateParam, 'yyyy-MM-dd');
		}
	};
	
	return SharedService;
});
