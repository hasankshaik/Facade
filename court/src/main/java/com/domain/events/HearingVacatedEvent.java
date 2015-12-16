package com.domain.events;

import com.domain.room.Hearing;


public class HearingVacatedEvent {
	public Hearing casehearing;

	public HearingVacatedEvent(Hearing casehearing) {
		super();
		this.casehearing = casehearing;
	}
}
