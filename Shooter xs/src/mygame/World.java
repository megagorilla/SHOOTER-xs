/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Sander
 */
public class World extends Node {
    private AssetManager am;
    private BulletAppState BAS;
    private Box floor;
    private Geometry floorGeom;
    private Material floorMat;
    private float floorW;
    private float floorL;
    private Box wall;
    private Geometry wallGeom;
    private Random rand = new Random();
    private List<Kubus> cube;
    
    World(AssetManager assetManager, BulletAppState bulletAppState, float floorW, float floorL){
        am = assetManager;
        BAS = bulletAppState;
       
        floor = new Box(floorW, 0.01f, floorL);
        floorGeom = new Geometry("Box", floor);
        floorGeom.setShadowMode(RenderQueue.ShadowMode.Receive);
        RigidBodyControl RBC = new RigidBodyControl(CollisionShapeFactory.createMeshShape(floorGeom),0f);
        floorGeom.addControl(RBC);
        bulletAppState.getPhysicsSpace().add(RBC);
        
        cube = new ArrayList<Kubus>();
        generateCubes(500);
        initMaterial(am);
        
        attachChild(floorGeom);
    }
    
    private void initMaterial(AssetManager assetManager){
        floorMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        floorMat.setColor("Diffuse",ColorRGBA.White);
        floorMat.setColor("Specular",ColorRGBA.White);
        floorMat.setFloat("Shininess", 64f);
        //floorMat.setColor("GlowColor", ColorRGBA.LightGray);
        floorMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        
        floorGeom.setMaterial(floorMat);
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
     
    private void generateCubes(int amount){
        for(int i = 0; i < amount; i++){
            float sizeVal = randomFloat(1);
            if(sizeVal < 5){
                sizeVal += 5;
            }
            float posX = randomFloat(rand.nextInt(3))*20f;
            float posZ = randomFloat(rand.nextInt(3))*20f;
            Vector3f size = new Vector3f(sizeVal, sizeVal, sizeVal);
            Vector3f pos = new Vector3f(posX, sizeVal, posZ);
            if(cube.isEmpty()){
                cube.add(new Kubus(am, BAS, size, pos));
                attachChild(cube.get(i));
            } else {
                cube.add(new Kubus(am, BAS, size, pos));
                attachChild(cube.get(i));
            }       
        }  
    }
}
