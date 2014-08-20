import java.awt.Robot;
import java.awt.event.InputEvent;

class xGloveMouse {
  Robot mouseRobot;     // create object from Robot class;
  static final int rate = 4; // multiplier to adjust movement rate

  xGloveGesture gesture;

  Dimension screen; //Computer screen data

  //TODO: Test this, and evaluate need to use AtomicInteger
  int x, y; //coordinates of mouse

  private boolean currentlyClicked;


  public xGloveMouse() {
    try {
      mouseRobot = new Robot();
    }
    catch (AWTException e) {
      e.printStackTrace();
    }

    screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    
    //Put mouse in middle of screen
    y =  (int)screen.getHeight() / 2 ;
    x =  (int)screen.getWidth() / 2;

    currentlyClicked = false;

    this.gesture = new xGloveGesture();
  }
  // method to move mouse from current posistion by given offset
  void move(int offsetX, int offsetY) {
    x += (rate * offsetX);
    y += (rate * offsetY);
    mouseRobot.mouseMove(x, y);
  }
  
  //Move mouse to a specified location
  void moveTo(int posX, int posY) {
    mouseRobot.mouseMove(posX, posY);
  }
 

    /* Function: mouse_left_click
   * --------------------------
   * The mouse_left_click function simulates a left click of the mouse
   * when the all fingers are bent slightly. The function uses the variable
   * currently_clicked avoid another click immediately after the previous click
   * because the hand was still in the clicking position. In order to click again
   * the glove first has to be returned to the non-click position (currently_clicked
   * then becomes zero). 
   */ 

  public boolean isCurrentlyClicked() {return currentlyClicked;}

  public void doMouseLeftClick() {        
    mouseRobot.mousePress(InputEvent.BUTTON1_DOWN_MASK);  // left click 
    currentlyClicked = true;    // there is a left click
  }  

  public void doMouseLeftClickRelease() {
    mouseRobot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); //release click
    currentlyClicked = false;   // there is no left click 
  }

    /* Function: move_mouse
   * --------------------
   * This function uses the data from the 9-DOF chip to move the mouse cursor. 
   * The function calls the readAxis function to turn the position of the 
   * glove into a corresponding position of the mouse cursor on the screen. 
   */
  public void moveMouse(){
      int xReading = getCursorPosition(gesture.getOrientation().heading, 0); // x-axis movement
      int yReading = getCursorPosition(gesture.getOrientation().roll, 1);    // y-axis movement

      //TODO: Make this pixel density independent 
      mouseRobot.move(5 * xReading, 4 * yReading, 0);       // move the mouse

      //May or may not need this delay
      mouseRobot.delay(10);
  }

    /* Function: get_cursor_position
   * -----------------------------------------------------------------
   * This function is used when moving the mouse. The function turns the
   * position of the glove into a corresponding position of the mouse cursor
   * on the screen. 
   */
  int getCursorPosition(int reading, int axisNumber) 
  {
      int distance = 0;    // distance from center of the output range
          
      // map the reading from the analog input range to the output range:
      reading = map(reading, minima[axisNumber], maxima[axisNumber], 0, range);
      
      // if the output reading is outside from the
      // rest position threshold,  use it:
      if (Math.abs(reading - center) > threshold) distance = (reading - center);

      // the reading needs to be inverted in order to 
      // map the movemment correctly:
      distance = -distance;

      // return the distance for this axis:
      return distance;
  }

    /* Function: mouse_scroll
   * ----------------------
   * This function simulates scrolling. The function enters and exits
   * scrolling mode when the ring finger is bent down while the middle finger
   * is not bent. After entering scrolling mode the scroll speed and direction
   * are determined by the bending direction and the inclination of all fingers. 
   * When the fingers are bent down, the page scrolls down. When the fingers are
   * bent up, the page scrolls up. The more the finger are bent the higher the 
   * scrolling speed. The scrolling speed is controlled by the for loop. 
   */

  void mouseScroll(){
    /* delay until user stretches ring finger again */
    while(gesture.exitScrollModeGesture()) Thread.sleep(20);
          
    /* while loop will exit when ring finger is bent much more than middle finger */
    while(!gesture.isMouseScrollGesture())
    {
      int inclinationPercentage = gesture.getInclinationPercentage();
            
      if(inclinationPercentage < 20)         // scroll up
      {
          mouseRobot.moveWheel(-1 * ((10 / (inclinationPercentage + 1) + 1))); //Negative value is up. Move wheel 1 or 2 notches depending on inclination percentage
      }
      else if(inclination_percentage > 80)   // scroll down
      { 
          mouseRobot.moveWheel(1 * (inclinationPercentage / 90 + 1)); //Move wheel down 1 or 2 notches depending on inclination percentage
      }
          
      /* delay until user stretches ring finger again */
      while(!gesture.exitScrollModeGesture()) Thread.sleep(20);
}
