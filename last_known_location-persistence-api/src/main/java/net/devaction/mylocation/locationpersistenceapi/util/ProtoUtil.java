package net.devaction.mylocation.locationpersistenceapi.util;

import net.devaction.mylocation.locationpersistenceapi.protobuf.LocationPersistenceRequest;

/**
 * @author Víctor Gil
 *
 * since December 2018
 */
public class ProtoUtil{

    public static String toString(LocationPersistenceRequest request){        
        StringBuilder stringBuilder = new StringBuilder(request.toString());
        
        if (request.hasTimestamp())
            stringBuilder.append("timestamp (string): ")
            .append(DateUtil.getDateString(request.getTimestamp())).append("\n");    
    
        if (request.hasTimeChecked())
            stringBuilder.append("timeChecked (string): ")
            .append(DateUtil.getDateString(request.getTimeChecked())).append("\n");    
        
        if (request.hasTimeMeasured())
            stringBuilder.append("timeMeasured (string): ")
            .append(DateUtil.getDateString(request.getTimeMeasured())).append("\n");
        
        return stringBuilder.toString();
    }
}

