
(function () {

    'use strict';

    angular.module('ccsApp').config(configure);

    function configure($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider,
        paginationTemplateProvider, datepickerConfig) {

        datepickerConfig.startingDay = 1;
        datepickerConfig.showWeeks = false;
        datepickerConfig.showButtonBar = false;
        paginationTemplateProvider.setPath('bower_components/angular-utils-pagination/dirPagination.tpl.html');


        $locationProvider.html5Mode(false);

        $stateProvider.state('schedule', { url: '/schedule', templateUrl: 'partial/schedule/schedule.html', data: { pageTitle: 'schedule' } });
        $stateProvider.state('case', { url: '/case', templateUrl: 'partial/case/case.html', data: { pageTitle: 'case' } });
        $stateProvider.state('manage-case', { url: '/manage-case', templateUrl: 'partial/case/manage-case/manage-case.html', data: { pageTitle: 'manage case' } });
        $stateProvider.state('sitting', { url: '/sitting', templateUrl: 'partial/sitting/sitting.html', data: { pageTitle: 'sitting' } });
        $stateProvider.state('admin', { url: '/admin', templateUrl: 'partial/admin/admin.html', data: { pageTitle: 'admin' } });
        /* Add New States Above */

        $urlRouterProvider.otherwise('/schedule');
    }
}());

