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
