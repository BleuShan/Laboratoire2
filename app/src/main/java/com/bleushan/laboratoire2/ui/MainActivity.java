/*
 * MainActivity.java
 * Laboratoire2
 *
 * Copyright (c) 2015. Philippe Lafontaine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.bleushan.laboratoire2.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bleushan.laboratoire2.R;
import com.facebook.appevents.AppEventsLogger;

/**
 * The MainActivity of the application. It mainly manages fragments.
 */
public class MainActivity extends AppCompatActivity {

  public static final String FRAGMENT_IMAGE_PLACEHOLDER_TAG = "ImagePlaceholder";
  public static final String FRAGMENT_IMAGE_CONTENT_TAG = "ImageContent";
  private static final int REQUEST_GALLERY_PICK = 1337;
  private static final String TAG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.activity_main);
    Toolbar mainToolbar =
      ((Toolbar) this.findViewById(R.id.com_bleushan_laboratoire2_main_toolbar));
    if (mainToolbar != null) {
      this.setSupportActionBar(mainToolbar);
    }
    // Instantiate and add the placeholder fragment.
    // We don't use the XML because our fragments layout use the merge tag which allows us to
    // have a shallower view hierarchy.
    // See: http://android-developers.blogspot.ca/2009/03/android-layout-tricks-3-optimize-by.html
    this.getSupportFragmentManager()
        .beginTransaction()
        .add(R.id.com_bleushan_laboratoire2_main_content_container,
             new ImagePlaceHolderFragment(),
             FRAGMENT_IMAGE_PLACEHOLDER_TAG)
        .commit();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    this.getMenuInflater().inflate(R.menu.main_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.com_bleushan_laboratoire2_action_load_image:
        // This intent calls the gallery app
        Intent galleryPickIntent = new Intent(Intent.ACTION_PICK);
        galleryPickIntent.setType("image/*");
        this.startActivityForResult(galleryPickIntent, REQUEST_GALLERY_PICK);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    AppEventsLogger.activateApp(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    AppEventsLogger.deactivateApp(this);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if ((requestCode == REQUEST_GALLERY_PICK) &&
          (data != null)) {
        // We use commitAllowingStateLoss because the support fragment manager doesn't allow
        // normal commit
        this.replaceCurrentFragmentWithFragmentWithTag(FRAGMENT_IMAGE_CONTENT_TAG, data.getData());
      }
    }
  }

  /**
   * A convenience method to replace the fragment in the main content container in the layout.
   * It's intended to provide a kind of {@link android.widget.ViewFlipper} behavior with
   * {@link Fragment} that uses a merge tag within its layout. This method and the
   * {@link ImagePlaceHolderFragment} exist because we are too dumb to actually use a
   * {@link android.widget.ViewFlipper}, some layouts and manage it through the
   * {@link ImageContentFragment}
   * <p/>
   * This method will first try to find an already instantiated method in the fragment manager
   * that match the tag. If none is found it will create a brand new one.
   *
   * @param fragmentTag
   *   The tag that match the fragment to swap in.
   *   If {@link MainActivity#FRAGMENT_IMAGE_CONTENT_TAG} is passed in it will replace with a
   *   {@link ImageContentFragment}.
   *   <p/>
   *   If {@link MainActivity#FRAGMENT_IMAGE_PLACEHOLDER_TAG} is
   *   passed in it will replace with a {@link ImagePlaceHolderFragment}.
   * @param uri
   *   Only used if {@link MainActivity#FRAGMENT_IMAGE_CONTENT_TAG} is passed in. Ignored
   *   otherwise.
   */
  public void replaceCurrentFragmentWithFragmentWithTag(@NonNull String fragmentTag,
                                                        @Nullable Uri uri) {
    FragmentManager fragmentManager = this.getSupportFragmentManager();
    Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
    if (fragment == null) {
      if (fragmentTag.equals(FRAGMENT_IMAGE_CONTENT_TAG)) {
        fragment = ImageContentFragment.newInstance(uri);
      } else if (fragmentTag.equals(FRAGMENT_IMAGE_PLACEHOLDER_TAG)) {
        fragment = new ImagePlaceHolderFragment();
      }
      fragmentManager.beginTransaction()
                     .replace(R.id.com_bleushan_laboratoire2_main_content_container,
                              fragment,
                              fragmentTag)
                     .commitAllowingStateLoss();
    } else {
      if (fragmentTag.equals(FRAGMENT_IMAGE_CONTENT_TAG)) {
        ImageContentFragment imageContentFragment = ((ImageContentFragment) fragment);
        imageContentFragment.setImageUri(uri);
      }
      fragmentManager.beginTransaction()
                     .replace(R.id.com_bleushan_laboratoire2_main_content_container,
                              fragment)
                     .commitAllowingStateLoss();
    }
  }

}
