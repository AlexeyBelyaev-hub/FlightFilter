package com;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*This module designed to provide filtration for list of flights (Flight).
It consists of FliterModule class and Rule interface.
It designed considering multiple rules of filtration.
FilterModule accept many rules at the same time.
Rule stores in the data structure and could be set dynamic depends on context.
 written by Alexey Belyaev*/
public final class FilterModule {

    private List<Flight> flights;
    private List<Rule> rules;

    public FilterModule(List<Flight> flights){
        this.flights = flights;
        this.rules = new ArrayList<>();
    }

    //Return filtered list of flights. if there is no rules return copy of full list of flights.
    public List<Flight> getFilteredList(){
        if (rules.isEmpty()){
            return List.copyOf(flights);
        }
        Rule finalRule = rules.stream().reduce(r->r, (r1,r2)-> s->r2.apply(r1.apply(s)));
        return finalRule.apply(flights.stream()).collect(Collectors.toList());
    }

    //add rule and return this to provide possibilities chaining
    public FilterModule addRule(Rule rule){
        if (rule!=null) {
            rules.add(rule);
        }
        return this;
    }

    public void clearRules(){
        rules.clear();
    }

    // This rule filter out all flights that departure before this moment
    Rule getRidOfFlightsBeforeNow(){
        return stream -> stream
                .filter(flight -> flight.getSegments().get(0).getDepartureDate().isBefore(LocalDateTime.now())?false:true);
    }

    // This rule filter out all flights that have segments where arrival date before departure date
    Rule getRidOfFlightsDepartureBeforeArrival(){
        return stream -> stream
                .filter(flight ->
                        flight.getSegments().stream().noneMatch((s)->s.getArrivalDate().isBefore(s.getDepartureDate())));
    }

    //This rule filter out all flights that have transit time between segments more than argument maxTransitionHours
    Rule getRidOfFlightsTransitionTimeMoreThan(Integer maxTransitionHours){
        return stream -> stream.filter(flight ->{
            List<Segment> segments = flight.getSegments();
            if (segments.isEmpty()){
                return false;
            }
            for (int i = 0; i<flight.getSegments().size()-1; i++){
                if (segments.get(i).getArrivalDate().plusHours(maxTransitionHours)
                        .isBefore(segments.get(i+1).getDepartureDate())){
                    return false;
                }
            }
            return true;
        });
    }

    Rule manualRule(Predicate<Flight> predicate){
        return stream->stream.filter(predicate);
    }
}

//Functional interface Rule makes it possible to store rules in data structures
// of filtering and compose it together
@FunctionalInterface
interface Rule{
    Stream<Flight> apply(Stream<Flight> s);
}