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
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
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
    Player player;
    public Gun gun;
    private AudioNode ammoPickup;
    private List<AmmoCrate> ammoCrates = new ArrayList<AmmoCrate>();
    BitmapText currentMagSize;
    private List<Enemy> Enemies = new ArrayList<Enemy>();
    
    public static void main(String[] args) {

        Main app = new Main();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        ammoPickup = new AudioNode(assetManager, "Sounds/reload.wav");
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

        /** Write text on the screen (HUD) */
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        currentMagSize = new BitmapText(guiFont, false);
        currentMagSize.setSize(guiFont.getCharSet().getRenderedSize());
        currentMagSize.setText(player.getInMagazine() + " / " + player.getMagsize());
        currentMagSize.setLocalTranslation(300, currentMagSize.getLineHeight(), 0);
        guiNode.attachChild(currentMagSize);
        
        if(false){ //enable/disable debug mode
            bulletAppState.getPhysicsSpace().enableDebug(assetManager);
            player.debug();
            player.setMinigun();
        }
        
        for(int i = 0; i< 10; i ++){
            Enemies.add(new Enemy(assetManager, bulletAppState, new Vector3f(4f, 50f, 4f)));            
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
    }

    @Override
    public void simpleUpdate(float tpf) {
        currentMagSize.setText(player.getInMagazine() + " / " + player.getMagsize());
        player.update(tpf);
        ammoDrop();
        ammoCratePickup();
    }

    private void ammoDrop(){
        if(ammoCrates.size()< 10){
            ammoCrates.add(new AmmoCrate(bulletAppState, assetManager,randomVectorBetween(-250, 250)));
            rootNode.attachChild(ammoCrates.get(ammoCrates.size()-1));
        }
    }
    
    private Vector3f randomVectorBetween(int min, int max){
        Vector3f locVector = new Vector3f();
        locVector.y = 100f;
        
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
