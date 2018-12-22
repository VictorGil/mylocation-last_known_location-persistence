package net.devaction.mylocation.lastknownlocationpersistence.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import net.devaction.mylocation.locationpersistenceapi.protobuf.LocationPersistenceRequest;

/**
 * @author Víctor Gil
 *
 * since December 2018
 */
public class LocationPersistenceServerHandlerImpl implements LocationPersistenceServerHandler{
    private static final Logger log = LoggerFactory.getLogger(LocationPersistenceServerHandlerImpl.class);
    
    private ErrorBufferProvider errorBufferProvider;
    private Persistor persistor;
    
    @Override
    public void handle(Message<Buffer> bufferMessage){
        
        Buffer buffer = bufferMessage.body();
        byte[] bytes = buffer.getBytes();
        
        LocationPersistenceRequest request = null;
        try{
            request = LocationPersistenceRequest.parseFrom(bytes);
        } catch (InvalidProtocolBufferException ex){
            log.error(ex.toString(), ex);
            bufferMessage.reply(errorBufferProvider.provide(ex.toString(), ex));
            return;
        }
        
        try{
            persistor.persist(request);
        } catch (IOException ex){
            bufferMessage.reply(errorBufferProvider.provide(ex.toString(), ex));
        }
    }
}