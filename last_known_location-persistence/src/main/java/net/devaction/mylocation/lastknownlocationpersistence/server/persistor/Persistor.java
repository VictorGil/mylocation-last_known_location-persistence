package net.devaction.mylocation.lastknownlocationpersistence.server.persistor;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import net.devaction.mylocation.lastknownlocationapi.protobuf.LastKnownLocationResponse;
import net.devaction.mylocation.lastknownlocationpersistence.config.FilePathProvider;
import net.devaction.mylocation.locationpersistenceapi.protobuf.LocationPersistenceRequest;
import net.devaction.mylocation.locationpersistenceapi.protobuf.LocationPersistenceResponse;
import net.devaction.mylocation.locationpersistenceapi.protobuf.Status;

/**
 * @author Víctor Gil
 *
 * since December 2018
 */
public class Persistor implements InitializingBean{
    private static final Logger log = LoggerFactory.getLogger(Persistor.class);
    
    private String filePath;
    private FilePathProvider filePathProvider;
    
    private LastKnownLocationResponseProvider locationResponseProvider;
    
    @Override
    public void afterPropertiesSet() throws Exception{
        if (filePath == null)
            filePath = filePathProvider.provideFilePath();
        log.info("File path to store the latest known location: " + filePath);
    }   
    
    public LocationPersistenceResponse persist(LocationPersistenceRequest persistenceRequest) throws IOException{
        final Path path = Paths.get(filePath);
        FileChannel fileChannel = null;
        
        LocationPersistenceResponse.Builder failureResponseBuilder = LocationPersistenceResponse.newBuilder();
        try{
            fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING);            
        } catch(IOException ex){
            String errMessage = "Unable to open file " + filePath + " for writing: " 
                    + ex;
            log.error(errMessage, ex);
            failureResponseBuilder.setStatus(Status.FAILURE);
            failureResponseBuilder.setErrorMessage(errMessage);
            return failureResponseBuilder.build();
        }
        
        FileLock lock = null;
        int attempts = 0;
        while (lock == null && attempts < 2) {
            if (attempts == 1) {
                log.warn("Unable to acquire exclusive lock, sleeping for half a second and then trying again");
                try{
                    Thread.sleep(500);
                } catch(InterruptedException ex){
                    log.error(ex.toString(), ex);                
                }
            }
            try{
                lock  = fileChannel.tryLock(0, Long.MAX_VALUE, false);
            } catch (OverlappingFileLockException | IOException ex) {
                String errMessage = "Unable to obtain exclusive lock on file " + filePath 
                    + " " + ex;
                log.error(errMessage, ex);
                failureResponseBuilder.setStatus(Status.FAILURE);
                failureResponseBuilder.setErrorMessage(errMessage);
                return failureResponseBuilder.build();
            }
            attempts++;
        }
        
        if (lock == null){
            String errMessage = "Unable to acquire write/exclusive lock after two attempts on " + filePath;
            log.error(errMessage);
            failureResponseBuilder.setStatus(Status.FAILURE);
            failureResponseBuilder.setErrorMessage(errMessage);
            return failureResponseBuilder.build();    
        }
        
        //We transform one protocol buffer message/object type into another one.
        LastKnownLocationResponse reponseToBePersisted = locationResponseProvider.provide(persistenceRequest);
        byte[] bytes = reponseToBePersisted.toByteArray();
        try {
            Files.write(path, bytes, StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,                
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch(IOException ex){
            String errMessage = "Unable to write bytes to file " + filePath + " " + ex;
            log.error(errMessage, ex);
            failureResponseBuilder.setStatus(Status.FAILURE);
            failureResponseBuilder.setErrorMessage(errMessage);
            return failureResponseBuilder.build();
        }       

        finally {
            log.trace("Running finally block");
            if (lock != null) {
                log.trace("Going to release the lock");
                try{
                    lock.release();
                } catch (IOException ex){
                    log.error("Error when releasing the lock: " + ex.toString(), ex);                    
                }
            }
            if (fileChannel != null){
                log.trace("Going to close the file channel");
                try{
                    fileChannel.close();
                } catch (IOException ex){
                    log.error("Error when closing the file channel: " + ex.toString(), ex);
                }
            }
        }
        
        LocationPersistenceResponse.Builder successResponse = LocationPersistenceResponse.newBuilder();
        successResponse.setStatus(Status.SUCCESS);
        
        return successResponse.build();
    }

    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public void setFilePathProvider(FilePathProvider filePathProvider){
        this.filePathProvider = filePathProvider;
    }

    public void setLocationResponseProvider(LastKnownLocationResponseProvider locationResponseProvider){
        this.locationResponseProvider = locationResponseProvider;
    }   
}

