package net.devaction.mylocation.lastknownlocationpersistence.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import net.devaction.mylocation.lastknownlocationpersistence.config.FilePathProvider;
import net.devaction.mylocation.locationpersistenceapi.protobuf.LocationPersistenceRequest;

/**
 * @author Víctor Gil
 *
 * since December 2018
 */
public class Persistor implements InitializingBean{
    private static final Logger log = LoggerFactory.getLogger(Persistor.class);
    
    private String filePath;
    private FilePathProvider filePathProvider;
    
    @Override
    public void afterPropertiesSet() throws Exception{
        if (filePath == null)
            filePath = filePathProvider.provideFilePath();
        log.info("File path to store the latest known location: " + filePath);
    }   
    
    public void persist(LocationPersistenceRequest request) throws IOException{
        final byte[] bytes = request.toByteArray();
        final Path path = Paths.get(filePath);
        try{
            Files.write(path, bytes, StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,                
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch(IOException ex){
            log.error(ex.toString(), ex);
            throw ex;
        }
    }

    public String getFilePath(){
        return filePath;
    }

    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public FilePathProvider getFilePathProvider(){
        return filePathProvider;
    }

    public void setFilePathProvider(FilePathProvider filePathProvider){
        this.filePathProvider = filePathProvider;
    }   
}

