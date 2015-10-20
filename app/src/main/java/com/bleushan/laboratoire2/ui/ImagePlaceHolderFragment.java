/*
 * ImagePlaceHolderFragment.java
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

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bleushan.laboratoire2.R;

/**
 * A {@link Fragment} subclass that serves as an image placeholder. It's basically a blank
 * fragment and it does nothing particular beside serving as a text view holder. Like
 * {@link ImageContentFragment}, its view must be inserted  within a valid {@link ViewGroup}
 * because its layout uses a merge tag. The reason behind the merge tag is that it makes a
 * shallower
 * view hierarchy.
 *
 * @see android.support.v4.app.FragmentTransaction#add(int, Fragment)
 * @see android.support.v4.app.FragmentTransaction#replace(int, Fragment)
 * @see MainActivity#replaceCurrentFragmentWithFragmentWithTag(String, Uri)
 */
public class ImagePlaceHolderFragment extends Fragment {

  public ImagePlaceHolderFragment() {
    // Required empty public constructor
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
      inflater.inflate(R.layout.fragment_image_placeholder, container, true);
    } else {
      throw new IllegalStateException("No container to inflate view. Please use " +
                                      "FragmentTransaction.add/replace(containerViewId, ...) to " +
                                      "show this fragment");
    }
    return null;
  }
}
