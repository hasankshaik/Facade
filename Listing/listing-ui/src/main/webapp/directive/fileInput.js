/**
 * @ngdoc directive
 * @name ccsApp.directive:fileInput
 * @element element
 * @function
 *
 * @description
 * This directive (fileInput) does something. <-- Edit this.
 *
 * @example
  <example module="ccsApp">
    <file name="index.html">
      <element fileInput></element>
    </file>
  </example>
 */
angular.module('ccsApp').directive('fileInput', function ($parse) {
  return {
    restrict: 'AE',
    link: function (scope, element, attrs) {
      var modelGet = $parse(attrs.fileInput);
      var modelSet = modelGet.assign;
      var onChange = $parse(attrs.onChange);

      element.bind('change', function(){
          scope.$apply(function(){
            modelSet(scope, element[0].files[0]);
            onChange(scope);
          });
      });
    }
  };
});
