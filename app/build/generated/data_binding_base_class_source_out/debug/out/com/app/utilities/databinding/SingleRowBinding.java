// Generated by view binder compiler. Do not edit!
package com.app.utilities.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.app.utilities.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class SingleRowBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final ImageView rowImageView;

  @NonNull
  public final TextView rowTextView;

  private SingleRowBinding(@NonNull RelativeLayout rootView, @NonNull ImageView rowImageView,
      @NonNull TextView rowTextView) {
    this.rootView = rootView;
    this.rowImageView = rowImageView;
    this.rowTextView = rowTextView;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static SingleRowBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static SingleRowBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.single_row, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static SingleRowBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.rowImageView;
      ImageView rowImageView = ViewBindings.findChildViewById(rootView, id);
      if (rowImageView == null) {
        break missingId;
      }

      id = R.id.rowTextView;
      TextView rowTextView = ViewBindings.findChildViewById(rootView, id);
      if (rowTextView == null) {
        break missingId;
      }

      return new SingleRowBinding((RelativeLayout) rootView, rowImageView, rowTextView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
