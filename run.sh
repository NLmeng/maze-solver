#!/bin/bash

# Compile the codes
javac -cp src -d bin src/rushhour/*.java test/test.java
javac -cp "src:lib/json-20210307.jar" -d bin src/gui/model/*.java src/gui/persistence/*.java src/gui/ui/*.java

# Run the test class
# java -cp "bin:test" test
# Run the GUI
java -cp "bin:lib/json-20210307.jar" gui/ui/Main

# For Unix-based systems (Linux or macOS):
    # Make the run.sh file executable:
        # chmod +x run.sh
    # Run the run.sh file:
        # ./run.sh