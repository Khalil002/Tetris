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
