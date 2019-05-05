/*
 * Copyright 2018 Google LLC
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

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.FrameTime;
import edu.ncf.ar.araquarium.R;

import edu.ncf.ar.araquarium.common.helpers.QuizActivity;
import edu.ncf.ar.araquarium.common.helpers.SnackbarHelper;
import com.google.ar.sceneform.ux.ArFragment;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This application demonstrates using augmented images to place anchor nodes. app to include image
 * tracking functionality.
 */
public class AugmentedImageActivity extends AppCompatActivity {

  private FrameLayout frameLayout;
  private ArFragment arFragment;
  private Resources mRes;
  private String currentFragment;

  // Augmented image and its associated center pose anchor, keyed by the augmented image in
  // the database.
  private final Map<AugmentedImage, AugmentedImageNode> augmentedImageMap = new HashMap<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_augmented_image);
    mRes = getResources();
    frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
    Button backpack = (Button) findViewById(R.id.btnBackPack);
    backpack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startAugmentedAquarium();
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    if(currentFragment == mRes.getString(R.string.AUGIMAGE)) {
    }
  }

  public void startQuiz(int questionId){
      Intent quiz = new Intent(this, QuizActivity.class);
      quiz.putExtra(mRes.getString(R.string.qid), questionId);
      startActivity(quiz);
  }

  public void startAugmentedAquarium(){
//    Toast.makeText(this, "start augmented aquarium pressed", Toast.LENGTH_LONG).show();
//    Log.d("AugImg", "backpack pressed");

    Intent intent = new Intent(AugmentedImageActivity.this, FurnitureActivity.class);
    startActivity(intent);
  }

  public void startAugmentedImage(){
    Log.d("activityMain", "Starting Augmented Image");
    currentFragment = mRes.getString(R.string.AUGIMAGE);
    AugmentedImageFragment aif = new AugmentedImageFragment();
    //aif.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.frameLayout, aif, mRes.getString(R.string.AUGIMAGE));
    fragmentTransaction.commit();
    arFragment = aif;
    Log.d("startAugmentedImage", " "+arFragment.toString());
    assert(arFragment != null);
    assert(arFragment.getArSceneView() != null);
    assert(arFragment.getArSceneView().getScene() != null);
  }

  /**
   * Registered with the Sceneform Scene object, this method is called at the start of each frame.
   *
   * @param frameTime - time since last frame.
   */
  public void onUpdateFrame(FrameTime frameTime) {
    if (currentFragment == mRes.getString(R.string.AUGIMAGE)) {
      Frame frame = arFragment.getArSceneView().getArFrame();

      // If there is no frame or ARCore is not tracking yet, just return.
      if (frame == null || frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
        return;
      }

      Collection<AugmentedImage> updatedAugmentedImages =
              frame.getUpdatedTrackables(AugmentedImage.class);
      for (AugmentedImage augmentedImage : updatedAugmentedImages) {
        switch (augmentedImage.getTrackingState()) {
          case PAUSED:
            // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
            // but not yet tracked.
            String text = "Detected Image " + augmentedImage.getIndex();
            SnackbarHelper.getInstance().showMessage(this, text);
            break;

          case TRACKING:
            // Have to switch to UI Thread to update View.

            // Create a new anchor for newly found images.
            if (!augmentedImageMap.containsKey(augmentedImage)) {
              AugmentedImageNode node = new AugmentedImageNode(this);
              node.setImage(augmentedImage);
              augmentedImageMap.put(augmentedImage, node);
              arFragment.getArSceneView().getScene().addChild(node);
            }
            break;

          case STOPPED:
            augmentedImageMap.remove(augmentedImage);
            break;
        }
      }
    }
  }

  public class StartQuizDialog extends AlertDialog{
    public StartQuizDialog(@NonNull Context context) {
      super(context);

    }
  }

}
