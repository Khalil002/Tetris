//Esta clase se encarga de crear los textBoxes
public class TextBox {
   private int x, y, w, h;
   private int txtSize = 25;
   private color background = color(255);
   private color foreground = color(0, 0, 0);
   private color backgroundSelected = color(160, 160, 160);
   private color border = color(30, 30, 30);
   
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
   public void setBackground(color background){this.background = background;}
   public void setForeground(color foreground){this.foreground = foreground;}
   public void setBackgroundSelected(color backgroundSelected){this.backgroundSelected =backgroundSelected;}
   public void setBorder(color border){this.border = border;}
   
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
