/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author Dylankuyjt67hr
 */
public class StartScreenState extends AbstractAppState implements ScreenController {
 
    private ViewPort viewPort;
    private Node rootNode;
    private Node guiNode;
    private AssetManager assetManager;  
    private InputManager inputManager;  
    private AudioRenderer audioRenderer;
    private FlyByCamera flyCam;
    private Nifty nifty;

    public StartScreenState(SimpleApplication app) {
        this.rootNode     = app.getRootNode();
        this.viewPort     = app.getViewPort();
        this.guiNode      = app.getGuiNode();
        this.assetManager = app.getAssetManager();
        this.inputManager = app.getInputManager();
        this.audioRenderer= app.getAudioRenderer();
        this.flyCam       = app.getFlyByCamera();
    }

    public void simpleInitApp() {                
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
        
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/Nifty/StartScreen.xml", "start", this);

        // attach the nifty display to the gui view port as a processor
        viewPort.addProcessor(niftyDisplay);

        // disable the fly cam
        flyCam.setEnabled(false);
        flyCam.setDragToRotate(true);
        
        inputManager.setCursorVisible(true);        
    }

    public void bind(Nifty nifty, Screen screen) {
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }
    
   @Override
   public void stateAttached(AppStateManager stateManager) {       
       
   }

   @Override
   public void stateDetached(AppStateManager stateManager) {
      nifty.exit();
   }   
 
}