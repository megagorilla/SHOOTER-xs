/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Sander
 */
public class AmmoCrate extends Node{
    private Material crateMaterial;
    private Box ammoBox;
    private Geometry ammoGeom;
    
    private BulletAppState bulletAppState;
    private AssetManager assetManager;
    
    private BitmapText text;
    
    AmmoCrate(BulletAppState bulletappstate,AssetManager assetmanager){
        bulletAppState = bulletappstate;
        assetManager = assetmanager;
        
        BitmapFont fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
        text = new BitmapText(fnt);
        text.setSize(2f);
        text.setText("DERPDERP");
        attachChild(text);
        ammoBox = new Box(1, 2, 0.5f);
        ammoGeom = new Geometry("AmmoCrate", ammoBox);
        initMaterial();
        Quaternion rotation = new Quaternion(1, 1, 1, 1);
        this.setLocalRotation(rotation);
        this.setLocalTranslation(0,100,0);
        RigidBodyControl ammoNode = new RigidBodyControl(CollisionShapeFactory.createDynamicMeshShape(ammoGeom), 1);
        ammoGeom.addControl(ammoNode);
        bulletAppState.getPhysicsSpace().add(ammoNode);
        attachChild(ammoGeom);

        ammoGeom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
    }
    
    private void initMaterial(){
        crateMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        ColorRGBA armyGreen = new ColorRGBA(75f/255f, 83f/255f, 32f/255f, 1f);
        crateMaterial.setColor("Color", armyGreen);
        ammoGeom.setMaterial(crateMaterial);
    }
}
