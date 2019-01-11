#!/bin/bash 

set -e 

BASE_DIR=`dirname "$0"`
CLASS_NAME="net.devaction.mylocation.vertxutilityextensions.main.VertxStarter"
CLASSPATH="${BASE_DIR}/../conf:${BASE_DIR}/../conf/lcl:${BASE_DIR}/../lib/*"

JAVA_SYSTEM_PROPERTIES="-Dhazelcast.logging.type=slf4j -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory -Djava.net.preferIPv4Stack=true -Dvertx.cluster.host=localhost -Dpersistence.base.dir=$PERSISTENCE_BASE_DIR -Dmylocation.service.id=last_known_location-persistence"

java $JAVA_SYSTEM_PROPERTIES -classpath $CLASSPATH $CLASS_NAME 
 
