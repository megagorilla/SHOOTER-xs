package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

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
    
    public Enemy(AssetManager assetManager, BulletAppState bulletAppState, Vector3f pos){
        am = assetManager;
        BAS = bulletAppState;
        position = pos;
        
        enemyBox = new Box(2f, 4f, 2f);
        enemyGeom= new Geometry("Enemy", enemyBox);
        enemyGeom.setLocalTranslation(position);
        enemyMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        enemyMat.setColor("Color", ColorRGBA.Red);

        CC = new CharacterControl();        
        CS = new BoxCollisionShape(new Vector3f(2f, 4f, 2f));
        CC.setCollisionShape(CS);
        enemyGeom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        CC.setPhysicsLocation(position);
        BAS.getPhysicsSpace().add(CC);
        enemyGeom.addControl(CC);
        
        enemyGeom.setMaterial(enemyMat);
        
        attachChild(enemyGeom);
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
}
