/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
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
public class Gun extends Node {
    Box gunBox;
    Geometry gunGeom;
    Material gunMat;
    AssetManager assetManager;
    BulletAppState bulletAppState;
    
    Gun(AssetManager assetmanager,BulletAppState bulletappstate){
        assetManager = assetmanager;
        bulletAppState = bulletappstate;
        
        gunBox = new Box(Vector3f.ZERO, 0.25f, 0.5f, 1f);
        gunGeom = new Geometry("Gun", gunBox);
        ColorRGBA neoGreen = new ColorRGBA(57f, 255f, 20f, 1.0f);
        gunMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        gunMat.setColor("Diffuse", ColorRGBA.Green);
        gunMat.setColor("Specular",ColorRGBA.Green);
        gunMat.setFloat("Shininess", 64f);
        gunMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        
        gunGeom.setMaterial(gunMat);
        gunGeom.setShadowMode(RenderQueue.ShadowMode.Cast);
        attachChild(gunGeom);
        
    }
}
