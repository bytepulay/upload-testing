package asia.nainglintun.androidphpmysql.Fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import asia.nainglintun.androidphpmysql.Activities.Constants;
import asia.nainglintun.androidphpmysql.Activities.RequestHandler;
import asia.nainglintun.androidphpmysql.Activities.SharePrefManager;
import asia.nainglintun.androidphpmysql.R;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class FormFragment extends Fragment implements View.OnClickListener,DatePickerDialog.OnDateSetListener {

    private Button UploadBn;
    private EditText Name;
    private ImageView imageView;
    private final int IMG_REQUEST=1;
    private  Bitmap bitmap;
    private Button btnFormOneNext;
    private LinearLayout linearLayoutFormOne;

    private int day,month,year;
    private DatePickerDialog picker;
    private ImageView imgCalendar;
    private Calendar calendar;

    private EditText txtDueDate;


    public FormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form, container, false);
        UploadBn = view.findViewById(R.id.uploadBn);
        Name = view.findViewById(R.id.name);
        imageView = view.findViewById(R.id.imageView);

        btnFormOneNext = view.findViewById(R.id.formOneNext);
        linearLayoutFormOne = view.findViewById(R.id.detailForm);
        btnFormOneNext.setOnClickListener(this);

       imageView.setOnClickListener(this);
       UploadBn.setOnClickListener(this);

       calendar = Calendar.getInstance();
        imgCalendar = view.findViewById(R.id.imageViewCalendar);
        day = calendar.get(calendar.DAY_OF_MONTH);
        month = calendar.get(calendar.MONTH);
        year = calendar.get(calendar.YEAR);

        txtDueDate = view.findViewById(R.id.txtDate);


        imgCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),FormFragment.this,year,month,day).show();
                Toast.makeText(getContext(),"date",Toast.LENGTH_LONG).show();
            }
        });

//        .setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent gallery = new Intent();
//                gallery.setType("image/*");
//                gallery.setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(Intent.createChooser(gallery,"Select Picture"),PICK_IMAGE);
//            }
//        });

        if (!SharePrefManager.getInstance(getContext()).isLoggedIn()){

        }
        return view;
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode==PICK_IMAGE && requestCode==RESULT_OK){
//
//            imageUri = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imageUri);
//                profileImage.setImageBitmap(bitmap);
//
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView:
                selectImage();

                break;

            case R.id.uploadBn:
              uploadImage();

                break;

            case R.id.formOneNext:
                linearLayoutFormOne.setVisibility(View.GONE);
                break;



        }
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),path);
                imageView.setImageBitmap(bitmap);
                //imageView.setVisibility(View.VISIBLE);
                Name.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    private void uploadImage(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_Upload,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObjectOne =new JSONObject(response);
                            String Response = jsonObjectOne.getString("response");
                            Toast.makeText(getContext(),Response,Toast.LENGTH_LONG).show();
                            imageView.setImageResource(0);
                            Name.setText("");
                            Name.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name",Name.getText().toString().trim());
                params.put("image",imageToString(bitmap));
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        txtDueDate.setText( dayOfMonth +"/"+  (month+1) +"/"+ year );
    }
}
