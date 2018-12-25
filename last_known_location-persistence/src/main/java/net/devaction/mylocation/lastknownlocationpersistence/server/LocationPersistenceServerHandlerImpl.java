package net.devaction.mylocation.lastknownlocationpersistence.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import net.devaction.mylocation.lastknownlocationpersistence.server.persistor.Persistor;
import net.devaction.mylocation.locationpersistenceapi.protobuf.LocationPersistenceRequest;
import net.devaction.mylocation.locationpersistenceapi.protobuf.LocationPersistenceResponse;

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
        
        LocationPersistenceResponse response = persistor.persist(request);
        //bufferMessage.reply(errorBufferProvider.provide(ex.toString(), ex));
        Buffer responseBuffer = Buffer.buffer();
        responseBuffer.appendBytes(response.toByteArray());
        bufferMessage.reply(responseBuffer);        
    }

    public void setErrorBufferProvider(ErrorBufferProvider errorBufferProvider){
        this.errorBufferProvider = errorBufferProvider;
    }

    public void setPersistor(Persistor persistor){
        this.persistor = persistor;
    }
}

