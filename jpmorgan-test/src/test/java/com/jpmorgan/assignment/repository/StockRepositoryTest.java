package com.jpmorgan.assignment.repository;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.jpmorgan.assignment.model.Stock;
import com.jpmorgan.assignment.model.Trade;

import org.junit.Assert;

public class StockRepositoryTest {

	/**
	 * simple test, we add two trades and then check that we get the ones we
	 * want by date
	 */
	@Test
	public void test() {
		StockRepository repository = new StockRepository();

		Stock stock1 = new Stock();
		stock1.setSymbol("TEA");
		Stock stock2 = new Stock();
		stock2.setSymbol("POP");

		// 10 minutes before
		Date date1 = new Date(System.currentTimeMillis() - 600_000);
		// 20 minutes before
		Date date2 = new Date(System.currentTimeMillis() - 1200_000);
		// 30 minutes before
		Date date3 = new Date(System.currentTimeMillis() - 1800_000);

		Trade trade = new Trade();
		trade.setAmount(20);
		trade.setBuy(false);
		trade.setDate(date1);
		trade.setPrice(100);
		trade.setStock(stock1);
		repository.save(trade);

		trade = new Trade();
		trade.setAmount(20);
		trade.setBuy(false);
		trade.setDate(date2);
		trade.setPrice(100);
		trade.setStock(stock2);
		repository.save(trade);

		trade = new Trade();
		trade.setAmount(20);
		trade.setBuy(false);
		trade.setDate(date3);
		trade.setPrice(100);
		trade.setStock(stock1);
		repository.save(trade);

		// 1 hour ago, some trades are not selected because of stock
		List<Trade> selected = repository.getRecentTrades(stock1, new Date(System.currentTimeMillis() - 3600_000));
		Assert.assertEquals(2, selected.size());
		for (Trade t : selected) {
			Assert.assertEquals(stock1, t.getStock());
		}
		selected = repository.getRecentTrades(stock2, new Date(System.currentTimeMillis() - 3600_000));
		Assert.assertEquals(1, selected.size());
		for (Trade t : selected) {
			Assert.assertEquals(stock2, t.getStock());
		}
		// 30 minutes before, the third trade falls out by time, even if it's a
		// ms
		selected = repository.getRecentTrades(stock1, new Date(date3.getTime() + 1));
		Assert.assertEquals(1, selected.size());
	}
}
