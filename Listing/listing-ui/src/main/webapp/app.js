(function () {

    'use strict';

    angular.module('ccsApp', [
    'config',
    'ui.bootstrap',
    'ui.utils',
    'ui.cpp',
    'angularUtils.directives.dirPagination',
    'ui.router',
    'ngAnimate',
    'ngLocalize'

    ]);

    angular.module('ccsApp').run(function ($rootScope) {

        $rootScope.safeApply = function (fn) {
            var phase = $rootScope.$$phase;
            if (phase === '$apply' || phase === '$digest') {
                if (fn && (typeof (fn) === 'function')) {
                    fn();
                }
            } else {
                this.$apply(fn);
            }
        };

    });

}());


