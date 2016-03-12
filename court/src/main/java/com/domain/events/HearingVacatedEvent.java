package com.domain.events;

import com.domain.room.Hearing;


public class HearingVacatedEvent {
	public Hearing hearing;

	public HearingVacatedEvent(Hearing hearing) {
		super();
		this.hearing = hearing;
	}
}
