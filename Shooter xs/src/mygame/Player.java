/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sander
 */

public class Player extends CharacterControl implements ActionListener{
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    CapsuleCollisionShape capsuleShape;
    BulletAppState bulletAppState;
    InputManager inputManager;
    
    public Player(BulletAppState bulletappstate, InputManager inputmanager, Camera thiscam) {
        bulletAppState = bulletappstate;
        inputManager = inputmanager;
        setUpKeys();
        List<Camera> cameras = new ArrayList<Camera>();
        cameras.add(thiscam);
        
        this.stepHeight = 0.05f;
        
        
    }
    
    private void setUpKeys() {
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addMapping("Reload", new KeyTrigger(KeyInput.KEY_R));
    inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addMapping("Zoom", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Up");
    inputManager.addListener(this, "Down");
    inputManager.addListener(this, "Jump");
    inputManager.addListener(this, "Shoot");
    inputManager.addListener(this, "Zoom");
    inputManager.addListener(this, "Reload");
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
    }else if (binding.equals("Jump")) {
        this.jump();
    }
    }
    
    public void update(Camera cam){
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
//        this.setWalkDirection(walkDirection);
//        cam.setLocation(this.getPhysicsLocation());
    }
}
