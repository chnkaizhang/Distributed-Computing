JC = javac
JFLAG = -g
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAG) $*.java

CLASSES = \
		DFSCLIENT.java \
		DFSinterface.java \
		

default: classes
classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class