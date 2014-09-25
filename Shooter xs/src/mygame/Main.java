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
import com.jme3.input.controls.ActionListener;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    private BulletAppState bulletAppState;
    Player player;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
        flyCam.setMoveSpeed(50);
        createLight();
        World world = new World(assetManager, bulletAppState);
        player = new Player(bulletAppState, inputManager, getCamera());
//        player.setJumpSpeed(20);
//        player.setFallSpeed(30);
//        player.setGravity(30);
//        player.setPhysicsLocation(new Vector3f(-10, 10, 10));
        //bulletAppState.getPhysicsSpace().add(player);
        rootNode.attachChild(world);
    }

    @Override
    public void simpleUpdate(float tpf) {
        player.update(cam);
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
    }
}
