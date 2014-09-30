/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

/**
 *
 * @author Sander
 */
public class Player extends Node implements ActionListener {
    //attributes
    private CharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    Camera cam;
    
    Gun gun;
    Vector3f camPos = new Vector3f();
    Vector3f gunBaseOffset = new Vector3f(-0.1f,-0.3f,1f); //offset from cam when looking straight ahead
    Vector3f tmpOffset = new Vector3f();
    Quaternion camRot = new Quaternion();
    
    private boolean left = false, right = false, up = false, down = false;
    CapsuleCollisionShape capsuleShape;
    BulletAppState bulletAppState;
    InputManager inputManager;
    AssetManager assetManager;
    
    Player(BulletAppState bulletappstate, InputManager inputmanager,AssetManager assetmanager, Camera Cam, Gun gunn){
        bulletAppState = bulletappstate;
        inputManager = inputmanager;
        assetManager = assetmanager;
        cam = Cam;
        gun = gunn;
        
        capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);
        player.setPhysicsLocation(new Vector3f(-10, 5, 10));
        bulletAppState.getPhysicsSpace().add(player);
        setUpKeys();
        
    }

    private void setUpKeys() {
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
    
    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Up");
    inputManager.addListener(this, "Down");
    inputManager.addListener(this, "Jump");

  }
    
    public void onAction(String binding, boolean value, float tpf) {
      if (binding.equals("Left")) {
      if (value) { left = true; } else { left = false; }
    } else if (binding.equals("Right")) {
      if (value) { right = true; } else { right = false; }
    } else if (binding.equals("Up")) {
      if (value) { up = true; } else { up = false; }
    } else if (binding.equals("Down")) {
      if (value) { down = true; } else { down = false; }
    }   else if (binding.equals("Jump")) {
        player.jump();
    }
}
    
    public void update(){
        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
        
        Quaternion extrarotation = new Quaternion(0.0f,-FastMath.PI/4 , 0.0f, 0.0f);
        gun.setLocalRotation(extrarotation);
        camPos.set(cam.getLocation());
        camRot.fromAxes(cam.getLeft(), cam.getUp(), cam.getDirection());
        tmpOffset.set(gunBaseOffset);
        camRot.multLocal(tmpOffset);
        camPos.addLocal(tmpOffset);

        gun.getLocalTranslation().set(camPos);
        gun.getLocalRotation().set(camRot);
    }
}
