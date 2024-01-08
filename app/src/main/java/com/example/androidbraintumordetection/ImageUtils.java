package com.example.androidbraintumordetection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static void saveImageToStorage(Context context, int drawableResId, String fileName) {
        // Charger l'image depuis les ressources drawable
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableResId);

        // Enregistrer l'image dans le stockage interne de l'application
        saveBitmapToFile(context, bitmap, fileName);
    }

    private static void saveBitmapToFile(Context context, Bitmap bitmap, String fileName) {
        // Chemin du fichier dans le stockage interne de l'application
        File file = new File(context.getFilesDir(), fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            // Compression de l'image au format JPEG avec une qualité de 100 (non compressée)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

