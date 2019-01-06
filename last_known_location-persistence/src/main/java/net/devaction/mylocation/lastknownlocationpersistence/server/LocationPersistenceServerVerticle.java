package net.devaction.mylocation.lastknownlocationpersistence.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import net.devaction.mylocation.lastknownlocationpersistence.config.AddressProvider;

/**
 * @author Víctor Gil
 *
 * since December 2018
 */
public class LocationPersistenceServerVerticle extends AbstractVerticle implements InitializingBean{
    private static final Logger log = LoggerFactory.getLogger(LocationPersistenceServerVerticle.class);
    
    //Spring should set this value from the configuration
    //private String address = "last_known_location_persist";
    private String address;
    private AddressProvider addressProvider;
    
    private LocationPersistenceServerHandler handler;
    
    @Override
    public void afterPropertiesSet() throws Exception{
        if (address == null)
            address = addressProvider.provideAddress();
        
        log.info("Vert.x event bus address to listen for request messages: " + address);        
    }  
    
    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        
        MessageConsumer<Buffer> consumer = eventBus.consumer(address, handler);
        
        consumer.completionHandler(asyncResult -> {
            if (asyncResult.succeeded()) {
                log.info("The " + handler.getClass().getSimpleName() + 
                        " has been successfully registered and it started listening for messages on " + address + " event bus address.");
            } else {
                log.error("FATAL: Registration of " + handler.getClass().getSimpleName() + " has failed.");
            }
        });        
    }

    public void setHandler(LocationPersistenceServerHandler handler){
        this.handler = handler;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setAddressProvider(AddressProvider addressProvider){
        this.addressProvider = addressProvider;
    }  
}

