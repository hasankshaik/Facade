package com.domain.events;

import com.domain.room.Hearing;


public class HearingScheduledEvent {
	public Hearing hearing;

	public HearingScheduledEvent(Hearing hearing) {
		super();
		this.hearing = hearing;
	}

}

