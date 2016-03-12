/**
 * @ngdoc directive
 * @name ccsApp.directive:dateFormat
 * @element element
 * @function
 *
 * @description
 * This directive (dateFormat) does something. <-- Edit this.
 *
 * @example
  <example module="ccsApp">
    <file name="index.html">
      <element dateFormat></element>
    </file>
  </example>
 */
(function() {
    'use strict';
    angular
        .module('ccsApp')
        .directive('dateFormat', function () {
            // return the directive link function. (compile function not needed)
            return {
                restrict: 'EA',
                replace: true,
                scope : {
                    obj         :'=',
                    model       :'=',
                    change      :'=',
                    formName    :'@',
                    name        :'@',
                    index       :'@'
                },
                template: function (elem, attr) {
                    var changeAttr  = (attr.change) ? 'ng-change="obj.' + attr.change + '(obj.'+ attr.model + ')"' : '';
                    var modelVal    = (attr.loop === 'true')  ? ' ng-model="model"' : ' ng-model="obj.'+ attr.model +'"';

                    var elem    =   '<div class="input-group" ng-form="{{formName}}">'
                                    +   '<input type="text" class="form-control"'
                                    +       ' uk-format'
                                    +       ' ng-enter'
                                    +       ' id="{{name}}{{index}}"'
                                    +       ' name="{{name}}"'
                                    +        modelVal
                                    +        changeAttr 
                                    +       ' datepicker-popup="{{format}}"'
                                    +       ' is-open="status.opened"'
                                    +       ' ng-required="true"'
                                    +       ' placeholder="dd/mm/yyyy"'
                                    +       ' close-text="Close" />'
                                    +   '<span class="input-group-btn">'
                                    +       '<button type="button" class="btn btn-default" ng-click="open($event)">'
                                    +           '<i class="glyphicon glyphicon-calendar"></i>'
                                    +       '</button>'
                                    +   '</span>'
                                    + '</div>';
                    return elem;

                },   
                controller: function ($scope) {
                    $scope.format   = 'dd/MM/yyyy';
                    $scope.open     = function($event) {
                        $event.preventDefault();
                        $event.stopPropagation();
                        $scope.status.opened = true;
                    };
                }
            };

        })
        .directive('ukFormat', function (dateFilter, datepickerPopupConfig) {
            
            return {
                restrict: 'EA',
                replace: true,
                require: 'ngModel',
                link: function(scope, element, attrs, ngModel) {

                    var format      = attrs.datepickerPopup;
                    var datefilter  = dateFilter;
                    var model       = ngModel;

                    ngModel.$parsers.push(function(viewValue) {
                        var newDate = model.$viewValue;
                        var date = null;

                        // pass through if we clicked date from popup
                        if (typeof newDate === "object" || newDate == "") return newDate;
         
                        // build a new date according to initial localized date format
                        if (format === "dd/MM/yyyy") {
                            // extract day, month and year
                            var splitted    = newDate.split('/');
                            var day         = splitted[0];
                            var month       = parseInt(splitted[1]) - 1;
                            var year        = splitted[2];
                            date            = new Date(year, month, day);  

                            if((day > 31 && viewValue === undefined) || month > 11) {
                              return false;
                            }              
                            model.$setValidity('date', true);
                            model.$setViewValue(date);
                        }

                        return date ? date : viewValue;
                    });
         
                    element.on('keydown', {scope:scope, varOpen:attrs.isOpen}, function(e) {
                        var response = true;
                        // the scope of the date control
                        var scope = e.data.scope;
                        // the variable name for the open state of the popup (also controls it!)
                        var openId = e.data.varOpen;
                        switch (e.keyCode) {
                        case 13: // ENTER
                            scope[openId] = !scope[openId];
                            // update manually view
                            if (!scope.$$phase) scope.$apply();
                            response = false;
                            break;
         
                        case 9: // TAB
                            scope[openId] = false;
                            // update manually view
                            if (!scope.$$phase) scope.$apply();
                            break;
                        }
         
                        return response;
                    });
         
                    // set input to the value set in the popup, which can differ if input was manually!
                    element.on('blur', {scope:scope}, function(e) {
                        // the value is an object if date has been changed! Otherwise it was set as a string.
                        if (typeof model.$viewValue === "object") {
                            element.context.value = isNaN(model.$viewValue) ? "" : dateFilter(model.$viewValue, format);
                            if (element.context.value == "") model.$setValidity('required', false);                    
                        }
                    });
                }
            };

        }).directive('ngEnter', function () {
            return {
                restrict: 'A',
                replace: true,
                link: function (scope, element, attrs, fn) {
                    element.bind("keydown keypress", function (event) {
                        if(event.which === 13) {
                            scope.$apply(function (){
                                scope.$eval(attrs.ngEnter);
                            });
                            scope.obj.navigateToDate();
                            event.preventDefault();
                        }
                    });
                }
            };
        });
})();