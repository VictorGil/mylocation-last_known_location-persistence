package net.devaction.mylocation.lastknownlocationpersistence.server.persistor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.mylocation.lastknownlocationapi.protobuf.LastKnownLocationResponse;
import net.devaction.mylocation.lastknownlocationapi.protobuf.Status;
import net.devaction.mylocation.locationpersistenceapi.protobuf.LocationPersistenceRequest;

/**
 * @author Víctor Gil
 *
 * since December 2018
 */
public class LastKnownLocationResponseProviderImpl implements LastKnownLocationResponseProvider{
    private static final Logger log = LoggerFactory.getLogger(LastKnownLocationResponseProviderImpl.class);

    @Override
    public LastKnownLocationResponse provide(LocationPersistenceRequest persistenceRequest){
        LastKnownLocationResponse.Builder locationResponseBuilder = LastKnownLocationResponse.newBuilder();

        if (persistenceRequest.hasLatitude())
            locationResponseBuilder.setLatitude(persistenceRequest.getLatitude());
        
        if (persistenceRequest.hasLongitude())
            locationResponseBuilder.setLongitude(persistenceRequest.getLongitude());
        
        if (persistenceRequest.hasHorizontalAccuracy())
            locationResponseBuilder.setHorizontalAccuracy(persistenceRequest.getHorizontalAccuracy());        
                
        if (persistenceRequest.hasAltitude())
            locationResponseBuilder.setAltitude(persistenceRequest.getAltitude());

        if (persistenceRequest.hasVerticalAccuracy())
            locationResponseBuilder.setVerticalAccuracy(persistenceRequest.getVerticalAccuracy());
        
        if (persistenceRequest.hasTimeChecked())
            locationResponseBuilder.setTimeChecked(persistenceRequest.getTimeChecked());
        
        if (persistenceRequest.hasTimeMeasured())
            locationResponseBuilder.setTimeMeasured(persistenceRequest.getTimeMeasured());
        
        locationResponseBuilder.setStatus(Status.SUCCESS);
        
        LastKnownLocationResponse locationResponse = locationResponseBuilder.build(); 
        
        log.trace("Last known location response to be sent:\n" + locationResponse);
        return locationResponse;
    }
}
