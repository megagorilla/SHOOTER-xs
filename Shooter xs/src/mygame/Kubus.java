/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
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
 * @author Sander
 */
public class Kubus extends Node{
    AssetManager am;
    BulletAppState BAS;
    Box kubus;
    Geometry kubusGeom;
    Material kubusMat;
    Vector3f dimension;
    Vector3f position;
    RigidBodyControl RBC;
    
    public Kubus(AssetManager assetManager, BulletAppState bulletAppState, Vector3f dimension, Vector3f position){
        am = assetManager;
        BAS = bulletAppState;
        
        kubus = new Box(dimension.x, dimension.y, dimension.z);
        kubusGeom = new Geometry("Box", kubus);
    
        initMaterial();
        
        RBC = new RigidBodyControl(0f);
        kubusGeom.addControl(RBC);
        kubusGeom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        RBC.setPhysicsLocation(position);
        BAS.getPhysicsSpace().add(RBC);
        
        attachChild(kubusGeom);
    }
    
    private void initMaterial(){
        kubusMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        kubusMat.setColor("GlowColor", ColorRGBA.randomColor());
        kubusMat.setColor("Color", ColorRGBA.Gray);
        
        kubusGeom.setMaterial(kubusMat);
    }
}
