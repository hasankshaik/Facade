package com.domain.courtcase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

import com.domain.room.Block;
import com.domain.room.BlockType;
import com.domain.room.Room;
import com.domain.room.Session;

public class Hearing {

	public Case casse;
	public Room courtRoom;
	public HearingType hearingType;
	public List<Block> blocks=new ArrayList<Block>();

	public Hearing(Case casse, Room courtRoom, HearingType hearingType) {
		super();
		this.casse = casse;
		this.courtRoom = courtRoom;
		this.hearingType = hearingType;
		if (hearingType == HearingType.PTP) {
			List<Session> sessions = courtRoom.getSessions(calculatePtpHearing(casse));
			for (Session session : sessions) {
				session.blocks.forEach((b) -> {
					if (b.blockType == BlockType.PTP) {
						blocks.add(b);
					}
				});
			}
		} else if (hearingType == HearingType.TRIAL) {
			List<Session> sessions = courtRoom.getSessions(calculateTrialHearing(casse));
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

	private Date calculatePtpHearing(Case caseForDef) {
		return DateUtils.addDays(caseForDef.dateOfSending, 28);
	}
	
	private Date calculateTrialHearing(Case caseForDef) {
		return DateUtils.addDays(caseForDef.dateOfSending, 182);
	}
}
