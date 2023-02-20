/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.pbrg_android.utility.rendering;

import android.content.Context;
import android.util.Log;

import com.example.pbrg_android.data.model.HoldData;
import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Pose;
import com.example.pbrg_android.utility.rendering.ObjectRenderer.BlendMode;

import java.io.IOException;

/** Renders an augmented image. */
public class AugmentedImageRenderer {
  private static final String TAG = "AugmentedImageRenderer";

  private static final float TINT_INTENSITY = 0.1f;
  private static final float TINT_ALPHA = 1.0f;
  private static final int[] TINT_COLORS_HEX = {
    0x000000, 0xF44336, 0xE91E63, 0x9C27B0, 0x673AB7, 0x3F51B5, 0x2196F3, 0x03A9F4, 0x00BCD4,
    0x009688, 0x4CAF50, 0x8BC34A, 0xCDDC39, 0xFFEB3B, 0xFFC107, 0xFF9800,
  };

  private final ObjectRenderer imageFrameUpperLeft = new ObjectRenderer();
  private final ObjectRenderer imageFrameUpperRight = new ObjectRenderer();
  private final ObjectRenderer imageFrameLowerLeft = new ObjectRenderer();
  private final ObjectRenderer imageFrameLowerRight = new ObjectRenderer();

  private final ObjectRenderer pyramidRenderer = new ObjectRenderer();

  public AugmentedImageRenderer() {}

  public void createOnGlThread(Context context) throws IOException {
//    imageFrameUpperLeft.createOnGlThread(
//        context, "models/frame_upper_left.obj", "models/frame_base1.png");
//    imageFrameUpperLeft.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
//    imageFrameUpperLeft.setBlendMode(BlendMode.AlphaBlending);
//
//    imageFrameUpperRight.createOnGlThread(
//        context, "models/frame_upper_right.obj", "models/frame_base1.png");
//    imageFrameUpperRight.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
//    imageFrameUpperRight.setBlendMode(BlendMode.AlphaBlending);
//
//    imageFrameLowerLeft.createOnGlThread(
//        context, "models/frame_lower_left.obj", "models/frame_base1.png");
//    imageFrameLowerLeft.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
//    imageFrameLowerLeft.setBlendMode(BlendMode.AlphaBlending);
//
//    imageFrameLowerRight.createOnGlThread(
//        context, "models/frame_lower_right.obj", "models/frame_base1.png");
//    imageFrameLowerRight.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
//    imageFrameLowerRight.setBlendMode(BlendMode.AlphaBlending);

    pyramidRenderer.createOnGlThread(
            context, "models/torus.obj" , "models/solid_purple.png");
    pyramidRenderer.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);
    pyramidRenderer.setBlendMode(BlendMode.AlphaBlending);
  }

  public void draw(
      float[] viewMatrix,
      float[] projectionMatrix,
      AugmentedImage augmentedImage,
      Anchor centerAnchor,
      float[] colorCorrectionRgba,
      HoldData[] holdDataArray) {
    float[] tintColor =
        convertHexToColor(TINT_COLORS_HEX[augmentedImage.getIndex() % TINT_COLORS_HEX.length]);

    final float mazeEdgeSize = 20.0f; // Magic number of torus size
    final float maxImageEdgeSize = Math.max(augmentedImage.getExtentX(), augmentedImage.getExtentZ()); // Get largest detected image edge size
    float mazeScaleFactor = maxImageEdgeSize / mazeEdgeSize; // scale to set Maze to image size
    Log.e("Maze scale factor", "" + mazeScaleFactor);
    float[] modelMatrix = new float[16];

    Pose anchorPose = centerAnchor.getPose();
//    Pose[] localBoundaryPoses = {
//      Pose.makeTranslation(
//          -0.5f * augmentedImage.getExtentX(),
//          0.0f,
//          -0.5f * augmentedImage.getExtentZ()), // upper left
//      Pose.makeTranslation(
//          0.5f * augmentedImage.getExtentX(),
//          0.0f,
//          -0.5f * augmentedImage.getExtentZ()), // upper right
//      Pose.makeTranslation(
//          0.5f * augmentedImage.getExtentX(),
//          0.0f,
//          0.5f * augmentedImage.getExtentZ()), // lower right
//      Pose.makeTranslation(
//          -0.5f * augmentedImage.getExtentX(),
//          0.0f,
//          0.5f * augmentedImage.getExtentZ()) // lower left
//    };
//
//    Pose[] worldBoundaryPoses = new Pose[4];
//    for (int i = 0; i < 4; ++i) {
//      worldBoundaryPoses[i] = anchorPose.compose(localBoundaryPoses[i]);
//    }
//
//    float scaleFactor = 1.0f;
//
//    worldBoundaryPoses[0].toMatrix(modelMatrix, 0);
//    imageFrameUpperLeft.updateModelMatrix(modelMatrix, scaleFactor);
//    imageFrameUpperLeft.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, tintColor);
//
//    worldBoundaryPoses[1].toMatrix(modelMatrix, 0);
//    imageFrameUpperRight.updateModelMatrix(modelMatrix, scaleFactor);
//    imageFrameUpperRight.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, tintColor);
//
//    worldBoundaryPoses[2].toMatrix(modelMatrix, 0);
//    imageFrameLowerRight.updateModelMatrix(modelMatrix, scaleFactor);
//    imageFrameLowerRight.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, tintColor);
//
//    worldBoundaryPoses[3].toMatrix(modelMatrix, 0);
//    imageFrameLowerLeft.updateModelMatrix(modelMatrix, scaleFactor);
//    imageFrameLowerLeft.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, tintColor);
    // Render centre
    Pose mazeModelLocalOffset = Pose.makeTranslation(0.0f,0.0f,0.0f);
    anchorPose.compose(mazeModelLocalOffset).toMatrix(modelMatrix, 0);
    pyramidRenderer.updateModelMatrix(modelMatrix, mazeScaleFactor);
    pyramidRenderer.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, tintColor);
    // Render route
    for (int i = 0; i < holdDataArray.length; i++) {
      Float x = holdDataArray[i].getX();
      Float y = holdDataArray[i].getY();

      mazeModelLocalOffset = Pose.makeTranslation(
              (x-0.5f) * augmentedImage.getExtentX(),
              0.0f,
              (y-0.5f) * augmentedImage.getExtentZ());

      anchorPose.compose(mazeModelLocalOffset).toMatrix(modelMatrix, 0);
      pyramidRenderer.updateModelMatrix(modelMatrix, mazeScaleFactor); // This line relies on a change in ObjectRenderer.updateModelMatrix later in this codelab.
      pyramidRenderer.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, tintColor);

    }




  }
// Adjust size of detected image and render it on-screen

//  public void draw(
//        float[] viewMatrix,
//        float[] projectionMatrix,
//        AugmentedImage augmentedImage,
//        Anchor centerAnchor,
//        float[] colorCorrectionRgba) {
//  float[] tintColor =
//          convertHexToColor(TINT_COLORS_HEX[augmentedImage.getIndex() % TINT_COLORS_HEX.length]);
//    final float mazeEdgeSize = 492.65f; // Magic number of maze size
//    final float maxImageEdgeSize = Math.max(augmentedImage.getExtentX(), augmentedImage.getExtentZ()); // Get largest detected image edge size
//
//    Pose anchorPose = centerAnchor.getPose();
//
//    float mazeScaleFactor = maxImageEdgeSize / mazeEdgeSize; // scale to set Maze to image size
//    float[] modelMatrix = new float[16];
//
//    // OpenGL Matrix operation is in the order: Scale, rotation and Translation
//    // So the manual adjustment is after scale
//    // The 251.3f and 129.0f is magic number from the maze obj file
//    // You mustWe need to do this adjustment because the maze obj file
//    // is not centered around origin. Normally when you
//    // work with your own model, you don't have this problem.
//    Pose mazeModelLocalOffset = Pose.makeTranslation(
//            -251.3f * mazeScaleFactor,
//            0.0f,
//            129.0f * mazeScaleFactor);
//    anchorPose.compose(mazeModelLocalOffset).toMatrix(modelMatrix, 0);
//  pyramidRenderer.updateModelMatrix(modelMatrix, mazeScaleFactor); // This line relies on a change in ObjectRenderer.updateModelMatrix later in this codelab.
//  pyramidRenderer.draw(viewMatrix, projectionMatrix, colorCorrectionRgba, tintColor);
//}

  private static float[] convertHexToColor(int colorHex) {
    // colorHex is in 0xRRGGBB format
    float red = ((colorHex & 0xFF0000) >> 16) / 255.0f * TINT_INTENSITY;
    float green = ((colorHex & 0x00FF00) >> 8) / 255.0f * TINT_INTENSITY;
    float blue = (colorHex & 0x0000FF) / 255.0f * TINT_INTENSITY;
    return new float[] {red, green, blue, TINT_ALPHA};
  }
}
