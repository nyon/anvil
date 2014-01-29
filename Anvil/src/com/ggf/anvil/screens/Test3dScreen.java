package com.ggf.anvil.screens;

import com.badlogic.gdx.Files.FileType;
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
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationDesc;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController.AnimationListener;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.UBJsonReader;
import com.ggf.anvil.Anvil;

public class Test3dScreen extends AbstractScreen {
	
	public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Environment environment;
    
    public AnimationController aniController;

	public Test3dScreen(Anvil game) {
		super(game);
		// TODO Auto-generated constructor stub
		
		modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
         
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(8f, 4f, 4f);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = 300f;
        cam.update();
 
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);        
        
        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        Model smith = modelLoader.loadModel(Gdx.files.getFileHandle("models/smith_2_4.g3db", FileType.Internal));
        Model anvil = modelLoader.loadModel(Gdx.files.getFileHandle("models/anvil_2.g3db", FileType.Internal));

        ModelInstance anvilInstance = new ModelInstance(anvil);
        anvilInstance.transform.rotate(new Vector3(1, 0, 0), -90f);
        anvilInstance.transform.rotate(new Vector3(0, 0, 1), 120f);
        anvilInstance.transform.translate(0, 0, -1.5f);
        instances.add(anvilInstance);
        
        ModelInstance smithInstance = new ModelInstance(smith);
        smithInstance.transform.rotate(new Vector3(1, 0, 0), -90f);
        smithInstance.transform.rotate(new Vector3(0, 0, 1), 50f);
        smithInstance.transform.translate(4f, 4f, 0);
        instances.add(smithInstance);
        
        aniController = new AnimationController(smithInstance);  
        aniController.setAnimation("Idle1", -1);	//TODO: animation does not work
	}
     
    @Override
    public void render(float delta) {
    	
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        
        camController.update();
        
        
        cam.rotateAround(Vector3.Zero, new Vector3(0,1,0),1f);
        cam.update();
        
        aniController.update(Gdx.graphics.getDeltaTime());
        
        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }
     
    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
    }	
}
