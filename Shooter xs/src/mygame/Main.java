/**
 * Door: Dylan Davids & Sander de jong
 * Datum(Start) 25 september 2014
 * Project Shooter xs
 * mini shooter game als eigen idee graphics
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.collision.CollisionResult;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.system.AppSettings;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * test
 * @author normenhansen
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
    
    
    @Override
    public void simpleInitApp() {
        //StartScreenState startScreenState = new StartScreenState(this);
        //stateManager.attach(startScreenState);
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
                Enemies.get(i).finishOfEnemy(Enemies.get(i));
                Enemies.remove(Enemies.indexOf(Enemies.get(i)));
                
                try{
                this.wait(10000);
                }catch(Exception e){
                    
                }
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

            moveX = (moveX / moveTotal) * 0.5f;
            moveZ = (moveZ / moveTotal) * 0.5f;

            //while (distance > 10) {                
                x.CC.setPhysicsLocation(new Vector3f((curLoc.x + moveX), curLoc.y, (curLoc.z + moveZ)));//.addLocal(moveX, moveY);
                //curLoc = x.enemyGeom.getLocalTranslation();
                //distance = curLoc.distance(playerLoc);
            //}
            
            playerLoc.setY(x.getLocalTranslation().y);
            x.lookAt(playerLoc, new Vector3f(0, 1, 0));
            x.playSound();
        }
    }
    
    private void collisionBetweenBulletAndEnemy(){
        CollisionResults results = new CollisionResults();
        
        outerloop: for(int j = 0; j<player.getGun().getBullets().size();j++){
            if(player.getGun().getBullets().indexOf(player.getGun().getBullets().get(j)) > 0){
                for(Enemy e : Enemies){
                    e.enemyGeom.collideWith(player.getGun().getBullets().get(j).getWorldBound(), results);
                    if(results.size()>0){
                        // Remove bullet
                        player.getGun().deleteBullet(j);
                        e.setHealth(e.getHealth() - 20);
                        if(e.getHealth() <= 0){
                            e.finishOfEnemy(e);
                            Enemies.remove(Enemies.indexOf(e));
                            newEnemy();
                            newEnemy();
                            score += 10;
                            enemyDying.playInstance();
                        }
                        break outerloop;
                     }
                }
            }
        }
    }
    
    private void newEnemy(){
        Enemies.add(new Enemy("Enemy "+ Enemies.size(), assetManager, bulletAppState, randomVectorBetween(-250, 250, 2)));            
        rootNode.attachChild(Enemies.get(Enemies.size()-1));
    }
    
    private void ammoDrop(){
        if(ammoCrates.size()< 100){
            ammoCrates.add(new AmmoCrate(bulletAppState, assetManager,randomVectorBetween(-250, 250, 100)));
            rootNode.attachChild(ammoCrates.get(ammoCrates.size()-1));
        }
    }
    
    private Vector3f randomVectorBetween(int min, int max, float y){
        Vector3f locVector = new Vector3f();
        locVector.y = y;
        
        Random rand = new Random();
        float finalX = rand.nextFloat() * (max - min) + min;
        locVector.x = finalX;
        
        rand = new Random();
        finalX = rand.nextFloat() * (max - min) + min;
        locVector.z = finalX;
        
        return locVector;
    }
    
    private void ammoCratePickup(){
        Vector3f playerLocation = player.getCamLocation();
        for(int i = 0; i< ammoCrates.size();i++){
            Vector3f crateLocation = ammoCrates.get(i).getLocalTranslation();
            if(playerLocation.distance(crateLocation) < 5f){
                player.addammo(10);
                rootNode.detachChild(ammoCrates.get(i));
                ammoCrates.get(i).destroyControl();
                ammoCrates.remove(i);
                ammoPickup.playInstance();
            }
        }
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
    }
    
    public PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    public void createLight(){
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.Yellow);
        sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        rootNode.addLight(sun);
        
        /* this shadow needs a directional light */
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, 1024, 4);
        dlsr.setLight(sun);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.Bilinear);
        dlsr.setShadowIntensity(0.5f);
        viewPort.addProcessor(dlsr); 
        
   }

}
