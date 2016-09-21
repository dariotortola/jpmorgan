package com.jpmorgan.assignment.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jpmorgan.assignment.model.Stock;
import com.jpmorgan.assignment.model.StockType;
import com.jpmorgan.assignment.model.Trade;
import com.jpmorgan.assignment.service.StockService;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@RunWith(SpringJUnit4ClassRunner.class)
public class StockServiceTest {

    private Stock ale;
    private Stock gin;

    private Stock joe;
    private Stock pop;
    @Mock
    private StockRepository repository;
    @InjectMocks
    private StockService service;
    private Stock tea;

    /**
     * tests dividend yield method
     */
    @Test
    public void dividend() {
        Assert.assertEquals(new BigDecimal("0"), service.getDividendYield(tea));
        Assert.assertEquals(new BigDecimal("0.08"), service.getDividendYield(pop));
        Assert.assertEquals(new BigDecimal("0.3833333"), service.getDividendYield(ale));
        Assert.assertEquals(new BigDecimal("2"), service.getDividendYield(gin));
        Assert.assertEquals(new BigDecimal("0.052"), service.getDividendYield(joe));
    }

    /**
     * tests all share index for the example stocks
     */
    @Test
    public void getAllShareIndex() {
        Assert.assertEquals(new BigDecimal("108.4471771"), service.getAllShareIndex(1));
    }

    /**
     * tests p/e ratio method
     */
    @Test
    public void peRatio() {
        // can't try since dividend is zero
        // Assert.assertEquals(new BigDecimal("0.000000"),
        // service.getPeRatio(tea));
        Assert.assertEquals(new BigDecimal("1250"), service.getPeRatio(pop));
        Assert.assertEquals(new BigDecimal("156.5217"), service.getPeRatio(ale));
        Assert.assertEquals(new BigDecimal("50"), service.getPeRatio(gin));
        Assert.assertEquals(new BigDecimal("4807.692"), service.getPeRatio(joe));
    }

    /**
     * prepare a list with the example stocks
     */
    @Before
    public void prepare() {
        List<Stock> stocks = new ArrayList<>();

        tea = new Stock();
        tea.setCompanyId(1);
        tea.setSymbol("TEA");
        tea.setType(StockType.COMMON);
        tea.setLastDividend(0);
        tea.setParValue(100);
        stocks.add(tea);

        pop = new Stock();
        pop.setCompanyId(1);
        pop.setSymbol("POP");
        pop.setType(StockType.COMMON);
        pop.setLastDividend(8);
        pop.setParValue(100);
        stocks.add(pop);

        ale = new Stock();
        ale.setCompanyId(1);
        ale.setSymbol("ALE");
        ale.setType(StockType.COMMON);
        ale.setLastDividend(23);
        ale.setParValue(60);
        stocks.add(ale);

        gin = new Stock();
        gin.setCompanyId(1);
        gin.setSymbol("GIN");
        gin.setType(StockType.PREFERRED);
        gin.setLastDividend(8);
        gin.setFixedDividend(2);
        gin.setParValue(100);
        stocks.add(gin);

        joe = new Stock();
        joe.setCompanyId(1);
        joe.setSymbol("JOE");
        joe.setType(StockType.COMMON);
        joe.setLastDividend(13);
        joe.setParValue(250);
        stocks.add(joe);

        Mockito.when(repository.getStocks(1)).thenReturn(stocks);
    }

    /**
     * tests record trade
     */
    @Test
    public void recordTrade() {
        BaseMatcher<Trade> matcher = new BaseMatcher<Trade>() {

            @Override
            public void describeTo(Description description) {
            }

            @Override
            public boolean matches(Object item) {
                Trade trade = (Trade) item;

                return trade.getStock().equals(tea) && trade.getAmount() == 100 && trade.getPrice() == 120
                        && !trade.isBuy() && new Date().getTime() - trade.getDate().getTime() < 1000;
            }
        };

        service.recordTrade(tea, 100, 120, false);
        Mockito.verify(repository).save(Matchers.argThat(matcher));
    }

    /**
     * tests stock price method
     */
    @Test
    public void stockPrice() {
        Stock stock = new Stock();
        // empty list, should default to par value
        stock.setParValue(100);
        Mockito.when(repository.getRecentTrades(Matchers.eq(stock), Matchers.any(Date.class)))
                .thenReturn(new ArrayList<>());

        Assert.assertEquals(new BigDecimal(100), service.getStockPrice(stock));

        // average not par value
        List<Trade> selected = new ArrayList<>();
        Trade trade = new Trade();
        trade.setAmount(120);
        trade.setBuy(true);
        trade.setDate(new Date());
        trade.setPrice(100);
        trade.setStock(stock);
        selected.add(trade);

        trade = new Trade();
        trade.setAmount(200);
        trade.setBuy(true);
        trade.setDate(new Date());
        trade.setPrice(80);
        trade.setStock(stock);
        selected.add(trade);

        Mockito.when(repository.getRecentTrades(Matchers.eq(stock), Matchers.any(Date.class))).thenReturn(selected);
        Assert.assertEquals(new BigDecimal("87.5"), service.getStockPrice(stock));
    }
}
