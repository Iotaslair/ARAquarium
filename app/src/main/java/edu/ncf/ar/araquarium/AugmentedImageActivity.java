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

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.FrameTime;
import edu.ncf.ar.araquarium.R;

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
  private ImageView fitToScanView;
  private Resources mRes;
  private String currentFragment;

  // Augmented image and its associated center pose anchor, keyed by the augmented image in
  // the database.
  private final Map<AugmentedImage, AugmentedImageNode> augmentedImageMap = new HashMap<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mRes = getResources();
    frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
//    arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
    fitToScanView = findViewById(R.id.image_view_fit_to_scan);
    fitToScanView.setVisibility(View.INVISIBLE);
//
//    arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);
    startQuiz(R.array.dummy_quiz);
  }

  @Override
  protected void onResume() {
    super.onResume();
//    if (augmentedImageMap.isEmpty()) {
//      fitToScanView.setVisibility(View.VISIBLE);
//    }
  }

  private void startQuiz(int questionId){
    Log.d("activityMain", "Starting Quiz");
    currentFragment = mRes.getString(R.string.QUIZ);
    QuizFragment qf = new QuizFragment();
    Bundle quizArgs = new Bundle();
    quizArgs.putInt(mRes.getString(R.string.qid), questionId);
    qf.setArguments(quizArgs);
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.add(R.id.frameLayout, qf, mRes.getString(R.string.QUIZ));
    fragmentTransaction.commit();
  }

  /**
   * Registered with the Sceneform Scene object, this method is called at the start of each frame.
   *
   * @param frameTime - time since last frame.
   */
//  private void onUpdateFrame(FrameTime frameTime) {
//    Frame frame = arFragment.getArSceneView().getArFrame();
//
//    // If there is no frame or ARCore is not tracking yet, just return.
//    if (frame == null || frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
//      return;
//    }
//
//    Collection<AugmentedImage> updatedAugmentedImages =
//        frame.getUpdatedTrackables(AugmentedImage.class);
//    for (AugmentedImage augmentedImage : updatedAugmentedImages) {
//      switch (augmentedImage.getTrackingState()) {
//        case PAUSED:
//          // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
//          // but not yet tracked.
//          String text = "Detected Image " + augmentedImage.getIndex();
//          SnackbarHelper.getInstance().showMessage(this, text);
//          break;
//
//        case TRACKING:
//          // Have to switch to UI Thread to update View.
//          fitToScanView.setVisibility(View.GONE);
//
//          // Create a new anchor for newly found images.
//          if (!augmentedImageMap.containsKey(augmentedImage)) {
//            AugmentedImageNode node = new AugmentedImageNode(this);
//            node.setImage(augmentedImage);
//            augmentedImageMap.put(augmentedImage, node);
//            arFragment.getArSceneView().getScene().addChild(node);
//          }
//          break;
//
//        case STOPPED:
//          augmentedImageMap.remove(augmentedImage);
//          break;
//      }
//    }
//  }
}
