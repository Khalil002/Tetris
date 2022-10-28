import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import ddf.minim.signals.*; 
import ddf.minim.spi.*; 
import ddf.minim.ugens.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Tetris extends PApplet {

//Se importan las librerías de Minim, que sirven para la reproducción de música







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
public void mousePressed(){
  if(screen==2){
    gover.pressed();
  }
}

//Le da una función distinta a las teclas dependiendo de qué pantalla esté mostrando
public void keyPressed(){
  if(screen==1){
    g.ga.move(key);
  }else if(screen==2){
    gover.kpressed();
  }
}
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
//Esta clase se encarga de dibujar todo el area del juego (la cuadrícula) y realizar toda la parte logica dentro de ella 
class GameArea{
  private int gridRows;
  private int gridColumns;
  private int gridCellSize;
  private int[][] background;
  private Tetriminium block;
  private Next n;
  private Hold h;
  private boolean sw;
  
  public GameArea(){
    this.n = new Next();
    this.h = new Hold();
    this.sw = true;
    this.gridRows = 20;
    this.gridColumns = 10;
    this.gridCellSize = 20;
    this.background = new int[gridRows][gridColumns];
    for(int r=0; r<gridRows; r++){
      for(int c=0; c<gridColumns; c++){
        background[r][c] = color(0);
      }
    }
  }
  
  public void show(){
    //Dibuja la figura que está en hold y las figuras que están en next, así como el fondo y de dibujar los bloques  
    n.show();
    h.show();
    pushMatrix();
    translate(200, 100);
    drawBackground();
    drawBlock();
    popMatrix();
  }
  //Se encarga de determinar hacia donde va a rotar la figura dependiendo de la tecla presionada
  private void move(char key){
    if(key == 'd' || key == 'D' || keyCode == 39){
      moveBlockRight();
    }
    if(key == 'a' || key == 'A' || keyCode == 37){
      moveBlockLeft();
    }
    if(key == 's' || key == 'S' || keyCode == 40){
      moveBlockDown();
    }
    if(key == 'w' || key == 'W' || keyCode == 38){
      rotateBlock();
    }
    //Se encarga específicamente de cambiar el bloque por el que se encuentra en hold
    if(key == 32){
      
      switchBlock();
    }
  }
  
  //Hace que aparezca el siguiente bloque 
  public void spawnBlock(){
    block = n.getBlock();
    block.spawn(gridColumns);
  }
  
  private void switchero(Tetriminium blok){
    int tempX = block.getX();
    int tempY = block.getY();
    block=blok;
    block.setX(tempX);
    block.setY(tempY);
  }
  
  //Cambia el bloque por el que está en hold
  public void switchBlock(){
    //Se verifica si ya se habia switcheado el bloque en el drop actual
    if(sw){
        //Si no hay ningún bloque en el hold, se lleva el actual a hold y se utiliza el siguiente bloque en cola
        if(h.getHold()==null){
          h.setHold(block);
          switchero(n.getBlock());
          if(!checkRotate()){
            switchero(h.getHold());
            h.setHold(null);
          }
        }else{
          Tetriminium tempBlock = block;
          switchero(h.getHold());
          h.setHold(tempBlock);
          if(!checkRotate()){
            tempBlock = block;
            switchero(h.getHold());
            h.setHold(tempBlock);
          }
        }
        
        
        if(block.getLeftEdge()<0) {block.setX(0);}
        if(block.getRightEdge()>=gridColumns) {block.setX(gridColumns - block.getWidth());}
        if(block.getBottomEdge() >= gridRows) {block.setY(gridRows - block.getHeight());}
        sw=false;
    }
  }
  
  //Verifica si el bloque se ha salido del área de juego
  public boolean isBlockOutOfBounds(){
    if(block.getY()<0){
      block=null;
      return true;
    }
    return false;
  }
  
  //Hace las respectivas validaciones para cada posible movimiento pedido y luego lo ejecuta o no hace nada
  public void moveBlockRight(){
    if(block==null){return;}
    if(!checkRight()){return;}
    block.moveRight();
  }
  
  public void moveBlockLeft(){
    if(block == null){return;}
    if(!checkLeft()){return;}
    block.moveLeft();
  }
  
  public boolean moveBlockDown(){
    if(checkBottom()){
      block.moveDown();
      return true;
    }
    return false;
  }
  
  //Rota el bloque y luego verifica que la rotación se haya podido efectuar. Después lo acomoda
  public void rotateBlock(){
    if(block == null){return;}
    block.rotate();
    if(!checkRotate()){
      block.unRotate();
    }
    if(block.getLeftEdge()<0) {block.setX(0);}
    if(block.getRightEdge()>=gridColumns) {block.setX(gridColumns - block.getWidth());}
    if(block.getBottomEdge() >= gridRows) {block.setY(gridRows - block.getHeight());}
  }
  //Hace que el bloque baje continuamente
  public void dropBlock(){
    if(block == null){return;}
    while(checkBottom()){
      block.moveDown();
    }
  }
  
  //Verifica los espacios abajo, a la derecha y a la izquierda del bloque
  private boolean checkRight(){
    if(block.getRightEdge() == gridColumns){
      return false;
    }
    
    int[][]shape = block.getShape();
    int w = block.getWidth();
    int h = block.getHeight();
    for(int row = 0; row<h; row++){
      for(int col = w-1; col>=0; col--){
        if(shape[row][col] != 0){
          int x = col + block.getX() + 1;
          int y = row + block.getY();
          if(y<0){
            break;
          }
          //Se verifica si los espacios a la derecha del bloque están ocupados por otros bloques
          if(background[y][x] != color(0)){
            return false;
          }
          break;
        }
      }
    }
    return true;
  }
  
  private boolean checkLeft(){
    if(block.getLeftEdge() == 0){
      return false;
    }
    int[][]shape = block.getShape();
    int w = block.getWidth();
    int h = block.getHeight();
    for(int row = 0; row<h; row++){
      for(int col = 0; col<w; col++){
        if(shape[row][col] != 0){
          int x = col + block.getX() - 1;
          int y = row + block.getY();
          if(y<0){
            break;
          }
          //Se verifica si los espacios a la izquierda del bloque están ocupados por otros bloques
          if(background[y][x] != color(0)){
            return false;
          }
          break;
        }
      }
    }
    return true;
  }
  
  private boolean checkBottom(){
    if(block == null){
      return false;
    }
    if(block.getBottomEdge() == gridRows){
      return false;
    }
    int[][]shape = block.getShape();
    int w = block.getWidth();
    int h = block.getHeight();
    for(int col = 0; col<w; col++){
      for(int row = h-1; row>=0; row--){
        if(shape[row][col] != 0){
          int x = col + block.getX();
          int y = row + block.getY() + 1;
          if(y<0){
            break;
          }
          //Se verifica si los espacios abajo del bloque están ocupados por otros bloques
          if(background[y][x] != color(0)){
            return false;
          }
          break;
        }
      }
    }
    return true;
  }
  
  //Se verifica que la figura si se pueda rotar, que no se salga de los límites ni se superponga con otros bloques
  private boolean checkRotate(){
    int[][] shape = block.getShape();
    int w = block.getWidth();
    int h = block.getHeight();
    for (int row = 0; row < h; row++) {
      for (int col = 0; col < w; col++) {
        if (shape[row][col] != 0) {
          int x = col + block.getX();
          int y = row + block.getY();
          if (y < 0) {
            break;
          }
          if (x >= gridColumns){
            break;
          }
          if (y >= gridRows){
            break;
          }
          //Se verifica si los espacios a los que va a rotar el bloque están ocupados por otros bloques
          if (background[y][x] != color(0)) {
            
            return false;
          }
        }
      }
    }
    return true;
  }
  
  //Elimina la fila deseada completa
  private void clearLine(int r){
    tetrisLine.play();
    tetrisLine.rewind();
    for(int i=0; i<gridColumns; i++){
      background[r][i]= color(0);
    }
  }
  
  //Revisa qué filas y qué cantidad de filas se llenaron, para proceder a eliminarlas y bajar las que están por encima
  public int clearLines(){
    boolean lineFilled;
    int linesCleared=0;
    for (int r = gridRows - 1; r >= 0; r--) {
      lineFilled = true;
      for (int c = 0; c < gridColumns; c++) {
        if (background[r][c] == color(0)) {
          lineFilled = false;
          break; 
        }
      }
      if (lineFilled) {
        linesCleared++;
        clearLine(r);
        shiftDown(r);
        clearLine(0);
        r++;
      }
    }
    return linesCleared;
  }
  
  //Baja todas las filas a la que está inmediatamente por debajo
  private void shiftDown(int r){
    for (int row = r; row > 0; row--) {
      for (int col = 0; col < gridColumns; col++) {
        background[row][col] = background[row-1][col];
      }
    }
  }
  
  //Deja dibujado el bloque guardado, estático y dibujado en el fondo para que se de espacio a otro bloque
  public void moveBlockToBackground(){
    int[][] shape = block.getShape();
    int h = block.getHeight();
    int w = block.getWidth();
    int xPos = block.getX();
    int yPos = block.getY();
    int clr = block.getColor();
    for (int r = 0; r < h; r++) {
      for (int c = 0; c < w; c++) {
        if (shape[r][c] == 1) {
          background[r + yPos][c + xPos] = clr;
        }
      }
    }
    sw=true;
  }
  
  //Dibuja el bloque con el que se está jugando en el momento
  private void drawBlock(){
    if(block==null) return;
    int h = block.getHeight();
    int w = block.getWidth();
    int c = block.getColor();
    int[][] shape = block.getShape();
    for(int row=0; row<h; row++){
      for(int col=0; col<w; col++){
        if(shape[row][col]==1){
          int x = (block.getX()+col)*gridCellSize;
          int y = (block.getY()+row)*gridCellSize;
          if(y/gridCellSize>=0){
            drawGridSquare(x, y, c);
          }
        }
      }
    }
  }
  
  //Dibuja la cuadrícula completa cuadro por cuadro
  private void drawBackground(){
    for(int r=0; r<gridRows; r++){
      for(int c=0; c<gridColumns; c++){
        int x = c*gridCellSize;
        int y = r*gridCellSize;
        drawGridSquare(x, y, background[r][c]);
      }
    }
  }
  
  //Dibuja cada espacio de la cuadrícula
  private void drawGridSquare(int x, int y, int c){
    stroke(255);
    strokeWeight(1);
    fill(c);
    rectMode(CORNER);
    rect(x, y, gridCellSize, gridCellSize);
  }
}
//Esta clase se encarga de mostrar la pantalla del gameOver y guardar tu puntaje si es lo suficientemente alto para entrar en el top
class GameOver{
  private ArrayList<Boton> botones;
  TextBox txtBox;
  public GameOver(){
    txtBox = new TextBox(320, 300, 150, 50);
    inicBotones();
  }
  //Se inicializan botones
  private void inicBotones(){
    botones = new ArrayList(); 
    botones.add(new Boton("BACK TO MENU", 15, 200, 450, 150, 50));
    botones.add(new Boton("SAVE SCORE", 15, 400, 450, 150, 50));
  }
  
  public void show(){
    //Se dibuja el menú para el final del juego
    stroke(255);
    strokeWeight(2);
    fill(0);
    rectMode(CORNER);
    rect(100,100,400,400);
    fill(255);
    textSize(50);
    textAlign(CENTER);
    text("Game Over", width/2, 180);
    textSize(15);
    //Se muestra el puntaje final
    text("Score: "+g.getScore(), 300, 250);
    text("Name (only caps):", 200, 330);
    textAlign(LEFT, CENTER);
    txtBox.show();
    for(Boton b: botones){
      b.display();
    }
    actions();
  }
  private void actions(){
    //Se determina qué acciones realizar dependiendo de la posición del cursor (si está sobre algún botón)
    if(mousePressed){
      if(botones.get(0).isInside()){screen = 0;}
      if(botones.get(1).isInside()){
        //Si se da a guardar puntaje, se guarda si el nombre ocupa 4 caracteres y se guarda en el leaderboard
        if(txtBox.getText().length()==4){
          setLb(new LeaderBoard());
          lb.guardarPuntaje(txtBox.getText(), g.getScore());
          screen = 3;
        }
        
      }
    }
  }
  //Se verifica donde se presionó el botón (si está encima del textBox se selecciona y se permite escribir)
  public void pressed(){
    txtBox.pressed(mouseX, mouseY);
  }
  //Se verifica qué teclas fueron oprimidas para representarlas en pantalla
  public void kpressed(){
    txtBox.keyPresseD(key, keyCode);
  }
}
//Esta clase se encarga de que el juego corra abriendo otra thread para que se actualize el puntaje, el nivel, que se cree otro bloque cuando llegue el bloque al piso o toque a otro, etc... 
class GameThread extends Thread{
  private Game g;
  private GameArea ga;
  private int pause=1000;
  private int speedUpPerLevel=100;
  private int scorePerLevel=100;
  
  public GameThread(Game g, GameArea ga){
    this.g = g;
    this.ga = ga;
  }
  
  //Se sobreescribe el método run que proviene de la clase Thread 
  @Override
  public void run(){
    while(true){
      ga.spawnBlock();
      while(ga.moveBlockDown()){
        try{
          Thread.sleep(pause);
        }catch(InterruptedException ex){
          println(ex);
        }
      }
      //Si el bloque está fuera de los límites del juego se considera como que se acabó el juego y se muestra el menú de Game Over
      if(ga.isBlockOutOfBounds()){
        setGover(new GameOver());
        screen=2;
        tetrisPlay.pause();
        tetrisSuccess.play();
        tetrisSuccess.rewind();
        tetrisChill.loop();
        break;
      }
      //Se mueve el bloque al suelo y se hace la verificación para eliminar filas cuando se han llenado
      ga.moveBlockToBackground();
      int s = ga.clearLines();
      //Se suma al puntaje el valor en puntos por la cantidad de filas que se han eliminado
      g.setScore(g.getScore()+10*(s*s));
      //Se verifica si se han sumado puntos y, si es el caso, se aumenta de nivel, por lo que disminuye el tiempo para que los bloques bajen
      int lvl = g.getScore()/scorePerLevel;
      if(lvl>g.getLevel()){
        g.setLevel(lvl);
        pause -=speedUpPerLevel;
      }
    }
  }
}
//Esta clase se encarga de dibujar y guardar el bloque del HOLD y intercambiarlo cuando se necesite
class Hold{
  private int gridCellSize;
  private Tetriminium block;
  private int origRotation;
  public Hold(){
    block = null;
    gridCellSize=10;
    origRotation=0;
  }
  
  public void show(){
    if(block!=null){
      drawB(block, 9, 15);
    }
  }
  
  //Se dibuja el bloque en el espacio del hold
  private void drawB(Tetriminium b, int posX, int posY){
    pushMatrix();
    int h = b.getHeight();
    int w = b.getWidth();
    int c = b.getColor();
    int[][] shape = b.getShape();
    for(int row=0; row<h; row++){
      for(int col=0; col<w; col++){
        if(shape[row][col]==1){
          int x = (posX+col)*gridCellSize;
          int y = (posY+row)*gridCellSize;
          drawGrid(x, y, c);
        }
      }
    }
    popMatrix();
  }
  
  private void drawGrid(int x, int y, int c){
    stroke(255);
    strokeWeight(1);
    fill(c);
    rectMode(CORNER);
    rect(x, y, gridCellSize, gridCellSize);
  }
  
  //Se devuelve el bloque del Hold al juego
  public Tetriminium getHold(){
    if(block!=null){
      block.setCurrentRotation(origRotation);
      this.block.setRotation();
    }
    return block;
  }
  //Se guarda el bloque introducido en el Hold
  public void setHold(Tetriminium block){
    if(block instanceof Tetriminium){
      if(block instanceof I){
        this.block = new I();
      }else if(block instanceof L){
        this.block = new L();
      }else if(block instanceof J){
        this.block = new J();
      }else if(block instanceof O){
        this.block = new O();
      }else if(block instanceof S){
        this.block = new S();
      }else if(block instanceof Z){
        this.block = new Z();
      }else if(block instanceof T){
        this.block = new T();
      }
      this.block = block;
      origRotation = block.getCurrentRotation();
      this.block.setCurrentRotation(3);
      this.block.setRotation();
    }else{
      this.block = null;
    }
  }
}
//Esta clase se encarga de dibujar la leaderboard y actualizarla
class LeaderBoard {
  private Boton[] b = new Boton[2];
  private String top[][] = new String[10][3];
  Table table;
  
  public LeaderBoard(){
    for(int i=0; i<10; i++){
        top[i][0] = str(i+1);
        top[i][1] = "";
        top[i][2] = "";
    }
    botones();
    loadTop();
  }
  //Se encarga buscar y guardar los 10 mejores puntajes registrados en el archivo de puntajes
  public void loadTop(){
    try{
      table = loadTable("data/top.csv", "header");
      for(int i=0; i<10; i++){
      TableRow row = table.getRow(i);
      String name = row.getString("name");
      String score = row.getString("score");
      top[i][1] = name;
      top[i][2] = score;
      }
    }catch(Exception er){
      newTop();
    }
  }
  //Se limpia la tabla y se crea con valores por defecto
  private void newTop(){
    for(int i=0; i<10; i++){
      top[i][1] = "-";
      top[i][2] = "0";
    }
    saveTop();
  }
  //Crea una tabla y la guarda en el archivo de los puntajes
  private void saveTop(){
    table = new Table();
    table.addColumn("name");
    table.addColumn("score");
    for(int i=0; i<10; i++){
      TableRow newRow = table.addRow();
      newRow.setString("name", top[i][1]);
      newRow.setString("score", top[i][2]);
    }
    saveTable(table, "data/top.csv");
  }
  //Si se guarda un puntaje, busca su posición en la lista y acomoda el resto de posiciones para guardarse debidamente
  public void guardarPuntaje(String n, int sc){
    loadTop();
    for(int i=0; i<top.length; i++){
      if(sc>=PApplet.parseInt(top[i][2])){
        for(int j=top.length-1; j>i; j--){
          top[j][1] = top[j-1][1];
          top[j][2] = top[j-1][2];
        }
        top[i][1] = n;
        top[i][2] = str(sc);
        break;
      }
    }
    saveTop();
  }
  
  //Muestra la lista de los puntajes con sus respectivos jugadores y lugares
  private void drawTop(){
    fill(255);
    textAlign(LEFT);
    text("RANKING", 100, 100);
    text("NAME", 250, 100);
    text("SCORE", 400, 100);
    for(int i=0; i<10; i++){
      text(top[i][0]+".", 100, 150+30*i);
      text(top[i][1], 250, 150+30*i);
      text(top[i][2], 400, 150+30*i);
    }
  }
  
  private void botones() {
    b[0] = new Boton("RETURN", 25, 200, 570, 200, 30);
    b[1] =  new Boton("CLEAN", 25, 400, 570, 200, 30);
  }

  //Dibuja todo el menú de la pantalla y ejecuta continuamente las funciones
  public void show() {
    drawBotones();
    Tabla();
    drawTop();
    actions();
  }

  private void drawBotones() {
    b[0].display();
    b[1].display();
    b[0].isInside();
    b[1].isInside();
  }

  //Dibuja la tabla donde aparecen los puntajes
  private void Tabla() {
    strokeWeight(8);
    stroke(255);
    fill(0);
    rect(width/2, height/2-50, width-60, 400, 10);
  }
  
  //Verifica qué botones fueron presionados y ejecuta sus respectivas acciones
  private void actions() {
    if (mousePressed) {
      if (b[0].isInside()) {
        mouseY=0;
        screen = 0;
      } else if (b[1].isInside()) {
        newTop();
      } 
    }
  }

  
}
//Esta clase dibuja el menu
class Menu{
  private ArrayList<Boton> botones;
  
  public Menu(){
    inicBotones();
  }
  //Se inicializan los botones
  private void inicBotones(){
    botones = new ArrayList(); 
    botones.add(new Boton("PLAY", 25, width/2, 300, 400, 50));
    botones.add(new Boton("LEADERBOARD", 25, width/2, 400, 400, 50));
    botones.add(new Boton("EXIT", 25, width/2, 500, 400, 50));
  }
  
  //Dibuja todo lo que tiene que mostrar el menú y verifica las acciones
  public void show(){
    showTitle();
    showBotones();
    actions();
  }
  
  private void showTitle() {
    textAlign(CENTER, TOP);
    textSize(100);
    fill(225);
    text("TETRIS", width/2, 100);
  }
  
  private void showBotones() {
    for (Boton b : botones) {
      b.display();
    }
  }
  
  //Verifica donde fue presionado el mouse y acorde a eso cambia la pantalla a mostrar
  private void actions() {
    if (mousePressed) {
      if (botones.get(0).isInside()) {
        setG(new Game()); //Cada vez que se oprima play se crea un nuevo tablero de juego y es mostrado
        screen = 1;
        tetrisChill.pause();
        tetrisPlay.rewind();
        tetrisPlay.loop();
      } else if (botones.get(1).isInside()) {
        setLb(new LeaderBoard());
        screen = 3; //Se muestra el leaderboard
      } else if (botones.get(2).isInside()) {
        exit(); //Se sale del juego
      }
    }
  }
}
//Esta clase se encarga de dibujar los bloques del NEXT y cambiarlos cuando se spawnee un bloque en el GameArea
class Next{
  int gridCellSize;
  Tetriminium nextB[];
  public Next(){
    this.gridCellSize=10;
    this.nextB = new Tetriminium[3];
    for(int i=0; i<3; i++){
      this.nextB[i] = createBlock();
    }
  }
  
  //Se dibujan los bloques de la cola en su posición correspondiente
  public void show(){
    for(int i=0; i<3; i++){
      drawB(this.nextB[i], 49, 15+8*i);
    }
  }
  
  //Se dibuja el bloque
  private void drawB(Tetriminium b, int posX, int posY){
    pushMatrix();
    int h = b.getHeight();
    int w = b.getWidth();
    int c = b.getColor();
    int[][] shape = b.getShape();
    for(int row=0; row<h; row++){
      for(int col=0; col<w; col++){
        if(shape[row][col]==1){
          int x = (posX+col)*gridCellSize;
          int y = (posY+row)*gridCellSize;
          drawGrid(x, y, c);
        }
      }
    }
    popMatrix();
  }
  
  private void drawGrid(int x, int y, int c){
    stroke(255);
    strokeWeight(1);
    fill(c);
    rectMode(CORNER);
    rect(x, y, gridCellSize, gridCellSize);
  }
  
  //Se crea un nuevo bloque aleatorio
  private Tetriminium createBlock(){
    int r = PApplet.parseInt(random(0, 7));
    Tetriminium b = null;
    switch(r){
      case 0:
        b = new I();
      break;
      case 1:
        b = new L();
      break;
      case 2:
        b = new J();
      break;
      case 3:
        b = new O();
      break;
      case 4:
        b = new S();
      break;
      case 5:
        b = new Z();
      break;
      case 6:
        b = new T();
      break;
    }
    return b;
  }
  
  //Se crea un nuevo bloque y se asigna a la cola
  public Tetriminium getBlock(){
    Tetriminium b = nextB[0];
    this.nextB[0] = nextB[1];
    this.nextB[1] = nextB[2];
    this.nextB[2] = createBlock();
    return b;
  }
}
//Esta clase crea las estrellas que se ven en el background
class Star{
  private float x;
  private float y;
  private float z;
  private float s;
  private int c;
  
  public Star(){  //Se crea una nueva estrella en una posición aleatoria de grosor aleatorio entre 1 y 5 píxeles
    x=random(-width,width);
    y=random(0,height);
    s= random(1, 5);
    colour();
  }
  
  //Se le asigna un color aleatorio a la estrella 
  private void colour(){
    switch(PApplet.parseInt(random(1,7))){
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
//Esta clase Tetriminium es la clase padre para todos los tetriminos 
abstract class Tetriminium{
  private int[][] shape;
  private int c;
  private int x, y;
  private int[][][] shapes;
  private int currentRotation;
  public Tetriminium(int[][] shape, int c){
    this.shape = shape;
    this.c = c;
    initShapes();
  }

  public void initShapes(){
    shapes = new int[4][][];
    for(int i=0; i<4; i++){
      int r = shape[0].length;
      int c = shape.length;
      
      shapes[i] = new int [r][c];
      
      for(int j=0; j<r; j++){
        for(int k=0; k<c; k++){
          shapes[i][j][k] = shape[c-k-1][j];
        }
      }
      shape = shapes[i];
    }
  }
  
  public void spawn(int gridWidth){
    currentRotation=3;
    shape=shapes[currentRotation];
    x=(gridWidth-getWidth())/2;
    y=-getHeight();
  }
  
  
  //Setters
  public void setX(int x){this.x=x;}
  public void setY(int y){this.y=y;}
  public void setColor(int c){this.c = c;}
  public void setCurrentRotation(int currentRotation){this.currentRotation = currentRotation;}
  public void setShape(int[][] shape){this.shape = shape;}
  public void setShapes(int[][][] shapes){this.shapes = shapes;}
  
  //Getters
  public int getX(){return x;}
  public int getY(){return y;}
  public int getHeight(){return shape.length;}
  public int getWidth(){return shape[0].length;}
  public int getColor(){return c;}
  public int getCurrentRotation(){return currentRotation;}
  public int[][] getShape(){return shape;}
  public int[][][] getShapes(){return shapes;}
  public int getBottomEdge(){ return y + getHeight(); }
  public int getRightEdge(){ return x + getWidth(); }
  public int getLeftEdge(){ return x; }
  
  public void moveDown(){y++;}
  public void moveRight(){x++;}
  public void moveLeft(){x--;}
  
  public void rotate(){
    currentRotation++;
    if(currentRotation > 3) { currentRotation = 0; }
    shape = shapes[currentRotation];
  }
  public void setRotation(){
    shape = shapes[currentRotation];
  }
  public void unRotate(){
    currentRotation--;
    if(currentRotation<0){currentRotation=3;}
    shape = shapes[currentRotation];
  }
  
  
}

//Cada tetrimino es construido con su forma y color especificos
public class I extends Tetriminium{
  I(){
    super(new int[][]{{1}, {1}, {1}, {1}}, color(0, 170, 228));
  }
}
public class L extends Tetriminium{
  L(){
    super(new int[][]{{1, 0}, {1, 0}, {1, 1}}, color(0, 0, 255));
  }
}
public class J extends Tetriminium{
  J(){
    super(new int[][]{{0, 1}, {0, 1}, {1, 1}}, color(255, 166, 0));
  }
}
public class O extends Tetriminium{
  O(){
    super(new int[][]{{1,1},{1,1}}, color(255, 204, 0));
  }
}
public class S extends Tetriminium{
  S(){
    super(new int[][]{{1, 0},{1, 1},{0, 1}}, color(0, 255, 0));
  }
}
public class Z extends Tetriminium{
  Z(){
    super(new int[][]{{0, 1},{1, 1},{1, 0}}, color(255, 0, 0));
  }
}
public class T extends Tetriminium{
  T(){
    super(new int[][]{{0, 1},{1, 1},{0, 1}}, color(255,120,203));
  }
}
//Esta clase se encarga de crear los textBoxes
public class TextBox {
   private int x, y, w, h;
   private int txtSize = 25;
   private int background = color(255);
   private int foreground = color(0, 0, 0);
   private int backgroundSelected = color(160, 160, 160);
   private int border = color(30, 30, 30);
   
   private boolean borderEnable = false;
   private int borderWeight = 1;
   
   private String text = "";
   private int textLength = 0;
   
   private boolean selected = false;
   
   // crea el default textBox 
   TextBox() {
      x = width/2;
      y = height/2;
      w = 100;
      h = 50;
   }
   
   TextBox(int x, int y, int w, int h) {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
   }
   //Getters y setters
   public String getText(){return text;}
   public void setTextSize(int txtSize){this.txtSize = txtSize;}
   public void setBackground(int background){this.background = background;}
   public void setForeground(int foreground){this.foreground = foreground;}
   public void setBackgroundSelected(int backgroundSelected){this.backgroundSelected =backgroundSelected;}
   public void setBorder(int border){this.border = border;}
   
   //Las características del textBox y como se debe mostrar
   public void show() {
      if (selected) {
         fill(backgroundSelected);
      } else {
         fill(background);
      }
      
      if (borderEnable) {
         strokeWeight(borderWeight);
         stroke(border);
      } else {
         noStroke();
      }
      
      rect(x, y, w, h, 10);
      fill(foreground);
      textSize(txtSize);
      text(text, x + (textWidth("a") / 2), y + txtSize);
   }
   
   //Decide que hacer al oprimir una tecla (permite borrar y escribir únicamente en mayúsculas)
   public  void keyPresseD(char Key, int KeyCode) {
      if (selected) {
         if (KeyCode == (int)BACKSPACE) {
            backspace();
         } else {
            boolean isKeyCapitalLetter = (Key >= 'A' && Key <= 'Z');
            if (isKeyCapitalLetter) {
               addText(Key);
            }
         }
      }
   }
   
   //Añade texto a la cadena al oprimir una tecla (máximo de 4 caracteres)
   private void addText(char text) {
      if (this.text.length()<4) {
         this.text += text;
         textLength++;
      }
   }
   
   //Borra texto en caso de apretar el borrar
   private void backspace() {
      if (textLength - 1 >= 0) {
         text = text.substring(0, textLength - 1);
         textLength--;
      }
   }
    
   //Verifica que el cursor esté sobre la textBox
   private boolean overBox(int x, int y) {
      if (x >= this.x && x <= this.x + w) {
         if (y >= this.y && y <= this.y + h) {
            return true;
         }
      }
      
      return false;
   }
   
   //Verifica si, estando el cursor sobre el textBox, fue oprimido
   public void pressed(int x, int y) {
      if (overBox(x, y)) {
         selected = true;
      } else {
         selected = false;
      }
   }
}
  public void settings() {  size(600, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Tetris" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
