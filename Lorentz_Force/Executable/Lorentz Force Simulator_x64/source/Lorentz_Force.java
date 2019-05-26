import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import peasy.*; 
import controlP5.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Lorentz_Force extends PApplet {


PeasyCam cam;



ControlP5 cp5;

float angle;

Axes axis;
int MF = color(255, 0, 0);
int EF = color(0, 255, 0);

Particle p;
int PColor = color(255, 255, 255);
int radius = 3;
float charge;
float EF_Mag;
float MF_Mag;

float theta = radians(90);
float q_temp = 0;
float EF_Mag_temp = 0.5f;
float MF_Mag_temp = 0.5f;

public void setup() {
  
  //size(800, 800, OPENGL);
  background(0);
  
  int len = 60;
  int gaps = (width - len);
  int space = gaps/5;
  
  cam = new PeasyCam(this, 600);
  
  cp5 = new ControlP5(this);
  cp5.addTextfield("CHARGE").setPosition(space, height-35).setSize(len, 20).setAutoClear(false);
  cp5.addTextfield("EF_MAG").setPosition((space * 2), height-35).setSize(len, 20).setAutoClear(false);
  cp5.addTextfield("MF_MAG").setPosition((space * 3), height-35).setSize(len, 20).setAutoClear(false);
  cp5.addTextfield("ANGLE").setPosition((space * 4), height-35).setSize(len, 20).setAutoClear(false);
  
  cp5.addButton("SUBMIT", 10, (width-len-10), 10, len, 20).setId(1);
  
  cp5.setAutoDraw(false);
  
  charge = q_temp;
  EF_Mag = EF_Mag_temp;
  MF_Mag = MF_Mag_temp;
  angle = theta;
  
  axis = new Axes("LEFT", 300);
  
  axis.Arrows(MF, EF, "Y", angle);
  p = new Particle(radius, charge, PColor, axis.EF_direction(), EF_Mag, axis.MF_direction(), MF_Mag);
}

public void draw() {
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
  cam.endHUD();
  
  axis.Arrows(MF, EF, "Y", angle);
  
  p.show();
  p.update();
  
  gui();
}

public void gui() {
  hint(DISABLE_DEPTH_TEST);
  cam.beginHUD();
  cp5.draw();
  cam.endHUD();
  hint(ENABLE_DEPTH_TEST);
}

public void controlEvent(ControlEvent theEvent) {
  q_temp = PApplet.parseFloat(cp5.get(Textfield.class, "CHARGE").getText());
  EF_Mag_temp = PApplet.parseFloat(cp5.get(Textfield.class, "EF_MAG").getText());
  MF_Mag_temp = PApplet.parseFloat(cp5.get(Textfield.class, "MF_MAG").getText());
  theta = radians(PApplet.parseFloat(cp5.get(Textfield.class, "ANGLE").getText()));
  
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

public void keyPressed() {
  if (key == ESC) {
    exit();
  }
}
class Axes {
  int index;
  PVector mid = new PVector(0, 0, 0);
  PVector[] directions = {
  new PVector(0, -1, 0),  // UP
  new PVector(0, 1, 0),   // DOWN
  new PVector(1, 0, 0),   // RIGHT
  new PVector(-1, 0, 0),   // LEFT
  new PVector(0, 0, 1),   // FRONT
  new PVector(0, 0, -1),   // BACK
  };
  PVector[] Arrow_direction = {
  new PVector(4, 5, 0),  // UP_ARROW
  new PVector(4, 5, 1),  // DOWN_ARROW
  new PVector(0, 1, 2),  // RIGHT_ARROW
  new PVector(0, 1, 3),  // LEFT_ARROW
  new PVector(0, 1, 4),  // FRONT_ARROW
  new PVector(0, 1, 5),  // BACK_ARROW
  };
  PVector v;
  PVector v1;
  PVector v2;
  PVector v3;
  float angle;
  String Rotate;
  PVector ve;
  PVector vm;
  
  Axes(String dir, int mag) {
    switch(dir) {
      case "UP":this.index = 0;
                break;
      case "DOWN":this.index = 1;
                break;
      case "RIGHT":this.index = 2;
                break;
      case "LEFT":this.index = 3;
                break;
      case "FRONT":this.index = 4;
                break;
      case "BACK":this.index = 5;
                break;
    }
    
    this.ve = new PVector(this.directions[this.index].x, this.directions[this.index].y, this.directions[this.index].z);
    
    
    this.v = new PVector((this.ve.x*mag), (this.ve.y*mag), (this.ve.z*mag));
    
    int arrowSize = 12;
    
    this.v1 = this.directions[PApplet.parseInt(this.Arrow_direction[this.index].x)];
    this.v1.mult(arrowSize);
    this.v2 = this.directions[PApplet.parseInt(this.Arrow_direction[this.index].y)];
    this.v2.mult(arrowSize);
    this.v3 = this.directions[PApplet.parseInt(this.Arrow_direction[this.index].z)];
    this.v3.mult(arrowSize);
  }
  
  public void drawArrow(int col) {
    pushMatrix();
    lights();
    
    beginShape(LINES);
    stroke(col);
    strokeWeight(2);
    vertex(mid.x, mid.y, mid.z);
    vertex(v.x, v.y, v.z);
    endShape(CLOSE);
    
    translate(v.x, v.y, v.z);
    fill(col);
    beginShape(TRIANGLE);
    vertex(this.v1.x, this.v1.y, this.v1.z);
    vertex(this.v2.x, this.v2.y, this.v2.z);
    vertex(this.v3.x, this.v3.y, this.v3.z);
    endShape(CLOSE);
    
    popMatrix();
  }
  
  public void Arrows(int C0, int C1, String rotate, float theta) {
    this.angle = theta;
    this.Rotate = rotate;
    
    drawArrow(C0);
    this.v = rot(rotate, this.v, theta);
    this.v1 = rot(rotate, this.v1, theta);
    this.v2 = rot(rotate, this.v2, theta);
    this.v3 = rot(rotate, this.v3, theta);
    drawArrow(C1);
    this.v = rot(rotate, this.v, -theta);
    this.v1 = rot(rotate, this.v1, -theta);
    this.v2 = rot(rotate, this.v2, -theta);
    this.v3 = rot(rotate, this.v3, -theta);
  }
  
  public PVector rot(String s, PVector v, float theta) {
    PVector result = new PVector();
    switch(s) {
      case "X":
               result.x = v.x;
               result.y = (v.y*cos(theta))-(v.z*sin(theta));
               result.z = (v.y*sin(theta))+(v.z*cos(theta));
               break;
      case "Y":
               result.x = (v.x*cos(theta))+(v.z*sin(theta));
               result.y = v.y;
               result.z = (-v.x*sin(theta))+(v.z*cos(theta));
               break;
      case "Z":
               result.x = (v.x*cos(theta))-(v.y*sin(theta));
               result.y = (v.x*sin(theta))-(v.y*cos(theta));
               result.z = v.z;
               break;
    }
    return(result);
  }
  
  public PVector MF_direction() {
    this.vm = new PVector(0, 0, 0);
    this.vm = rot(this.Rotate, this.ve, this.angle);
    return(vm);
  }
  public PVector EF_direction() {
    return(ve);
  }
}
class Particle {
  float radius;
  float q;
  int Color;
  PVector position;
  PVector velocity;
  PVector E;
  PVector B;
  PVector E_backup;
  PVector force = new PVector(0, 0, 0);
  int historySize = 10000;
  boolean VelnotAssigned = true;
  
  ArrayList <PVector> tragectory = new ArrayList<PVector>(this.historySize);
  
  Particle(float rad, float charge, int Color, PVector EF, float EF_Mag, PVector MF, float MF_Mag) {
    this.radius = rad;
    this.q = charge;
    this.Color = Color;
    this.position = new PVector(0, 0, 0);
    this.velocity = new PVector(0, 0, 0);
    
    this.E_backup = EF.copy();
    
    this.E = EF.mult(EF_Mag).copy();
    this.B = MF.mult(MF_Mag).copy();
  }
  
  public void calForce() {
    this.force = this.position.copy();
    this.force = this.force.cross(this.B).mult(-1);
    this.force.add(this.E);
    this.force.mult(q);
  }
  
  public void update() {
    calForce();
    
    if (this.VelnotAssigned) {
      this.velocity = this.force.copy();
      this.VelnotAssigned = false;
    }
    
    this.position.add(this.velocity);
    this.position.add(this.force);
    
    if (frameCount % 1 == 0) {
      this.tragectory.add(new PVector(this.position.x, this.position.y, this.position.z));
     }
    if (this.tragectory.size() > this.historySize) {
      this.tragectory.remove(0);
    }
  }
  
  public void show() {
  pushMatrix();
    lights();
    translate(position.x, position.y, position.z);
    noStroke();
    fill(this.Color);
    sphere(this.radius);
    popMatrix();
    Tragectory();
  }
  
  public void Tragectory() {
    if (this.tragectory.size() > 2) {
      pushMatrix();
      lights();
      beginShape();
      noFill();
      stroke(255, 255, 0);
      strokeWeight(2);
      for (int i=0; i<this.tragectory.size(); i++) {
        PVector v = this.tragectory.get(i);
        vertex(v.x, v.y, v.z);
      }
      endShape();
      popMatrix();
    }
  }
  
  public void reset(float charge, float EF_Mag, PVector MF, float MF_Mag) {
    this.q = charge;
    this.position = new PVector(0, 0, 0);
    this.velocity = new PVector(0, 0, 0);
    
    int i = 0;
    while (this.tragectory.size() != 0) {
      this.tragectory.remove(i);
    }
    
    this.tragectory.add(new PVector(0, 0, 0));
    this.tragectory.add(new PVector(0, 0, 0));
    this.tragectory.add(new PVector(0, 0, 0));
    
    Tragectory();
    
    this.VelnotAssigned = true;
    
    this.E = this.E_backup.copy();
    this.E.mult(EF_Mag);
    
    this.B = MF.copy();
    this.B.mult(MF_Mag);
  }
}
  public void settings() {  fullScreen(OPENGL); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Lorentz_Force" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
