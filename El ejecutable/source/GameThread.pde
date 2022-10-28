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
