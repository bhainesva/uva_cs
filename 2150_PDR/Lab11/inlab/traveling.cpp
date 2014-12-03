/*
* Name: Ben Haines
* ID: bmh5wx
* Date: 12/02/14
* Filename: traveling.cpp
*/

#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <algorithm>

using namespace std;

#include "middleearth.h"

float computeDistance (MiddleEarth &me, string start, vector<string> dests);
void printRoute (string start, vector<string> dests);

/** @brief Main method.
 *
 * This is a brute force solution to the traveling salesman problem on a randomly created collection of cities.
 * @author Ben Haines
 * @version 0.1
 * @date 12/2/14
 */
int main (int argc, char **argv) {
    // check the number of parameters
    if ( argc != 6 ) {
        cout << "Usage: " << argv[0] << " <world_height> <world_width> "
             << "<num_cities> <random_seed> <cities_to_visit>" << endl;
        exit(0);
    }
    // we'll assume the parameters are all well-formed
    int width, height, num_cities, rand_seed, cities_to_visit;
    sscanf (argv[1], "%d", &width);
    sscanf (argv[2], "%d", &height);
    sscanf (argv[3], "%d", &num_cities);
    sscanf (argv[4], "%d", &rand_seed);
    sscanf (argv[5], "%d", &cities_to_visit);
    // Create the world, and select your itinerary
    MiddleEarth me(width, height, num_cities, rand_seed);
    vector<string> dests = me.getItinerary(cities_to_visit);

    computeDistance(me, dests[0], dests);
    string start = dests[0];
    vector<string> arg(dests.begin() + 1, dests.end());
    vector<string> minPath;
    float minLen = -1;
    float temp = 0;
    sort(arg.begin(), arg.end());
    do {
        temp = computeDistance(me, start, arg);
        if (temp <= minLen || minLen == -1) {
            minLen = temp;
            minPath = arg;
        }
      } while (next_permutation(arg.begin(),arg.end()));

    cout << "Minimum path has distance " << minLen << ": ";
    printRoute(start, minPath);
    return 0;
}

/** @brief Computes length of cycle.
 *  Starting with the 'start' parameter we visit each city in the dests vector in order and then return to start.
 * 
 * @param me MiddleEarth object containing the randomly generated cities.
 * @param start Name the city to start the cycle at.
 * @param dests Vector of cities to visit in order.
 * @return Total length of the path from the start node, through the dest nodes in order, and back to start.
 * @author Ben Haines
 * @version 0.1
 * @date 12/2/14
 */
float computeDistance (MiddleEarth &me, string start, vector<string> dests) {
    float totalDist = 0;
    string source = start;
    string target = dests[0];
    totalDist += me.getDistance(source, target);
    for (int i=1;i<dests.size();i++) {
        source = target;
        target = dests[i];
        totalDist += me.getDistance(source, target);
    }
    totalDist += me.getDistance(target, start);

    return totalDist;
}

/** @brief Prints route.
 *  Prints the entire route, starting and ending at the 'start' parameter. The output is of the form: Erebor -> Khazad-dum -> Michel Delving -> Bree -> Cirith Ungol -> Erebor
 * 
 * @param start Name of the city that the route starts at.
 * @param dests Vector of cities to visit in order.
 * @author Ben Haines
 * @version 0.1
 * @date 12/2/14
 */
void printRoute (string start, vector<string> dests) {
    cout << start << " -> ";
    for (int i =0; i<dests.size(); i++) {
        cout << dests[i] << " -> ";
    }
    cout << start << endl;
}
