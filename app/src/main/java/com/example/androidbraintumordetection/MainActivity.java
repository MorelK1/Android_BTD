package com.example.androidbraintumordetection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidbraintumordetection.ml.MobilenetV110224Quant;
import com.example.androidbraintumordetection.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button selectBtn, predictBtn;
    TextView result;
    ImageView imageView;
    Bitmap bitmap;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] labels = {"Pas de tumeur détecté", "Tumeur détecté"};
        int cnt = 0;

//        ImageUtils.saveImageToStorage(this, R.drawable.test, "test.jpg");

        selectBtn = findViewById(R.id.selectBtn);
        predictBtn = findViewById(R.id.predictBtn);
        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        predictBtn.setOnClickListener(v -> {
            try {
                if (bitmap == null) {
                    result.setText("Veuillez sélectionner une image.");
                    return;
                }

                // Redimensionner l'image sélectionnée à la taille attendue
                bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

                // Convertir l'image Bitmap en tableau de pixels (intArray)
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int[] intArray = new int[width * height];
                bitmap.getPixels(intArray, 0, width, 0, 0, width, height);

                // Normaliser les valeurs de pixel à l'intervalle [0, 1]
                float[] floatArray = new float[width * height * 3];
                for (int i = 0; i < intArray.length; ++i) {
                    final int val = intArray[i];
                    floatArray[i * 3 + 0] = ((val >> 16) & 0xFF) / 255.0f; // Rouge
                    floatArray[i * 3 + 1] = ((val >> 8) & 0xFF) / 255.0f;  // Vert
                    floatArray[i * 3 + 2] = (val & 0xFF) / 255.0f;
                }

                // Créer le TensorBuffer d'entrée du modèle
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                inputFeature0.loadArray(floatArray, new int[]{1, 224, 224, 3});

                // Effectuer l'inférence du modèle et récupérer le résultat
                Model model = Model.newInstance(MainActivity.this);
                Model.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                // Récupérer la classe prédite et la probabilité
                int maxIndex = getMax(outputFeature0.getFloatArray());
                result.setText("Classe prédite : " + labels[maxIndex] +
                        ".\n Probabilité : " + outputFeature0.getFloatArray()[maxIndex]);

                // Libérer les ressources du modèle
                model.close();

            } catch (IOException e) {
                e.printStackTrace();
                result.setText("Erreur lors de la prédiction : " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                result.setText("Une erreur s'est produite : " + e.getMessage());
            }

        });
    }

    int getMax(float[] arr){
        int max = 0;
        for (int i=0; i<arr.length; i++){
            if (arr[i] > arr[max]) max=i;
        }
        return max;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 10){
            if (data != null){
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap( this.getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}