/**
 * @ngdoc directive
 * @name ccsApp.directive:caseNote
 * @function
 *
 * @description
 * This directive (caseNote) does something. <-- Edit this.
 *
 * @example
  <example module="ccsApp">
    <file name="index.html">
      <caseNote></caseNote>
    </file>
  </example>
 */
angular.module('ccsApp').directive('caseNote', function () {
    return {
        restrict: 'AE',
        replace: true,
        scope: {
            case: '='
        },
        templateUrl: 'directive/caseNote/caseNote.html',
        controller: 'NoteController',
        link: function () {

        }
    };
}).controller('NoteController', function ($scope, $modal, $timeout, CaseService, SharedService) {

    $scope.addNote = function() {
        $scope.noteDesc ='';
        $scope.dateNote ='';
        $scope.showAddNote = true;
    };

    $scope.saveNote = function (crestCaseNumber) {
        var params = {
            'crestCaseNumber'   : crestCaseNumber,
            'courtCenter'       : SharedService.courtCenter,
            'notesWeb'          : [{
                'note'              : $scope.noteDesc,
                'diaryDate'         : $scope.dateNote,
                'creationDate'      : new Date()
            }]
        };

        CaseService.saveNote(params).then(function () {
            $scope.case.getCaseDetail(crestCaseNumber);
            $scope.showAddNote = false;
            $scope.noteSaveMessage = 'Note has been added...';
        }, function (error) {
            $scope.noteSaveMessage = error.message;
        }).finally(function () {
            $timeout(function () {
                $scope.noteSaveMessage = null;
            }, SharedService.timerDelay);
        });
    };

    $scope.deleteNote = function(note, crestCaseNumber) {

        var msg = "Do you want to delete this note ?";

        $modal.open({
            templateUrl:    'partial/alertDialog/alertDialog.html',
            controller:     'RemoveNoteCtrl',
            resolve: {
                bodyMessage     : function() { return msg; },
                headerMessage   : function() { return "Delete note"; },
                description     : function() { return note.description; },
                crestCaseNumber : function() { return crestCaseNumber; }
            }
        }).result.then(function () {

         }, function (message) {
             if (message && message.indexOf("deleted") > 0) {
                $scope.updateNoteMessage = message;
                $scope.case.getCaseDetail(crestCaseNumber);
             }
             $timeout(function () {
                $scope.updateNoteMessage = null;
            }, SharedService.timerDelay);
        });
    };
});
