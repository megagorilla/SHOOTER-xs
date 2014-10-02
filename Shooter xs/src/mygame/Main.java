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
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
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

    private void setUpKeys() {
        inputManager.addMapping("Reposition", new KeyTrigger(KeyInput.KEY_X));
        inputManager.addListener(actionListener, "Reposition");
    }
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String binding, boolean value, float tpf) {
          if (binding.equals("Reposition")) {
            for(int i = 0; i < cube.size(); i++){
                Vector3f newPos = new Vector3f(randomFloat(rand.nextInt(2))*5f, cube.get(i).getDimension().y, randomFloat(rand.nextInt(2))*5f);
                cube.get(i).setPosition(newPos);
                cube.get(i).getKubusGeom().setLocalTranslation(newPos);
                for(int j = 0; j < cube.size(); j++){
                    if(j != i){
                        while(Math.abs(cube.get(i).getPosition().x - cube.get(j).getPosition().x) < cube.get(i).getDimension().x * 2
                        && Math.abs(cube.get(i).getPosition().x - cube.get(j).getPosition().x) < cube.get(i).getDimension().x * 2){
                            newPos = new Vector3f(randomFloat(rand.nextInt(2))*6f, cube.get(i).getDimension().y, randomFloat(rand.nextInt(2))*6f);
                            cube.get(i).setLocalTranslation(newPos);
                            cube.get(i).setPosition(newPos);                      
                        }
                    }                    
                    //System.out.println(cube.get(j).getPosition() + " - " + cube.get(i).getPosition());
                }  
            }
//            System.out.println("X: "+ (cube.get(0).getPosition().x - cube.get(1).getPosition().x));
//            System.out.println("Z: "+ (cube.get(0).getPosition().z - cube.get(1).getPosition().z));
//            System.out.println("Clicked X");
          }
        }
    };
    
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
        setUpKeys();
        
        world = new World(assetManager, bulletAppState, 500f, 500f);

        flyCam.setMoveSpeed(50);
        gun = new Gun(assetManager, viewPort, bulletAppState, cam);
        player = new Player(bulletAppState, inputManager,assetManager, cam, gun);
        rootNode.attachChild(gun);
        createLight();

        rootNode.attachChild(world);
        
        if(false){ //enable/disable debug mode
            bulletAppState.getPhysicsSpace().enableDebug(assetManager);
            player.debug();
            player.setMinigun();
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        player.update(tpf);
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
        bloom.setDownSamplingFactor(10.0f); 
        fpp.addFilter(bloom);
        
        viewPort.addProcessor(fpp);
    }
    
    public void createLight(){
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.Yellow);
        sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        rootNode.addLight(sun);
    }
  
    float randomFloat(int negOrPos){
        int rf = rand.nextInt(20);
        while(rf < 1){
            rf = rand.nextInt(20);
        }
        if(negOrPos == 1)
            return rand.nextFloat() + rf;
        else
            return rand.nextFloat() - rf;
    }
     
    private void generateCubes(){
        for(int i = 0; i < 500; i++){
            float sizeVal = randomFloat(1);
            if(sizeVal < 5){
                sizeVal += 5;
            }
            float posX = randomFloat(rand.nextInt(3))*20f;
            float posZ = randomFloat(rand.nextInt(3))*20f;
            Vector3f size = new Vector3f(sizeVal, sizeVal, sizeVal);
            Vector3f pos = new Vector3f(posX, sizeVal, posZ);
            if(cube.isEmpty()){
                cube.add(new Kubus(assetManager, bulletAppState, size, pos));
                rootNode.attachChild(cube.get(i));
            } else {
                cube.add(new Kubus(assetManager, bulletAppState, size, pos));
                rootNode.attachChild(cube.get(i));
//                int counter = 0;
//                int j = 0;
//                while(j < cube.size()){
//                    if(j != i){
//                        CollisionResults results = new CollisionResults();
//                        cube.get(j).collideWith(cube.get(i).getWorldBound(), results);
//                        if(results.size() != 0){
//                            while(results.size() != 0){
//                                Vector3f newPos = new Vector3f(randomFloat(rand.nextInt(2))*6f, sizeVal, randomFloat(rand.nextInt(2))*6f);
//                                cube.get(i).setLocalTranslation(newPos);
//                                cube.get(i).setPosition(newPos); 
//                                results = new CollisionResults();
//                                cube.get(j).collideWith(cube.get(i).getWorldBound(), results);                     
//                            }
//                            j = 0;
//                        } else {
//                            j++;
//                        }
////                        while(Math.abs(cube.get(j).getPosition().x - cube.get(i).getPosition().x) < cube.get(i).getDimension().x * 2
////                        && Math.abs(cube.get(j).getPosition().z - cube.get(i).getPosition().z) < cube.get(i).getDimension().z * 2){
////                            Vector3f newPos = new Vector3f(randomFloat(rand.nextInt(2))*6f, sizeVal, randomFloat(rand.nextInt(2))*6f);
////                            cube.get(i).setLocalTranslation(newPos);
////                            cube.get(i).setPosition(newPos);                      
////                        }
//                    } else {
//                        j++;
//                    }                    
////                    System.out.println(cube.get(j).getPosition() + " - " + cube.get(i).getPosition());
//                }
            }       
        }  
    }
}
