package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// Customized by SY

public class UploadProductActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 200;
    private ImageView imageView;
    private RelativeLayout relativeLayout;
    Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);

        uploadImage();
        goToMainActivity();

        Dateleft();
        Timeleft();
    }

    public void uploadImage() {
        imageView = findViewById(R.id.upload_product_img_iv);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    imageView.setImageBitmap(img);

                    relativeLayout = findViewById(R.id.upload_product_img_layout);
                    relativeLayout.bringChildToFront(imageView);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Customized by 민승
    public void goToMainActivity() {
        RelativeLayout btn = findViewById(R.id.upload_act_back_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void Dateleft() {
        final Calendar calendar = Calendar.getInstance();
        final TextView dateleft = findViewById(R.id.upload_products_act_dateleft);
        dateleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOf) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOf);

                        String dateM = String.valueOf(calendar.getTime()).substring(4,7);
                        String monthM = new String();

                        switch(dateM) {
                            case "Jan" : monthM = "01";  break;
                            case "Feb" : monthM = "02";  break;
                            case "Mar" : monthM = "03";  break;
                            case "Apr" : monthM = "04";  break;
                            case "May" : monthM = "05";  break;
                            case "Jun" : monthM = "06";  break;
                            case "Jul" : monthM = "07";  break;
                            case "Aug" : monthM = "08";  break;
                            case "Sep" : monthM = "09";  break;
                            case "Oct" : monthM = "10";  break;
                            case "Nov" : monthM = "11";  break;
                            case "Dec" : monthM = "12";  break;
                        }


                        String string = String.valueOf(calendar.getTime()).substring(30,34)+"/"+monthM+"/"+String.valueOf(calendar.getTime()).substring(8,10);
                        dateleft.setText(string);
                        Log.d("날짜", String.valueOf(calendar.getTime()).substring(8,10));
                        Log.d("날짜", String.valueOf(calendar.getTime()).substring(4,7));
                        Log.d("날짜", String.valueOf(calendar.getTime()).substring(30,34));
                        dateleft.setTextColor(Color.parseColor("#3A3A3A"));

                        /*
                        SimpleDateFormat simpleD = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                        dateleft.setText(simpleD.format(calendar.getTime()));
                        dateleft.setTextColor(Color.parseColor("#3A3A3A"));

                         */
                    }
                };
                new DatePickerDialog(view.getRootView().getContext(), onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }

    public void Timeleft() {
        final Calendar calendar = Calendar.getInstance();
        final TextView timeleft = findViewById(R.id.upload_products_act_timeleft);
        timeleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOf, int minOf) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOf);
                        calendar.set(Calendar.MINUTE, minOf);

                        timeleft.setText(String.valueOf(calendar.getTime()).substring(11, 16));
                        Log.d("시간", String.valueOf(calendar.getTime()).substring(11, 16)+":00");
                        timeleft.setTextColor(Color.parseColor("#3A3A3A"));
                    }
                };
                new TimePickerDialog(view.getRootView().getContext(), onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

            }
        });
    }


}
