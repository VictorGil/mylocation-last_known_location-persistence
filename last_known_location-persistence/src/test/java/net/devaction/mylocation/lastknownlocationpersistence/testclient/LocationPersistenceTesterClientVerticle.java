package net.devaction.mylocation.lastknownlocationpersistence.testclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import net.devaction.mylocation.locationpersistenceapi.protobuf.LocationPersistenceRequest;
import net.devaction.mylocation.locationpersistenceapi.util.ProtoUtil;

/**
 * @author Víctor Gil
 *
 * since December 2018
 */
public class LocationPersistenceTesterClientVerticle extends AbstractVerticle{
    private static final Logger log = LoggerFactory.getLogger(LocationPersistenceTesterClientVerticle.class);

    //Spring should set this value from the configuration in the live/main code
    private String address = "last_known_location_persist";
    
    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        
        LocationPersistenceRequest protoRequest = LocationPersistenceTestRequestProvider.provide();
        log.info("Request proto message to be sent to the server:\n" + ProtoUtil.toString(protoRequest));
        
        Buffer buffer = Buffer.buffer(); 
        buffer.appendBytes(protoRequest.toByteArray());
        
        log.info("Going to send a request message to " + address + " address.");
        eventBus.send(address, buffer, new ResponseFromServerHandler());        
    }    
}

