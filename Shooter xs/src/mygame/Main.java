/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 *
 * @author Dylankuyjt67hr
 */
public class Main extends SimpleApplication implements ScreenController {

    private Trigger start_trigger = new KeyTrigger(KeyInput.KEY_SPACE);
    private boolean isRunning = false; // starts at startscreen
    private GameRunningState gameRunningState;
    private StartScreenState startScreenState;
    
    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);

        gameRunningState    = new GameRunningState(this);
        startScreenState    = new StartScreenState(this);
        startScreenState.simpleInitApp();      
        stateManager.attach(startScreenState);

        inputManager.addMapping("Start", start_trigger);
        inputManager.addListener(actionListener, new String[]{"Start"});
    }
    
    private ActionListener actionListener = new ActionListener() {
      public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Start") && !isPressed) {
          if (!isRunning) {
            stateManager.attach(gameRunningState);
            stateManager.detach(startScreenState);
            isRunning = true;
          }
        }
      }
    };    
    
    public static void main(String[] args) {
       Main app = new Main();
       app.setShowSettings(false);
        AppSettings settings = new AppSettings(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)screenSize.getWidth();
        int height = (int)screenSize.getHeight();
        settings.put("Width", width);
        settings.put("Height", height);
        settings.setFullscreen(true);
        settings.put("Shooter X", "My awesome Game");
        settings.put("VSync", true);
        //Anti-Aliasing
        settings.put("Samples", 4);
        app.setSettings(settings);
       app.start();
    }
    
    public void bind(Nifty nifty, Screen screen) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onStartScreen() {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onEndScreen() {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
