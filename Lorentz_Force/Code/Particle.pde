class Particle {
  float radius;
  float q;
  color Color;
  PVector position;
  PVector velocity;
  PVector E;
  PVector B;
  PVector E_backup;
  PVector force = new PVector(0, 0, 0);
  int historySize = 10000;
  boolean VelnotAssigned = true;
  
  ArrayList <PVector> tragectory = new ArrayList<PVector>(this.historySize);
  
  Particle(float rad, float charge, color Color, PVector EF, float EF_Mag, PVector MF, float MF_Mag) {
    this.radius = rad;
    this.q = charge;
    this.Color = Color;
    this.position = new PVector(0, 0, 0);
    this.velocity = new PVector(0, 0, 0);
    
    this.E_backup = EF.copy();
    
    this.E = EF.mult(EF_Mag).copy();
    this.B = MF.mult(MF_Mag).copy();
  }
  
  void calForce() {
    this.force = this.position.copy();
    this.force = this.force.cross(this.B).mult(-1);
    this.force.add(this.E);
    this.force.mult(q);
  }
  
  void update() {
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
  
  void show() {
  pushMatrix();
    lights();
    translate(position.x, position.y, position.z);
    noStroke();
    fill(this.Color);
    sphere(this.radius);
    popMatrix();
    Tragectory();
  }
  
  void Tragectory() {
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
  
  void reset(float charge, float EF_Mag, PVector MF, float MF_Mag) {
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
