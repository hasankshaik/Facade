package com.service;

import com.domain.commands.SchedulingHearingCommand;
import com.domain.commands.VacateHearingCommand;
import com.domain.events.HearingScheduledEvent;
import com.domain.events.HearingVacatedEvent;
import com.domain.room.Hearing;

public class ScheduleHearingService {

	public static HearingScheduledEvent scheduleHearingForCase(SchedulingHearingCommand schedulingHearing) {
		Hearing casse = new Hearing(schedulingHearing.caseId, schedulingHearing.dateOfSending, schedulingHearing.room, schedulingHearing.hearingType);
		return new HearingScheduledEvent(casse);

	}

	public static HearingVacatedEvent vacateHearingForCase(
			VacateHearingCommand schedulingHearing) {
		//find hearing
		Hearing casse = new Hearing(schedulingHearing.caseId, schedulingHearing.dateOfSending, schedulingHearing.room, schedulingHearing.hearingType);
		//remove hearing from block
		return new HearingVacatedEvent(casse);

	}

}
