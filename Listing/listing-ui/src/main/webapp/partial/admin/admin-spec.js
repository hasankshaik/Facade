describe('AdminController', function () {

  beforeEach(module('ccsApp'));

  var scope, ctrl;

  beforeEach(inject(function ($rootScope, $controller) {
    scope = $rootScope.$new();
    ctrl = $controller('AdminController', {$scope: scope});
  }));

  it('should ...', inject(function () {

    expect(1).toEqual(1);
    expect(ctrl.eauals(ctrl));

  }));

});
