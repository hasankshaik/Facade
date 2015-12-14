package com.domain.service;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import com.domain.commands.SchedulingHearingCommand;
import com.domain.commands.VacateHearingCommand;
import com.domain.courtcase.Case;
import com.domain.courtcase.HearingType;
import com.domain.db.InMemoryDB;
import com.domain.events.HearingScheduledEvent;
import com.domain.events.HearingVacatedEvent;
import com.domain.panel.Judge;
import com.domain.room.Block;
import com.domain.room.BlockType;
import com.domain.room.Room;
import com.domain.room.Session;
import com.service.ScheduleHearingService;

public class ScheduleTest {

	/*
	 * When date of sending is added to the case a ptp hearing is booked on the
	 * calculated hearing date
	 * 
	 * When all the documents are available before booked ptp hearing date we
	 * vacate ptp hearing
	 * 
	 * And book a trail hearing which will be first available date from 28 days
	 * before kpi date
	 */

	@Test
	public void test() {
		// SetUp
		Block ptpBlock = new Block(BlockType.PTP);
		Session session = new Session(DateUtils.addDays(new Date(), 28));
		session.addBlocks(Arrays.asList(ptpBlock));
		session.addJudge(new Judge("HHJ", 2000));

		Block trialBlock = new Block(BlockType.TRIAL);
		Session trialSession = new Session(DateUtils.addDays(new Date(), 182));
		trialSession.addBlocks(Arrays.asList(trialBlock));
		trialSession.addJudge(new Judge("HHJ", 2000));

		Room room = new Room(100, "RoomA");
		room.addSession(Arrays.asList(session, trialSession));

		// Given when case exist and has the date of sending
		Case caseForDef = new Case("T300");
		caseForDef.addDateOfSending(new Date());
		InMemoryDB.listCase.add(caseForDef);

		// Then PTP Hearing is booked on calculated date
		HearingScheduledEvent hearingScheduledEvent =  ScheduleHearingService.scheduleHearingForCase(new SchedulingHearingCommand("T300", room, HearingType.PTP));
		Assert.assertTrue(hearingScheduledEvent.caseHearing.listOfHearing.size() == 1);

		// When all the documents are available before booked ptp hearing date
		// we vacate ptp hearing
		HearingVacatedEvent hearingVacatedEvent=ScheduleHearingService.vacateHearingForCase(new VacateHearingCommand("T300", room, HearingType.PTP));
		Assert.assertTrue(hearingVacatedEvent.casehearing.listOfHearing.size() == 0);

		// And book a trail hearing which will be first available date from 28
		// days before kpi date
		hearingScheduledEvent =  ScheduleHearingService.scheduleHearingForCase(new SchedulingHearingCommand("T300", room, HearingType.TRIAL));
		Assert.assertTrue(hearingScheduledEvent.caseHearing.listOfHearing.size() == 1);

	}
}
