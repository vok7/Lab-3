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
import edu.wit.scds.comp2000.queue.app.utilities.RouteSpecification ;
import edu.wit.scds.comp2000.queue.app.utilities.RouteStyle ;

import java.io.FileNotFoundException ;
import java.io.IOException ;
import java.util.ArrayList ;

/**
 * Representation of a train route consisting of a pair of parallel tracks,
 * Station(s) at fixed locations along the tracks, and Train(s) that move along the
 * tracks. Each Station serves both tracks. No two Stations are at the same location.
 * Two trains may be passing each other at the same location however only one per
 * track.
 * <p>
 * <b>WARNING</b>: Some CIRCULAR route functionality is not yet implemented!
 * <p>
 * NOTE: If you modify this class, you must retain all authorship comments. You
 * should add/update your authorship and modification history tags after the existing
 * tags.
 *
 * @author David M Rosenberg
 * @version 0.1.0 base version
 * @version 1.0.0 initial implementation
 * @version 1.1.0 2019-10-30 replace iteration with indexing of {@code stations} and
 *     {@code trains} in {@code getXxx( int )}.
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
public final class TrainRoute
    {

    // class-wide/shared information
    private static int nextId = 1 ; // enables automatic id assignment

    private static TrainSimulation simulationCallback ;   // provides access to simulation state

    // per-instance fields

    /** the unique identifier for this TrainRoute */
    private final int id ;          // unique id for this train route

    private final RouteStyle style ;
    private final int length ;

    private ArrayList<Station> stations = null ;
    private ArrayList<Train> trains = null ;

    
    /**
     * @param routeSpecification
     *     the route style and length from the configuration file
     */
    public TrainRoute( RouteSpecification routeSpecification )
        {
        // assign the next unique id
        this.id = TrainRoute.nextId++ ;

        // save the configuration parameters
        this.style = routeSpecification.style ;
        this.length = routeSpecification.length ;

        // instantiate the collections for stations and trains
        this.stations = new ArrayList<>() ;
        this.trains = new ArrayList<>() ;

        }   // end constructor

    
    /*
     * getter methods
     */


    /**
     * Retrieves the route id
     * 
     * @return the route id was set when the route was instantiated
     */
    public int getId()
        {
        return this.id ;

        }   // end getId()


    /**
     * Retrieves the route length as specified in the configuration file
     * 
     * @return the route length was set when the route was instantiated
     */
    public int getLength()
        {
        return this.length ;

        }   // end getLength()


    /**
     * Retrieves the route style as specified in the configuration file
     * 
     * @return the route style was set when the route was instantiated
     */
    public RouteStyle getStyle()
        {
        return this.style ;

        }   // end getStyle()

    
    /*
     * functional methods
     */


    /**
     * Add a station to this route. If the station is not already assigned a route,
     * it is placed on this route.
     * 
     * @param newStation
     *     the station object to add
     */
    public void addStation( Station newStation )
        {
        // assign our route to the station if it doesn't already have one
        if ( newStation.getLocation().getRoute() == null )
            {
            newStation.getLocation().setRoute( this ) ;
            }

        // add the station to our collection of stations
        this.stations.add( newStation ) ;

        }   // end addStation()


    /**
     * Add a train to this route. If the train is not already assigned a route, it is
     * placed on this route.
     * 
     * @param newTrain
     *     the train object to add
     */
    public void addTrain( Train newTrain )
        {
        // assign our route to the train if it doesn't already have one
        if ( newTrain.getLocation().getRoute() == null )
            {
            newTrain.getLocation().setRoute( this ) ;
            }

        // add the station to our collection of stations
        this.trains.add( newTrain ) ;

        }   // end addTrain()


    /**
     * Retrieve a station by id
     * 
     * @param targetId
     *     the id for the desired station
     * @return the station with the specified id or {@code null} if none
     */
    public Station getStation( int targetId )
        {
        if ( ( targetId >= 1 ) && ( targetId <= this.stations.size() ) )
            {
            return this.stations.get( targetId - 1 ) ;
            }

        return null ;   // invalid id

        }   // end getStation() by id


    /**
     * Retrieve a station at a given location
     * 
     * @param atLocation
     *     the location for the desired station
     * @return the station at the specified location or {@code null} if none
     */
    public Station getStationAt( Location atLocation )
        {
        for ( Station aStation : this.stations )
            {
            if ( aStation.getLocation().equals( atLocation ) )
                {
                return aStation ;
                }
            }

        return null ;

        }   // end getStationAt() given location


    /**
     * Retrieve the number of stations currently on this route
     * 
     * @return the number of stations currently on this route
     */
    public int getStationCount()
        {
        return this.stations.size() ;

        }   // end getStationCount()


    /**
     * Retrieve a train by id
     * 
     * @param targetId
     *     the id for the desired train
     * @return the train with the specified id or {@code null} if none
     */
    public Train getTrain( int targetId )
        {
        if ( ( targetId >= 1 ) && ( targetId <= this.trains.size() ) )
            {
            return this.trains.get( targetId - 1 ) ;
            }

        return null ;   // invalid id

        }   // end getTrain() by id


    /**
     * Retrieve a train by location
     * 
     * @param atLocation
     *     the location for the desired train
     * @return the train at the specified location or {@code null} if none
     */
    public Train getTrainAt( Location atLocation )
        {
        for ( Train aTrain : this.trains )
            {
            if ( aTrain.getLocation().equals( atLocation ) )
                {
                return aTrain ; // found it
                }
            }

        return null ;   // didn't find it

        }   // end getTrainAt() by location


    /**
     * Retrieve the number of trains currently on this route
     * 
     * @return the number of trains currently on this route
     */
    public int getTrainCount()
        {
        return this.trains.size() ;

        }   // end getTrainCount()
    
    
    /**
     * Retrieve the stations on the route
     *
     * @return an array containing the stations on the route
     */
    public Station[] getStations()
        {
        return this.stations.toArray( new Station[ this.stations.size() ] ) ;
        
        }   // end getStations()
    
    
    /**
     * Retrieve the trains on the route
     *
     * @return an array containing the trains on the route
     */
    public Train[] getTrains()
        {
        return this.trains.toArray( new Train[ this.trains.size() ] ) ;
        
        }   // end getTrains()


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

        if ( ( obj == null ) || ! ( obj instanceof TrainRoute ) )
            {
            return false ;
            }

        TrainRoute other = (TrainRoute) obj ;
        return this.id == other.id ;

        }   // end equals()


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

        fullDescription.append( String.format( "%s: %s; length: %,d%n",
                                               toString(),
                                               this.style,
                                               this.length ) ) ;

        fullDescription.append( String.format( "%n%,d station%s:%n",
                                               getStationCount(),
                                               ( getStationCount() == 1
                                                   ? ""
                                                       : "s" ) ) ) ;

        for ( Station aStation : this.stations )
            {
            fullDescription.append( String.format( "\t%s%n",
                                                   aStation.describe() ) ) ;
            }

        fullDescription.append( String.format( "%,d train%s:%n",
                                               getTrainCount(),
                                               ( getTrainCount() == 1
                                                   ? ""
                                                   : "s" ) ) ) ;

        for ( Train aTrain : this.trains )
            {
            fullDescription.append( String.format( "\t%s%n",
                                                   aTrain.describe() ) ) ;
            }

        return fullDescription.toString() ;

        }   // end describe()


    /**
     * Determines the direction an entity needs to travel to move from the 'from'
     * station to the 'to' station. It takes into account the route style.
     * 
     * @param fromStationId
     *     starting point station id #
     * @param toStationId
     *     ending point station id #
     * @return the Direction in which an entity must travel
     */
    public static Direction whichDirection( int fromStationId,
                                            int toStationId )
        {
        return whichDirection( TrainRoute.simulationCallback.getTrainRoute()
                                                            .getStation( fromStationId ),
                               TrainRoute.simulationCallback.getTrainRoute()
                                                            .getStation( toStationId ) ) ;

        }   // end whichDirection() given Station Ids


    /**
     * Determines the direction an entity needs to travel to move from the 'from'
     * station to the 'to' station. It takes into account the route style.
     * 
     * @param fromStation
     *     starting point station instance
     * @param toStation
     *     ending point station instance
     * @return the Direction in which an entity must travel
     */
    public static Direction whichDirection( Station fromStation,
                                            Station toStation )
        {
        return whichDirection( fromStation.getLocation(), toStation.getLocation() ) ;
        
        }   // end whichDirection() given Stations


    /**
     * Determines the Direction an entity must move to travel between <i>from</i> and
     * <i>to</i>
     * <p>
     * <b>WARNING</b>: As implemented, this method only works correctly for LINEAR
     * routes!
     * 
     * @param fromLocation
     *     the Location at the start of travel
     * @param toLocation
     *     the Location at the destination
     * @return the Direction in which an entity must travel
     */
    public static Direction whichDirection( Location fromLocation,
                                            Location toLocation )
        {
        Direction calculatedDirection = Direction.NOT_APPLICABLE ;
        if ( fromLocation.getRoute().equals( toLocation.getRoute() ) )
            {   // same route so continue
            int comparison = fromLocation.compareTo( toLocation ) ;
            if ( comparison < 0 )
                {
                calculatedDirection = Direction.OUTBOUND ;
                }
            else if ( comparison > 0 )
                {
                calculatedDirection = Direction.INBOUND ;
                }
            else                            // at same location
                {
                calculatedDirection = Direction.STATIONARY ;
                }
            }
// DMR ENHANCEMENT for multi-route, need to find a transfer point

        return calculatedDirection ;
        
        }   // end whichDirection() given Locations
    
    
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
        TrainRoute.simulationCallback = currentSimulation ;
        
        }   // end setSimulationCallback()


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
        
        Configuration theConfiguration = TrainRoute.simulationCallback.getConfiguration() ;

        TrainRoute theRoute = TrainRoute.simulationCallback.getTrainRoute() ;

        System.out.printf( "Using configuration:%n\t%s%n", theConfiguration.getRoute() ) ;
        System.out.printf( "The result is:%n\t%s is %s with length %,d%n",
                           theRoute,
                           theRoute.getStyle(),
                           theRoute.getLength() ) ;
        System.out.println( theRoute.describe() ) ;

        System.out.println() ;
        Location fromLocation = new Location( theRoute,
                                              3,
                                              Direction.NOT_SPECIFIED ) ;
        Location toLocation = new Location( theRoute, 3, Direction.NOT_SPECIFIED ) ;
        System.out.printf( "From: %s --> to: %s --> is: %s%n",
                           fromLocation,
                           toLocation,
                           whichDirection( fromLocation, toLocation ) ) ;

        toLocation.setPosition( 5 ) ;
        System.out.printf( "From: %s --> to: %s --> is: %s%n",
                           fromLocation,
                           toLocation,
                           whichDirection( fromLocation, toLocation ) ) ;

        toLocation.setPosition( 1 ) ;
        System.out.printf( "From: %s --> to: %s --> is: %s%n",
                           fromLocation,
                           toLocation,
                           whichDirection( fromLocation, toLocation ) ) ;

//        @formatter:off
//        // DMR TODO - support for handling CIRCULAR routes is not implemented
//        System.out.println() ;
//        RouteSpecification circularRouteSpecification = theConfig.new RouteSpecification( RouteStyle.CIRCULAR,
//                                                               20 ) ;
//        TrainRoute circularRoute = new TrainRoute( circularRouteSpecification ) ;
//        fromLocation.setRoute( circularRoute ) ;
//        toLocation.setRoute( circularRoute ) ;
//        toLocation.setPosition( 3 ) ;
//        System.out.printf( "From: %s --> to: %s --> is: %s%n",
//                           fromLocation,
//                           toLocation,
//                           circularRoute.whichDirection( fromLocation, toLocation ) ) ;
//        @formatter:on

        // DMR TODO need to test remaining methods

        }   // end main()

    }   // end class TrainRoute
