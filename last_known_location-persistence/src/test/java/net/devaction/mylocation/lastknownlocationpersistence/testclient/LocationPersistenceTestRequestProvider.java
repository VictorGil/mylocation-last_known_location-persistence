package net.devaction.mylocation.lastknownlocationpersistence.testclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.mylocation.locationpersistenceapi.protobuf.LocationPersistenceRequest;

/**
 * @author Víctor Gil
 *
 * since December 2018
 */
public class LocationPersistenceTestRequestProvider{
    private static final Logger log = LoggerFactory.getLogger(LocationPersistenceTestRequestProvider.class);

    public static LocationPersistenceRequest provide(){
        
        LocationPersistenceRequest.Builder reqBuilder = LocationPersistenceRequest.newBuilder();
        reqBuilder.setTimestamp(System.currentTimeMillis() / 1000);
        
        reqBuilder.setLatitude("5");
        reqBuilder.setLongitude("5");
        reqBuilder.setHorizontalAccuracy("0.05");
                
        reqBuilder.setAltitude("5");
        reqBuilder.setVerticalAccuracy("0.1");
        
        reqBuilder.setTimeChecked(System.currentTimeMillis() / 1000 - 60 * 60); //one hour ago
        reqBuilder.setTimeMeasured(System.currentTimeMillis() / 1000 - 60 * 60 * 2); //two hours ago
        
        return reqBuilder.build();
    }
}

