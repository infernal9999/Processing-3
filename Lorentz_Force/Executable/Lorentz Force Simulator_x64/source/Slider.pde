class Slider {
  int x;
  int y;
  int l;
  int size = 15;
  float pos;
  color line_col = color(255, 255, 255);
  color dragger_col = color(255, 100, 100);
  color text_col = color(0, 255, 255);
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
  
  void Onit() {
    if ((mouseX >= this.pos-size) && (mouseX <= this.pos+size) && (mouseY >= this.y-size) && (mouseY <= this.y+size)) {
      if (mousePressed)
      {
        if ((mouseX >= this.x) && (mouseX <= this.x+this.l)) {
          this.pos = mouseX;
        }
      }
    }
  }
  
  void drawSlider() {
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
  
  float ValReturn() {
    drawSlider();
    
    return(this.val);
  }
}
