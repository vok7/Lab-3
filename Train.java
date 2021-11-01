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
import edu.wit.scds.comp2000.queue.app.utilities.TrainSpecification ;

import java.io.FileNotFoundException ;
import java.util.ArrayList ;
import java.util.Arrays ;
import java.util.Iterator ;
import java.util.Objects ;

/**
 * Representation of a train on a train route. A Train has a fixed, limited capacity
 * to carry Passengers. Passengers board() and disembark().
 * <p>
 * NOTE: This class is incomplete - you may want to restructure it based on your
 * implementation's requirements.
 * <p>
 * <b>WARNING</b>: Some CIRCULAR route functionality is not yet implemented!
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
public final class Train
    {

    // class-wide/shared information
    private static int nextId = 1 ; // enables automatic id assignment

    private static TrainSimulation simulationCallback ;   // provides access to simulation state

    // per-instance fields

    /** the unique identifier for this Train */
    private final int id ;              // unique id for this train

    private final int capacity ;
    private Location currentLocation ;
    private ArrayList<Passenger> passengers ;

    /**
     * @param onRoute
     *     the instance of the TrainRoute on which this Train operates
     * @param trainSpecification
     *     the specifications from the configuration file
     */
    public Train( TrainRoute onRoute, TrainSpecification trainSpecification )
        {
        this.id = Train.nextId++ ;  // assign the next unique id

        // create an empty collection to hold Passengers while they're on board
        this.passengers = new ArrayList<>() ;

        // save the configuration parameters
        this.capacity = trainSpecification.capacity ;
        this.currentLocation = new Location( onRoute,
                                             trainSpecification.location,
                                             trainSpecification.direction ) ;

        }   // end constructor


    /**
     * Retrieves the capacity (maximum number of Passengers simultaneously on this
     * train) as specified in the configuration file
     * 
     * @return the capacity was set when the train was instantiated
     */
    public int getCapacity()
        {
        return this.capacity ;

        }   // end getCapacity()


    /**
     * Retrieves the id for this train
     * 
     * @return the train id was set when the train was instantiated
     */
    public int getId()
        {
        return this.id ;

        }   // end getId()


    /**
     * Retrieves the current location along a route
     * 
     * @return the current location object
     */
    public Location getLocation()
        {
        return this.currentLocation ;

        }   // end getLocation()


    /**
     * Retrieves the number of passengers currently on-board
     * 
     * @return the number of passengers currently on-board
     */
    public int getPassengerCount()
        {
        return this.passengers.size() ;

        }   // end getPassengerCount()


    /**
     * Close the doors
     * 
     * @param aStation
     *     the station we are at
     */
    private void closeDoors( Station aStation )
        {
        // TODO log pending departure ??
        
        }   // end closeDoors()


    /**
     * Open the doors
     * 
     * @param aStation
     *     the station we are at
     */
    private void openDoors( Station aStation )
        {
        // inform the station of our arrival
        aStation.trainArrived( this ) ;

        // TODO other options?  what else to do (if anything)...?

        }   // end openDoors()


    /**
     * Let passengers get off the train if at their destination
     * 
     * @param aStation
     *     the station we are at
     */
    private void offloadPassengers( Station aStation )
        {
        // TODO ??

        }   // end offloadPassengers()


    /**
     * Let passengers board
     * 
     * @param aStation
     *     the station we are at
     */
    private void boardPassengers( Station aStation )
        {
        // TODO you might use depending upon your logic

        }   // end boardPassengers()


    /**
     * Board a single passenger
     * 
     * @param aPassenger
     *     the passenger getting on
     */
    private void board( Passenger aPassenger )
        {
        // set the time the passenger boarded
        aPassenger.boardTrain();

        // welcome them on-board
        // TODO add the passenger to our collection of Passengers

        }   // end board()


    /**
     * Perform these steps when arriving at a station:
     * <ul>
     * <li>open doors
     * <li>let passengers disembark
     * <li>let passengers board
     * <li>close doors
     * </ul>
     * 
     * @param aStation
     *     the station we arrived at
     */
    public void atStation( Station aStation )
        {
        // TODO this is a sample scenario - there are other good options - you may
        // need to change the visibility of some of these methods if your logic is
        // different from mine
        
        openDoors( aStation ) ;

        offloadPassengers( aStation ) ;

        boardPassengers( aStation ) ;

        closeDoors( aStation ) ;

        // we're ready to leave the station

        }   // end atStation()


    /**
     * Move forward one unit of time/distance
     */
    public void move()
        {
        this.currentLocation.move() ;

        Station aStation = this.currentLocation.getRoute()
                                               .getStationAt( this.currentLocation ) ;
        String atStation = ( aStation == null
                                ? ""
                                : ( " at " + aStation.toString() ) ) ;

        Train.simulationCallback.getLogger()
                      .printf( "%n%s is moving to %s%s%n",
                               this,
                               this.currentLocation,
                               atStation ) ;

        }   // end move()


    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
        {
        return Objects.hashCode( this.id ) ;

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

        if ( ( obj == null ) || ! ( obj instanceof Train ) )
            {
            return false ;
            }

        Train otherTrain = (Train) obj ;
        return this.id == otherTrain.id ;

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

        Station aStation = this.currentLocation.getRoute()
                                               .getStationAt( this.currentLocation ) ;
        String atStation = ( aStation == null
                                ? ""
                                : ( " at " + aStation.toString() ) ) ;

        fullDescription.append( String.format( "%s is %s%s; capacity: %,d; on-board: %,d%n",
                                               toString(),
                                               this.currentLocation,
                                               atStation,
                                               this.capacity,
                                               this.passengers.size() ) ) ;

        fullDescription.append( String.format( "\tPassengers: %s%n",
                                               this.passengers ) ) ;

        return fullDescription.toString() ;

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
        Train.simulationCallback = currentSimulation ;
        
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
        Configuration theConfiguration = new Configuration() ;
        TrainRoute theRoute = new TrainRoute( theConfiguration.getRoute() ) ;
        TrainSpecification[] theTrainSpecifications = theConfiguration.getTrains() ;

        System.out.printf( "Using configuration:%n\t%s%n",
                           Arrays.toString( theTrainSpecifications ) ) ;

        System.out.printf( "The result is:%n" ) ;

        for ( TrainSpecification aTrainSpecification : theTrainSpecifications )
            {
            Train aTrain = new Train( theRoute, aTrainSpecification ) ;
            System.out.printf( "\t%s is %s with capacity %,d%n",
                               aTrain,
                               aTrain.currentLocation,
                               aTrain.capacity ) ;
            }   // end foreach()

        }   // end main()

    }   // end class Train
