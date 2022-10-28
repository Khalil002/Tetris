//Esta clase se encarga de dibujar todo el area del juego (la cuadrícula) y realizar toda la parte logica dentro de ella 
class GameArea{
  private int gridRows;
  private int gridColumns;
  private int gridCellSize;
  private color[][] background;
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
    this.background = new color[gridRows][gridColumns];
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
    color clr = block.getColor();
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
    color c = block.getColor();
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
  private void drawGridSquare(int x, int y, color c){
    stroke(255);
    strokeWeight(1);
    fill(c);
    rectMode(CORNER);
    rect(x, y, gridCellSize, gridCellSize);
  }
}
