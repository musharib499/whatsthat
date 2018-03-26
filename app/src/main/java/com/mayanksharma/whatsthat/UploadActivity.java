package com.mayanksharma.whatsthat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

import static android.R.attr.bitmap;
import static android.R.attr.data;

public class UploadActivity extends AppCompatActivity {
    private Spinner spinner1, spinner2, spinner3, spinner4;
    private ArrayAdapter<CharSequence> adapter1, adapter2, adapter3, adapter4;
    private EditText mRoomNo;
    private Button Upload_btn, Pdf_btn, Qr_generate;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private static final int GALLERY_REQUEST = 1;
    private ImageView Image;
    private TextView PdfStatus;
    private String text2Qr;
    String get_id;
    private Bitmap bitmap;
    String Get_qr_url;
    private int flag = 0;
    private Uri mQrCodeUri = null;
    //ProgressBar progressBar;
    private ProgressDialog mProgress;
    private Uri mPdfUri = null;
    private Uri mQRUri = null;
    private String COURSE;
    public final static int QRcodeWidth = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorage = storage.getReferenceFromUrl("gs://whatsthat-52cbb.appspot.com/");
        mDatabase = FirebaseDatabase.getInstance().getReference("Docs");

        spinner1 = (Spinner) findViewById(R.id.course_spinner);
        spinner2 = (Spinner) findViewById(R.id.year_spinner);
        spinner3 = (Spinner) findViewById(R.id.semester_spinner);
        spinner4 = (Spinner) findViewById(R.id.event_spinner);
        mRoomNo = (EditText)findViewById(R.id.roomNo);

        adapter1 = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_item);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.years, android.R.layout.simple_spinner_item);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.semesters, android.R.layout.simple_spinner_item);
        adapter4 = ArrayAdapter.createFromResource(this, R.array.event, android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_item);

        Upload_btn = (Button) findViewById(R.id.upload_button);
        Pdf_btn = (Button) findViewById(R.id.pdf_button);
        Qr_generate = (Button) findViewById(R.id.qr_generate);
        Image = (ImageView) findViewById(R.id.qr_image);
        PdfStatus = (TextView) findViewById(R.id.pdf_status);
        //progressBar = (ProgressBar) findViewById(R.id.progressbar);

        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);
        spinner4.setAdapter(adapter4);


        get_id = mDatabase.push().getKey();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadIt(mPdfUri);
                //UploadQR();
                //Toast.makeText(this, "uploaded", Toast.LENGTH_LONG).show();
                //Toast makeText (Context context, CharSequence text, int duration);
            }
        });

        //for selecting files from the device
        Pdf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                // galleryIntent.setType("application/pdf");
                // startActivityForResult(galleryIntent, GALLERY_REQUEST);
                getPDF();
            }
        });



        //For generating QR CODE
        Qr_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Get_Course = spinner1.getSelectedItem().toString();
                final String Get_Year = spinner2.getSelectedItem().toString();
                final String Get_Sem = spinner3.getSelectedItem().toString();
                final String Get_Event = spinner4.getSelectedItem().toString();
                final String Get_Room = mRoomNo.getText().toString().trim();

                if(Get_Event.equals("Yes"))
                {
                    text2Qr = get_id;
                } else{
                    text2Qr = Get_Course + Get_Sem + Get_Year ;
                }
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    Image.setImageBitmap(bitmap);
                    Image.setImageURI(mQrCodeUri);

                    //BitmapDrawable drawable = (BitmapDrawable) Image.getDrawable();
                    //Bitmap bitmap1 = drawable.getBitmap();
                    //storeImage(bitmap1);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        mProgress = new ProgressDialog(this);

    }




    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    private void getPDF() {

        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                PdfStatus.setText("Selected");

                mPdfUri = data.getData();
                flag = 1;
            } else {
                Toast.makeText(UploadActivity.this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            
        }
    }

    private void UploadIt(Uri data) {
        //progressBar.setVisibility(View.VISIBLE);
        mProgress.setMessage("Uploading, Please Wait...");


        final String Get_Course = spinner1.getSelectedItem().toString();
        final String Get_Year = spinner2.getSelectedItem().toString();
        final String Get_Sem = spinner3.getSelectedItem().toString();
        final String Get_Event = spinner4.getSelectedItem().toString();
        final String Get_Room = mRoomNo.getText().toString().trim();

        if (!TextUtils.isEmpty(Get_Course) && !TextUtils.isEmpty(Get_Year) && !TextUtils.isEmpty(Get_Sem) && flag == 1) {
            mProgress.show();

            //String get_id = mDatabase.push().getKey();
            StorageReference filePath = mStorage.child(Constants.STORAGE_PATH_UPLOADS).child(mPdfUri.getLastPathSegment() + ".pdf");
            filePath.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    @SuppressWarnings("VisibleForTests")
                    final String Get_pdf_url = taskSnapshot.getDownloadUrl().toString();

                    Data data = new Data(Get_Course, Get_Year, Get_Sem, get_id, Get_Room, Get_pdf_url, Get_Event);
                    mDatabase.child(get_id).setValue(data);
                    //progressBar.setVisibility(View.GONE);

                if(Get_Event.equals("No"))
                {    Intent intent = new Intent(UploadActivity.this, DisplayActivity.class);
                    //Intent intent = new Intent(UploadActivity.this, DisplayActivity.class);
                    mProgress.dismiss();
                    Bundle b = new Bundle();
                    b.putSerializable("user", data);
                    intent.putExtras(b);
                    startActivity(intent);
                    //Toast.makeText(this, "uploaded", Toast.LENGTH_LONG).show();
                }else
                {
                    Intent intent = new Intent(UploadActivity.this, DisplayActivity2.class);
                    //Intent intent = new Intent(UploadActivity.this, DisplayActivity.class);
                    mProgress.dismiss();
                    Bundle b = new Bundle();
                    b.putSerializable("user", data);
                    intent.putExtras(b);
                    startActivity(intent);
                }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }

            });


        } else {
            Toast.makeText(UploadActivity.this, "Please select a pdf file", Toast.LENGTH_LONG).show();
        }

    }

}

