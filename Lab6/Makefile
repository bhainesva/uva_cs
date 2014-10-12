##
# Name: Ben Haines
# ID: bmh5wx
# File: Makefile-pizza
# Date: 09/26/14
##

# Show all warnings and optimize
CXXFLAGS=-Wall -O2

# Compile with clang++ and any flags
CXX=clang++ $(CXXFLAGS)

# Compile with debug information
DEBUG=-Wall -g

# List all .o files to avoid redundant typing
OFILES = pizza.o pizzadough.o tomatosauce.o toppings.o \
    mushrooms.o peppers.o cheese.o pepperoni.o

# Suffix rule for how to treat .cpp files
.SUFFIXES: .o .cpp

pizza:  $(OFILES)
	$(CXX) $(OFILES) -o pizza
	@echo "Pizza is Hot and Ready!"
	
clean:
	-rm -f *.o *~ pizza

debug: $(OFILES)

cheese.o: cheese.cpp cheese.h
mushrooms.o: mushrooms.cpp mushrooms.h
pepperoni.o: pepperoni.cpp pepperoni.h
peppers.o: peppers.cpp peppers.h
pizza.o: pizza.cpp pizzadough.h tomatosauce.h toppings.h cheese.h \
  mushrooms.h peppers.h pepperoni.h
pizzadough.o: pizzadough.cpp pizzadough.h
tomatosauce.o: tomatosauce.cpp tomatosauce.h
toppings.o: toppings.cpp toppings.h cheese.h mushrooms.h peppers.h \
  pepperoni.h
