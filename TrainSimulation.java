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
 * advance, from myself (and the instructor of any other course).
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
import edu.wit.scds.comp2000.queue.app.utilities.Logger ;
import edu.wit.scds.comp2000.queue.app.utilities.PairedLimit ;
import edu.wit.scds.comp2000.queue.app.utilities.TrainSpecification ;

import java.io.FileNotFoundException ;
import java.io.IOException ;
import java.util.Date ;
import java.util.Random ;

/**
 * A fully parameterized, synchronous, single-threaded train simulation.
 * <p>
 * NOTE: You must retain all authorship comments. You should add/update your
 * authorship and modification history tags after the existing tags.
 *
 * @author Dave Rosenberg
 * @version 0.1.0 base version
 * @version 1.0.0 2019-03-16 Initial implementation
 * @version 2.0.0 2021-10-30
 *     <ul>
 *     <li>restructure to be object-oriented
 *     <li>add callback mechanism to enable entity classes to share simulation
 *     resources
 *     </ul>
 * 
 * @author Your Name
 * @version 2.1.0 2021-11-01 finish implementation
 */
public final class TrainSimulation
    {
    // instance variables
    
    // these are all package private to enable the various entities in the simulation
    // to access them easily
    
    /** configuration specification */
    private Configuration theConfiguration ;

    /** handle to the simulated train route including the stations and trains */
    private TrainRoute theRoute ;

    /** simulation time in ticks */
    private int currentTime ;
    
    /** random number generator instance for this simulation run */
    private Random pseudoRandom ;
    
    /** logging support */
    private Logger logger ;

    /**
     * set up the simulation based upon the settings in the configuration file
     * 
     * @throws IOException
     *     if the logs folder doesn't exist and can't be created
     */
    public TrainSimulation() throws IOException
        {
        // create log
        this.logger = new Logger( "TrainSimulation" ) ;
        
        
        // enable callback in all entities
        TrainRoute.setSimulationCallback( this ) ;
        Station.setSimulationCallback( this ) ;
        Train.setSimulationCallback( this ) ;
        Passenger.setSimulationCallback( this ) ;
        
        
        // load the configuration
        this.theConfiguration = new Configuration() ;

        System.out.printf( "Simulation configuration:%n%s%n%n----------%n%n",
                           this.theConfiguration.toString() ) ;

        // log the configuration
        this.logger.logConfiguration( this.theConfiguration ) ;
        
        // create and initialize the pseudo-random number generator
        this.pseudoRandom = new Random() ;
        
        // seed it if specified in configuration
        long seed = this.theConfiguration.getSeed() ;

        if ( seed == -1 )
            {
            // use current time - will produce a different sequence each run
            seed = new Date().getTime() ;
            }
        else if ( seed != 0 )
            {
            // use configuration seed - will produce the same sequence each run 
            this.pseudoRandom.setSeed( seed ) ;
            }
        
        this.currentTime = 0 ;

        
        // build the route and populate stations with initial set of passengers
        System.out.printf( "Setting up...%n" ) ;
        setup() ;

        }   // end no-arg constructor


    /**
     * Instantiate a passenger and send them to their starting station
     */
    private void createAPassenger()
        {
        // determine starting and ending stations
        int stationCount = this.theRoute.getStationCount() ;

        int fromStationId = this.pseudoRandom.nextInt( stationCount ) + 1 ;
        Station fromStation = this.theRoute.getStation( fromStationId ) ;

        int toStationId ;
        do  // randomly select a destination station until it doesn't match
            // the starting station
            {
            toStationId = this.pseudoRandom.nextInt( stationCount ) + 1 ;
            }
        while ( fromStationId == toStationId ) ;
        Station toStation = this.theRoute.getStation( toStationId ) ;

        // create a passenger - will automatically enter fromStation
        new Passenger( fromStation, toStation ) ;

        }   // end createAPassenger()


    /**
     * Creates a set of passengers sending each to their starting stations
     *
     * @param passengerCountLimits
     *     range of number of passengers to create
     */
    private void createPassengers( PairedLimit passengerCountLimits )
        {
        int minimumPassengersCount = passengerCountLimits.minimum ;
        int maximumPassengersCount = passengerCountLimits.maximum ;

        // determine the number of passengers to create
        int newPassengerCount = minimumPassengersCount == maximumPassengersCount
                                   ? minimumPassengersCount
                                   : this.pseudoRandom.nextInt( maximumPassengersCount -
                                                                minimumPassengersCount ) +
                                       minimumPassengersCount + 1 ;

        this.logger.printf( "%nGenerating %d passenger%s:%n%n",
                       newPassengerCount,
                       ( newPassengerCount == 1
                           ? ""
                           : "s" ) ) ;

        for ( int passengerCount = 1 ;
              passengerCount <= newPassengerCount ;
              passengerCount++ )
            {
            createAPassenger() ;
            }   // end for()

        }   // end createPassengers

    
    /*
     * callback support
     */

    /**
     * Utility method to retrieve the configuration.
     *
     * @return the configuration
     */
    public Configuration getConfiguration()
        {
        return this.theConfiguration ;

        }   // end getConfiguration()

    /**
     * Utility method to retrieve the current time/tick.
     *
     * @return the current time/tick
     */
    public int getCurrentTime()
        {
        return this.currentTime ;

        }   // end getCurrentTime()

    
    /**
     * Utility method to set the current time/tick.
     * 
     * @param newTime
     *     the new value for currentTime
     * @return the previous time/tick
     */
    int setCurrentTime( int newTime )
        {
        int savedTime = this.currentTime ;
        this.currentTime = newTime ;
        
        return savedTime ;

        }   // end setCurrentTime()
    
    
    /**
     * Utility method to retrieve the current Logger instance
     * 
     * @return the current Logger
     */
    public Logger getLogger()
        {
        return this.logger ;
        
        }   // end getLogger()
    
    
    /**
     * Utility method to retrieve the current Random instance
     * 
     * @return the current Random
     */
    public Random getPseudoRandom()
        {
        return this.pseudoRandom ;
        
        }   // end getPseudoRandom()
    
    
    /**
     * Utility method to retrieve the current TrainRoute instance
     * 
     * @return the current TrainRoute
     */
    public TrainRoute getTrainRoute()
        {
        return this.theRoute ;
        
        }   // end getTrainRoute()

    
    /*
     * miscellaneous private utilities
     */

    /**
     * Utility method to write simulation state into the log
     *
     * @param description
     *     brief introductory label
     */
    private void logSnapshot( String description )
        {
        // descriptive message
        this.logger.printf( description ) ;

        // log the route state
        this.logger.printf( this.theRoute.describe() ) ;

        }   // end logSnapshot()


    /**
     * Utility method to report concluding statistics
     */
    private void reportStatistics()
        {
        // TODO implement this
        
        }   // end statistics()


    /**
     * Runs the simulation
     */
    private void run()
        {
        PairedLimit passengerCountLimits =
                                        this.theConfiguration.getPassengers()[ Configuration.PASSENGERS_PER_TICK ] ;
        
        int ticks = this.theConfiguration.getTicks() ;
        
        // TODO complete this

        }   // end run


    /**
     * Builds the route - including stations and trains - then populates the stations
     * with passengers
     */
    private void setup()
        {
        this.logger.printf( "Setting up:%n" ) ;

        /*
         * create the route(s)
         */
        this.logger.printf( "%nCreating %,d route%s:%n%n", 1, "" ) ;

        // create the route for the specified configuration
        this.theRoute = new TrainRoute( this.theConfiguration.getRoute() ) ;

        this.logger.printf( this.theRoute.describe() ) ;

        /*
         * add the stations
         */
        int numberOfStations = this.theConfiguration.getStations().length ;

        this.logger.printf( "%nCreating %,d station%s:%n%n",
                            numberOfStations,
                            ( numberOfStations == 1
                                ? ""
                                : "s" ) ) ;

        for ( int stationLocation : this.theConfiguration.getStations() )
            {
            Station newStation = new Station( this.theRoute, stationLocation ) ;

            this.logger.printf( newStation.describe() ) ;

            this.theRoute.addStation( newStation ) ;
            }

        /*
         * add the trains
         */
        int numberOfTrains = this.theConfiguration.getTrains().length ;

        this.logger.printf( "%nCreating %,d train%s:%n%n",
                            numberOfTrains,
                            ( numberOfTrains == 1
                                ? ""
                                : "s" ) ) ;

        for ( TrainSpecification trainSpecification : this.theConfiguration.getTrains() )
            {
            Train newTrain = new Train( this.theRoute, trainSpecification ) ;

            this.logger.printf( newTrain.describe() ) ;

            this.theRoute.addTrain( newTrain ) ;
            }

        /*
         * populate the stations with the initial set of passengers
         */
        createPassengers( this.theConfiguration.getPassengers()[ Configuration.PASSENGERS_INITIAL ] ) ;

        // log the starting configuration
        logSnapshot( "%n----------%n%nStarting state%n" ) ;

        }   // end setup()


    /**
     * Simulation driver
     *
     * @param args
     *     -unused-
     * @throws FileNotFoundException
     *     if the configuration file isn't found or is inaccessible
     * @throws IOException
     *     if the logs folder doesn't exist and it can't be created
     */
    public static void main( String[] args ) throws FileNotFoundException, IOException
        {
        TrainSimulation theSimulation = null ;
        try
            {
            // create TrainSimulation instance - loads the configuration and pre-populates
            // the Stations with Passengers
            theSimulation = new TrainSimulation() ;

            // run the simulation
            System.out.printf( "Running...%n" ) ;
            theSimulation.run() ;

            // log the concluding configuration
            theSimulation.logSnapshot( "%n----------%n%nConcluding state%n" ) ;

            // report on the results of the simulation run
            theSimulation.reportStatistics() ;

            System.out.printf( "%nDone.%n" ) ;
            }
        catch ( Exception e )
            {
            System.err.printf( "Unexpected exception: %s%n", e.getMessage() ) ;
            }
        finally
            {
            // flush the log and release the resources
            if ( theSimulation != null )
                {
                theSimulation.logger.close() ;
                }
            }

        }   // end main()

    }   // end class TrainSimulation
