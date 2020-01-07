package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ssak.DB.SharedPreferenceController;
import com.example.ssak.Network.ApplicationController;
import com.example.ssak.Network.NetworkService;
import com.example.ssak.Post.PostUploadProductRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Customized by SY

public class UploadProductActivity extends AppCompatActivity {

    ApplicationController applicationController = new ApplicationController();
    NetworkService networkService = applicationController.buildNetworkService("http://52.79.193.54:3000/");

    private static final int REQUEST_CODE = 200;
    private ImageView imageView;
    private RelativeLayout relativeLayout;
    Context context;

    String expDate = "";
    MultipartBody.Part image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);

        uploadImage();
        goToMainActivity();
        uploadProduct();

        Dateleft();
        Timeleft();

        limitText(); //글자수 제한, 토스트 띄우기
    }

    //Customized by 민승
    public void limitText() {
        final EditText edName = findViewById(R.id.upload_product_name_et);
        final EditText edComment = findViewById(R.id.upload_product_comments_et);

        final Toast toastN = Toast.makeText(this, "글자수를 초과했습니다(최대 10자)", Toast.LENGTH_SHORT);
        final Toast toastC = Toast.makeText(this, "글자수를 초과했습니다(최대 20자)", Toast.LENGTH_SHORT);

        //특수문자 입력 제한 - 천지인 가능
        edName.setFilters(new InputFilter[] {new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int start, int end, Spanned spanned, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(charSequence.charAt(i))) {
                        return "";
                    }
                } return null;
            }
        }});

        //글자 수 제한 - 상품명
        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (edName.length() > 10) {
                    toastN.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 100);
                    toastN.show();

                    InputFilter[] filters = new InputFilter[]{new InputFilter.LengthFilter(10)};
                    edName.setFilters(filters);
                }
            }
        });

        //글자 수 제한 - 상품 코멘트
        edComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (edComment.length() > 20) {
                    toastC.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 100);
                    toastC.show();

                    InputFilter[] filters = new InputFilter[]{new InputFilter.LengthFilter(20)};
                    edComment.setFilters(filters);
                }
            }
        });
    }

    //Customized by SY
    public void uploadImage() {
        imageView = findViewById(R.id.upload_product_img_iv);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestReadExternalStoragePermission();
            }
        });
    }

    public void showAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    // BitmapFactory.Options options = new BitmapFactory.Options();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                    inputStream.close();
                    imageView.setImageBitmap(bitmap);

                    String imgURI = getRealPathFromURI(data.getData());
                    File photo = new File(imgURI);

                    RequestBody photoBody = RequestBody.create(MediaType.parse("multipart/form-data"), photo);
                    image = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);

                    relativeLayout = findViewById(R.id.upload_product_img_layout);
                    relativeLayout.bringChildToFront(imageView);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show();
            }
        }
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

                        String dateM = String.valueOf(calendar.getTime()).substring(4, 7);
                        String monthM = new String();

                        switch (dateM) {
                            case "Jan":
                                monthM = "01";
                                break;
                            case "Feb":
                                monthM = "02";
                                break;
                            case "Mar":
                                monthM = "03";
                                break;
                            case "Apr":
                                monthM = "04";
                                break;
                            case "May":
                                monthM = "05";
                                break;
                            case "Jun":
                                monthM = "06";
                                break;
                            case "Jul":
                                monthM = "07";
                                break;
                            case "Aug":
                                monthM = "08";
                                break;
                            case "Sep":
                                monthM = "09";
                                break;
                            case "Oct":
                                monthM = "10";
                                break;
                            case "Nov":
                                monthM = "11";
                                break;
                            case "Dec":
                                monthM = "12";
                                break;
                        }

                        String string = String.valueOf(calendar.getTime()).substring(30, 34) + "/" + monthM + "/" + String.valueOf(calendar.getTime()).substring(8, 10);
                        dateleft.setText(string);
                        expDate = "";
                        expDate += string;
//                        Log.d("날짜", String.valueOf(calendar.getTime()).substring(8,10));
//                        Log.d("날짜", String.valueOf(calendar.getTime()).substring(4,7));
//                        Log.d("날짜", String.valueOf(calendar.getTime()).substring(30,34));
                        dateleft.setTextColor(Color.parseColor("#3A3A3A"));

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
                        expDate += " " + String.valueOf(calendar.getTime()).substring(11, 16) + ":00";
//                        Log.d("시간", String.valueOf(calendar.getTime()).substring(11, 16)+":00");
                        timeleft.setTextColor(Color.parseColor("#3A3A3A"));
                    }
                };
                new TimePickerDialog(view.getRootView().getContext(), onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

            }
        });
    }

    public void uploadProduct() {
        RelativeLayout btn = findViewById(R.id.upload_act_bottom_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText nameEt = findViewById(R.id.upload_product_name_et);
                String name = String.valueOf(nameEt.getText());
                RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);

                EditText commentEt = findViewById(R.id.upload_product_comments_et);
                String comment = String.valueOf(commentEt.getText());
                RequestBody commentBody = RequestBody.create(MediaType.parse("text/plain"), comment);

                EditText quantityEt = findViewById(R.id.upload_products_quantity);
                int quantity = 0;
                if (quantityEt.getText() != null)
                    quantity = Integer.parseInt(String.valueOf(quantityEt.getText()));

                EditText originPriceEt = findViewById(R.id.upload_products_price_origin);
                int originPrice = 0;
                if (originPriceEt.getText() != null)
                    originPrice = Integer.parseInt(String.valueOf(originPriceEt.getText()));

                EditText salePriceEt = findViewById(R.id.detail_act_products_price_sale);
                int salePrice = 0;
                if (salePriceEt.getText() != null)
                    salePrice = Integer.parseInt(String.valueOf(salePriceEt.getText()));

                RequestBody expDateBody = RequestBody.create(MediaType.parse("text/plain"), expDate);
                Log.d("date", expDate);

                Call<PostUploadProductRequest> call = networkService.postUploadProductRequest(SharedPreferenceController.getMyId(getApplicationContext()), nameBody, image, quantity, originPrice, commentBody, salePrice, expDateBody);
                call.enqueue(new Callback<PostUploadProductRequest>() {
                    @Override
                    public void onResponse(Call<PostUploadProductRequest> call, Response<PostUploadProductRequest> response) {
                        if (response.isSuccessful()) {
                            int status = response.body().status;
                            Log.d("status", String.valueOf(status));
                            if (status == 200) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PostUploadProductRequest> call, Throwable t) {
                        Log.e("Upload error:", t.getMessage());
                    }
                });
            }
        });
    }

    public String getRealPathFromURI(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public void requestReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 사용자에게 권한을 왜 허용해야 되는지에 대한 메시지 추가
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
            }
        } else
            showAlbum();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAlbum();
            } else
                finish();
        }
    }

    //Customized by 민승
    public void goToMainActivity() {
        RelativeLayout btn = findViewById(R.id.upload_act_back_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
