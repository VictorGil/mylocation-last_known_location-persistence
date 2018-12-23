package net.devaction.mylocation.lastknownlocationpersistence.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since December 2018
 */
public class BaseDirProviderImpl implements BaseDirProvider{
    private static final Logger log = LoggerFactory.getLogger(BaseDirProviderImpl.class);
    
    private static final String BASE_DIR = "base.dir";
    
    @Override
    public String provide(){
        String baseDir = System.getProperty(BASE_DIR);
        log.info("Base directory: " + baseDir);
        
        if (baseDir == null || baseDir.isEmpty()) {
            String errMessage = "FATAL: the " + BASE_DIR + " Java system property must be set";
            log.error(errMessage);
            throw new RuntimeException("errMessage");
        }
        return baseDir;
    }  
}

