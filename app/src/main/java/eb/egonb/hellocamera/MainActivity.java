package eb.egonb.hellocamera;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //magic numbers are bad m'kay
    final int IMAGE_REQUEST = 1000;

    //UI
    private ImageView ivProfile;

    private View.OnClickListener profileIVListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dispatchTakePicture();
        }
    };
    private File currentImage;

    private void dispatchTakePicture() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //nakijken of er wel een camera op het toestel zit
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            currentImage = createImageFile();

            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(),
                    "eb.egonb.hellocamera.fileprovider",
                    currentImage);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(pictureIntent, IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK){
            Picasso.get().load(currentImage)
                    .resize(200, 200)
                    .centerCrop()
                    .into(ivProfile);
        }
    }

    private File createImageFile(){
        String fileName = "/" + System.currentTimeMillis() + "profile.jpeg";
        File storageDir = getFilesDir();

        return new File(storageDir + fileName);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivProfile = findViewById(R.id.iv_photo);
        ivProfile.setOnClickListener(profileIVListener);
    }
}
