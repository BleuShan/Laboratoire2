/*
 * ImageContentFragment.java
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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bleushan.laboratoire2.Laboratoire2Application;
import com.bleushan.laboratoire2.R;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer.Result;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

/**
 * A simple {@link Fragment} subclass intended to manage an {@link ImageView} and sharing its
 * image on facebook. Please use the {@link ImageContentFragment#newInstance} factory method to
 * create an instance of this fragment. Like {@link ImagePlaceHolderFragment}, its view must be
 * inserted within a valid {@link ViewGroup} because its layout uses a merge tag. The reason
 * behind the merge tag is that it makes a shallower view hierarchy.
 *
 * @see android.support.v4.app.FragmentTransaction#add(int, Fragment)
 * @see android.support.v4.app.FragmentTransaction#replace(int, Fragment)
 * @see MainActivity#replaceCurrentFragmentWithFragmentWithTag(String, Uri)
 */
public class ImageContentFragment extends Fragment {

  private static final String ARG_IMAGE_URI = "IMAGE_URI";
  private Uri imageUri;
  private ShareDialog shareDialog;
  private Laboratoire2Application application;
  private ImageView imageView;

  public ImageContentFragment() {
    // Required empty public constructor
  }

  /**
   * A factory method to create a new instance of this fragment.
   *
   * @param uri
   *   The uri of the image to be displayed.
   *
   * @return A new instance of fragment ImageContentFragment.
   */
  public static ImageContentFragment newInstance(Uri uri) {
    if (uri == null) {
      throw new IllegalStateException("the uri parameter cannot be null");
    }
    ImageContentFragment fragment = new ImageContentFragment();
    Bundle args = new Bundle();
    args.putParcelable(ARG_IMAGE_URI, uri);
    fragment.setArguments(args);
    return fragment;
  }

  /**
   * Getter for the fragment imageUri.
   *
   * @return the current imageUri
   */
  public Uri getImageUri() {
    return this.imageUri;
  }

  /**
   * Setter for the fragment imageUri. If its view has been instantiated, it will also change the
   * image uri.
   *
   * @param imageUri
   *   the new uri
   */
  public void setImageUri(Uri imageUri) {
    this.imageUri = imageUri;
    if (this.imageView != null) {
      this.imageView.setImageURI(imageUri);
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = this.getArguments();
    if (args != null) {
      this.imageUri = args.getParcelable(ARG_IMAGE_URI);
    } else if (savedInstanceState != null) {
      this.imageUri = savedInstanceState.getParcelable(ARG_IMAGE_URI);
    }
    this.setHasOptionsMenu(true);
    Activity activity = this.getActivity();
    if (activity != null) {
      this.application = ((Laboratoire2Application) activity.getApplication());
      if (this.application != null) {
        this.shareDialog = new ShareDialog(this);
        FacebookCallback<Result> callback = new FacebookCallback<Result>() {
          @Override
          public void onSuccess(Result result) {
            // Swap the image view for the placeholder text on success;
            MainActivity activity = ((MainActivity) ImageContentFragment.this.getActivity());
            if (activity != null) {
              String tag = MainActivity.FRAGMENT_IMAGE_PLACEHOLDER_TAG;
              activity.replaceCurrentFragmentWithFragmentWithTag(tag, null);
            }
          }

          @Override
          public void onCancel() {

          }

          @Override
          public void onError(FacebookException error) {
            // throw the error
            throw error;
          }
        };
        // Setup the callbacks.
        this.shareDialog.registerCallback(this.application.callbackManager, callback);
      }
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(ARG_IMAGE_URI, this.imageUri);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the view only if we have a container otherwise throw an exception. This is
    // required because this fragment layout uses a merge tag.
    if (container != null) {
      // Clear all the container subviews and inflate the layout within it.
      container.removeAllViews();
      View view = inflater.inflate(R.layout.fragment_image_content, container, true);
      this.imageView =
        ((ImageView) view.findViewById(R.id.com_bleushan_laboratoire2_fragment_content_image));
      if ((this.imageView != null) && (this.imageUri != null)) {
        this.imageView.setImageURI(this.imageUri);
      } else {
        throw new IllegalStateException("Cannot display image because there's no source or " +
                                        "destination.");
      }
    } else {
      throw new IllegalStateException("No container to inflate view. Please use " +
                                      "FragmentTransaction.add/replace(containerViewId, ...) to " +
                                      "show this fragment");
    }
    return null;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.fragment_image_content_menu, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    View view = null;
    Activity activity = this.getActivity();
    if (activity != null) {
      view = activity.findViewById(R.id.com_bleushan_laboratoire2_main_content_container);
    }
    switch (item.getItemId()) {
      case R.id.com_bleushan_laboratoire2_action_facebook_share:
        if ((this.imageUri != null)) {
          SharePhoto sharePhoto = new SharePhoto.Builder().setImageUrl(this.imageUri)
                                                          .build();
          SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(sharePhoto)
                                                                     .build();
          if (ShareDialog.canShow(SharePhotoContent.class) &&
              (this.shareDialog != null)) {
            this.shareDialog.show(content);
          } else if (view != null) {
            Snackbar.make(view,
                          R.string.com_bleushan_laboratoire2_error_cannot_share,
                          Snackbar.LENGTH_LONG)
                    .show();
          }
        } else if (view != null) {
          Snackbar.make(view, R.string.com_bleushan_laboratoire2_app_noimage, Snackbar.LENGTH_LONG)
                  .show();
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (this.application != null) {
      this.application.callbackManager.onActivityResult(requestCode, resultCode, data);
    }
  }
}
