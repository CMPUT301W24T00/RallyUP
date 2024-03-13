package com.example.rallyup.uiReference;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

//  code sourced from: https://firebaseopensource.com/projects/firebase/firebaseui-android/storage/readme/

/**
 * This class represents the apps glide module
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    /**
     * This method registers all necessary components
     * @param context An Application {@link android.content.Context}.
     * @param glide The Glide singleton that is in the process of being initialized.
     * @param registry An {@link com.bumptech.glide.Registry} to use to register components.
     */
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, Registry registry) {
        // Register FirebaseImageLoader to handle StorageReference
        registry.append(StorageReference.class, InputStream.class,
                new FirebaseImageLoader.Factory());
    }
}
