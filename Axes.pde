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
    
    this.v1 = this.directions[int(this.Arrow_direction[this.index].x)];
    this.v1.mult(arrowSize);
    this.v2 = this.directions[int(this.Arrow_direction[this.index].y)];
    this.v2.mult(arrowSize);
    this.v3 = this.directions[int(this.Arrow_direction[this.index].z)];
    this.v3.mult(arrowSize);
  }
  
  void drawArrow(color col) {
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
  
  void Arrows(color C0, color C1, String rotate, float theta) {
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
  
  PVector rot(String s, PVector v, float theta) {
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
  
  PVector MF_direction() {
    this.vm = new PVector(0, 0, 0);
    this.vm = rot(this.Rotate, this.ve, this.angle);
    return(vm);
  }
  PVector EF_direction() {
    return(ve);
  }
}
