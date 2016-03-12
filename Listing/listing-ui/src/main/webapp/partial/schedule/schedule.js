/**
 * @ngdoc function
 * @name ccsApp.controller:DiaryController
 * @description
 * # DiaryController
 * Controller of the ccsApp app
 */
(function () {
    'use strict';

    angular
        .module('ccsApp')
        .controller('DiaryController', DiaryController);

    function DiaryController($scope, $q, $filter, $timeout, $location, $modal, SharedService,
        SittingsService, HearingService, JudgeService, FinancialYearService, SessionService) {

        var self = this;
        self.courtCentre = $location.search()['courtCenter'] || "Birmingham";
        self.selectedFinancialYear = FinancialYearService.financialYears;
        self.views = [{ label: 'Agenda', value: 1 }, { label: 'Week', value: 7 }, { label: '4 Weeks', value: 28, cssClass: 'frozen-table-header' }];
        self.currentView = self.views[1];

        var dateUtility = function (date) {
            return new Date(date.setHours(0, 0, 0, 0)); //Setting Hours, Minutes and Seconds to zero
        };

        var renderCalendar = function (date, renderFull) {
            FinancialYearService.renderCalendar(date, self.currentView, renderFull).then(function (response) {
                self.roomList = response.populateSittings; 			// For loading roomList with Judges & Hearing Types: 	RHS
                self.days = response.populateDays; 	 			// For loading the calendar: 							RHS
                self.annualActualSittingDays = response.populateAnnualActualSitting; // For loading Actual sittings days: 					RHS
                self.sittings = response.getSittings; 				// For loading Annual Target sitting days: 				RHS

                if (renderFull) {
                    self.courtRoomList = response.getCourtRooms; 				// For loading Court Rooms: 							LHS
                    self.listJudges = response.getListOfJudges; 			// For loading list of Judges: 							LHS
                }
            }, function (error) {
                self.tempError = error;
            }).finally(function () {
                self.currentDate = $filter('date')(date, 'yyyy-MM-dd');
            });
        };

        self.resetView = function () {
            var date = new Date();
            self.sessionDeAllocError = '';
            self.sessionDeAllocSucceed = '';
            self.editEstimateSucceed = '';
            renderCalendar(date, true);
        };

        self.next = function () {
            var parsedDate = Date.parse(self.currentDate);
            var date = new Date(parsedDate);
            var currentDate = new Date(self.currentDate);
            date.setDate(currentDate.getDate() + self.currentView.value);
            renderCalendar(date, false);
        };

        self.prev = function () {
            var parsedDate = Date.parse(self.currentDate);
            var date = new Date(parsedDate);
            var currentDate = new Date(self.currentDate);
            date.setDate(currentDate.getDate() - self.currentView.value);
            renderCalendar(date, false);
        };

        self.today = function () {
            renderCalendar(new Date(), false);
        };

        self.navigateToDate = function () {
            var parsedDate = dateUtility(new Date(Date.parse(self.currentDate)));
            if (parsedDate === 'Invalid Date') {
                self.invalidDate = true;
                return false;
            } else {
                self.invalidDate = false;
                renderCalendar(parsedDate, true);
            }
        };

        self.onUnScheduledHearingSelection = function () {
            self.selectedHearing = angular.copy(self.selectedUnscheduledHearing);
            self.showUnScheduledHearingFlag = true;
            self.showEditEstimateFlag = true;
            self.editTrialEstimate = self.selectedUnscheduledHearing.trialEstimate;
            self.showHearingInfoFlag = true;
            self.showScheduledHearingFlag = false;
        };

        self.showScheduledHearingDetails = function (hearing) {
            HearingService.showScheduledHearingDetails(hearing).then(function (response) {
                self.scheduledHearing = response;
                self.selectedHearing = response;
                self.scheduledHearingStartDate = new Date(self.scheduledHearing.startDateForSlot);
                self.showUnScheduledHearingFlag = false;
                self.showEditEstimateFlag = false;
                self.showHearingInfoFlag = true;
                self.showScheduledHearingFlag = true;
                self.selectedUnscheduledHearing = "";
            }, function (error) {
                self.tempError = error;
            });
            $scope.$broadcast('caseNumber', hearing.hearingCase);
        };

        self.moveDatesForListing = function (startDate) {
            renderCalendar(new Date(startDate), false);
        };

        self.updateHearing = function () {
            var params = {
                "hearingCase": self.scheduledHearing.hearingCase,
                "hearingName": self.scheduledHearing.hearingName,
                "hearingKey": self.scheduledHearing.hearingKey,
                "startDateForSlot": self.scheduledHearingStartDate,
                "trialEstimate": self.scheduledHearing.trialEstimate,
                "bookingType": self.scheduledHearing.bookingType,
                "courtRoomName": self.scheduledHearing.courtRoomName,
                "hearingNote": self.scheduledHearing.hearingNote,
                "active": self.scheduledHearing.active
            };
            HearingService.updateHearing(params).then(function () {
                self.editHearingMessage = 'Hearing is Listed';
                self.showScheduledHearingDetails(params);
                renderCalendar(new Date(self.scheduledHearingStartDate), false);
            }, function (error) {
                self.editHearingMessage = error.message;
            }).finally(function () {
                $timeout(function () {
                    self.editHearingMessage = null;
                }, SharedService.timerDelay);
            });
        };

        var getUnlistedHearings = function () {
            HearingService.getUnlistedHearings().then(function (response) {
                self.unlistedHearings = response;
                self.selectedUnscheduledHearing = '';
            }, function (error) {
                self.tempError = error;
            });
        };

        self.listHearing = function () {
            var params = {
                "hearingCase": self.selectedUnscheduledHearing.hearingCase,
                "hearingName": self.selectedUnscheduledHearing.hearingName,
                "hearingKey": self.selectedUnscheduledHearing.hearingKey,
                "startDateForSlot": self.hearingStartDate,
                "trialEstimate": self.selectedUnscheduledHearing.trialEstimate,
                "bookingType": self.selectedUnscheduledHearing.bookingType,
                "courtRoomName": self.selectedUnscheduledHearing.courtRoomName,
                "active": self.selectedUnscheduledHearing.active
            };
            HearingService.listHearing(params).then(function (response) {
                self.scheduledHearing = response;
                self.selectedHearing = response;
                self.scheduledHearingStartDate = new Date(response.startDateForSlot);
                self.showUnScheduledHearingFlag = false;
                self.showEditEstimateFlag = false;
                self.showHearingInfoFlag = true;
                self.showScheduledHearingFlag = true;
                self.listHearingMessage = 'Hearing has been listed';
                getUnlistedHearings();
                renderCalendar(new Date(response.startDateForSlot), false);
            }, function (error) {
                self.listHearingMessage = error.message;
            }).finally(function () {
                $timeout(function () {
                    self.listHearingMessage = null;
                }, SharedService.timerDelay);
            });
        };

        self.unListHearing = function () {
            var msg = "Confirm that " + self.selectedHearing.hearingName + " starting on " + self.selectedHearing.startDate + " should be un-listed";
            $modal.open({
                templateUrl: 'partial/alertDialog/alertDialog.html',
                controller: 'UnlistHearingCtrl',
                resolve: {
                    bodyMessage: function () { return msg; },
                    hearingKey: function () { return self.selectedHearing.hearingKey; }
                }
            }).result.then(function () {
            }, function (message) {
                if (message && message.indexOf("unlist") > 0) {
                    self.selectedHearing = '';
                    self.showUnScheduledHearingFlag = false;
                    self.showEditEstimateFlag = false;
                    self.showHearingInfoFlag = false;
                    self.showScheduledHearingFlag = false;
                    getUnlistedHearings();
                    self.resetView();
                }
                $timeout(function () {
                    $scope.defendantUpdatedMessage = null;
                }, SharedService.timerDelay);
            });
        };

        self.editEstimate = function () {
            self.selectedUnscheduledHearing.trialEstimate = self.editTrialEstimate;
            HearingService.editEstimate(self.selectedUnscheduledHearing).then(function (response) {
                if (response.errorMessage !== null) {
                    self.editEstimateMessage = response.errorMessage;
                } else {
                    self.editEstimateMessage = 'Estimate Saved';
                }
                self.onUnScheduledHearingSelection();
            }, function (error) {
                self.tempError = error;
            }).finally(function () {
                $timeout(function () {
                    self.editEstimateMessage = null;
                }, SharedService.timerDelay);
            });
        };

        self.allocateJudge = function () {
            var params = {
                "judgeName": self.judgeNameSelected,
                "courtRoomName": self.courtRoomNameSelected,
                "sittingStartDate": self.courtRoomStartDate,
                "sittingEndDate": self.courtRoomEndDate
            };
            JudgeService.allocateJudge(params).then(function () {
                self.navigateToDate();
                self.judgeNameSelected = '';
                self.courtRoomNameSelected = '';
                self.courtRoomStartDate = '';
                self.courtRoomEndDate = '';
                self.sessionAllocMessage = 'Judge has been allocated';
            }, function (error) {
                self.sessionAllocMessage = error.message;
            }).finally(function () {
                $timeout(function () {
                    self.sessionAllocMessage = null;
                }, SharedService.timerDelay);
            });
        };

        self.deallocateJudge = function () {
            var params = {
                "judgeName": self.judgeNameSelectedDeAlloc,
                "courtRoomName": self.courtRoomNameSelectedDeAlloc,
                "sittingStartDate": self.courtRoomStartDateDeAlloc,
                "sittingEndDate": self.courtRoomEndDateDeAlloc
            };
            JudgeService.deallocateJudge(params).then(function () {
                self.navigateToDate();
                self.sessionDeAllocError = '';
                self.judgeNameSelectedDeAlloc = '';
                self.courtRoomNameSelectedDeAlloc = '';
                self.courtRoomStartDateDeAlloc = '';
                self.courtRoomEndDateDeAlloc = '';
                self.sessionDeAllocMessage = 'Judge no longer allocated to these sessions';
            }, function (error) {
                self.sessionDeAllocMessage = error.message;
            }).finally(function () {
                $timeout(function () {
                    self.sessionDeAllocMessage = null;
                }, SharedService.timerDelay);
            });
        };

        self.manageSessions = function (closed) {
            var params = {
                "courtRoomNames": self.managesession.courtRoomNameSelected,
                "sittingStartDate": self.managesession.courtRoomStartDate,
                "sittingEndDate": self.managesession.courtRoomEndDate,
                "closed": closed
            };

            var resetManagesessionsParams = function () {
                self.managesession.courtRoomStartDate = null;
                self.managesession.courtRoomNameSelected = null;
                self.managesession.courtRoomEndDate = null;
                params = null;
            };

            if (closed === true) {
                SessionService.managesessions(params).then(function () {
                    self.manageSessionCloseMessage = 'Session closed.';
                    self.navigateToDate();
                    resetManagesessionsParams();
                }, function (error) {
                    self.manageSessionCloseMessage = error.message;
                }).finally(function () {
                    $timeout(function () {
                        self.manageSessionCloseMessage = null;
                    }, SharedService.timerDelay);
                });
            } else {
                SessionService.managesessions(params).then(function () {
                    self.manageSessionOpenMessage = 'Session open.';
                    self.navigateToDate();
                    resetManagesessionsParams();
                }, function (error) {
                    self.manageSessionOpenMessage = error.message;
                }).finally(function () {
                    $timeout(function () {
                        self.manageSessionOpenMessage = null;
                    }, SharedService.timerDelay);
                });
            }
        };

        self.openDialog = function () {
            $modal.open({
                templateUrl: 'partial/hearingSlots/hearingSlots.html',
                controller: 'HearingslotsCtrl',
                resolve: {
                    courtCenter: function () { return self.courtCentre; },
                    hearing: function () { return self.selectedHearing; },
                    overbook: function () { return self.overbook; },
                    selected: function () { return self.selectedSlot; }
                }
            }).result.then(function (resultString) {
                var slot = angular.fromJson(resultString.slot);
                self.selectedSlot = slot;
                self.currentDate = new Date(slot.slotDate);
                self.navigateToDate();

                if (resultString.open === 'list') {
                    self.selectedUnscheduledHearing.courtRoomName = slot.roomName;
                    self.hearingStartDate = new Date(slot.slotDate);
                    self.selectedUnscheduledHearing.bookingType = resultString.bookingType;
                    self.listHearing();
                }
            }, function () {
                console.info('Modal dismissed at: ' + new Date());
            });
        };

        var getSessionBlockType = function () {
            HearingService.getSessionBlockType().then(function (response) {
                self.sessionBlockTypeList = response.data;
            }, function (error) {
                self.tempError = error;
            });
        };


        self.manageBlocks = function () {
            var params = {
                "blockType": self.blockTypeSelected,
                "courtRoomName": self.courtRoomNameSelectedManageBlocks,
                "sittingStartDate": self.courtRoomStartDateManageBlocks,
                "sittingEndDate": self.courtRoomEndDateManageBlocks
            };
            JudgeService.manageBlocks(params).then(function () {
                self.blockTypeSelected = '';
                self.courtRoomNameSelectedManageBlocks = '';
                self.courtRoomStartDateManageBlocks = '';
                self.courtRoomEndDateManageBlocks = '';
                self.manageBlocksMessage = 'Block allocated successfully';
                self.navigateToDate();
            }, function (error) {
                self.manageBlocksMessage = error.message;
                self.navigateToDate();
            }).finally(function () {
                $timeout(function () {
                    self.manageBlocksMessage = null;
                }, SharedService.timerDelay);
            });
        };

        self.deleteBlocks = function () {
            var params = {
                "blockType": self.blockTypeSelected,
                "courtRoomName": self.courtRoomNameSelectedManageBlocks,
                "sittingStartDate": self.courtRoomStartDateManageBlocks,
                "sittingEndDate": self.courtRoomEndDateManageBlocks
            };
            JudgeService.deleteBlocks(params).then(function () {
                self.blockTypeSelected = '';
                self.courtRoomNameSelectedManageBlocks = '';
                self.courtRoomStartDateManageBlocks = '';
                self.courtRoomEndDateManageBlocks = '';
                self.manageBlocksMessage = 'Block deleted successfully';
                self.navigateToDate();
            }, function (error) {
                self.manageBlocksMessage = error.message;
                self.navigateToDate();
            }).finally(function () {
                $timeout(function () {
                    self.manageBlocksMessage = null;
                }, SharedService.timerDelay);
            });
        };

        var getPchmHeringsForAction = function () {
            HearingService.getPchmHeringsForAction().then(function (response) {
                self.pcmhTomorrowList = response.future;
                self.pcmhTodayList = response.current;
                self.pcmhPastList = response.past;
            });
        };

        self.updatePcmhHearing = function (pcmhHeraing, status) {

            var hearingWebForPcmh = {
                hearingStatus: status,
                hearingKey: pcmhHeraing.hearingKey
            };

            HearingService.updatePcmhHearing(hearingWebForPcmh).then(function () {
                getPchmHeringsForAction();
                self.pcmhEditSuccess = 'Hearing saved';
            }, function (error) {
                self.tempError = error;
            });
        };

        self.init = function () {
            self.currentDate = dateUtility(new Date());
            self.resetView();
            getUnlistedHearings();
            getSessionBlockType();
            getPchmHeringsForAction();
        };

    }
}());
