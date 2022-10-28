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
    color c = b.getColor();
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
  
  private void drawGrid(int x, int y, color c){
    stroke(255);
    strokeWeight(1);
    fill(c);
    rectMode(CORNER);
    rect(x, y, gridCellSize, gridCellSize);
  }
  
  //Se crea un nuevo bloque aleatorio
  private Tetriminium createBlock(){
    int r = int(random(0, 7));
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
