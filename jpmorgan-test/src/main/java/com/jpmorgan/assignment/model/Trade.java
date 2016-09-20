package com.jpmorgan.assignment.model;

import java.util.Date;

/**
 * Represents the trading of stocks
 * 
 * @author dtortola
 *
 */
public class Trade {
    private int amount;
    /**
     * TODO assumes only buy and sell, if more possibilities exist, it should be
     * changed for an enumeration
     */
    private boolean buy;
    private Date date;
    private int price;
    private Stock stock;

    public int getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    /**
     * @return price per stock, in pennies
     */
    public int getPrice() {
        return price;
    }

    public Stock getStock() {
        return stock;
    }

    /**
     * @return true if it's a buy operation, false if it's sale
     */
    public boolean isBuy() {
        return buy;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

}
