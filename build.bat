:: COMPILES THE PROGRAM UNDER THE "SRC" DIRECTORY
@echo off
cd src
javac .\Engine\*.java
jar cvfe game.jar Engine.MainClass Engine/*.class
del .\Engine\*.class
