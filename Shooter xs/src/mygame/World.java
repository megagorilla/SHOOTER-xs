/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
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
public class World extends Node{
    
    
    Box floor = new Box(Vector3f.ZERO, 75f, 0.5f, 75f);
    Geometry floorGeom;
    Material worldMat;
    World(AssetManager assetManager){
        
        worldMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        worldMat.setColor("Diffuse",ColorRGBA.White);
        worldMat.setColor("Specular",ColorRGBA.White);
        worldMat.setFloat("Shininess", 64f);
        worldMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        
        
        floorGeom = new Geometry("Box", floor);
        floorGeom.setMaterial(worldMat);
        floorGeom.setShadowMode(RenderQueue.ShadowMode.Receive);
        attachChild(floorGeom);
    }
}
