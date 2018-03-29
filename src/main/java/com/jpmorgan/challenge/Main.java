package com.jpmorgan.challenge;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args) {

		List<Trade> tradeList = new ArrayList<Trade>(); // All trades

		TradeCreator creator = new TradeCreator();
		InstructionDateComparator comp = new InstructionDateComparator();

		/* Create 100 trades for testing purposes */
		for (int i = 0; i < 100; i++) {
			tradeList.add(creator.createTrade());
		}

		DecimalFormat decFormat = new DecimalFormat("#.00");
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

		/* Report 1: Output All Trades */

		TableBuilder tb1 = new TableBuilder();
		tb1.addRow("Entity", "Buy/Sell", "Agreed Fx", "Currency", "Instruction Date", "Settlement Date", "Units",
				"Price Per Unit", "USD Amount");
		tb1.addRow("------", "--------", "----------", "--------", "----------------", "---------------", "-----",
				"--------------", "----------");

		/* Sort trade log by instruction date */
		Collections.sort(tradeList, comp);

		for (Trade trade : tradeList) {

			tb1.addRow(trade.getEntity().toString(), trade.getInstruction().toString(),
					decFormat.format(trade.getAgreedFx()), trade.getCurrency().toString(),
					dateFormat.format(trade.getInstructionDate()), dateFormat.format(trade.getSettlementDate()),
					trade.getUnits().toString(), decFormat.format(trade.getPricePerUnit()),
					decFormat.format(trade.getUSDAmount()));
		}


		/* Report 2: Daily USD Settled Incoming */

		TableBuilder tb2 = new TableBuilder();
		tb2.addRow("Instruction Date", "USD Amount");
		tb2.addRow("----------------", "----------");

		Map<String, Double> purchaseReport = tradeList.stream()
				.filter(trade -> trade.getInstruction() == Instruction.BUY)
				.sorted(Comparator.comparing(trade -> trade.getInstructionDate()))
				.collect(Collectors.groupingBy(trade -> dateFormat.format(trade.getInstructionDate()),
						LinkedHashMap::new,
						Collectors.summingDouble(trade -> trade.getUSDAmount())));
		
		MapUtil.orderByDate(purchaseReport);
		
		for (String date : purchaseReport.keySet()) {
			String key = date;
			String value = decFormat.format(purchaseReport.get(date));
			tb2.addRow(key, value);
		}

		/* Report 3: Daily USD Settled Outgoing */

		TableBuilder tb3 = new TableBuilder();
		tb3.addRow("Instruction Date", "USD Amount");
		tb3.addRow("----------------", "----------");
		
		Map<String, Double> salesReport = tradeList.stream()
				.filter(trade -> trade.getInstruction() == Instruction.SELL)
				.sorted(Comparator.comparing(trade -> trade.getInstructionDate()))
				.collect(Collectors.groupingBy(trade -> dateFormat.format(trade.getInstructionDate()),
						LinkedHashMap::new,
						Collectors.summingDouble(trade -> trade.getUSDAmount())));
		
		MapUtil.orderByDate(salesReport);

		for (String date : salesReport.keySet()) {
			String key = date;
			String value = decFormat.format(salesReport.get(date));
			tb3.addRow(key, value);
		}

		/* Report 4: Entities Ranking (Incoming) */

		TableBuilder tb4 = new TableBuilder();
		tb4.addRow("Entity", "USD Amount");
		tb4.addRow("------", "----------");

		Map<String, Double> entityIncomingReport = 
			      tradeList.stream()
			               .filter(trade -> trade.getInstruction() == Instruction.BUY)
			               .sorted(Comparator.comparing(trade -> trade.getInstructionDate()))
			               .collect(Collectors.groupingBy(trade -> String.valueOf(trade.getEntity()),
			                        LinkedHashMap::new,
			                        Collectors.summingDouble(trade -> trade.getUSDAmount())));
		
		MapUtil.orderByValue(entityIncomingReport);


		for (String entity : entityIncomingReport.keySet()) {
			String key = entity;
			String value = decFormat.format(entityIncomingReport.get(entity));
			tb4.addRow(key, value);
		}

		/* Report 5: Entities Ranking (Outgoing) */

		TableBuilder tb5 = new TableBuilder();
		tb5.addRow("Entity", "USD Amount");
		tb5.addRow("------", "----------");
		
		Collections.sort(tradeList, new InstructionDateComparator());

		Map<String, Double> entityOutgoingReport = 
			      tradeList.stream()
			               .filter(trade -> trade.getInstruction() == Instruction.SELL)
			               .sorted(Comparator.comparing(trade -> trade.getInstructionDate()))
			               .collect(Collectors.groupingBy(trade -> String.valueOf(trade.getEntity()),
			                        LinkedHashMap::new,
			                        Collectors.summingDouble(trade -> trade.getUSDAmount())));
		
		MapUtil.orderByValue(entityOutgoingReport);

		for (String entity : entityOutgoingReport.keySet()) {
			String key = entity;
			String value = decFormat.format(entityOutgoingReport.get(entity));
			tb5.addRow(key, value);
		}
				
		System.out.println("Report 1: Trade Log");
		System.out.println("====================");
		System.out.println();
		System.out.println(tb1.toString());

		System.out.println("Report 2: Daily Settled Amount in USD (INCOMING)");
		System.out.println("================================================");
		System.out.println();
		System.out.println(tb2.toString());

		System.out.println("Report 3: Daily Settled Amount in USD (OUTGOING)");
		System.out.println("=================================================");
		System.out.println();
		System.out.println(tb3.toString());

		System.out.println("Report 4: Entity Ranking in USD (INCOMING)");
		System.out.println("=================================================");
		System.out.println();
		System.out.println(tb4.toString());

		System.out.println("Report 5: Entity Ranking in USD (OUTGOING)");
		System.out.println("=================================================");
		System.out.println();
		System.out.println(tb5.toString());

	}
}
