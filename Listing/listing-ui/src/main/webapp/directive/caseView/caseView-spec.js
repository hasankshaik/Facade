describe('caseView', function () {

  beforeEach(module('ccsApp'));

  var scope, compile;

  beforeEach(inject(function ($rootScope, $compile) {
    scope = $rootScope.$new();
    compile = $compile;
  }));

  it('should ...', function () {

    /*
     To test your directive, you need to create some html that would use your directive,
     send that through compile() then compare the results.

     */

	  expect(scope.eauals(scope));
	  expect(compile.eauals(compile));
  });
});
