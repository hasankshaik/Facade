package com.domain.room;

import java.util.Date;
import java.util.List;

import com.domain.panel.Judge;

public class Session {
	public Date sittingDate;
	public List<Block> blocks;
	public Judge judge;

	public Session(Date sittingDate) {
		super();
		this.sittingDate = sittingDate;
	}

	public void addBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}


	public void addJudge(Judge judge) {
		this.judge = judge;
	}

}
