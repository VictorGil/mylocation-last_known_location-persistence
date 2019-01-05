package net.devaction.mylocation.lastknownlocationpersistence.config;

import net.devaction.mylocation.vertxutilityextensions.config.ConfigValuesProvider;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since January 2019
 */
public class LastKnownLocationPersistenceConfigValuesProvider implements AddressProvider, FilePathProvider{
    private static final Logger log = LoggerFactory.getLogger(LastKnownLocationPersistenceConfigValuesProvider.class);
    
    private ConfigValuesProvider configValuesProvider;
    private PersistenceBaseDirProvider persistenceBaseDirProvider;
    
    @Override
    public String provideFilePath(){
        String relativeFilePath = configValuesProvider.getString("file_path");
        String baseDir = persistenceBaseDirProvider.provide();
        String filePath = baseDir + File.separator + relativeFilePath;
        log.info("File path to write/persist the latest known location: " + filePath);
        
        return filePath; 
    }

    @Override
    public String provideAddress(){
        //the service needs to listen on this address
        return configValuesProvider.getString("event_bus_location_persist_address");
    }

    public void setConfigValuesProvider(ConfigValuesProvider configValuesProvider){
        this.configValuesProvider = configValuesProvider;
    }

    public void setPersistenceBaseDirProvider(PersistenceBaseDirProvider persistenceBaseDirProvider){
        this.persistenceBaseDirProvider = persistenceBaseDirProvider;
    }
}

