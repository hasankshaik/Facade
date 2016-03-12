package com.domain.room;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

public class Hearing {

	public String caseId;
	public HearingType hearingType;

	public Hearing(String caseId,Date dateOfSending, Room courtRoom, HearingType hearingType) {
		super();
		this.caseId = caseId;
		this.hearingType = hearingType;
		if (hearingType == HearingType.PTP) {
			List<Session> sessions = courtRoom.getSessions(calculatePtpHearing(dateOfSending));
			for (Session session : sessions) {
				session.blocks.forEach((b) -> {
					if (b.blockType == BlockType.PTP) {
						b.bookHearing(this);
					}
				});
			}
		} else if (hearingType == HearingType.TRIAL) {
			List<Session> sessions = courtRoom.getSessions(calculateTrialHearing(dateOfSending));
			for (Session session : sessions) {
				session.blocks.forEach((b) -> {
					if (b.blockType == BlockType.TRIAL) {
						b.bookHearing(this);
					}
				});
			}
		} else {
			throw new RuntimeException();
		}
//		Validate.notEmpty(blocks);
	}

	private Date calculatePtpHearing(Date caseForDef) {
		return DateUtils.addDays(caseForDef, 28);
	}
	
	private Date calculateTrialHearing(Date caseForDef) {
		return DateUtils.addDays(caseForDef, 182);
	}
}
