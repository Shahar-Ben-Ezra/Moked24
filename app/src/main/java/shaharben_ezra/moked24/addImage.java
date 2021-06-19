package shaharben_ezra.moked24;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class addImage extends AppCompatActivity {

    private EditText etDISC;
    private ImageView iv, iv1;
    private Bitmap imageBitmap, imageBitmapthr;
    private static String whichImage = "";
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    public static final int PICK_IMAGE = 100;
    Spinner spinnerInputType;
    private static String itemSelected = "";
    private static String currentPhotoPath = "";
    private boolean flagChange = false;
    private int positionSelected = 0;
    private evidence evidence = new evidence(null, null, "", "", "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        etDISC = findViewById(R.id.etDISC);
        ImageButton BtnPicking = findViewById(R.id.BtnPickdig);
        Button btnSave = findViewById(R.id.btnSave);
        Button ban_cancel = findViewById(R.id.btn_cancle);
        ImageButton imageGallery = findViewById(R.id.imageGallerydig);
        ImageButton BtnPicThr = findViewById(R.id.BtnPick);
        ImageButton imageGalleryThr = findViewById(R.id.imageGallery);
        iv = findViewById(R.id.iv);
        iv1 = findViewById(R.id.iv1);
        spinnerInputType = findViewById(R.id.spinnerInputType);
        Intent intent = getIntent();
        if (intent.getSerializableExtra("position") != null) {
            try {
                positionSelected = (int) intent.getSerializableExtra("position");
                evidence = StartCreatePdfFile.evidenceArrayList.get(positionSelected - 1);
                flagChange = true;
                iv1.setImageBitmap(evidence.getThermalImageView());
                imageBitmapthr = evidence.getThermalImageView();
                iv.setImageBitmap(evidence.getRegularImageView());
                imageBitmap = evidence.getRegularImageView();
                etDISC.setText(evidence.getDescription());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String[] arrayItems = {"ללא פירוט מובנה",
                "זוהתה חדירת מים המגיעה מכיוון",
                "זוהה איטום לקוי",
                "זוהה מעבר רטיבות מכיוון",
                "בוצעה בדיקת הצפה לצנרת דלוחין נמצא תקין",
                "בוצעה בדיקת הצפה לצנרת דלוחין נמצא לא תקין",
                "בוצעה בדיקת לחץ לצנרת מים חמים/ קרים נמצא תקין",
                "זוהתה נזילת מים בצנרת",
                "ניתן לזהות עלייה קאפלארית בקיר"};

        ArrayAdapter<String> dataAdapter = new ArrayAdapter(getBaseContext(), R.layout.simple_spinner_item, arrayItems);
        //Dropdown layout style
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        //attaching data adapter to spinner
        spinnerInputType.setAdapter(dataAdapter);
        spinnerInputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //METHOD 1: Get text from selected item's position & set it to TextView
                itemSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        BtnPicking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/// take a picture
                whichImage = "digital";
                dispatchTakePictureIntent();
            }
        });
        imageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/// take a  picture from gallery
                whichImage = "digital";
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        BtnPicThr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/// take a picture
                whichImage = "thermal";
                dispatchTakePictureIntent();
            }
        });
        imageGalleryThr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/// take a  picture from gallery
                whichImage = "thermal";
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        ban_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/// save

                String txt = etDISC.getText().toString();
                if (!itemSelected.equals("ללא פירוט מובנה")) {
                    txt = itemSelected + " " + txt;
                }
                evidence.setRegularImageView(imageBitmap);
                evidence.setThermalImageView(imageBitmapthr);
                evidence.setDescription(txt);
                imageBitmap = null;
                imageBitmapthr = null;
                if (positionSelected > 0) {
                    Toast.makeText(addImage.this, getString(R.string.edited), Toast.LENGTH_SHORT).show();
                } else {
                    StartCreatePdfFile.evidenceArrayList.add(evidence);
                    Toast.makeText(addImage.this, getString(R.string.you_add), Toast.LENGTH_SHORT).show();
                }
                positionSelected = 0;
                addImage.this.finish();
            }
        });
    }

    /**
     * get the results from the camera or the results from gallery
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (whichImage.equals("thermal")) {
                imageBitmapthr = BitmapFactory.decodeFile(currentPhotoPath);// all the picture is Bitmap
                try {
                    Bitmap bitmap = fixingRotaitonx(currentPhotoPath, imageBitmapthr);
                    evidence.setThermalImageViewPath(currentPhotoPath);
                    iv1.setImageBitmap(bitmap);
                    imageBitmapthr = bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);// all the picture is Bitmap
                try {
                    Bitmap bitmap = fixingRotaitonx(currentPhotoPath, imageBitmap);
                    iv.setImageBitmap(bitmap);
                    imageBitmap = bitmap;
                    evidence.setRegularImageViewPath(currentPhotoPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if ((requestCode == PICK_IMAGE && resultCode == RESULT_OK)) {
            Uri ImageUri = data.getData();
            if (whichImage.equals("thermal")) {
                iv1.setImageURI(ImageUri);
                iv1.setDrawingCacheEnabled(true);
                iv1.buildDrawingCache();
                imageBitmapthr = Bitmap.createBitmap(iv1.getDrawingCache());
                saveBitmapToImagesFolder(imageBitmapthr);
                evidence.setThermalImageViewPath(currentPhotoPath);
            } else {
                iv.setImageURI(ImageUri);
                iv.setDrawingCacheEnabled(true);
                iv.buildDrawingCache();
                imageBitmap = Bitmap.createBitmap(iv.getDrawingCache());
                saveBitmapToImagesFolder(imageBitmap);
                evidence.setRegularImageViewPath(currentPhotoPath);

            }
        }
    }

    private void saveBitmapToImagesFolder(Bitmap PICK_IMAGE) {
        try (FileOutputStream out = new FileOutputStream(createImageFile())) {
            PICK_IMAGE.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap fixingRotaitonx(String photoPath, Bitmap bitmap) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap rotatedBitmap = null;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "jpg_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/MokedApp", "ImagesMokedApp");
        storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.getStackTrace();
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
}