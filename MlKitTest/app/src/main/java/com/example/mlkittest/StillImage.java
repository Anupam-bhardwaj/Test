package com.example.mlkittest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;

import java.io.ByteArrayOutputStream;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StillImage extends Fragment {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    TextView labelText;
    Bitmap bitmap;

    public static StillImage newInstance() {
        return new StillImage();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_still_image, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button detect = view.findViewById(R.id.detect);
        imageView = view.findViewById(R.id.captureImage);
        labelText = view.findViewById(R.id.labelText);

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getActivity().startActivityFromFragment(StillImage.this, cameraIntent, REQUEST_IMAGE_CAPTURE);

            }
        });
    }

    public Bitmap getBitmap() {
        return bitmap;
    }


    private Void getLabel() {

        Log.d("TAG", String.valueOf(getBitmap()));
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(getBitmap());

        FirebaseVisionOnDeviceImageLabelerOptions options = new FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                .setConfidenceThreshold(0.7f)
                .build();
        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance()
                .getOnDeviceImageLabeler(options);

        labeler.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                        String text = new String();
                        String entityId = new String();
                        float confidence = 0;
                        for (FirebaseVisionImageLabel label: labels) {
                            text = label.getText();
                            entityId = label.getEntityId();
                            confidence = label.getConfidence();
                        }
                        String labelBuilder = "Label: " + text + "\n" + "confidence: " + String.valueOf(confidence);
                        Log.d("label", labelBuilder);
                        labelText.setText(labelBuilder);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Error", e.toString());
                    }
                });
            return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (resultCode == Activity.RESULT_OK && data != null) {

                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    // convert byte array to Bitmap

                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                            byteArray.length);

                    StillImage.this.bitmap = bitmap;
                    imageView.setImageBitmap(bitmap);
                    getLabel();


                }
            }
        }catch (Exception e){}
    }


}
