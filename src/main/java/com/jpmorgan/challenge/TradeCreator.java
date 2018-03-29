package com.jpmorgan.challenge;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class TradeCreator {

	public static final int MAX_UNITS = 999;
	public static final double MAX_FX = 10;
	public static final double MAX_PRICE = 999.99;
	public static final int MAX_YEAR = 2019;
	public static final int MAX_MONTH = 11;
	public static final int MAX_DAY_DIFF = 90;

	public Trade createTrade() {

		Currency currency = getRandomFromEnum(Currency.class);
		Date instructionDate = getRandomInstructionDate(currency);

		Trade trade = new Trade(getRandomFromEnum(Entity.class), getRandomFromEnum(Instruction.class),
				getRandomDouble(MAX_FX), currency, instructionDate,
				getRandomSettlementDate(instructionDate), getRandomInteger(MAX_UNITS), getRandomDouble(MAX_PRICE));

		return trade;
	}

	public Double getRandomDouble(Double max) {
		return ThreadLocalRandom.current().nextDouble(1, max);
	}

	public Date getRandomInstructionDate(Currency currency) {
		int year, month, day;

		/* Current Values (Today) */
		Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DAY_OF_MONTH);

		/* Update with random generated within range */
		year = ThreadLocalRandom.current().nextInt(year, MAX_YEAR);
		month = ThreadLocalRandom.current().nextInt(month, MAX_MONTH + 1);
		day = ThreadLocalRandom.current().nextInt(1, Calendar.getInstance().getActualMaximum(month) + 1);

		cal.set(year, month, day);
		while(!isWorkingDay(cal, currency)) {
			cal.add(Calendar.DATE, 1);
		}
		
		Date instructionDate = cal.getTime();

		return instructionDate;
	}

	public Date getRandomSettlementDate(Date instructionDate) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(instructionDate);
		Date settlementDate = new Date();

		cal.add(Calendar.DATE, ThreadLocalRandom.current().nextInt(1, MAX_DAY_DIFF + 1));
		if (cal.get(Calendar.YEAR) <= MAX_YEAR) {
			settlementDate = cal.getTime();
		} else {
			settlementDate = instructionDate;
		}
		return settlementDate;
	}

	public Integer getRandomInteger(Integer max) {
		return ThreadLocalRandom.current().nextInt(1, max + 1);
	}

	public static boolean isWorkingDay(Calendar cal, Currency curr) {
		switch (curr) {
		case AED:
		case SAR:
			if (cal.get(Calendar.DAY_OF_WEEK) == 6 || 
				cal.get(Calendar.DAY_OF_WEEK) == 7) {				
				return false;
			} else {
				return true;
			}
		default:
			if (cal.get(Calendar.DAY_OF_WEEK) == 7 || 
				cal.get(Calendar.DAY_OF_WEEK) == 1) {
				return false;				
			} else {
				return true;
			}
		}
	}

	public static <T extends Enum<?>> T getRandomFromEnum(Class<T> clazz) {
		int x = ThreadLocalRandom.current().nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}
}
