#include <iostream>
 
using namespace std;

class Parent 
{
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
class Child: public Parent
{
   public:
      int xPlusY()
      { 
         return (x + y); 
      }

      int luckyFin;
};

void scope() {
    Child * nemo = new Child();
    nemo->setX(2);
    nemo->setY(3);
    int add = nemo->xPlusY();
}

int main(void) {
    scope();
    return 0;
}
