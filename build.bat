:: COMPILES THE PROGRAM UNDER THE "SRC" DIRECTORY
@ECHO OFF
CD src
javac ./Engine/*.java
jar cvfe game.jar Engine.MainClass Engine/*.class