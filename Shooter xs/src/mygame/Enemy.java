package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.Random;

/**
 *
 * @author Dylankuyjt67hr
 */

public class Enemy extends Node {    
    private AssetManager am;
    private BulletAppState BAS;
    private RigidBodyControl RBC;
    int health = 100;
    CharacterControl CC;
    BoxCollisionShape CS;
    Vector3f position;
    Box enemyBox;
    Geometry enemyGeom;
    Material enemyMat;
    AudioNode enemySound;
    int soundDelay = 0;
    
    
    public Enemy(String name, AssetManager assetManager, BulletAppState bulletAppState, Vector3f pos){
        am = assetManager;
        BAS = bulletAppState;
        position = pos;
        enemySound = new AudioNode(assetManager, "Sounds/Enemy.wav");
        
        enemyBox = new Box(2f, 4f, 2f);
        enemyGeom= new Geometry(name, enemyBox);
        enemyGeom.setLocalTranslation(position);
        enemyMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        enemyMat.setColor("Color", ColorRGBA.Red);
        
        // CS = new BoxCollisionShape(CollisionShapeFactory.createBoxShape(enemyGeom)));
        CC = new CharacterControl(CollisionShapeFactory.createBoxShape(enemyGeom), 1);        
 
        CC.setJumpSpeed(20);
        CC.setFallSpeed(30);
        CC.setGravity(30);
        enemyGeom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        CC.setPhysicsLocation(position);
        BAS.getPhysicsSpace().add(CC);
        enemyGeom.addControl(CC);
        
        enemyGeom.setMaterial(enemyMat);
        
        attachChild(enemyGeom);
        attachChild(enemySound);
    }

    public void playSound(){
        soundDelay -= 1/60;
        if(soundDelay < 0){
            if(enemySound.getStatus() == AudioSource.Status.Stopped)
                enemySound.play();
            soundDelay = randomInt(10, 100);
        }
    }
    
    private int randomInt(int min, int max){
        Random r = new Random();
        int R = r.nextInt(max-min) + min;
        return R;
    }
    
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Geometry getEnemyGeom() {
        return enemyGeom;
    }
    
    public Vector3f getPosition() {
        return this.position;
    }

    public BulletAppState getBAS() {
        return BAS;
    }

    public CharacterControl getCC() {
        return CC;
    }    
    
    public void finishOfEnemy(Enemy e){
        detachChild(e.getEnemyGeom());
        e.getBAS().getPhysicsSpace().remove(e.getCC());
    }
}
