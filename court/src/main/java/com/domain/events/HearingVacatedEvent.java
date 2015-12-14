package com.domain.events;

import com.domain.courtcase.Case;

public class HearingVacatedEvent {
	public Case casehearing;

	public HearingVacatedEvent(Case casehearing) {
		super();
		this.casehearing = casehearing;
	}
}
