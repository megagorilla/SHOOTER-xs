/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Dylankuyjt67hr
 */
public class World extends Node {
    private AssetManager am;
    Box floor;
    Geometry floorGeom;
    Material floorMat;
    float floorW;
    float floorL;
    Box wall;
    Geometry wallGeom;
    
    public World(AssetManager assetManager, float floorW, float floorL){
        am = assetManager;
        floor = new Box(Vector3f.ZERO, floorW, 0.5f, floorL);
        floorGeom = new Geometry("Box", floor);
        floorGeom.setLocalTranslation(0f, 0f, 0f);
        
        initMaterial();
        
        attachChild(floorGeom);
    }
    
    private void initMaterial(){
        floorMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        floorMat.setColor("GlowColor", ColorRGBA.LightGray);
        floorMat.setColor("Color", ColorRGBA.Gray);
        
        floorGeom.setMaterial(floorMat);
    }
}
