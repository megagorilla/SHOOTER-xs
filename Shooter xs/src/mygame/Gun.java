/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
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
    private Box gunBox;
    private Geometry gunGeom;
    private Material gunMat;
    private AssetManager assetManager;
    private List<Geometry> bullets = new ArrayList<Geometry>();
    private List<RigidBodyControl> bulletRBC = new ArrayList<RigidBodyControl>();
    private static Sphere bullet;
    private static SphereCollisionShape bulletCollisionShape;
    private ViewPort viewPort;
    private BulletAppState bulletAppState;
    private Camera cam;
    private int gunSpeed;
    private int maxBullets;

    public void setMaxBullets(int maxBullets) {
        this.maxBullets = maxBullets;
    }
    
    Gun(AssetManager assetmanager, ViewPort viewport, BulletAppState bulletappstate, Camera Cam){
        assetManager = assetmanager;
        viewPort = viewport;
        bulletAppState = bulletappstate;
        cam = Cam;
        maxBullets = 100;
        
        gunBox = new Box(Vector3f.ZERO, 0.05f, 0.08f, 0.3f);
        gunGeom = new Geometry("Gun", gunBox);
        gunMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

        gunMat.setColor("Color",ColorRGBA.Green);
        gunMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        //gunMat.getAdditionalRenderState().setWireframe(true);
        
        gunGeom.setMaterial(gunMat);
        gunGeom.setShadowMode(RenderQueue.ShadowMode.Receive);
        attachChild(gunGeom);
        initBullet();
        
    }

    public List<Geometry> getBullets() {
        return bullets;
    }

    public List<RigidBodyControl> getBulletRBC() {
        return bulletRBC;
    }

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }
    
    private void initBullet(){
        bullet = new Sphere(10, 10, 0.05f, true, false);
        bullet.setTextureMode(Sphere.TextureMode.Projected);
        bulletCollisionShape = new SphereCollisionShape(0.1f);
    }
    
    public void deleteBullet (int id){
        detachChild(bullets.get(id));
        bulletAppState.getPhysicsSpace().remove(bulletRBC.get(id));
        bullets.remove(id);
        bulletRBC.remove(id);
    }
    
    public void shoot(){
        bullets.add(new Geometry("bullet " + (bullets.size()), bullet));
        bullets.get(bullets.size()-1).setMaterial(gunMat);
        bullets.get(bullets.size()-1).setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        bullets.get(bullets.size()-1).setLocalTranslation(cam.getLocation().add(cam.getDirection().mult(1)));
        
        bulletRBC.add(new RigidBodyControl(CollisionShapeFactory.createDynamicMeshShape(bullets.get(bullets.size()-1)), 0.1f));
        bulletRBC.get(bulletRBC.size()-1).setCcdMotionThreshold(0.015f);
        bulletRBC.get(bulletRBC.size()-1).setCcdSweptSphereRadius(0.005f);
        bulletRBC.get(bullets.size()-1).setLinearVelocity(cam.getDirection().mult(100));
        bullets.get(bullets.size()-1).addControl(bulletRBC.get(bullets.size()-1));
        bulletAppState.getPhysicsSpace().add(bulletRBC.get(bullets.size()-1));
        attachChild(bullets.get(bullets.size()-1));
        if(bullets.size()>maxBullets){
            detachChild(bullets.get(0));
            bulletAppState.getPhysicsSpace().remove(bulletRBC.get(0));
             bullets.remove(0);
             bulletRBC.remove(0);
        }      
    }
}
