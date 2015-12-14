package com.domain.commands;

import com.domain.courtcase.HearingType;
import com.domain.room.Room;

public class VacateHearing {
	private Room room;
	private HearingType hearingType;

	public VacateHearing(Room room, HearingType hearingType) {
		super();
		this.room = room;
		this.hearingType = hearingType;
	}
}
