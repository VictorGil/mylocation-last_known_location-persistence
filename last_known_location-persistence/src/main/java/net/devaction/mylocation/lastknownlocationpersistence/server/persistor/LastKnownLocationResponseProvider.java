package net.devaction.mylocation.lastknownlocationpersistence.server.persistor;

import net.devaction.mylocation.lastknownlocationapi.protobuf.LastKnownLocationResponse;
import net.devaction.mylocation.locationpersistenceapi.protobuf.LocationPersistenceRequest;

/**
 * @author Víctor Gil
 *
 * since December 2018
 */
public interface LastKnownLocationResponseProvider{
    
    public LastKnownLocationResponse provide(LocationPersistenceRequest persistenceRequest);
}

