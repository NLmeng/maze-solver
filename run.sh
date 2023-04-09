#!/bin/bash

# Compile the code
javac -cp src -d bin src/rushhour/*.java test/test.java

# Run the test class
java -cp "bin:test" test

# For Unix-based systems (Linux or macOS):
    # Make the run.sh file executable:
        # chmod +x run.sh
    # Run the run.sh file:
        # ./run.sh