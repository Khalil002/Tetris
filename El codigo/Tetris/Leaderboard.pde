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
  void loadTop(){
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
      if(sc>=int(top[i][2])){
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
