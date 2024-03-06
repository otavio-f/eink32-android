package br.otaviof.esp32manager.fragment;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.otaviof.esp32manager.R;

public class ImageFragment extends Fragment implements View.OnClickListener, ActivityResultCallback<Uri> {

    private final ViewModel mViewModel = new ViewModel();

    public static ImageFragment newInstance() {
        return new ImageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.image = view.findViewById(R.id.image_preview);
        mViewModel.placeholder = view.findViewById(R.id.image_placeholder);
        mViewModel.initSelection = view.findViewById(R.id.button_select_image);
        mViewModel.initSelection.setOnClickListener(this);

        mViewModel.imageSelector = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                this
        ); //TODO: THIS DOESN'T WORK
    }

    @Override
    public void onActivityResult(Uri o) {
        if (o == null)
            return;
        String path = o.getPath();
        Drawable img = Drawable.createFromPath(path);
        if (img == null) {
            Toast.makeText(this.getContext(), R.string.open_img_err, Toast.LENGTH_SHORT).show();
            return;
        }
        mViewModel.placeholder.setText(path);
        mViewModel.image.setImageDrawable(img);
    }

    /***
     * Starts the system file picker, searching for an image
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() != R.id.button_select_image)
            return;
        PickVisualMediaRequest picker = new PickVisualMediaRequest.Builder()
                .setMediaType(new ActivityResultContracts.PickVisualMedia.SingleMimeType("image/*"))
                .build();

        mViewModel.imageSelector.launch(picker);
    }

    private static class ViewModel {
        public ImageView image;
        public Button initSelection;
        public TextView placeholder;
        ActivityResultLauncher<PickVisualMediaRequest> imageSelector;
    }
}