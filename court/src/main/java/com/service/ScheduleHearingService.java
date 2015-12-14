package com.service;

import com.domain.commands.SchedulingHearingCommand;
import com.domain.commands.VacateHearingCommand;
import com.domain.courtcase.Case;
import com.domain.db.InMemoryDB;
import com.domain.events.HearingScheduledEvent;

public class ScheduleHearingService {

	public static HearingScheduledEvent scheduleHearingForCase(SchedulingHearingCommand schedulingHearing) {
		Case casse = InMemoryDB.listCase.stream()
				.filter(c -> c.caseId == schedulingHearing.caseId).findFirst()
				.orElse(new Case(schedulingHearing.caseId));
		return new HearingScheduledEvent(casse.scheduleHearing(schedulingHearing.room,
				schedulingHearing.hearingType));

	}

	public static void vacateHearingForCase(VacateHearingCommand vacateHearing) {
		Case casse = InMemoryDB.listCase.stream()
				.filter(c -> c.caseId == vacateHearing.caseId).findFirst()
				.orElse(null);
		if (casse != null) {
			casse.vacateHearing(vacateHearing.room,
					vacateHearing.hearingType);
		}

	}

}
