JAVAC=javac
JFLAGS=-g

default: 
	@echo Main-Class: Engine.MainClass > manifest.txt
	$(JAVAC) $(JFLAGS) ./Engine/*.java
	jar cvfe game.jar Engine.MainClass Engine/*.class
	del manifest.txt