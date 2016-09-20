package com.jpmorgan.assignment.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpmorgan.assignment.model.Stock;
import com.jpmorgan.assignment.model.Trade;
import com.jpmorgan.assignment.repository.StockRepository;

@Service
public class StockService {

	/**
	 * 15 minutes in ms
	 */
	private static final long LAST_15_MINUTES = 900_000;

	private MathContext mathContext = MathContext.DECIMAL32;

	@Autowired
	private StockRepository repository;

	/**
	 * @param companyId
	 * @return all share index of company with the id
	 */
	public BigDecimal getAllShareIndex(int companyId) {
		List<Stock> stocks = repository.getStocks(companyId);
		switch (stocks.size()) {
		case 0:
			return BigDecimal.ZERO;
		case 1:
			return getStockPrice(stocks.get(0));
		default:
			int n = stocks.size();
			double nthRoot = 1 / (double) n;
			Iterator<Stock> iterator = stocks.iterator();
			// at least two elements
			BigDecimal total = new BigDecimal(Math.pow(getStockPrice(iterator.next()).doubleValue(), nthRoot),
					mathContext);
			while (iterator.hasNext()) {
				// pow-ing each element we avoid the risk of overflowing
				total = total.multiply(new BigDecimal(Math.pow(getStockPrice(iterator.next()).doubleValue(), nthRoot)),
						mathContext);
			}
			return total;
		}
	}

	/**
	 * Common and preferred dividend yields follow a different formula, but both
	 * are divided by the stock price. To avoid calculating that twice in P/E
	 * Ratio while avoiding duplicating code, we do this method
	 * 
	 * @param stock
	 * @return dividend base, as described before
	 */
	private int getDividendBase(Stock stock) {
		/*
		 * not initialising, compiler warns us when we fail to assign a value.
		 * That is useful if we forget to add a case to the following switch
		 */
		int base;
		switch (stock.getType()) {
		case COMMON:
			base = stock.getLastDividend();
			break;

		case PREFERRED:
			base = stock.getFixedDividend() * stock.getParValue();
			break;

		default:
			/*
			 * just in case a new type is created
			 */
			throw new UnsupportedOperationException("Invalid stock's type ");
		}
		return base;
	}

	/**
	 * @param stock
	 * @return dividend yield for the stock
	 */
	public BigDecimal getDividendYield(Stock stock) {
		return new BigDecimal(getDividendBase(stock)).divide(getStockPrice(stock), mathContext);
	}

	/**
	 * @return math context used by this service for precision and rounding
	 *         description
	 */
	public MathContext getMathContext() {
		return mathContext;
	}

	/**
	 * @param stock
	 * @return P/E Ratio as ticker price/dividend
	 */
	public BigDecimal getPeRatio(Stock stock) {
		/*
		 * stockPrice/dividend = stockPrice/(dividendBase/stockPrice) =
		 * stockPrice^2/dividendBase. Calculating it like this, we calculate
		 * StockPrice once, instead of once here and once in dividend
		 */
		BigDecimal tickerPrice = getStockPrice(stock);
		return tickerPrice.multiply(tickerPrice, mathContext)
				.divide(new BigDecimal(getDividendBase(stock), mathContext), mathContext);
	}

	/**
	 * Bases the calculation on the arithmetic average price per stock on trades
	 * on the last 15 minutes, defaulting to the par value if none
	 * 
	 * @param stock
	 * @return stock price per stock based on trades over the last 15 minutes
	 */
	public BigDecimal getStockPrice(Stock stock) {
		List<Trade> recent = repository.getRecentTrades(stock, new Date(System.currentTimeMillis() - LAST_15_MINUTES));
		if (recent.isEmpty()) {
			return BigDecimal.valueOf(stock.getParValue());
		} else {
			long totalPrice = 0;
			long totalAmount = 0;
			for (Trade t : recent) {
				totalPrice += t.getAmount() * t.getPrice();
				totalAmount += t.getAmount();
			}
			return new BigDecimal(totalPrice, mathContext).divide(new BigDecimal(totalAmount), mathContext);
		}
	}

	/**
	 * Records a trade
	 * 
	 * @param stock
	 *            stock traded
	 * @param amountShares
	 *            amount of shares traded
	 * @param price
	 *            price per shared, in pence
	 * @param isBuy
	 *            true if it's a buy, false if it's a sale
	 */
	public void recordTrade(Stock stock, int amountShares, int price, boolean isBuy) {
		Trade trade = new Trade();
		trade.setAmount(amountShares);
		trade.setBuy(isBuy);
		trade.setPrice(price);
		trade.setStock(stock);
		trade.setDate(new Date());
		repository.save(trade);
	}

	public void setMathContext(MathContext mathContext) {
		this.mathContext = mathContext;
	}

}
