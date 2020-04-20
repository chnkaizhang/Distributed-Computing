JC = javac
JAR = .:zookeeper-3.6.0.jar:zookeeper-jute-3.6.0.jar:log4j-1.2.17.jar:slf4j-api-1.7.25.jar:slf4j-log4j12-1.7.25.jar
JFLAGS = -g
JFLAG = -g -cp
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAG) $(JAR) $*.java

CLASSES = \
		DFSZSERVER.java \
		DFSZinterface.java \
		ZKSevice.java \
		CONFIG.java \

default: classes
classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class