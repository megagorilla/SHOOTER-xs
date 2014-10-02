/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
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
public class World extends Node {
    private AssetManager am;
    private Box floor;
    private Geometry floorGeom;
    private Material floorMat;
    private float floorW;
    private float floorL;
    private Box wall;
    private Geometry wallGeom;
    
    World(AssetManager assetManager, BulletAppState bulletAppState, float floorW, float floorL){
        am = assetManager;
       
        floor = new Box(floorW, 0.5f, floorL);
        floorGeom = new Geometry("Box", floor);
        floorGeom.setShadowMode(RenderQueue.ShadowMode.Receive);
        RigidBodyControl RBC = new RigidBodyControl(CollisionShapeFactory.createMeshShape(floorGeom),0f);
        floorGeom.addControl(RBC);
        bulletAppState.getPhysicsSpace().add(RBC);
        //bulletAppState.getPhysicsSpace().add(floorGeom);
        
        initMaterial(am);
       
        attachChild(floorGeom);
    }
    
    private void initMaterial(AssetManager assetManager){
        floorMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        floorMat.setColor("Diffuse",ColorRGBA.White);
        floorMat.setColor("Specular",ColorRGBA.White);
        floorMat.setFloat("Shininess", 64f);
        floorMat.setColor("GlowColor", ColorRGBA.LightGray);
        floorMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        
        floorGeom.setMaterial(floorMat);
    }
}
