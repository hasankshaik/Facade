package com.domain.service;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import com.domain.courtcase.Case;
import com.domain.courtcase.Hearing;
import com.domain.courtcase.HearingType;
import com.domain.panel.Judge;
import com.domain.room.Block;
import com.domain.room.BlockType;
import com.domain.room.Room;
import com.domain.room.Session;

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

		// Given when case exist
		Case caseForDef = new Case("T300");

		// When I add the date of sending to a case
		caseForDef.addDateOfSending(new Date());

		// Then PTP Hearing is booked on calculated date
		Hearing hearing = caseForDef.scheduleHearingForCase(room, HearingType.PTP);
		Assert.assertTrue(hearing.blocks.size() == 1);

		// When all the documents are available before booked ptp hearing date
		// we vacate ptp hearing
		caseForDef.vacateHearingForCase(room, HearingType.PTP);
		Assert.assertTrue(caseForDef.listOfHearing.size() == 0);

		// And book a trail hearing which will be first available date from 28
		// days before kpi date
		hearing = caseForDef.scheduleHearingForCase(room, HearingType.TRIAL);
		Assert.assertTrue(hearing.blocks.size() == 1);

	}
}
