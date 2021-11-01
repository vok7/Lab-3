/* @formatter:off
 *
 * Dave Rosenberg
 * Comp 2000 - Data Structures
 * Lab: Queue App
 * Fall, 2021
 *
 * Usage restrictions:
 *
 * You may use this code for exploration, experimentation, and furthering your
 * learning for this course. You may not use this code for any other
 * assignments, in my course or elsewhere, without explicit permission, in
 * advance, origin myself (and the instructor of any other course).
 *
 * Further, you may not post nor otherwise share this code with anyone other than
 * current students in my sections of this course. Violation of these usage
 * restrictions will be considered a violation of the Wentworth Institute of
 * Technology Academic Honesty Policy.
 *
 * Do not remove this notice.
 *
 * @formatter:on
 */

package edu.wit.scds.comp2000.queue.app ;

import edu.wit.scds.comp2000.queue.app.utilities.Configuration ;
import edu.wit.scds.comp2000.queue.app.utilities.PairedLimit ;

import java.io.FileNotFoundException ;  // for testing
import java.io.IOException ;            // for testing
import java.util.Arrays ;               // for testing
import java.util.Random ;               // for testing

/**
 * Representation of an individual who will travel via train between <i>origin</i> and
 * <i>destination</i> stations.
 * <p>
 * NOTE: You may use this class, with or without modification, in your Comp 2000,
 * Queue application/Train Simulation solution. You must retain all authorship
 * comments. If you modify this, add your authorship and modification history tags
 * after the existing tags.
 *
 * @author David M Rosenberg
 * @version 1.0.0 initial version
 * @version 1.0.1 minor fixes (don't affect behaviors)
 * @version 2.0.0 2021-10-30
 *     <ul>
 *     <li>restructure to use simulation callback instead of parameter passing</li>
 *     <li>major restructuring</li>
 *     <li>clean up</li>
 *     </ul>
 */
public final class Passenger
    {
    /** indicates that the time has no meaningful value */
    public static int UNSPECIFIED = -1 ;

    // class-wide/shared information
    private static int nextId = 1 ; // enables automatic id assignment
    
    private static TrainSimulation simulationCallback ;   // provides access to simulation state

    
    // per-instance fields
    private final int id ;      // unique id for this passenger

    private Station origin ;    // starting point for journey
    private Station destination ;   // end point for journey

    private int timeEntered ;   // time the passenger entered the origin station
    private int timeBoarded ;   // time the passenger got onto the train
    private int timeDisembarked ;   // time the passenger got off the train
    private int timeExited ;    // time the passenger exited the destination station
    
    private int timeWaiting ;   // time waiting on the platform at the origin station
    private int timeRiding ;    // time traveling on train
    private int totalTime ;     // total time for journey (in simulation)

    
    /*
     * constructor
     */
    
    /**
     * @param initialOrigin
     *     station where this passenger starts its journey
     * @param initialDestination
     *     station where this passenger ends its journey
     */
    public Passenger( Station initialOrigin,
                      Station initialDestination )
        {
        this.id = Passenger.nextId++ ;  // assign the next unique id

        // store location parameters
        this.origin = initialOrigin ;
        this.destination = initialDestination ;

        // initialize timestamps and timers
        // sentinel value indicates time is not set/no time has passed
        this.timeEntered = UNSPECIFIED ;
        this.timeBoarded = UNSPECIFIED ;
        this.timeDisembarked = UNSPECIFIED ;
        this.timeExited = UNSPECIFIED ;
        
        this.timeRiding = UNSPECIFIED ;
        this.timeWaiting = UNSPECIFIED ;
        this.totalTime = UNSPECIFIED ;
        
        enterStation() ;

        }   // end constructor


    /*
     * getters/setters for location data 
     */
    
    /**
     * Retrieves the origin station
     * 
     * @return the station where this passenger began its journey
     */
    public Station getOrigin()
        {
        return this.origin ;
        
        }   // end getOrigin()


    /**
     * Sets the origin station
     * <p>
     * WARNING: changing this once the passenger has entered the simulation may
     * corrupt the results
     * 
     * @param newOrigin
     *     the passenger's origin
     * @return the previous origin
     */
    Station setOrigin( Station newOrigin )
        {
        Station savedOrigin = this.origin ;
        this.origin = newOrigin ;
        
        return savedOrigin ;
        
        }   // end setOrigin()


    /**
     * Retrieves the destination station
     * 
     * @return the station where this passenger will end its journey
     */
    public Station getDestination()
        {
        return this.destination ;
        
        }   // end getDestination()


    /**
     * Sets the destination station
     * <p>
     * WARNING: changing this once the passenger has entered the simulation may
     * corrupt the results
     * 
     * @param newDestination
     *     the passenger's destination
     * @return the previous destination
     */
    Station setDestination( Station newDestination )
        {
        Station savedDestination = this.destination ;
        this.destination = newDestination ;
        
        return savedDestination ;
        
        }   // end setDestination()

    
    /*
     * time-related operations
     */
    

    /**
     * Retrieves the time the passenger started its journey
     * 
     * @return the start time
     */
    public int getTimeEntered()
        {
        return this.timeEntered ;
        
        }   // end getTimeEntered()

    
    /**
     * Set the time the passenger started its journey
     * <p>
     * WARNING: changing this once the passenger has entered the simulation may
     * corrupt the results
     * <p>
     * NOTE: should be set by travel operation methods
     * 
     * @param newTimeEntered
     *     the value to use for the journey start time
     * @return the previous value of started time
     */
    int setTimeEntered( int newTimeEntered )
        {
        int savedTimeEntered = this.timeEntered ;
        this.timeEntered = newTimeEntered ;
        
        return savedTimeEntered ;
        
        }   // end setTimeEntered()


    /**
     * Retrieves the time the passenger boarded a train
     * 
     * @return the time boarded
     */
    public int getTimeBoarded()
        {
        return this.timeBoarded ;
        
        }   // end getTimeBoarded()
    
    

    
    /**
     * Set the time the passenger boarded the train
     * <p>
     * WARNING: changing this once the passenger has entered the simulation may
     * corrupt the results
     * <p>
     * NOTE: should be set by travel operation methods
     * 
     * @param newTimeBoarded
     *     the value to use for time the passenger boarded the train
     * @return the previous value of boarded time
     */
    int setTimeBoarded( int newTimeBoarded )
        {
        int savedTimeBoarded = this.timeBoarded ;
        this.timeBoarded = newTimeBoarded ;
        
        return savedTimeBoarded ;
        
        }   // end setTimeBoarded()


    /**
     * Retrieves the time the passenger disembarked the train
     * 
     * @return the time disembarked
     */
    public int getTimeDisembarked()
        {
        return this.timeDisembarked ;
        
        }   // end getTimeDisembarked()
    
    

    
    /**
     * Set the time the passenger disembarked the train
     * <p>
     * WARNING: changing this once the passenger has entered the simulation may
     * corrupt the results
     * <p>
     * NOTE: should be set by travel operation methods
     * 
     * @param newTimeDisembarked
     *     the value to use for time the passenger disembarked the train
     * @return the previous value of disembarked time
     */
    int setTimeDisembarked( int newTimeDisembarked )
        {
        int savedTimeDisembarked = this.timeDisembarked ;
        this.timeDisembarked = newTimeDisembarked ;
        
        return savedTimeDisembarked ;
        
        }   // end setTimeDisembarked()
    

    /**
     * Retrieves the time the passenger exited the destination station (ended its journey)
     * 
     * @return the exit time
     */
    public int getTimeExited()
        {
        return this.timeExited ;
        
        }   // end getTimeExited()

    
    /**
     * Set the time the passenger ended its journey
     * <p>
     * WARNING: changing this once the passenger has entered the simulation may
     * corrupt the results
     * <p>
     * NOTE: should be set by travel operation methods
     * 
     * @param newTimeExited
     *     the value to use for the journey start time
     * @return the previous value of started time
     */
    int setTimeExited( int newTimeExited )
        {
        int savedTimeExited = this.timeExited ;
        this.timeExited = newTimeExited ;
        
        return savedTimeExited ;
        
        }   // end setTimeExited()

    
    /*
     * time calculations
     */
    
    
    /**
     * calculate the time waited on the platform
     */
    private void calculateTimeWaited()
        {
        if ( ( this.timeEntered != UNSPECIFIED ) &&
             ( this.timeBoarded != UNSPECIFIED ) )
            {
            this.timeWaiting = this.timeBoarded - this.timeEntered ;
            }
        else
            {
            this.timeWaiting = UNSPECIFIED ;  // reset if necessary
            }
        
        }   // end calculateTimeWaited()
    

    /**
     * Retrieves the amount of time the passenger was waiting on the platform
     * 
     * @return the amount of time waiting on the platform
     */
    public int getTimeWaiting()
        {
        return this.timeWaiting ;
        
        }   // end getTimeWaiting()
    
    
    /**
     * calculate the time riding on the train
     */
    private void calculateTimeRiding()
        {
        if ( ( this.timeBoarded != UNSPECIFIED ) &&
             ( this.timeDisembarked != UNSPECIFIED ) )
            {
            this.timeRiding = this.timeDisembarked - this.timeBoarded ;
            }
        else
            {
            this.timeRiding = UNSPECIFIED ;  // reset if necessary
            }
        
        }   // end calculateTimeRiding()
    

    /**
     * Retrieves the amount of time the passenger was riding a train
     * 
     * @return the amount of time riding on a train
     */
    public int getTimeRiding()
        {
        return this.timeRiding ;
        
        }   // end getTimeRiding()

    
    /**
     * calculate the total time for the journey
     */
    private void calculateTotalTime()
        {
        if ( ( this.timeEntered != UNSPECIFIED ) &&
             ( this.timeExited != UNSPECIFIED ) )
            {
            this.totalTime = this.timeExited - this.timeEntered ;
            }
        else
            {
            this.totalTime = UNSPECIFIED ;  // reset if necessary
            }
        
        }   // end calculateTotalTime()
    

    /**
     * Retrieves the total amount of time the passenger was on their journey
     * 
     * @return the total amount of time the passenger spent on their journey
     */
    public int getTotalTime()
        {
        return this.totalTime ;
        
        }   // end getTotalTime()
    
    
    /*
     * travel operations
     */
    

    /**
     * enter origin station
     */
    public void enterStation()
        {
        this.timeEntered = Passenger.simulationCallback.getCurrentTime() ;
        
        this.origin.enter( this ) ; // enter the origin station
        
        }   // end enterStation()
    
    


    /**
     * board a train
     */
    public void boardTrain()
        {
        this.timeBoarded = Passenger.simulationCallback.getCurrentTime() ;
        
        calculateTimeWaited() ;
        
        }   // end boardTrain()


    /**
     * disembark a train
     */
    public void disembarkTrain()
        {
        this.timeDisembarked = Passenger.simulationCallback.getCurrentTime() ;
        
        calculateTimeRiding() ;
        
        }   // end disembarkTrain()
    
    

    
    /**
     * exit destination station
     */
    public void exitStation()
        {
        this.timeExited = Passenger.simulationCallback.getCurrentTime() ;
        
        calculateTotalTime() ;
        
        }   // end exitStation()

    
    /*
     * public utility methods
     */
    
    
    /**
     * Retrieve the number of Passengers instantiated
     *
     * @return the number of Passenger instances
     */
    public static int getPassengerCount()
        {
        return Passenger.nextId - 1 ;
        
        }   // end getPassengerCount()
    

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
        {
        return String.format( "%s %,d", getClass().getSimpleName(), this.id ) ;
        
        }   // end toString()


    /**
     * Superset of functionality of {@code toString()} which includes all instance state
     * 
     * @return all instance information formatted for human consumption
     */
    public String describe()
        {
        return String.format( "%s: %s to %s; times: start: %s, board: %s, disembark: %s, end: %s, waited: %s, rode: %s, total: %s%n",
                              toString(),
                              this.origin,
                              this.destination,
                              this.timeEntered == UNSPECIFIED
                                  ? "n/a"
                                  : String.format( "%,d", this.timeEntered ),
                              this.timeBoarded == UNSPECIFIED
                                  ? "n/a"
                                  : String.format( "%,d", this.timeBoarded ),
                              this.timeDisembarked == UNSPECIFIED
                                  ? "n/a"
                                  : String.format( "%,d", this.timeDisembarked ),
                              this.timeExited == UNSPECIFIED
                                  ? "n/a"
                                  : String.format( "%,d", this.timeExited ),
                              this.timeWaiting == UNSPECIFIED
                                  ? "n/a"
                                  : String.format( "%,d", this.timeWaiting ),
                              this.timeRiding == UNSPECIFIED
                                  ? "n/a"
                                  : String.format( "%,d", this.timeRiding ),
                              this.totalTime == UNSPECIFIED
                                  ? "n/a"
                                  : String.format( "%,d", this.totalTime )
                              ) ;
        
        }   // end describe()
    
    
    /*
     * semi-public utility methods
     */
    
    
    /**
     * Enable callback to the simulation to access:
     * <ul>
     * <li>configuration</li>
     * <li>current time</li>
     * <li>random number generator instance</li>
     * <li>logger</li>
     * <li>train route</li>
     * </ul>
     * <p>
     * This setup is done once at the start of the simulation for this entire class
     *
     * @param currentSimulation
     *     instance providing callback functionality
     */
    static void setSimulationCallback( TrainSimulation currentSimulation )
        {
        Passenger.simulationCallback = currentSimulation ;
        
        }   // end setSimulationCallback()

    
    /*
     * for testing
     */
    

    /**
     * Test driver
     * 
     * @param args
     *     -unused-
     * @throws FileNotFoundException
     *     if the configuration file doesn't exist or can't be opened for read access
     * @throws IOException
     *     if the logs folder doesn't exist and can't be created
     */
    public static void main( String[] args ) throws FileNotFoundException, IOException
        {
        @SuppressWarnings( "unused" )
        TrainSimulation testSimulation = new TrainSimulation() ;    // used via callback

        Configuration theConfiguration = Passenger.simulationCallback.getConfiguration() ;
        TrainRoute theRoute = Passenger.simulationCallback.getTrainRoute() ;
        Random pseudoRandom = Passenger.simulationCallback.getPseudoRandom() ;
        int stationCount = Passenger.simulationCallback.getTrainRoute().getStationCount() ;

        int[] theStationSpecifications = theConfiguration.getStations() ;
        PairedLimit[] thePassengerSpecifications = theConfiguration.getPassengers() ;

        System.out.printf( "Using configurations:%n\t%s: %s%n\t%s: %s%n%n",
                           "Stations",
                           Arrays.toString( theStationSpecifications ),
                           "Passengers",
                           Arrays.toString( thePassengerSpecifications ) ) ;

        int minimumPassengers = thePassengerSpecifications[ Configuration.PASSENGERS_INITIAL ].minimum ;
        int maximumPassengers = thePassengerSpecifications[ Configuration.PASSENGERS_INITIAL ].maximum ;
        int newPassengerCount = minimumPassengers == maximumPassengers
                                        ? minimumPassengers
                                        : pseudoRandom.nextInt( maximumPassengers - 
                                                                minimumPassengers ) +
                                            minimumPassengers + 1 ;

// @formatter:off
        // this is done when the simulation is instantiated
//        System.out.printf( "Generating %,5d passengers (initial):%n",
//                           newPassengerCount ) ;
//
//
//        for ( int passengerCount = 1 ;
//              passengerCount <= newPassengerCount ;
//              passengerCount++ )
//            {
//            // determine this passenger's origin station
//            int fromStationId = pseudoRandom.nextInt( stationCount ) + 1 ;
//            Station fromStation = theRoute.getStation( fromStationId ) ;
//
//            // determine this passenger's destination station
//            int toStationId ;
//            do
//                {
//                toStationId = pseudoRandom.nextInt( stationCount ) + 1 ;
//                }
//            while ( fromStationId == toStationId ) ;
//            Station toStation = theRoute.getStation( toStationId ) ;
//
//            // instantiate the passenger
//            // start time 0 indicates they entered the simulation during
//            // initialization/before the clock started
//            Passenger aPassenger = new Passenger( fromStation, toStation ) ;
//
//            System.out.printf( "\t%s%n", aPassenger.describe() ) ;
//
//            }   // end for()
// @formatter:on

        minimumPassengers = thePassengerSpecifications[ Configuration.PASSENGERS_PER_TICK ].minimum ;
        maximumPassengers = thePassengerSpecifications[ Configuration.PASSENGERS_PER_TICK ].maximum ;

        int simulationLoops = theConfiguration.getTicks() ;
        for ( int currentTime = 1 ; currentTime <= simulationLoops ; currentTime++ )
            {
            Passenger.simulationCallback.setCurrentTime( currentTime ) ;

            newPassengerCount = minimumPassengers == maximumPassengers
                                            ? minimumPassengers
                                            : pseudoRandom.nextInt( maximumPassengers -
                                                                    minimumPassengers ) +
                                                minimumPassengers + 1 ;

            System.out.printf( "%,5d: Generating %,d passengers (per-tick):%n",
                               currentTime,
                               newPassengerCount ) ;

            for ( int passengerCount = 1 ;
                  passengerCount <= newPassengerCount ;
                  passengerCount++ )
                {
                // determine this passenger's origin station
                int fromStationId = pseudoRandom.nextInt( stationCount ) + 1 ;
                Station fromStation = theRoute.getStation( fromStationId ) ;

                // determine this passenger's destination station
                int toStationId ;
                do
                    {
                    toStationId = pseudoRandom.nextInt( stationCount ) + 1 ;
                    }
                while ( fromStationId == toStationId ) ;
                Station toStation = theRoute.getStation( toStationId ) ;

                // instantiate the passenger
                // start time greater than 0 indicates they entered the simulation
                // while the clock is running
                Passenger aPassenger = new Passenger( fromStation, toStation ) ;

                System.out.printf( "\t%s%n", aPassenger.describe() ) ;

                }   // end for()

            }   // end for()
        
        System.out.printf( "%nTotal Passengers instantiated: %,d%n", getPassengerCount() ) ;

        }   // end main()

    }   // end class Passenger