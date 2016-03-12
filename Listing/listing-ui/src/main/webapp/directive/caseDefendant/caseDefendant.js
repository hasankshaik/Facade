/**
 * @ngdoc directive
 * @name ccsApp.directive:caseDefendant
 * @function
 *
 * @description
 * This directive (caseDefendant) does something. <-- Edit this.
 *
 * @example
  <example module="ccsApp">
    <file name="index.html">
      <caseDefendant></caseDefendant>
    </file>
  </example>
 * $scope.case contains reference to the parent.
 */
 (function() {
    'use strict';
    angular
        .module('ccsApp').directive('caseDefendant', function () {

            return {
                restrict: 'AE',
                replace: true,
                scope: {
                    case: '='
                },
                templateUrl: 'directive/caseDefendant/caseDefendant.html',
                controller: 'DefendantController',
                link: function () {
                }
            };
        })
        .controller('DefendantController', function ($scope, $modal, $timeout, CaseService, SharedService) {
            $scope.addDefendant = function() {
                $scope.defendantName            = '';
                $scope.custodyStatusSelected    = 'Bail';
                $scope.ctlExpiryDate            =''; 
                $scope.defendantURN             = '';
                $scope.showAddDefendant         = true;       
            };

            $scope.editDefendant = function (person, crestCaseNumber) {
                var params = {
                        'crestCaseNumber'       : crestCaseNumber,
                        'courtCenter'           : SharedService.courtCenter,
                        'personInCaseList'      : [{
                            'ctlExpiryDate'         : person.ctlExpiryDate,
                            'crestURN'              : person.crestURN,
                            'fullname'              : person.fullname,
                            'originalFullname'      : person.originalFullname,
                            'custodyStatus'         : person.custodyStatus
                        }]
                };
                CaseService.editDefendant(params).then(function (response) {
                    $scope.case.getCaseDetail(crestCaseNumber);
                    $scope.showAddDefendant = false;
                    $scope.defendantUpdatedMessage = 'Defendant has been Updated...';
                }, function (error) {
                    $scope.defendantUpdatedMessage = error.message;
                }).finally(function () {
                    $timeout(function () {
                        $scope.defendantUpdatedMessage = null;
                    }, SharedService.timerDelay);
                });
            };
            
            $scope.deleteDefendant = function(person, crestCaseNumber) {
                var msg = "Do you want to delete this defendant ?";
                $modal.open({
                    templateUrl:    'partial/alertDialog/alertDialog.html',
                    controller:     'RemoveDefendantCtrl', 
                    resolve: {
                        bodyMessage     : function() { return msg; },
                        headerMessage   : function() { return "Delete defendant"; }, 
                        personFullName  : function() { return person.fullname; },
                        crestCaseNumber : function() { return crestCaseNumber; }
                    }
                }).result.then(function () {

                }, function (message) {
                    if (message && message.indexOf("deleted") > 0) {
                        $scope.defendantUpdatedMessage = message;
                        $scope.case.getCaseDetail(crestCaseNumber);
                    } 
                    $timeout(function () {
                        $scope.defendantUpdatedMessage = null;
                    }, SharedService.timerDelay);
                });
            };

            $scope.saveDefendant = function (crestCaseNumber) {
                var params = {
                        'crestCaseNumber'       : crestCaseNumber,
                        'courtCenter'           : SharedService.courtCenter,
                        'personInCaseList'      : [{
                            'ctlExpiryDate'         : $scope.ctlExpiryDate,
                            'crestURN'              : $scope.defendantURN,
                            'fullname'              : $scope.defendantName,
                            'custodyStatus'         : $scope.custodyStatusSelected
                        }]
                };
                console.log(params);
                CaseService.saveDefendant(params).then(function (response) {
                    $scope.case.getCaseDetail(crestCaseNumber);
                    $scope.showAddDefendant = false;
                    $scope.defendantSaveMessage = 'Defendant has been added...';
                }, function (error) {
                    $scope.defendantSaveMessage = error.message;
                }).finally(function () {
                    $timeout(function () {
                        $scope.defendantSaveMessage = null;
                    }, SharedService.timerDelay);
                });
            };

            var getCustodyStatus = function () {
                CaseService.getCustodyStatus().then(function (response) {
                    $scope.custodyStatusList = response.data;
                }, function (error) {
                });
            };

            $scope.init = function () {
                getCustodyStatus();
            };
        });
})();