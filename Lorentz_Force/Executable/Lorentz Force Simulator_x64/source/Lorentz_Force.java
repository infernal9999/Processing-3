import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import peasy.*; 

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

Slider s1;
Slider s2;
Slider s3;
Slider s4;

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

float theta;

float q_temp;
float EF_Mag_temp;
float MF_Mag_temp;

public void setup() {
  
  //size(800, 800, P3D);
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
  isChanged();
  cam.endHUD();
  
  axis.Arrows(MF, EF, "Y", angle);
  
  p.show();
  p.update();
}

public void isChanged() {
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
    
    for (int i=0; i<this.tragectory.size(); i++) {
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
class Slider {
  int x;
  int y;
  int l;
  int size = 15;
  float pos;
  int line_col = color(255, 255, 255);
  int dragger_col = color(255, 100, 100);
  int text_col = color(0, 255, 255);
  String text;
  float min;
  float max;
  float val;
  
  Slider(int x, int y, int l, float min, float max, String text) {
    this.x = x;
    this.y = y;
    this.l = l;
    this.min = min;
    this.max = max;
    this.pos = (x+(x+l))/2;
    this.text = text;
  }
  
  public void Onit() {
    if ((mouseX >= this.pos-size) && (mouseX <= this.pos+size) && (mouseY >= this.y-size) && (mouseY <= this.y+size)) {
      if (mousePressed)
      {
        if ((mouseX >= this.x) && (mouseX <= this.x+this.l)) {
          this.pos = mouseX;
        }
      }
    }
  }
  
  public void drawSlider() {
    this.val = map(this.pos, this.x, this.x+this.l, this.min, this.max);
    
    fill(this.text_col);
    textSize(15);
    text(this.text, ((x+(x+l))-textWidth(this.text))/2, this.y-15);
    
    text(val, ((x+(x+l))-textWidth(this.text))/2, this.y+20);
    
    strokeWeight(2);
    stroke(this.line_col);
    line(this.x, this.y, this.x+this.l, this.y);
    
    Onit();
    fill(this.dragger_col);
    noStroke();
    ellipse(this.pos, this.y, size, size);
  }
  
  public float ValReturn() {
    drawSlider();
    
    return(this.val);
  }
}
  public void settings() {  fullScreen(P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Lorentz_Force" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
