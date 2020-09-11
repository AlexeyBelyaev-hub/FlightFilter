package com;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.*;

import static org.junit.Assert.*;

/*This module tests FliterModule class
 written by Alexey Belyaev*/
@RunWith(Parameterized.class)
public class FilterModuleTest {

    private static final List<Flight> flights = FlightBuilder.createFlights();
    private static FilterModule filterModule = new FilterModule(flights);

    //Parameters
    private Rule rule;
    private List<Flight> expected;

    @org.junit.Before
    public void setup(){
        filterModule.clearRules();
    }

    public FilterModuleTest(Rule rule, List<Flight> expected) {
        this.rule = rule;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object> testConditions(){

        return Arrays.asList(new Object[][]{
                {filterModule.getRidOfFlightsBeforeNow(), Arrays.asList(flights.get(0),
                        flights.get(1),
                        flights.get(3),
                        flights.get(4),
                        flights.get(5))},
                {filterModule.getRidOfFlightsDepartureBeforeArrival(), Arrays.asList(flights.get(0),
                        flights.get(1),
                        flights.get(2),
                        flights.get(4),
                        flights.get(5))},
                {filterModule.getRidOfFlightsTransitionTimeMoreThan(2), Arrays.asList(flights.get(0),
                        flights.get(1),
                        flights.get(2),
                        flights.get(3),
                        flights.get(5))},
                {null, Arrays.asList(flights.get(0),
                        flights.get(1),
                        flights.get(2),
                        flights.get(3),
                        flights.get(4),
                        flights.get(5))},
        });
    }


    @org.junit.Test
    public void getFilteredList() {
        filterModule.clearRules();
        filterModule.addRule(rule);
        List<Flight> currentResult = filterModule.getFilteredList();
        assertArrayEquals(expected.toArray(), currentResult.toArray());
    }

}