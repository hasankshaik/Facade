package com.domain.commands;

import java.util.Date;

import com.domain.room.HearingType;
import com.domain.room.Room;

public class VacateHearingCommand {
	public VacateHearingCommand(String caseId, Date dateOfSending,
			Room room, HearingType hearingType) {
		super();
		this.caseId = caseId;
		this.hearingType = hearingType;
	}
	public String caseId;
	public HearingType hearingType;
}
