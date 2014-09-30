/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sander
 */
public class Gun extends Node {
    Box gunBox;
    Geometry gunGeom;
    Material gunMat;
    AssetManager assetManager;
    private List<Geometry> bullets = new ArrayList<Geometry>();
    private static Sphere bullet;
    private static SphereCollisionShape bulletCollisionShape;
    ViewPort viewPort;
    BulletAppState bulletAppState;
    Camera cam;
    
    Gun(AssetManager assetmanager, ViewPort viewport, BulletAppState bulletappstate, Camera Cam){
        assetManager = assetmanager;
        viewPort = viewport;
        bulletAppState = bulletappstate;
        cam = Cam;
        
        gunBox = new Box(Vector3f.ZERO, 0.05f, 0.08f, 0.3f);
        gunGeom = new Geometry("Gun", gunBox);
        gunMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        gunMat.setColor("Color",ColorRGBA.Green);
        gunMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        gunMat.getAdditionalRenderState().setWireframe(true);
        
        gunGeom.setMaterial(gunMat);
        gunGeom.setShadowMode(RenderQueue.ShadowMode.Cast);
        attachChild(gunGeom);
        initBullet();
        
    }
    
    private void initBullet(){
        bullet = new Sphere(4, 4, 0.1f, true, false);
        bullet.setTextureMode(Sphere.TextureMode.Projected);
        bulletCollisionShape = new SphereCollisionShape(0.4f);
    }
    
        public void shoot(){
                bullets.add(new Geometry("bullet", bullet));
                bullets.get(bullets.size()-1).setMaterial(gunMat);
                bullets.get(bullets.size()-1).setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
                bullets.get(bullets.size()-1).setLocalTranslation(cam.getLocation().add(cam.getDirection().mult(2)));
                
                SphereCollisionShape bulletCollisionShape = new SphereCollisionShape(0.1f);
                RigidBodyControl bulletNode = new RigidBodyControl(bulletCollisionShape, 10.0f);
                bulletNode.setLinearVelocity(cam.getDirection().mult(250));
                bullets.get(bullets.size()-1).addControl(bulletNode);
                attachChild(bullets.get(bullets.size()-1));
                if(bullets.size()>200){
                    bullets.remove(0);
                    detachChild(bullets.get(0));
                }
                bulletAppState.getPhysicsSpace().add(bulletNode);
    }
}
