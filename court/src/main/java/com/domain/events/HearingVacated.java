package com.domain.events;

import com.domain.courtcase.Hearing;

public class HearingVacated {
	private Hearing hearing;

	public HearingVacated(Hearing hearing) {
		super();
		this.hearing = hearing;
	}
}
