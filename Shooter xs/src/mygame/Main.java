/**
 * Door: Dylan Davids & Sander de jong
 * Datum(Start) 25 september 2014
 * Project Shooter xs
 * mini shooter game als eigen idee graphics
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.RenderManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    private BulletAppState bulletAppState;
    private FilterPostProcessor fpp;
    private BloomFilter bloom;
    private Random rand = new Random();
    private List<Kubus> cube;
    Player player;
    public Gun gun;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.White);
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);

        createLight();
        initViewport();
        cube = new ArrayList<Kubus>();
        generateCubes();
        
        World world = new World(assetManager, bulletAppState, 50f, 50f);

        flyCam.setMoveSpeed(50);
        gun = new Gun(assetManager);
        player = new Player(bulletAppState, inputManager,assetManager, cam, gun);
        rootNode.attachChild(gun);
        createLight();

        rootNode.attachChild(world);
    }

    @Override
    public void simpleUpdate(float tpf) {
        player.update();
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
    
    public PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }
    
    public void initViewport(){
        fpp = new FilterPostProcessor(assetManager);
        bloom = new BloomFilter(BloomFilter.GlowMode.Objects);      
        bloom.setDownSamplingFactor(1.0f); 
        fpp.addFilter(bloom);
        
        viewPort.addProcessor(fpp);
    }
    
    public void createLight(){
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.Yellow);
        sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        rootNode.addLight(sun);
    }
  
    float positiveRandomFloat(){
        return rand.nextFloat() + rand.nextInt(50);
    }
    
    private void generateCubes(){
        for(int i = 0; i < 8; i++){
            Vector3f size = new Vector3f(positiveRandomFloat(), 4.0f, positiveRandomFloat());
            Vector3f pos = new Vector3f(positiveRandomFloat(), 4f, positiveRandomFloat());
            if(cube.isEmpty()){
                cube.add(new Kubus(assetManager, bulletAppState, size, pos));
                rootNode.attachChild(cube.get(i));
            } else {
                cube.add(new Kubus(assetManager, bulletAppState, size, pos));
                rootNode.attachChild(cube.get(i));
            }
            System.out.println(positiveRandomFloat() + " < float");            
        }  
    }
}
