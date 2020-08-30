#!/bin/bash

#------------------------------------------------------------------------------
# Run this script from the parent directory of "src" containing the source code
#------------------------------------------------------------------------------

printf "Building poker app ... \n"

# Creating output folder for the .class files
mkdir tmpDir

# Compiling the .java files and saving the .class files in the folder "tmpDir"
javac src/*/*.java -d tmpDir

# Creating a manifest file in the tmpDir folder 
touch tmpDir/manifest.txt

# Adding content to the manifest file
echo "Manifest-Version: 1.0" > tmpDir/manifest.txt
echo "Main-Class: game.Main" >> tmpDir/manifest.txt
echo >> tmpDir/manifest.txt

# Creating a .jar file containing the .class files.
# Options to jar:
#  c : create a jar file
#  f : make the output to go to the jar file
#  m : merge information from existing files into the manifest file of the jar file.
# (NOTE : the f and m options must be in the same order as thier corresponding arguments )
jar cfm pokerApp.jar tmpDir/manifest.txt -C tmpDir/ .

# Delete the tmpDir folder
rm -r tmpDir/

# Creating the bin folder if it doesn't exist already
mkdir -p bin
# Adding the binary to the bin folder
mv pokerApp.jar bin/pokerApp.jar

echo -e -------------------------------------------------------------------
printf "Build Complete!\n"
echo ----------------------------------------------------------------------

