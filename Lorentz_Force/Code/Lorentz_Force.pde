import peasy.*;
PeasyCam cam;

Slider s1;
Slider s2;
Slider s3;
Slider s4;

float angle;

Axes axis;
color MF = color(255, 0, 0);
color EF = color(0, 255, 0);

Particle p;
color PColor = color(255, 255, 255);
int radius = 3;
float charge;
float EF_Mag;
float MF_Mag;

float theta;

float q_temp;
float EF_Mag_temp;
float MF_Mag_temp;

boolean FULLSCREEN = true;
void setup() {
  //fullScreen(P3D);
  size(800, 800, P3D);
  background(0);
  
  cam = new PeasyCam(this, 600);
  
  int len = 100;
  int gaps = (width - len);
  int space = gaps/5;
  
  s1 = new Slider(space, height-25, len, -1, 1, "CHARGE");
  s2 = new Slider((space * 2), height-25, len, 0, 1, "EF_MAG");
  s3 = new Slider((space * 3), height-25, len, 0, 1, "MF_MAG");
  s4 = new Slider((space * 4), height-25, len, 0, 180, "ANGLE");
  
  charge = s1.ValReturn();
  EF_Mag = s2.ValReturn();
  MF_Mag = s3.ValReturn();
  angle = radians(s4.ValReturn());
  
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
  text(p.force.mag(), 112, 100);
  isChanged();
  cam.endHUD();
  
  axis.Arrows(MF, EF, "Y", angle);
  
  p.show();
  p.update();
}

void isChanged() {
  q_temp = s1.ValReturn();
  EF_Mag_temp = s2.ValReturn();
  MF_Mag_temp = s3.ValReturn();
  theta = radians(s4.ValReturn());
  
  if ((q_temp != charge) || (EF_Mag_temp != EF_Mag) || (MF_Mag_temp != MF_Mag) || (theta != angle)){
    charge = q_temp;
    EF_Mag = EF_Mag_temp;
    MF_Mag = MF_Mag_temp;
    angle = theta;
    
    axis.Arrows(MF, EF, "Y", angle);
    p.reset(charge, EF_Mag, axis.MF_direction(), MF_Mag);

  }
}

void keyPressed() {
  if (key == ESC) {
    exit();
  }
}
