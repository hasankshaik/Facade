/**
 * @ngdoc function
 * @name ccsApp.controller:AdminController
 * @description
 * # AdminController
 * Controller of the ccsApp app
 */

(function () {

    'use strict';

    angular
        .module('ccsApp')
        .controller('AdminController', AdminController);

    function AdminController($scope, $timeout, $location, SharedService, CourtService, JudgeService, FileService) {
        var self = this;

        self.courtName = SharedService.courtName;
        self.courtCenter = $location.search()['courtCenter'] || "Birmingham";
        self.delayTime = SharedService.timerDelay;
        self.nameRegex = /^(([a-zA-Z.\-|'])+\s{0,1})*$/;
        self.courtCentreRegex = /^([a-zA-Z0-9]+[ ]*)*$/;
        self.dateRegex = /^(([0-9])|([0-2][0-9])|([3][0-1]))\/([a-zA-Z]{3})\/\d{4}$/;

        $scope.init = function () {
            self.getJudges();
            self.getJudicialOfficerTypes();
            self.getJudicialOfficerTickets();
            self.getCourtRooms();
            self.getCourtCentres();
            self.refreshProcessingStatus();
            self.fileUploadDisabled = false;
        };

        self.getJudgeTicket= function(tickets){
    		return tickets.toString();
    		
    	}
        
        self.getJudges = function () {
            JudgeService.getListOfJudges().then(function (judges) {
                self.judges = judges;
            }, function (error) {
                self.tempError = error;
            });
        };

        self.removeResident = function (judge) {
        	if(judge.judgeType != 'Circuit') {
        		self.judge.isResident = false;
        	}
        	self.judgeTicketMurder = false;
        	self.judgeTicketAM = false;
        	self.judgeTicketSO = false;
        	self.judgeTicketF = false;
        	self.judgeTicketHS =false;
        };
        	
        self.addJudge = function (judge) {
        	var tickets = [] ;
    		if(self.judgeTicketMurder == true){
    			tickets.push("Murder");	
    		}
    		if(self.judgeTicketAM == true){
    			tickets.push("Attempted Murder");
    		}
    		if(self.judgeTicketSO == true){
    			tickets.push("Sexual Offences");
    		}
    		if(self.judgeTicketF == true){
    			tickets.push("Fraud");
    		}
    		if(self.judgeTicketHS == true){
    			tickets.push("Health and Safety");
    		}
    		if(judge.judgeType == 'High Court'){
    			tickets.push("Murder");	
    			tickets.push("Attempted Murder");
    			tickets.push("Sexual Offences");
    			tickets.push("Fraud");
    			tickets.push("Health and Safety");
    		}
    		
    		var params = {"fullname": judge.judgeName, "type": judge.judgeType, "isQC": judge.isQC, "isResident": judge.isResident,"judicialTickets":tickets};
            JudgeService.addJudge(params).then(function () {
                self.getJudges();
                self.adminForm.$setPristine();
                self.judge.judgeName = '';
                self.judge.judgeType = '';
                self.judge.isQC = false;
                self.judge.isResident = false;
                self.judgeSavedMessage = 'Saved...';
            }, function (error) {
                self.judgeSavedMessage = error.message;
            }).finally(function () {
                $timeout(function () {
                    self.judgeSavedMessage = null;
                }, self.delayTime);
            });
        };

        self.getCourtRooms = function () {
            CourtService.getCourtRooms(self.courtCenter).then(function (rooms) {
                self.rooms = rooms;
            }, function (error) {
                self.tempError = error;
            });
        };

        self.getJudicialOfficerTypes = function () {
            JudgeService.getJudicialOfficerType().then(function (judgeTypes) {
                self.judgeTypes = judgeTypes.data;
            }, function (error) {
                self.tempError = error;
            });
        };
        
        self.getJudicialOfficerTickets = function () {
    		JudgeService.getJudicialOfficerTickets().then(function (judgeTickets) {
    			//["Murder", "Attempted Murder", "Sexual Offences", "Fraud", "Health and Safety"]
    			console.log(judgeTickets.data);
    		}, function (error) {
    			self.tempError = error;
    		});
    	};

        self.addCourtRoom = function (courtRoomName) {
            var params = { "courtRoomName": courtRoomName, "courtCenterName": self.courtCenter };
            CourtService.addCourtRoom(params).then(function () {
                self.getCourtRooms();
                self.courtRoomName = '';
                self.courtRoomSavedMessage = 'Saved...';
            }, function (error) {
                self.courtRoomSavedMessage = error.message;
            }).finally(function () {
                $timeout(function () {
                    self.courtRoomSavedMessage = null;
                }, self.delayTime);
            });
        };

        self.getCourtCentres = function () {
            CourtService.getCourtCentres().then(function (centerList) {
                self.centerList = centerList;
            }, function (error) {
                self.tempError = error;
            });
        };

        self.addCourtCentre = function (court) {
            var params = { "courtCenterName": court.centreName, "code": court.centreCode };
            CourtService.addCourtCentre(params).then(function () {
                self.getCourtCentres();
                self.adminForm.$setPristine();
                self.court = '';
                self.courtCenterSavedMessage = 'Saved...';
            }, function (error) {
                self.courtCenterSavedMessage = error.message;
            }).finally(function () {
                $timeout(function () {
                    self.courtCenterSavedMessage = null;
                }, self.delayTime);
            });
        };

        self.uploadFile = function () {
            var file = self.fileName;
            self.fileUploadDisabled = true;
            FileService.uploadFile(file).then(function () {
                self.fileUploadDisabled = false;
                self.adminForm.$setPristine();
                self.refreshProcessingStatus();
                self.fileUploadSuccessMessage = 'File uploaded successfully.';
            }, function (error) {
                self.fileUploadDisabled = false;
                self.fileUploadSuccessMessage = error.message;
            }).finally(function () {
                $timeout(function () {
                    self.fileUploadSuccessMessage = null;
                }, self.delayTime);
            });

        };

        self.refreshProcessingStatus = function () {
            FileService.getProcessingStatus().then(function (response) {
                self.processingStatusList = response;
            }, function (error) {
                self.tempError = error;
            });
        };
    }

}());