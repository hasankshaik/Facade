package com.domain.room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

public class Hearing {

	public String caseId;
	public Room courtRoom;
	public HearingType hearingType;
	public List<Block> blocks=new ArrayList<Block>();

	public Hearing(String caseId,Date dateOfSending, Room courtRoom, HearingType hearingType) {
		super();
		this.caseId = caseId;
		this.courtRoom = courtRoom;
		this.hearingType = hearingType;
		if (hearingType == HearingType.PTP) {
			List<Session> sessions = courtRoom.getSessions(calculatePtpHearing(dateOfSending));
			for (Session session : sessions) {
				session.blocks.forEach((b) -> {
					if (b.blockType == BlockType.PTP) {
						blocks.add(b);
					}
				});
			}
		} else if (hearingType == HearingType.TRIAL) {
			List<Session> sessions = courtRoom.getSessions(calculateTrialHearing(dateOfSending));
			for (Session session : sessions) {
				session.blocks.forEach((b) -> {
					if (b.blockType == BlockType.TRIAL) {
						blocks.add(b);
					}
				});
			}
		} else {
			throw new RuntimeException();
		}
		Validate.notEmpty(blocks);
	}

	private Date calculatePtpHearing(Date caseForDef) {
		return DateUtils.addDays(caseForDef, 28);
	}
	
	private Date calculateTrialHearing(Date caseForDef) {
		return DateUtils.addDays(caseForDef, 182);
	}
}
