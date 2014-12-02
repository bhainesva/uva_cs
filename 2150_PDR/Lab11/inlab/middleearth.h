#include <iostream>
#include <vector>
#include <string>
#include <map>

#ifndef MIDDLEEARTH_H
#define MIDDLEEARTH_H

using namespace std;

// see the comments in the lab11 write-up, or in middleearth.cpp

class MiddleEarth {
private:
    int num_city_names, xsize, ysize;
    vector<float> xpos, ypos;
    vector<string> cities;
    float *distances;
    map<string, int> indices;

public:
/** @brief Iluvatar, the creator of Middle-Earth.
 *
 * This is a constructor for the middle earth class that takes 4 arguments.
 * @param xsize Size of middle earth in x direction.
 * @param ysize Size of middle earth in y direction.
 * @param num_cities The number of cities that are desired. 
 * @param seed The value used to seed the random number generator. Uses the time if -1 is given.
 */
    MiddleEarth (int xsize, int ysize, int numcities, int seed);
/** @brief Sauron, the destructor of Middle-Earth.
 *
 * This is the deconstructor for the middle earth class.
 */
    ~MiddleEarth();
/** @brief The Mouth of Sauroun! 
 *
 * Prints out information on the created world. Displays the number of cities and the names of the available cities and their locations.
 */
    void print();
/** @brief Prints table of distances.
 *
 * Prints out a tab-separated table of the distances that can be loaded in Excel or similar.
 */
    void printTable();
/** @brief Calculates distance between two cities.
 *
 * @param  city1 The name of the first city.
 * @param  city2 The name of the second city.
 * @return The distance between the two cities.
 */
    float getDistance (string city1, string city2);
    vector<string> getItinerary(unsigned int length);
};

#endif
