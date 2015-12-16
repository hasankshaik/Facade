package com.domain.room;

import java.util.ArrayList;
import java.util.List;


public class Block {
	public BlockType blockType;

	 private List<Hearing> bookedHearings = new ArrayList<Hearing>();

	public Block(BlockType blockType) {
		super();
		this.blockType = blockType;
	}

	 public void bookHearing(Hearing e) {
	 this.bookedHearings.add(e);
	 }
	
	 public void unBookHearing(Hearing e) {
	 this.bookedHearings.remove(e);
	 }
}
