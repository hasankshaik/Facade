package com.domain.commands;

import com.domain.courtcase.HearingType;
import com.domain.room.Room;

public class VacateHearingCommand {
	public String caseId;
	public Room room;
	public HearingType hearingType;

	public VacateHearingCommand(String caseId, Room room, HearingType hearingType) {
		super();
		this.caseId = caseId;
		this.room = room;
		this.hearingType = hearingType;
	}
}
