package com.domain.commands;

import java.util.Date;

import com.domain.room.HearingType;
import com.domain.room.Room;

public class SchedulingHearingCommand {
	public SchedulingHearingCommand(String caseId, Date dateOfSending,
			Room room, HearingType hearingType) {
		super();
		this.caseId = caseId;
		this.dateOfSending = dateOfSending;
		this.room = room;
		this.hearingType = hearingType;
	}
	public String caseId;
	public Date dateOfSending;
	public Room room;
	public HearingType hearingType;


}
