//Esta clase crea las estrellas que se ven en el background
class Star{
  private float x;
  private float y;
  private float z;
  private float s;
  private color c;
  
  public Star(){  //Se crea una nueva estrella en una posición aleatoria de grosor aleatorio entre 1 y 5 píxeles
    x=random(-width,width);
    y=random(0,height);
    s= random(1, 5);
    colour();
  }
  
  //Se le asigna un color aleatorio a la estrella 
  private void colour(){
    switch(int(random(1,7))){
      case 1:
      c = color(255, 204, 0);
      break;
      case 2:
      c = color(0, 0, 255);
      break;
      case 3:
      c = color(255, 166, 0);
      break;
      case 4:
      c = color(255,120,203);
      break;
      case 5:
      c = color(0, 170, 228);
      break;
      case 6:
      c = color(255, 0, 0);
      break;
      case 7:
      c = color(0, 255, 0);
      break;
    }
  }
  
  public void update(){ //Una vez la estrella haya llegado al fondo, se reposiciona de forma aleatoria
    y+=1;
    if(y>height){
      y=0;
      x=random(-width,width);
      y=random(0,height);
      s= random(1, 5);
      
    }
  }
  public void show(){ //Se muestra una elipse blanca, que sería la estrella
    noStroke();
    fill(c);
    ellipse(x,y,s,s);
  }
}
