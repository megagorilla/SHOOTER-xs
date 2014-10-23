/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sander
 */
public class AmmoCrate extends Node{
    private Material crateMaterial;
    private Box ammoBox;
    private Quaternion rotation;
    private Geometry ammoGeom = new Geometry();
    private RigidBodyControl ammoControl = new RigidBodyControl();
    private BulletAppState bulletAppState;
    private AssetManager assetManager;
    
   
    
    AmmoCrate(BulletAppState bulletappstate,AssetManager assetmanager, float x, float z){
        bulletAppState = bulletappstate;
        assetManager = assetmanager;
        ammoBox = new Box(1, 2, 0.5f);
        initMaterial();
        rotation = new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(0,0,1));
                ammoGeom = new Geometry("AmmoCrate", ammoBox);
//        ammoGeom.setLocalRotation(rotation);
        
        ammoGeom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        ammoGeom.setMaterial(crateMaterial);
        ammoControl = new RigidBodyControl(new BoxCollisionShape(new  Vector3f(1, 2, 0.5f)));
        this.addControl(ammoControl);

        bulletAppState.getPhysicsSpace().add(this);
        ammoControl.setPhysicsLocation(new Vector3f(0,100,10*z));
//        ammoGeom.setLocalTranslation(0,100,10*z);
        this.setLocalTranslation(0,100,10*z);
        attachChild(ammoGeom);
    }
    
    public Geometry getAmmoGeom() {
        return ammoGeom;
    }
    
    public void destroyControl(){
        bulletAppState.getPhysicsSpace().remove(this);
        this.removeControl(ammoControl);
    }
    
    private void initMaterial(){
        crateMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        ColorRGBA armyGreen = new ColorRGBA(75f/255f, 83f/255f, 32f/255f, 1f);
        crateMaterial.setColor("Color", armyGreen);
        
    }
}


