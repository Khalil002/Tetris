//Esta clase Tetriminium es la clase padre para todos los tetriminos 
abstract class Tetriminium{
  private int[][] shape;
  private color c;
  private int x, y;
  private int[][][] shapes;
  private int currentRotation;
  public Tetriminium(int[][] shape, color c){
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
  public void setColor(color c){this.c = c;}
  public void setCurrentRotation(int currentRotation){this.currentRotation = currentRotation;}
  public void setShape(int[][] shape){this.shape = shape;}
  public void setShapes(int[][][] shapes){this.shapes = shapes;}
  
  //Getters
  public int getX(){return x;}
  public int getY(){return y;}
  public int getHeight(){return shape.length;}
  public int getWidth(){return shape[0].length;}
  public color getColor(){return c;}
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
