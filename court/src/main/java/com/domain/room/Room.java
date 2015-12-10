package com.domain.room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

public class Room {
	private int id;
	private String name;
	public List<Session> sessions = new ArrayList<Session>();

	public Room(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public void addSession(List<Session> sessions) {
		this.sessions.addAll(sessions);
	}

	public void removeSession(List<Session> sessions) {
		this.sessions.removeAll(sessions);
	}

	public List<Session> getSessions(Date date) {
		List<Session> sessionOnTheDay = new ArrayList<Session>();
		sessions.forEach((s) -> {
			if (DateUtils.isSameDay(s.sittingDate, date)) {
				sessionOnTheDay.add(s);
			}
		});
		return sessionOnTheDay;
	}
}
