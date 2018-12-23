package net.devaction.mylocation.lastknownlocationpersistence.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import net.devaction.mylocation.lastknownlocationpersistence.config.ConfigValuesProvider;
import net.devaction.mylocation.lastknownlocationpersistence.server.LocationPersistenceServerVerticle;
import net.devaction.mylocation.lastknownlocationpersistence.config.ConfigValuesProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Víctor Gil
 * 
 * since June 2018 
 */
public class MainVerticle extends AbstractVerticle{
    private static final Logger log = LoggerFactory.getLogger(MainVerticle.class);
    private JsonObject vertxConfig;
    
    private static final String LAST_KNOWN_LOCATION_SERVICE_CONFIG = "last_known_location_service_config";

    @Override
    public void start(){
        log.info("Starting " + this.getClass().getSimpleName());        

        ConfigRetriever retriever = ConfigRetriever.create(vertx);
        retriever.getConfig(asyncResult -> {
            if (asyncResult.failed()) {
                log.error("FATAL: Failed to retrieve configuration: " + asyncResult.cause(), asyncResult.cause());
                vertx.close(closeHandler -> {
                    log.info("Vert.x has been closed");
                });
            } else{
                vertxConfig = asyncResult.result();
                log.info("Retrieved configuration: " + vertxConfig);
                
                //This is a workaround, kind of        
                ConfigValuesProvider.setLocationPersistenceServiceConfig(vertxConfig.getJsonObject(LAST_KNOWN_LOCATION_SERVICE_CONFIG));
                
                ApplicationContext appContext = new ClassPathXmlApplicationContext("conf/spring/beans.xml");
                LocationPersistenceServerVerticle verticle = (LocationPersistenceServerVerticle) appContext.getBean("lastKnownLocationServerVerticle");
                ((ConfigurableApplicationContext) appContext).close();                
                
                //this is for the sun.misc.SignalHandler.handle method to be able to shutdown Vert.x
                LocationPersistenceMain.setVertx(vertx);
                
                deployVerticle(verticle);
            }
        });     
    }
    
    public void deployVerticle(LocationPersistenceServerVerticle verticle){
        log.info("Going to deploy " + LocationPersistenceServerVerticle.class.getSimpleName());
        vertx.deployVerticle(verticle, asyncResult -> {
            if (asyncResult.succeeded()){
                log.info("Successfully deployed " +  
                        LocationPersistenceServerVerticle.class.getSimpleName() + ". Result: " + asyncResult.result());                             
            } else{
                log.error("FATAL: Error when trying to deploy " + LocationPersistenceServerVerticle.class.getSimpleName());
                vertx.close(closeHandler -> {
                    log.info("vertx has been closed");
                });
            }
        });    
    }    

    @Override
    public void stop(){
        log.info(this.getClass().getSimpleName() + " verticle has been stopped");
    }
}

