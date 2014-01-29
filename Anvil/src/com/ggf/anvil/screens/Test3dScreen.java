package com.ggf.anvil.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.ggf.anvil.Anvil;

public class Test3dScreen extends AbstractScreen {
	
	public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public AssetManager assets;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Environment environment;
    public boolean loading;

	public Test3dScreen(Anvil game) {
		super(game);
		// TODO Auto-generated constructor stub
		
		modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
         
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(4f, 4f, 4f);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();
 
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);        
        
        assets = new AssetManager();
        assets.load("models/anvil_1.obj", Model.class);
        loading = true;
	}
 
    private void doneLoading() {
        Model anvil = assets.get("models/anvil_1.obj", Model.class);
        ModelInstance anvilInstance = new ModelInstance(anvil); 
        instances.add(anvilInstance);
        loading = false;
    }
     
    @Override
    public void render(float delta) {
    	
        if (loading && assets.update())
            doneLoading();
        camController.update();
        
        if(instances.size > 0 ) {
        	ModelInstance anvilInstance = instances.get(0);
        	anvilInstance.transform.rotate( new Vector3(0,1,0), delta * 10);
        }
         
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }
     
    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }	
}
