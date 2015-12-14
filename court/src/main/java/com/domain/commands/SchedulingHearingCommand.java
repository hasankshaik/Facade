package com.domain.commands;

import com.domain.courtcase.HearingType;
import com.domain.room.Room;

public class SchedulingHearingCommand {
	public String caseId;
	public Room room;
	public HearingType hearingType;

	public SchedulingHearingCommand(String caseId, Room room, HearingType hearingType) {
		super();
		this.caseId = caseId;
		this.room = room;
		this.hearingType = hearingType;
	}

}
