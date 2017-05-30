#!/bin/sh

PROJECT=/git/xpro_min
OPENEJB=/devel/apache-openejb-7.0.3
JETTY=/devel/jetty-6.1.24

OPTS=""
OPTS="$OPTS -Dgoxpro.ejb-provider=OPENEJB_4_LOCAL"
OPTS="$OPTS -Dgoxpro.jetty.use.ajp=false"
OPTS="$OPTS -Dlog4j.configurationFile=log4j.properties"
OPTS="$OPTS -Djava.security.auth.login.config=web/src/test/conf/jaas.config"
OPTS="$OPTS -Dtapestry.production-mode=false"
OPTS="$OPTS -Dtapestry.hmac-passphrase=blahblah"
OPTS="$OPTS -Djetty.home=$JETTY"
OPTS="$OPTS -Dorg.mortbay.jetty.webapp.parentLoaderPriority=true"
OPTS="$OPTS -Dopenejb.configuration=business/src/test/conf/openejb.xml"
OPTS="$OPTS -Dopenejb.home=$OPENEJB"
OPTS="$OPTS -Dorg.apache.openejb.assembler.classic.WebAppBuilder=com.goxpro.xpro.web.NoWebAppBuilder"
OPTS="$OPTS -Dhibernate.hbm2ddl.auto=validate"
OPTS="$OPTS -Dhibernate.id.new_generator_mappings=true"
OPTS="$OPTS -Duser.timezone=UTC"
OPTS="$OPTS -Dfile.encoding=UTF-8"
OPTS="$OPTS -Xmx800m"
OPTS="$OPTS -Xms800m"
OPTS="$OPTS -XX:ReservedCodeCacheSize=80m"
OPTS="$OPTS -XX:+UseCodeCacheFlushing"
OPTS="$OPTS -XX:+UseConcMarkSweepGC"
OPTS="$OPTS -XX:+CMSParallelRemarkEnabled"
OPTS="$OPTS -XX:+UseCMSInitiatingOccupancyOnly"
OPTS="$OPTS -XX:CMSInitiatingOccupancyFraction=70"
OPTS="$OPTS -XX:+ScavengeBeforeFullGC"
OPTS="$OPTS -XX:+CMSScavengeBeforeRemark"

CP="$PROJECT/web/src/test/conf"
CP="$CP:$PROJECT/collapsed/xpro.war/WEB-INF/classes"
CP="$CP:$PROJECT/collapsed/xpro.war/WEB-INF/lib/*"
CP="$CP:$PROJECT/web/src/test/lib-test/dom4j-1.6.1.jar"
CP="$CP:$PROJECT/web/src/test/lib-test/hamcrest-core-1.3.jar"
CP="$CP:$PROJECT/web/src/test/lib-test/junit-4.11.jar"
CP="$CP:$PROJECT/web/src/test/lib-test/log4j-1.2.17.jar"
CP="$CP:$PROJECT/web/src/test/lib-test/slf4j-api-1.7.21.jar"
CP="$CP:$PROJECT/web/src/test/lib-test/slf4j-log4j12-1.7.21.jar"
CP="$CP:$PROJECT/business/src/test/lib-test-hibernate/*"
CP="$CP:$OPENEJB/lib/*"
CP="$CP:$JETTY/lib/jetty-6.1.24.jar"
CP="$CP:$JETTY/lib/jetty-util-6.1.24.jar"
CP="$CP:$JETTY/lib/naming/jetty-naming-6.1.24.jar"
CP="$CP:$JETTY/lib/plus/jetty-plus-6.1.24.jar"
CP="$CP:$JETTY/lib/ext/jetty-ajp-6.1.24.jar" 

DEBUG=""
#DEBUG="-agentlib:jdwp=transport=dt_socket,suspend=y,address=localhost:64650"

java $DEBUG $OPTS -classpath $CP com.goxpro.xpro.web.RunJetty
