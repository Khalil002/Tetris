//Clase de los botones
class Boton{
  private String txt;
  private float txtSize, x, y, w, h;
  
  //Constructor del boton
  public Boton(String txt, float txtSize, float x, float y, float w, float h){
    this.txt= txt;
    this.txtSize= txtSize;
    this.x= x;
    this.y= y;
    this.w= w;
    this.h= h;
  }
  
  //Dibuja el boton
  public void display(){
    rectMode(CENTER);
    fill(0);
    stroke(isInside()? color(0,255,0) : color(225));
    strokeWeight(8);
    rect(x,y,w,h, 10);
    
    //Dibuja el texto del boton
    textAlign(CENTER, CENTER);
    textSize(txtSize);
    fill(225);
    text(txt, x, y-2);
  }
  
  //Verifica si el cursor esta dentro del boton
  public boolean isInside() {
    return mouseX > (x-w/2) & mouseX < (x+w/2) & mouseY > (y-h/2) & mouseY < (y+h/2);
  }
}
