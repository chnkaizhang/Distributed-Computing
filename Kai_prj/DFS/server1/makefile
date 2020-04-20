JC = javac
JFLAG = -g
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAG) $*.java

CLASSES = \
		ServerSevice.java \
		CONFIG.java \
		DFSinterface.java \
		DFSSERVER.java \
		

default: classes
classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class