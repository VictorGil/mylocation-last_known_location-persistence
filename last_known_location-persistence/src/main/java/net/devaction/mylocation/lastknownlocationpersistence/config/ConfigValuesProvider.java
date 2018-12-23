package net.devaction.mylocation.lastknownlocationpersistence.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

/**
 * @author Víctor Gil
 *
 * since December 2018
 */
public class ConfigValuesProvider implements AddressProvider{
    private static final Logger log = LoggerFactory.getLogger(ConfigValuesProvider.class);

    private static JsonObject locationPersistenceServiceConfig;

    
    public static void setLocationPersistenceServiceConfig(JsonObject locationPersistenceServiceConfig){
        ConfigValuesProvider.locationPersistenceServiceConfig = locationPersistenceServiceConfig;
    }


    @Override
    public String provideAddress(){
                
        //the service needs to listen on this address
        return locationPersistenceServiceConfig.getString("last_known_location_persist");
    }   
    
}


