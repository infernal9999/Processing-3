import peasy.*;
PeasyCam cam;

float angle = radians(90);

Axes axis;
color MF = color(255, 0, 0);
color EF = color(0, 255, 0);

Particle p;
color PColor = color(255, 255, 255);
int radius = 3;
float charge = 3;
float EF_Mag = 0.1;
float MF_Mag = 0.01;

void setup() {
  //fulcreen(P3D);
  size(800, 800, P3D);
  background(0);
  cam = new PeasyCam(this, 600);
  axis = new Axes("LEFT", 300);
  axis.Arrows(MF, EF, "Y", angle);
  p = new Particle(radius, charge, PColor, axis.EF_direction(), EF_Mag, axis.MF_direction(), MF_Mag);
}

void draw() {
  background(0);
  cam.beginHUD();
  fill(MF);
  textSize(15);
  text("ELECTRIC FIELD", 10, 20);
  fill(EF);
  text("MAGNETIC FIELD", 5, 40);
  fill(255, 75, 10);
  text("VELOCITY:", 40, 80);
  text(p.velocity.mag(), 112, 80);
  text("TOTAL FORCE:", 10, 100);
  text(p.force.mag(), 112, 100);;
  cam.endHUD();
  
  axis.Arrows(MF, EF, "Y", angle);
  
  p.show();
  p.update();
}
