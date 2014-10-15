##
# Name: Ben Haines
# ID: bmh5wx
# File: Makefile
# Date: 10/12/14
##

# Show all warnings
CXXFLAGS=-Wall

# Compile with clang++ and any flags
CXX=clang++ $(CXXFLAGS)

# Compile with debug information
DEBUG= -g

# List all .o files to avoid redundant typing
OFILES = List.o ListItr.o ListNode.o hashTable.o timer.o hashFunctions.o wordPuzzle.o

# Suffix rule for how to treat .cpp files
.SUFFIXES: .o .cpp

solver:  $(OFILES)
	$(CXX) $(OFILES)
	@echo "Compiling Finished!"
	
clean:
	-rm -f *.o *~ a.out

debug: $(OFILES)
	$(CXX) $(DEBUG) $(OFILES)

hashFunctions.o: hashFunctions.cpp hashFunctions.h hashTable.h List.h \
  ListNode.h ListItr.h
hashTable.o: hashTable.cpp hashTable.h List.h ListNode.h ListItr.h \
  hashFunctions.h
List.o: List.cpp List.h ListNode.h ListItr.h
ListItr.o: ListItr.cpp ListItr.h ListNode.h List.h
ListNode.o: ListNode.cpp ListNode.h
test.o: test.cpp hashFunctions.h hashTable.h List.h ListNode.h ListItr.h
timer.o: timer.cpp timer.h
wordPuzzle.o: wordPuzzle.cpp hashTable.h List.h ListNode.h ListItr.h \
  hashFunctions.h timer.h
