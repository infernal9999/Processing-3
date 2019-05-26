import peasy.*;
PeasyCam cam;

import controlP5.*;
import processing.opengl.*;
ControlP5 cp5;

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

float theta = radians(90);
float q_temp = 0;
float EF_Mag_temp = 0.5;
float MF_Mag_temp = 0.5;

void setup() {
  fullScreen(OPENGL);
  //size(800, 800, OPENGL);
  background(0);
  
  int len = 60;
  int gaps = (width - len);
  int space = gaps/6;
  
  cam = new PeasyCam(this, 600);
  
  cp5 = new ControlP5(this);
  cp5.addTextfield("CHARGE").setPosition(space, height-50).setSize(len, 20).setFont(createFont("Arial Black", 12)).setColor(color(255, 75, 10)).setAutoClear(false);
  cp5.addTextfield(" EF_MAG").setPosition((space * 2), height-50).setSize(len, 20).setFont(createFont("Arial Black", 12)).setColor(EF).setAutoClear(false);
  cp5.addTextfield(" MF_MAG").setPosition((space * 3), height-50).setSize(len, 20).setFont(createFont("Arial Black", 12)).setColor(MF).setAutoClear(false);
  cp5.addTextfield("  ANGLE").setPosition((space * 4), height-50).setSize(len, 20).setFont(createFont("Arial Black", 12)).setColor(color(255, 100, 255)).setAutoClear(false);
  
  cp5.addButton("SUBMIT", 10, (space * 5), height-50, (len+10), 20).setFont(createFont("Arial Black", 12)).setColorBackground(color(20, 120, 210)).setId(1);
  cp5.addButton("CLOSE", 10, (width-len-20), 20, len, 20).setFont(createFont("Arial Black", 12)).setColorBackground(color(255, 0, 0)).setId(2);
  
  cp5.setAutoDraw(false);
  
  charge = q_temp;
  EF_Mag = EF_Mag_temp;
  MF_Mag = MF_Mag_temp;
  angle = theta;
  
  axis = new Axes("LEFT", 300);
  
  axis.Arrows(MF, EF, "Y", angle);
  p = new Particle(radius, charge, PColor, axis.EF_direction(), EF_Mag, axis.MF_direction(), MF_Mag);
}

void draw() {
  background(0);
  rotateX(-0.2);
  rotateY(-0.5);
  
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
  cam.endHUD();
  
  axis.Arrows(MF, EF, "Y", angle);
  
  p.show();
  p.update();
  
  gui();
}

void gui() {
  hint(DISABLE_DEPTH_TEST);
  cam.beginHUD();
  cp5.draw();
  cam.endHUD();
  hint(ENABLE_DEPTH_TEST);
}

void controlEvent(ControlEvent theEvent) {
  if (theEvent.getController().getId() == 1) {
    q_temp = float(cp5.get(Textfield.class, "CHARGE").getText());
    EF_Mag_temp = float(cp5.get(Textfield.class, " EF_MAG").getText());
    MF_Mag_temp = float(cp5.get(Textfield.class, " MF_MAG").getText());
    theta = radians(float(cp5.get(Textfield.class, "  ANGLE").getText()));
    
    if ((!Float.isNaN(q_temp)) && (!Float.isNaN(EF_Mag_temp)) && (!Float.isNaN(MF_Mag_temp)) && (!Float.isNaN(theta))) {
      if ((q_temp != charge) || (EF_Mag_temp != EF_Mag) || (MF_Mag_temp != MF_Mag) || (theta != angle)){
        charge = q_temp;
        EF_Mag = EF_Mag_temp;
        MF_Mag = MF_Mag_temp;
        angle = theta;
        
        axis.Arrows(MF, EF, "Y", angle);
        p.reset(charge, EF_Mag, axis.MF_direction(), MF_Mag);
    
      }
    }
  }
  else if (theEvent.getController().getId() == 2) {
    exit();
  }
}
