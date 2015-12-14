package com.domain.events;

import com.domain.courtcase.Case;

public class HearingScheduledEvent {
	public Case caseHearing;

	public HearingScheduledEvent(Case caseHearing) {
		super();
		this.caseHearing = caseHearing;
	}

}

