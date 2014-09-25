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
        
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(50);
        
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);

        createLight();
        initViewport();
        
        World world = new World(assetManager, 50f, 50f);
        
        for(int i = 0; i < 8; i++){
            Kubus kubus = new Kubus(assetManager, bulletAppState, new Vector3f(positiveRandomFloat(), positiveRandomFloat(), positiveRandomFloat()), new Vector3f(positiveRandomFloat(), 4f, positiveRandomFloat()));
            System.out.println(positiveRandomFloat() + " < float");
            rootNode.attachChild(kubus);
        }
     
        rootNode.attachChild(world);
    }

    @Override
    public void simpleUpdate(float tpf) {
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
}
