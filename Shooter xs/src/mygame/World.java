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
import java.util.Random;

/**
 *
 * @author Sander
 */
public class World extends Node {
    private AssetManager am;
    private BulletAppState BAS;
    private Box floor;
    private RigidBodyControl floorPhy;
    private float floorW;
    private float floorL;
    private Geometry floorGeom;
    private Material floorMat;
    private Geometry wallLeftGeom;
    private Geometry wallRightGeom;
    private Geometry wallTopGeom;
    private Geometry wallBottomGeom;
    private Material wallMat;
    private Random rand = new Random();
    private List<Kubus> cube;
    private Node worldNode = new Node();
    
    World(AssetManager assetManager, BulletAppState bulletAppState, float floorW, float floorL){
        am = assetManager;
        BAS = bulletAppState;
       
        /* Floor */
        floor = new Box(floorW, 0f, floorL);
        floorGeom = new Geometry("Floor", floor);
        floorGeom.setLocalTranslation(0, 0f, 0);
        floorGeom.setShadowMode(RenderQueue.ShadowMode.Receive);
        floorPhy = new RigidBodyControl(CollisionShapeFactory.createMeshShape(floorGeom),0f);
        floorGeom.addControl(floorPhy);
        bulletAppState.getPhysicsSpace().add(floorPhy);
        
        /* Walls */
        Box wallLeft = new Box(1.5f, 20f, floorL);
        wallLeftGeom = new Geometry("Box", wallLeft);
        wallLeftGeom.setShadowMode(RenderQueue.ShadowMode.Cast);
        wallLeftGeom.setLocalTranslation(-floorW, 20f, 0f);
        RigidBodyControl RBC = new RigidBodyControl(CollisionShapeFactory.createMeshShape(wallLeftGeom),0f);
        wallLeftGeom.addControl(RBC);
        bulletAppState.getPhysicsSpace().add(RBC);
        
        Box wallRight = new Box(1.5f, 20f, floorL);
        wallRightGeom = new Geometry("Box", wallRight);
        wallRightGeom.setShadowMode(RenderQueue.ShadowMode.Cast);
        wallRightGeom.setLocalTranslation(floorW, 20f, 0f);        
        RBC = new RigidBodyControl(CollisionShapeFactory.createMeshShape(wallRightGeom),0f);
        wallRightGeom.addControl(RBC);
        bulletAppState.getPhysicsSpace().add(RBC);
        
        Box wallTop = new Box(floorW, 20f, 1.5f);
        wallTopGeom = new Geometry("Box", wallTop);
        wallTopGeom.setShadowMode(RenderQueue.ShadowMode.Cast);
        wallTopGeom.setLocalTranslation(0f, 20f, floorL);
        RBC = new RigidBodyControl(CollisionShapeFactory.createMeshShape(wallTopGeom),0f);
        wallTopGeom.addControl(RBC);
        bulletAppState.getPhysicsSpace().add(RBC);
 
        Box wallBottom = new Box(floorW, 20f, 1.5f);
        wallBottomGeom = new Geometry("Box", wallBottom);
        wallBottomGeom.setShadowMode(RenderQueue.ShadowMode.Cast);
        wallBottomGeom.setLocalTranslation(0f, 20f, -floorL);
        RBC = new RigidBodyControl(CollisionShapeFactory.createMeshShape(wallBottomGeom),0f);
        wallBottomGeom.addControl(RBC);
        bulletAppState.getPhysicsSpace().add(RBC);
        
        /* Cubes */
        cube = new ArrayList<Kubus>();
        generateCubes(0);
        
        initMaterial(am);
                        
        worldNode.attachChild(floorGeom);
        worldNode.attachChild(wallLeftGeom);
        worldNode.attachChild(wallRightGeom);
        worldNode.attachChild(wallTopGeom);
        worldNode.attachChild(wallBottomGeom);
        
        attachChild(worldNode);
    }

    public Node getWorldNode() {
        return worldNode;
    }
    
    private void initMaterial(AssetManager assetManager){
        /*floorMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        floorMat.setColor("Diffuse",ColorRGBA.White);
        floorMat.setColor("Specular",ColorRGBA.White);
        floorMat.setFloat("Shininess", 64f);
        floorMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);*/
        
        floorMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        floorMat.setColor("Color", ColorRGBA.DarkGray);        
        
        floorGeom.setMaterial(floorMat);
        
        wallMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wallMat.setColor("Color", ColorRGBA.DarkGray);
        
        wallLeftGeom.setMaterial(wallMat);
        wallRightGeom.setMaterial(wallMat);
        wallTopGeom.setMaterial(wallMat);
        wallBottomGeom.setMaterial(wallMat);
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
                worldNode.attachChild(cube.get(i));
            } else {
                cube.add(new Kubus(am, BAS, size, pos));
                worldNode.attachChild(cube.get(i));
            }       
        }  
    }
    
    public static final Quaternion YAW090   = new Quaternion().fromAngleAxis(FastMath.PI/2,   new Vector3f(1,0,0));
}
