package com.domain.commands;

import com.domain.courtcase.HearingType;
import com.domain.room.Room;

public class SchedulingHearing {
	private Room room;
	private HearingType hearingType;

	public SchedulingHearing(Room room, HearingType hearingType) {
		super();
		this.room = room;
		this.hearingType = hearingType;
	}

}
