/**
 * @ngdoc service
 * @name ccsApp.FinancialYearService
 *
 * @description
 * # FinancialYearService
 * Service in the ccsApp app.
 */
angular.module('ccsApp').factory('FinancialYearService', function($http, $q, $filter, SharedService, SittingsService, CourtService, JudgeService) {

	return {
		getYears : function () {
			var financialYears = [];
			var date = new Date(),
				iteration = 0,
				financialYearBegin = 0,
				financialYearEnd = 0;

			if (date.getMonth() > 2) { // if its in april change the financial year
				financialYearBegin = date.getFullYear();
				financialYearEnd = date.getFullYear() + 1;
			} else {
				financialYearBegin = date.getFullYear() - 1;
				financialYearEnd = date.getFullYear();
			}

			while(iteration < 5) {
				financialYears.push(financialYearBegin + "-" + financialYearEnd);
				financialYearBegin ++;
				financialYearEnd ++;
				iteration++;
			}
			return financialYears;
		},

		getFinancialYear : function(date) {
			if (date.getMonth() > 2) { // if its in april change the financial year
				var financialYearBegin = date.getFullYear();
				var financialYearEnd = date.getFullYear() + 1;
			} else {
				financialYearBegin = date.getFullYear() - 1;
				financialYearEnd = date.getFullYear();
			}
			var financialYear = financialYearBegin + "-" + financialYearEnd;

			return financialYear;
		},

		populateMonths : function (selectedFinancialYear) {
			var monthList = [];

			var date = new Date();
			date.setYear(selectedFinancialYear.split("-")[0]);
			date.setDate(1);
			date.setMonth(3);

			monthList.push(new Date(date));

			while (date.getMonth() !== 2) {
				date.setMonth(date.getMonth()+1);
				monthList.push(new Date(date));
			}
			return monthList;
		},

		populateDays : function(startDate, endDate) {
			var days = [];
			while (startDate <= endDate) {
				var weekDay = startDate.getDay();
				(weekDay !== 0 && weekDay !== 6) ? days.push(new Date(startDate)) : '' ;
				startDate.setDate(startDate.getDate() + 1);
			}
			return days;
		},

		renderCalendar : function(date, currentView, renderFull) {
			var calendar		= {};
			var currentDate		= date;
			var plannerStart	= new Date(date);
			var plannerEnd		= new Date(plannerStart);
			var financialYear	= this.getFinancialYear(date);

			if (currentDate === "Invalid Date") { return; }
			if (currentView.value > 1) { plannerStart.setDate(date.getDate() - date.getDay() + 1); }
		
			plannerEnd.setTime(plannerStart.getTime() + ( currentView.value-1) * 86400000);

			calendar.populateSittings				= SittingsService.populateSittings(SharedService.courtCenter, plannerStart, plannerEnd);
			calendar.populateDays					= this.populateDays(plannerStart, plannerEnd);
			calendar.populateAnnualActualSitting	= SittingsService.populateAnnualActualSitting(SharedService.courtCenter, date);
			calendar.getSittings					= SittingsService.getSittings(SharedService.courtCenter, financialYear);
			
			if(renderFull) {
				calendar.getCourtRooms				= CourtService.getCourtRooms(SharedService.courtCenter);
				calendar.getListOfJudges			= JudgeService.getListOfJudges();
			} 
			
			return $q.all(calendar).then(function (response) {
				return response;
			}, function (error) {
				q.reject(error);
			});
		}
	};
});
