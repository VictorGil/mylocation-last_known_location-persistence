package net.devaction.mylocation.lastknownlocationpersistence.testclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;

import net.devaction.mylocation.locationpersistenceapi.protobuf.LocationPersistenceResponse;

import sun.misc.Signal;
/**
 * @author Víctor Gil
 *
 * since December 2018
 */
public class ResponseFromServerHandler implements Handler<AsyncResult<Message<Buffer>>>{
    private static final Logger log = LoggerFactory.getLogger(ResponseFromServerHandler.class);

    @SuppressWarnings("restriction")
    @Override
    public void handle(AsyncResult<Message<Buffer>> asyncResult){
        if (asyncResult.succeeded()){
            Message<Buffer> message = asyncResult.result();
            Buffer buffer = message.body();
            log.info("Received reply from the server, number of bytes in the body of the message: " + buffer.length());
            
            LocationPersistenceResponse response = null;
            try{
                response = LocationPersistenceResponse.parseFrom(buffer.getBytes());
            } catch (InvalidProtocolBufferException ex){
                String errorMessage = "Unable to parse " + LocationPersistenceResponse.class + " object: " 
                        + ex;
                log.error(errorMessage, ex);
                return;
            }
            
            log.info("Response proto object from the server: " + response);
            
        } else{
            Throwable throwable = asyncResult.cause();
            log.error("The request could not be processed by the server: " + throwable, throwable);
        }
        
        log.info("Going to raise a WINCH signal in order to trigger the graceful shutdown of both Vert.x and the JVM");
        Signal.raise(new Signal("WINCH")); 
    }
}

