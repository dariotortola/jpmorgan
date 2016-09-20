package com.jpmorgan.assignment.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

/**
 * Data for a Stock
 * 
 * @author dtortola
 *
 */
public class Stock {
    /**
     * big decimal to avoid rounding problems with double type calculations
     */
    private BigDecimal fixedDividend;
    /**
     * TODO assuming it's mandatory, 0 if need be; using int the limit is about
     * 21 million pounds, I guess it's enough, otherwise it could use long or
     * {@link BigInteger}
     */
    private int lastDividend;
    /**
     * TODO assuming it's mandatory; using int the limit is about 21 million
     * pounds, I guess it's enough, otherwise it could use long or
     * {@link BigInteger}
     */
    private int parValue;
    private String symbol;
    private StockType type = StockType.COMMON;

    /**
     * TODO assumes working on memory, it could need just {@link BigDecimal} if
     * this class were to be saved in a database
     * 
     * @return fixed dividend, may be null
     */
    public Optional<BigDecimal> getFixedDividend() {
        return Optional.of(fixedDividend);
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

    public void setFixedDividend(BigDecimal fixedDividend) {
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

}
