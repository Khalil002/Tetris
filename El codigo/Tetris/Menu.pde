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
