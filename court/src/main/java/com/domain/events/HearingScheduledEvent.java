package com.domain.events;

import com.domain.room.Hearing;


public class HearingScheduledEvent {
	public Hearing caseHearing;

	public HearingScheduledEvent(Hearing caseHearing) {
		super();
		this.caseHearing = caseHearing;
	}

}

