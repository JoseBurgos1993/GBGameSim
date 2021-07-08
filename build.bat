:: COMPILES THE PROGRAM BY RUNNING THE MAKEFILE UNDER THE "SRC" DIRECTORY
@ECHO OFF
CD src
ECHO Main-Class: Engine.MainClass > manifest.txt
javac ./Engine/*.java
jar cvfe game.jar Engine.MainClass Engine/*.class
DEL manifest.txt