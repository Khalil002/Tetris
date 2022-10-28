//Se importan las librerías de Minim, que sirven para la reproducción de música
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.signals.*;
import ddf.minim.spi.*;
import ddf.minim.ugens.*;

Minim minim;
AudioPlayer tetrisChill, tetrisPlay, tetrisLine, tetrisSuccess;

//Declaración de atributos del juego
private int screen;
private Menu m;
private Game g;
private LeaderBoard lb;
private GameOver gover;
private Star[] stars;
PFont fuente;

public void setup(){
  size(600, 600);
  screen=0;
  m = new Menu();
  //Se llena un vector de estrellas que servirá para la decoración del fondo
  stars= new Star[100];
  for (int i=0; i<stars.length; i++) {
    stars[i] = new Star();
  }
  //Se carga la música
  minim= new Minim(this);
  tetrisChill=minim.loadFile("assets/Lo-Fi Tetris Theme (Korobeiniki).mp3");
  tetrisPlay=minim.loadFile("assets/TETRIS THEME SONG (TRAP REMIX) - DaBrozz.mp3");
  tetrisLine=minim.loadFile("assets/line.wav");
  tetrisSuccess=minim.loadFile("assets/success.wav");
  tetrisChill.loop();
  fuente = createFont("assets/forcedSquare.ttf", 32);
}

public void draw(){
  background(0);
  showStars();
  textFont(fuente);
  //Se determina el menú o pantalla que se debe mostrar
  switch(screen){
    case 0:
      //Pantalla del menú
      m.show(); 
    break;
    case 1:
      //Juego
      g.show();
    break;
    case 2:
      //Game over
      gover.show();
    break;
    case 3:
      //Se muestra el leaderboard
      lb.show();
    break;
  }
}

//Dibuja las estrellas
private void showStars() {
    for (int i=0; i<stars.length; i++) {
      stars[i].update();
      stars[i].show();
    }
}

//Getters
public int getScreen(){return screen;}
public Menu getM(){return m;}
public Game getG(){return g;}
public LeaderBoard getLb(){return lb;}
public GameOver getGover(){return gover;}

//Setters
public void setScreen(int screen){this.screen = screen;}
public void setG(Game g){this.g = g;}
public void setLb(LeaderBoard lb){this.lb = lb;}
public void setGover(GameOver gover){this.gover = gover;}

//Ayuda a determinar si se oprimió en el textbox que está en el menú de Game Over
void mousePressed(){
  if(screen==2){
    gover.pressed();
  }
}

//Le da una función distinta a las teclas dependiendo de qué pantalla esté mostrando
void keyPressed(){
  if(screen==1){
    g.ga.move(key);
  }else if(screen==2){
    gover.kpressed();
  }
}
