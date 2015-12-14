package com.domain.courtcase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.domain.room.Room;

public class Case {
	public String caseId;
	public Date dateOfSending;
	public List<Hearing> listOfHearing = new ArrayList<Hearing>();

	public Case(String caseId) {
		super();
		this.caseId = caseId;
	}

	public void addDateOfSending(Date dateOfSending) {
		this.dateOfSending = dateOfSending;
	}

	public Hearing scheduleHearing( Room room ,HearingType hearingType) {
		Hearing hearing = new Hearing(this , room, hearingType);
		listOfHearing.add(hearing);
		return hearing;
	}

	public void vacateHearing( Room room ,HearingType hearingType) {
		List<Hearing> toBeRemoved = new ArrayList<Hearing>();
		listOfHearing.forEach(h -> {
			if (h.hearingType == hearingType && h.courtRoom == room) {
				toBeRemoved.add(h);
			}
		});
		if (toBeRemoved .size()>0) {
			listOfHearing.removeAll(toBeRemoved);
		}
	}

}
