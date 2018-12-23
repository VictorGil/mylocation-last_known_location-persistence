package net.devaction.mylocation.lastknownlocationpersistence.config;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

/**
 * @author Víctor Gil
 *
 * since December 2018
 */
public class ConfigValuesProvider implements AddressProvider, FilePathProvider{
    private static final Logger log = LoggerFactory.getLogger(ConfigValuesProvider.class);

    private static JsonObject locationPersistenceServiceConfig;
    
    private BaseDirProvider baseDirProvider;
    
    @Override
    public String provideAddress(){
                
        //the service needs to listen on this address
        return locationPersistenceServiceConfig.getString("event_bus_location_persist_address");
    }

    @Override
    public String provideFilePath(){
        String relativeFilePath = locationPersistenceServiceConfig.getString("file_path");
        String baseDir = baseDirProvider.provide();
        String filePath = baseDir + File.separator + relativeFilePath;
        log.info("Fiel path to write/persist the latest known location: " + filePath);
        
        return filePath; 
    }    
    
    public static void setLocationPersistenceServiceConfig(JsonObject locationPersistenceServiceConfig){
        ConfigValuesProvider.locationPersistenceServiceConfig = locationPersistenceServiceConfig;
    }

    public void setBaseDirProvider(BaseDirProvider baseDirProvider){
        this.baseDirProvider = baseDirProvider;
    }
}

