package com.jpmorgan.assignment.model;

/**
 * Data for a Stock
 * 
 * @author dtortola
 *
 */
public class Stock {
	/*
	 * id of the company, just so we can deal with more than one. In a real
	 * world case, it would be many to one relationship in a database
	 */
	private int companyId;
	/*
	 * assumes not decimal percentages, in a more serious application it may
	 * need be a BigDecimal
	 */
	private int fixedDividend;
	/*
	 * assuming it's mandatory, 0 if need be; using int the limit is about 21
	 * million pounds, I guess it's enough, otherwise it could use long or
	 * BigInteger
	 */
	private int lastDividend;
	/*
	 * assuming it's mandatory; using int the limit is about 21 million pounds,
	 * I guess it's enough, otherwise it could use long or BigInteger
	 */
	private int parValue;
	/*
	 * using this as Stock's id
	 */
	private String symbol;
	private StockType type = StockType.COMMON;

	/**
	 * @return fixed dividend, may be null
	 */
	public int getFixedDividend() {
		return fixedDividend;
	}

	/**
	 * @return last dividend in pennies
	 */
	public int getLastDividend() {
		return lastDividend;
	}

	/**
	 * @return par value in pennies
	 */
	public int getParValue() {
		return parValue;
	}

	public String getSymbol() {
		return symbol;
	}

	public StockType getType() {
		return type;
	}

	public void setFixedDividend(int fixedDividend) {
		this.fixedDividend = fixedDividend;
	}

	public void setLastDividend(int lastDividend) {
		this.lastDividend = lastDividend;
	}

	public void setParValue(int parValue) {
		this.parValue = parValue;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void setType(StockType type) {
		this.type = type;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

}
