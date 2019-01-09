package net.devaction.mylocation.lastknownlocationpersistence.server;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;

/**
 * @author Víctor Gil
 *
 * since December 2018
 */
public interface LocationPersistenceServerHandler extends Handler<Message<Buffer>>{

}

