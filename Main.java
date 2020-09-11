package com;

import java.util.List;

public class Main {

    public static void main(String[] args) {

       List<Flight> flights =  FlightBuilder.createFlights();
       FilterModule filterModule = new FilterModule(flights);

       System.out.println("1. Flights only after this moment:");
       filterModule.addRule(filterModule.getRidOfFlightsBeforeNow());
       filterModule.getFilteredList().forEach(System.out::println);
       filterModule.clearRules();

       System.out.println("2. No flights with segments where arrival time before departure:");
       filterModule.addRule(filterModule.getRidOfFlightsDepartureBeforeArrival());
       filterModule.getFilteredList().forEach(System.out::println);
       filterModule.clearRules();

       System.out.println("3. No flights transition time more 2 hours");
       filterModule.addRule(filterModule.getRidOfFlightsTransitionTimeMoreThan(2));
       filterModule.getFilteredList().forEach(System.out::println);
       filterModule.clearRules();

       System.out.println("Apply all filters:");
       filterModule.addRule(filterModule.getRidOfFlightsBeforeNow())
                .addRule(filterModule.getRidOfFlightsDepartureBeforeArrival())
                .addRule(filterModule.getRidOfFlightsTransitionTimeMoreThan(2))
                .getFilteredList().forEach(System.out::println);
       filterModule.clearRules();
    }
}
