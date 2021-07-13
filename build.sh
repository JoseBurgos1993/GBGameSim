#!/bin/bash
## COMPILES THE PROGRAM UNDER THE "SRC" DIRECTORY
cd src
javac ./Engine/*.java
jar cvfe game.jar Engine.MainClass Engine/*.class
rm -f ./Engine/*.class
