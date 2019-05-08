/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.ncf.ar.araquarium;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.concurrent.CompletableFuture;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class AquariumActivity extends AppCompatActivity {
    private static final String TAG = AquariumActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ScreenShotArFragment arFragment;
    private ModelRenderable andyRenderable;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_aquarium);
        arFragment = (ScreenShotArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);
        initializeGallery();
        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(andyRenderable);
                    andy.select();
                });

        Button backToIR = (Button) findViewById(R.id.btnBackToIR);
        backToIR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAugmentedImage();
            }
        });

        Button screenShot = (Button) findViewById(R.id.btnScreenShot);
        screenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenShot();
            }
        });

    }

    public void takeScreenShot() {
        arFragment.takePhoto();
    }

    public void startAugmentedImage() {
        Intent augImg = new Intent(this, AugmentedImageActivity.class);
        startActivity(augImg);
    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    private CompletableFuture<Void> buildObject(String object) {
        return ModelRenderable.builder()
                .setSource(this, Uri.parse(object))
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

    }

    private void initializeGallery() {
        LinearLayout gallery = findViewById(R.id.gallery_layout);
        ImageView crab = new ImageView(this);
        crab.setImageResource(R.drawable.crab_image);
        crab.setContentDescription("Crab");
        crab.setOnClickListener(view -> {
            CompletableFuture<ModelRenderable> crabModel = ModelRenderable.builder()
                    .setSource(this, Uri.parse("Crab.sfb"))
                    .build();
            Node crabModelNode = new Node();
            crabModelNode.setLocalRotation(Quaternion.eulerAngles(new Vector3(0,0,0)));
            crabModelNode.setRenderable(crabModel.getNow(null));
        });
        gallery.addView(crab);

        ImageView dolphin = new ImageView(this);
        dolphin.setImageResource(R.drawable.dolphin_image);
        dolphin.setContentDescription("Dolphin");
        dolphin.setOnClickListener(view -> {
            buildObject("Dolphin.sfb");
        });
        gallery.addView(dolphin);

        ImageView dory = new ImageView(this);
        dory.setImageResource(R.drawable.dory_image);
        dory.setContentDescription("Dory");
        dory.setOnClickListener(view -> {
            buildObject("TropicalFish02.sfb");
        });
        gallery.addView(dory);

        ImageView nemo = new ImageView(this);
        nemo.setImageResource(R.drawable.nemo_image);
        nemo.setContentDescription("Nemo");
        nemo.setOnClickListener(view -> {
            buildObject("TropicalFish12.sfb");
        });
        gallery.addView(nemo);
    }
}
