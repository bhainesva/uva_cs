##
# Name: Ben Haines
# ID: bmh5wx
# File: Makefile
# Date: 10/12/14
##

# Show all warnings
CXXFLAGS=-Wall -O2

# Compile with clang++ and any flags
CXX=clang++ $(CXXFLAGS)

# Compile with debug information
DEBUG=-Wall -g

# List all .o files to avoid redundant typing
OFILES = hashTable.o timer.o hashFunctions.o wordPuzzle.o

# Suffix rule for how to treat .cpp files
.SUFFIXES: .o .cpp

pizza:  $(OFILES)
	$(CXX) $(OFILES)
	@echo "Compiling Finished!"
	
clean:
	-rm -f *.o *~ a.out

debug: $(OFILES)



hashFunctions.o: hashFunctions.cpp hashFunctions.h hashTable.h
hashTable.o: hashTable.cpp hashTable.h hashFunctions.h
timer.o: timer.cpp timer.h
wordPuzzle.o: wordPuzzle.cpp hashTable.h hashFunctions.h timer.h
