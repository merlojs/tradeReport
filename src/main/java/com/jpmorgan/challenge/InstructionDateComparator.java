package com.jpmorgan.challenge;

import java.util.Comparator;
import java.util.Date;

public class InstructionDateComparator implements Comparator<Trade>{

	@Override
	public int compare(Trade t1, Trade t2) {
		Date date1 = t1.getInstructionDate();
		Date date2 = t2.getInstructionDate();

		// ascending order
		return date1.compareTo(date2);
	}

}
