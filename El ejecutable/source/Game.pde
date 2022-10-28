//Clase del juego, se encarga de guardar tu puntaje y empezar el thread y el gameArea
class Game{
  private GameThread gt;
  private GameArea ga;
  private int score=0;
  private int level=1;
  
  public Game(){
    this.ga = new GameArea();
    this.gt = new GameThread(this, ga);
    gt.start();
  }
  
  public void show(){
    //Dibuja los elementos complementarios del juego
    noFill();
    strokeWeight(3);
    stroke(240);
    rectMode(CORNER);
    rect(40, 270, 120, 200, 20);
    rect(200, 100, 200, 400);
    rect(450, 130, 100, 240, 20);
    rect(50, 130, 100, 80, 20);
    rect(480, 390, 40, 40, 10);
    rect(480, 440, 40, 40, 10);
    rect(430, 440, 40, 40, 10);
    rect(530, 440, 40, 40, 10);
    rect(430, 490, 140, 40, 10);
    rectMode(CENTER);
    noStroke();
    fill(255);
    textAlign(CENTER);
    text("W", 502, 420);
    text("A", 452, 468);
    text("S", 502, 468);
    text("D", 552, 468);
    textSize(18);
    text("ROTATE", 562, 415);
    text("SPACE BAR", 502, 515);
    text("Press to HOLD", 502, 550);
    textSize(32);
    text("NEXT", 500, 120);
    text("HOLD", 100, 120);
    text("LEVEL", 100, 300);
    text(level, 100, 340);
    text("SCORE", 100, 400);
    text(score, 100, 440);
    ga.show();
  }
  //Setters y getters
  public void setScore(int score){this.score=score;}
  public void setLevel(int level){this.level=level;}
  public int getScore(){return score;}
  public int getLevel(){return level;}
}
