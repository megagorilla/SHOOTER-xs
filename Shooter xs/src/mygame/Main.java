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

/**
 *
 * @author Dylankuyjt67hr
 */
public class Main extends SimpleApplication {
    private World world;
    private BulletAppState bulletAppState;
    private Player player;
    private Gun gun;
    private AudioNode ammoPickup;
    private AudioNode enemyDying;
    private List<AmmoCrate> ammoCrates = new ArrayList<AmmoCrate>();
    private BitmapText currentMagSize;
    private BitmapText currentScore;
    private BitmapText GameoverText;
    private int score = 0;
    private List<Enemy> Enemies = new ArrayList<Enemy>();
    
    public static void main(String[] args) throws InterruptedException {
        
        
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
    
public class Main extends SimpleApplication implements ScreenController {

    private Trigger start_trigger = new KeyTrigger(KeyInput.KEY_SPACE);
    private boolean isRunning = false; // starts at startscreen
    private GameRunningState gameRunningState;
    private StartScreenState startScreenState;
    
    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        ammoPickup = new AudioNode(assetManager, "Sounds/reload.wav");
        enemyDying = new AudioNode(assetManager, "Sounds/deathEnemy.wav");
        viewPort.setBackgroundColor(ColorRGBA.Cyan);
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);

        world = new World(assetManager, bulletAppState, 500f, 500f);
        rootNode.attachChild(world);
        
        flyCam.setMoveSpeed(50);
        gun = new Gun(assetManager, viewPort, bulletAppState, cam);
        player = new Player(bulletAppState, inputManager,assetManager, cam, gun);
        rootNode.attachChild(player);
        rootNode.attachChild(gun);
        createLight();
        
        if(false){ //enable/disable debug mode
            bulletAppState.getPhysicsSpace().enableDebug(assetManager);
            player.debug();
            player.setMinigun();
        }
        
        for(int i = 0; i < 5; i++){
            Enemies.add(new Enemy("Enemy "+i, assetManager, bulletAppState, randomVectorBetween(-250, 250, 2)));            
            rootNode.attachChild(Enemies.get(Enemies.size()-1));            
        }
        
        CollisionResults results = new CollisionResults();
        for(Enemy x : Enemies){            
            world.getWorldNode().collideWith(x.getWorldBound(), results);
            Vector3f currentPos = x.getPosition();
            if(results.size() > 1){
                x.setLocalTranslation(currentPos.x + 1f, currentPos.y, currentPos.z + 1f);
            }
        }
        
        /** Write text on the screen (HUD) */
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Aharoni.fnt");
        
        currentMagSize = new BitmapText(guiFont, false);
        currentMagSize.setSize(guiFont.getCharSet().getRenderedSize());
        currentMagSize.setText("Ammo: " +player.getInMagazine() + " / " + player.getMagsize());
        currentMagSize.setColor(ColorRGBA.White);
        currentMagSize.setSize(50);
        currentMagSize.setLocalTranslation(0, this.settings.getHeight() -currentMagSize.getLineHeight(), 0);
        
        currentScore = new BitmapText(guiFont, false);
        currentScore.setSize(guiFont.getCharSet().getRenderedSize());
        currentScore.setText("Score: " +score);
        currentScore.setSize(50);
        currentScore.setLocalTranslation(0, this.settings.getHeight(), 0);
        
        guiNode.attachChild(currentMagSize);
        guiNode.attachChild(currentScore);
    }

    @Override
    public void simpleUpdate(float tpf) {
        currentMagSize.setText("Ammo: " +player.getInMagazine() + " / " + player.getMagsize());
        currentScore.setText("Score: " +score);
        player.update(tpf);
        ammoDrop();
        if(player.getInMagazine() < player.getMagsize())
            ammoCratePickup();
        deathCollisionCheck();
        enemyMovement();
        collisionBetweenBulletAndEnemy();
    }
    
    private void deathCollisionCheck(){
        Vector3f playerLocation = player.getCamLocation();
        for(int i = 0; i< Enemies.size();i++){
            Vector3f enemyLocation = Enemies.get(i).getLocalTranslation();
            if(playerLocation.distance(enemyLocation) < 50f){
                System.out.println("ONLY DEATH IS CERTAIN");
                bulletAppState.setEnabled(false);
                GameoverText = new BitmapText(guiFont, false);
                GameoverText.setSize(guiFont.getCharSet().getRenderedSize());
                GameoverText.setText("Game over. Score: " +score);
                GameoverText.setSize(50);
                GameoverText.setLocalTranslation(0, this.settings.getHeight()/2, 0);
                guiNode.attachChild(GameoverText);
            }
        }
    }
    
    private void enemyMovement(){
        for(Enemy x : Enemies){
            Vector3f playerLoc = player.getCamLocation();                
            Vector3f curLoc = x.CC.getPhysicsLocation();
            float distance = curLoc.distance(playerLoc);          

            float moveX = FastMath.floor(playerLoc.x) - FastMath.floor(curLoc.x);
            float moveZ = FastMath.floor(playerLoc.z) - FastMath.floor(curLoc.z);
            float moveTotal = FastMath.abs(moveX) + FastMath.abs(moveZ);

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
