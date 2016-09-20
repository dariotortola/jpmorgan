package com.jpmorgan.assignment.repository;

import java.util.ArrayList;
import java.util.List;

import com.jpmorgan.assignment.model.Trade;

/**
 * TODO a more complex application could extend once of the Spring Crud/Jpa
 * repositories. As for the assignment, though, a memory representation should
 * be enough
 * 
 * @author dtortola
 *
 */
public class StockRepository {

    /**
     * TODO of course, a real repository would be using a database. Here we are
     * just using memory. Since we only add at the end of the list, it's ordered
     * on ascendent date order
     */
    private static final List<Trade> trades = new ArrayList<>();

}
