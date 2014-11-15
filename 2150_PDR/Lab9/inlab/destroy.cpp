#include <iostream>
 
using namespace std;

class Planet {
   public:
      void setX(int w)
      {
         x = w;
      }
      void setY(int h)
      {
         y = h;
      }
   protected:
      int x;
      int y;
};

// Derived class
class DevourerOfWorlds {
   public:
      int luckyFin;
      DevourerOfWorlds();
      ~DevourerOfWorlds();

    private:
      Planet * earth;
};

DevourerOfWorlds::DevourerOfWorlds() {
    earth = new Planet();
}

DevourerOfWorlds::~DevourerOfWorlds() {
    delete earth;
}

void scope() {
    DevourerOfWorlds Galactus = DevourerOfWorlds();
}

int main(void) {
    scope();
    return 0;
}

