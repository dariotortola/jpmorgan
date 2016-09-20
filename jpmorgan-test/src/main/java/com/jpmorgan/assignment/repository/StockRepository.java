package com.jpmorgan.assignment.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jpmorgan.assignment.model.Stock;
import com.jpmorgan.assignment.model.Trade;

/*
 * a more complex application could extend once of the Spring Crud/Jpa
 * repositories. As for the assignment, though, a memory representation should
 * be enough
 * 
 */
public class StockRepository {

	/*
	 * somewhere to store all stocks we've got
	 */
	private static final List<Stock> stocks = new ArrayList<>();

	/*
	 * we keep this in ascendent order by date to ease other methods
	 */
	private static final List<Trade> trades = new ArrayList<>();

	private static int compare(Trade t1, Trade t2) {
		return t1.getDate().compareTo(t2.getDate());
	}

	/**
	 * @param stock
	 * @param after
	 * @return trades of stock after the Date parameter
	 */
	public List<Trade> getRecentTrades(Stock stock, Date after) {
		/*
		 * in a database it'd be something like (JPA) SELECT t FROM Trade WHERE
		 * t.stock.symbol = :symbol AND t.date > :after
		 */
		List<Trade> selected = new ArrayList<>();
		// iterate from most recent
		for (int i = trades.size() - 1; i >= 0; i--) {
			Trade t = trades.get(i);
			if (after.compareTo(t.getDate()) > 0) {
				// from this one on, all are previous
				break;
			} else if (StringUtils.equalsIgnoreCase(stock.getSymbol(), t.getStock().getSymbol())) {
				// add on 0 to preserve order
				selected.add(0, t);
			}
		}
		return selected;
	}

	/**
	 * @param companyId
	 * @return stocks from this company
	 */
	public List<Stock> getStocks(int companyId) {
		List<Stock> selected = new ArrayList<>();
		for (Stock stock : stocks) {
			/*
			 * == because it's int, but if it were Long, Integer, etc,
			 * equals(Object), of course. Anyway, in a real world situation we
			 * would be using a database's query
			 */
			if (stock.getCompanyId() == companyId) {
				selected.add(stock);
			}
		}
		return selected;
	}

	/**
	 * Saves stock, ignores duplicates
	 * 
	 * @param stock
	 */
	public void save(Stock stock) {
		if (!stocks.contains(stock)) {
			stocks.add(stock);
		}
	}

	/**
	 * Saves trade
	 * 
	 * @param trade
	 */
	public void save(Trade trade) {
		// keep them ordered by time, to ease getRecentTrades
		int index = Collections.binarySearch(trades, trade, StockRepository::compare);
		if (index < 0) {
			index = -1 - index;
		}
		trades.add(index, trade);
		/*
		 * if stock is not already saved, save it
		 */
		save(trade.getStock());
	}
}
