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
import edu.wit.scds.comp2000.queue.app.utilities.Direction ;
import edu.wit.scds.comp2000.queue.app.utilities.Location ;
import edu.wit.scds.comp2000.queue.app.utilities.RouteStyle ;

import java.io.FileNotFoundException ;
import java.util.Arrays ;
import java.util.HashMap ;
import java.util.LinkedList ;
import java.util.Queue ;

/**
 * Representation of a station on a train route. A Station has two platforms (queues)
 * where Passengers wait before boarding a train. Passengers wait on the platform
 * which serves trains traveling in the direction that can take them to their
 * destination in the least time.
 * <p>
 * NOTE: This class is incomplete - you may want to restructure it based on your
 * implementation's requirements.
 * <p>
 * NOTE: You must retain all authorship comments. You should add/update your
 * authorship and modification history tags after the existing tags.
 *
 * @author Dave Rosenberg
 * @version 0.1.0 base version
 * @version 1.0.0 initial implementation
 * @version 2.0.0 2021-10-30
 *     <ul>
 *     <li>restructure to use simulation callback instead of parameter passing</li>
 *     <li>major restructuring</li>
 *     <li>clean up</li>
 *     </ul>
 * 
 * @author Your Name
 * @version 2.1.0 2021-11-01 finish implementation
 */
public final class Station
    {

    // class-wide/shared information
    private static int nextId = 1 ; // enables automatic id assignment

    private static TrainSimulation simulationCallback ;   // provides access to simulation state

    // per-instance fields

    /** the unique identifier for this Station */
    public final int id ;           // unique id for this station

    private final Location location ;
    private HashMap<Direction, Queue<Passenger>> platforms ;

    /**
     * @param onRoute
     *     the instance of the TrainRoute on which this Train operates
     * @param positionOnRoute
     *     the specifications from the configuration file
     */
    public Station( TrainRoute onRoute, int positionOnRoute )
        {
        this.id = Station.nextId++ ;    // assign the next unique id

        // create a collection of platforms, determine the directions based on the
        // route style,
        // and create a pair of platforms indexable by the direction they service
        this.platforms = new HashMap<>() ;

        Direction oneDirection = onRoute.getStyle() == RouteStyle.LINEAR
                                        ? Direction.OUTBOUND
                                        : Direction.CLOCKWISE ;
        this.platforms.put( oneDirection, new LinkedList<Passenger>() ) ;
        this.platforms.put( oneDirection.reverse(), new LinkedList<Passenger>() ) ;

        // save the position along the route
        this.location = new Location( onRoute,
                                      positionOnRoute,
                                      Direction.STATIONARY ) ;

        }   // end constructor


    /**
     * Retrieves the id for this station
     * 
     * @return the station id was set when the station was instantiated
     */
    public int getId()
        {
        return this.id ;

        }   // end getId()


    /**
     * Retrieves the location for this station
     * 
     * @return the location object for this station
     */
    public Location getLocation()
        {
        return this.location ;

        }   // end getLocation()


    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
        {
        return this.id ;

        }   // end hashCode()


    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj )
        {
        if ( this == obj )
            {
            return true ;
            }

        if ( ( obj == null ) || ! ( obj instanceof Station ) )
            {
            return false ;
            }

        Station other = (Station) obj ;
        return this.id == other.id ;

        }   // end equals()


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
     * Superset of functionality of toString()
     * 
     * @return all instance information formatted for human consumption
     */
    public String describe()
        {
        StringBuilder fullDescription = new StringBuilder() ;

        fullDescription.append( String.format( "%s is %s%n",
                                               toString(),
                                               this.location ) ) ;

        for ( Direction platformSelector : this.platforms.keySet() )
            {
            fullDescription.append( String.format( "\t%s: %s%n",
                                                   platformSelector,
                                                   this.platforms.get( platformSelector ) ) ) ;
            }

        return fullDescription.toString() ;

        }   // end toStringFull()


    /**
     * Log the arrival of a train
     * 
     * @param aTrain
     *     the train that arrived
     */
    public void trainArrived( Train aTrain )
        {
        Station.simulationCallback.getLogger()
                        .printf( "%n%s arrived at %s's %s platform%n",
                                 aTrain,
                                 this,
                                 aTrain.getLocation().getDirection() ) ;

        // TODO make this more interesting ???

        }   // end trainArrived()


    /**
     * Handle a passenger arrival (disembarking from a train)
     * 
     * @param aPassenger
     *     the arriving passenger
     * @param aTrain
     *     the train the passenger arrived on
     */
    public void passengerDisembarked( Passenger aPassenger,
                                      Train aTrain )
        {
        // indicate when they arrived
        aPassenger.disembarkTrain() ;

        // the passenger now exits the station/simulation
        exit( aPassenger ) ;

        // TODO additional logging ?

        }   // end passengerDisembarked()


    /**
     * The passenger has completed their journey and is leaving the simulation
     * 
     * @param aPassenger
     *     the happy passenger
     */
    private void exit( Passenger aPassenger )
        {
        // the passenger is at their destination and leaving the station
        aPassenger.exitStation() ;

        Station.simulationCallback.getLogger()
                        .printf( "%s arrived after waiting %,d ticks and riding %,d ticks; total travel time was %,d ticks%n",
                                 aPassenger,
                                 aPassenger.getTimeWaiting(),
                                 aPassenger.getTimeRiding(),
                                 aPassenger.getTotalTime() ) ;
        
        // TODO accumulate statistics

        }   // end exit()


    /**
     * Indicate whether there are any passengers waiting to go in the specified
     * direction
     * 
     * @param goingInDirection
     *     the direction the train is traveling
     * @return true if the platform exists and has at least one passenger on it and
     *     false otherwise
     */
    public boolean hasWaitingPassenger( Direction goingInDirection )
        {
        Queue<Passenger> selectedPlatform = this.platforms.get( goingInDirection ) ;

        return selectedPlatform == null
                    ? false
                    : !selectedPlatform.isEmpty() ;

        }   // hasWaitingPassenger()


    /**
     * Returns the next passenger waiting to go in the specified direction
     * 
     * @param goingInDirection
     *     the direction the train is traveling
     * @return the first passenger on the specified platform or null if the platform
     *     doesn't exist or there are no passengers waiting on it
     */
    public Passenger getWaitingPassenger( Direction goingInDirection )
        {
        Queue<Passenger> selectedPlatform = this.platforms.get( goingInDirection ) ;

        return null ;   // TODO retrieve a Passenger from this platform if at least one in the queue

        }   // getWaitingPassenger()


    /**
     * Guide a passenger entering this station to the appropriate platform
     * 
     * @param aPassenger
     *     a passenger traveling from here to elsewhere
     */
    public void enter( Passenger aPassenger )
        {
        Location from = this.location ;
        Location to = aPassenger.getDestination().getLocation() ;

        Direction platformSelector = null ; // TODO get direction from TrainRoute
                                // then add the Passenger to the appropriate platform queue

        Station.simulationCallback.getLogger()
                        .printf( "%s is waiting on %s's %s platform heading to %s%n",
                                 aPassenger,
                                 this,
                                 platformSelector,
                                 Station.simulationCallback.getTrainRoute().getStationAt( to ) ) ;

        }   // end enter()

    
    // TODO complete this
    
    
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
        Station.simulationCallback = currentSimulation ;
        
        }   // end setSimulationCallback()


    /**
     * Test driver
     * 
     * @param args
     *     -unused-
     * @throws FileNotFoundException
     *     if the configuration file doesn't exist or can't be opened for read access
     */
    public static void main( String[] args ) throws FileNotFoundException
        {
        Configuration theConfig = new Configuration() ;

        TrainRoute theRoute = new TrainRoute( theConfig.getRoute() ) ;
        int[] theStationSpecs = theConfig.getStations() ;

        System.out.printf( "Using configuration:%n\t%s%n",
                           Arrays.toString( theStationSpecs ) ) ;

        System.out.printf( "The result is:%n" ) ;

        for ( int stationPosition : theStationSpecs )
            {
            Station aStation = new Station( theRoute, stationPosition ) ;
            System.out.printf( "\t%s is %s%n%n\n",
                               aStation,
                               aStation.getLocation(),
                               aStation.describe() ) ;
            }   // end foreach()
        
        }   // end main()

    }   // end class TrainRoute
