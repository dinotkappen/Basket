package com.shopping.basket.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;
import com.shopping.basket.API.API;
import com.shopping.basket.API.APIClient;
import com.shopping.basket.Application;
import com.shopping.basket.Model.AddressListModel.AddressListData;
import com.shopping.basket.Model.CityListModel.CityData;
import com.shopping.basket.Model.CityListModel.CityListModel;
import com.shopping.basket.Model.LoginModel.LoginData;
import com.shopping.basket.Model.LoginModel.LoginModel;
import com.shopping.basket.Model.ProfileListModel.ProfileLisModel;
import com.shopping.basket.Model.ProfileUpdateModel.ProfileUpdateModel;
import com.shopping.basket.R;
import com.shopping.basket.Util.CompressImage;
import com.shopping.basket.Util.util;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.shopping.basket.Activity.MainActivity.backhide;
import static com.shopping.basket.Activity.MainActivity.getCart;
import static com.shopping.basket.Activity.MainActivity.rightBackHide;
import static com.shopping.basket.Activity.MainActivity.titlHide;
import static com.shopping.basket.Activity.MainActivity.updateHeader;
import static com.shopping.basket.Util.util.loadingAnimation;

public class ProfileFragment extends MainContanier {

    String cityId;
    Button submit;
    TextView txtCity;
    API apiInterface;
    String userToken;
    Spinner spinCity;
    File ImagePath=null;
    Bitmap photo = null;
    Uri selectedMediaUri;
    ImageView imgProfile,img_loder;
    int REQUEST_CODE_SHIPPING=102;
    TextView txtLocation;
    LinearLayout lenAddressSelect;
    ArrayList city = new ArrayList();
    public static Call<LoginModel> callLoadProfile;
    LoginModel details = new LoginModel();
    private static AVLoadingIndicatorView avi;
    List<CityData> listCity = new ArrayList<>();
    AddressListData addressdata = new AddressListData();
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_GALLERY_REQUEST_CODE = 101;
    EditText editNmae,editPhone,editBuilding,editZone,editStreeet,txtEmail;
    private static final int MainEXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(API.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.profile, container, false);
        if (Hawk.get("logStatus","0").equals("1")) {
            getCart();
            LoginData userDt;
            userDt = Hawk.get("user_details");
            userToken = userDt.getToken();
            Log.e("profile",userToken);
        }

        backhide();
        titlHide(getResources().getString(R.string.your_info));

        submit              = rootView.findViewById(R.id.bt_update);
        imgProfile          = rootView.findViewById(R.id.img_profile);
        editNmae            = rootView.findViewById(R.id.edit_name);
        editPhone           = rootView.findViewById(R.id.edit_phone);
        txtEmail            = rootView.findViewById(R.id.txt_email);
        txtLocation         = rootView.findViewById(R.id.txt_location);
        avi                 = rootView.findViewById(R.id.avi);
        lenAddressSelect    = rootView.findViewById(R.id.len_address_select);
        spinCity            = rootView.findViewById(R.id.spin_city);
        editBuilding        = rootView.findViewById(R.id.edit_build);
        editZone            = rootView.findViewById(R.id.edit_zone);
        editStreeet         = rootView.findViewById(R.id.edit_street);
        txtCity             = rootView.findViewById(R.id.edit_city);
        img_loder   = rootView.findViewById(R.id.img_loder);
        txtEmail.setFocusable(false);
        Glide.with(getActivity())
                .load(R.drawable.loading_bar_2)
                .placeholder(R.drawable.loading_bar_2)
                .into(img_loder);

        lenAddressSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.put("address_page","profile");
                Intent i = new Intent("Basket.address.select");
                startActivityForResult(i, REQUEST_CODE_SHIPPING);
//                Intent address = new Intent(getActivity(), AddressListActivity.class);
//                startActivity(address);
            }
        });
        ArrayAdapter aa = new ArrayAdapter(getActivity(),R.layout.spinner_text,city);
        aa.setDropDownViewResource(R.layout.spinner_text);
        spinCity.setAdapter(aa);

        spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("city",listCity.get(position).getCity());
//                if (flageFirstLoading!=0) {
                if (position!=0) {
                    txtCity.setText(listCity.get(position).getCity());
                    txtCity.setTextColor(getResources().getColor(R.color.black));
                    cityId = String.valueOf(listCity.get(position).getId());
                }
//                }
//                flageFirstLoading=1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editNmae.getText().toString().equals("")){
                    editNmae.setError(getString(R.string.vaild_name));
                }
               else if (editPhone.getText().toString().equals("")){
                    editPhone.setError(getString(R.string.enter_phone));
                }
                else if (editPhone.length()!=8){
                    editPhone.setError(getString(R.string.invalid_phone));
//                    Toast.makeText(getActivity(),getString(R.string.invalid_phone),Toast.LENGTH_SHORT).show();
                }

                else if (txtCity.getText().toString().equals("City")){
                    Toast.makeText(Application.getContext1(),getResources().getString(R.string.select_city),Toast.LENGTH_SHORT).show();
                }
                else if (editBuilding.getText().toString().equals("")){
                    editBuilding.setError(getResources().getString(R.string.valid_building));
                }
                else if (editZone.getText().toString().equals("")){
                    editZone.setError(getResources().getString(R.string.valid_zone));
                }
                else if (editStreeet.getText().toString().equals("")){
                    editStreeet.setError(getResources().getString(R.string.valid_city));
                }
                else {
                    updateProfile();
                }

            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean sdCardPermission = LightPermissionGrand(getContext());
//                LightPermissionGrand(getContext());
                if (sdCardPermission) {
                    //
                    proceedAfterPermission();
                }
            }
        });
        getProfile();
        return rootView;

    }

    public void citys(){
//        loadingAnimation(avi,true);
        img_loder.setVisibility(View.VISIBLE);
        Call<CityListModel> call = apiInterface.getCityList();
        call.enqueue(new Callback<CityListModel>() {
            @Override
            public void onResponse(Call<CityListModel> call, Response<CityListModel> response) {

                CityListModel status = new CityListModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                status = gson.fromJson(jsonElement, CityListModel.class);
                listCity.clear();
                if (status.getSuccess().getStatus()==200){
                    listCity = status.getSuccess().getData();


                    CityData zone_name= new CityData();

                    zone_name.setCity("City");
                    zone_name.setId(0);
                    listCity.add(0,zone_name);


                    for (int i = 0; i < listCity.size(); i++) {

                        city.add(listCity.get(i).getCity());
                        if (details.getSuccess().getData().getCity()!=null) {
                        if (details.getSuccess().getData().getCity().equals(String.valueOf(listCity.get(i).getId()))) {
                            txtCity.setText(listCity.get(i).getCity());
                            txtCity.setTextColor(getResources().getColor(R.color.black));
                        }
//                        zip_code.add(listCity.get(i).getZoneCode());

                    }


                }
                    ArrayAdapter aa = new ArrayAdapter(getActivity(), R.layout.spinner_text, city);
                    aa.setDropDownViewResource(R.layout.spinner_text);
                    spinCity.setAdapter(aa);
                }
                else {
                    Toast.makeText(Application.getContext1(),status.getSuccess().getMessage(),Toast.LENGTH_SHORT);
                }
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CityListModel> call, Throwable t) {
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proceedAfterPermission();
                } else {
                    //code for deny
//                    finish();
                }
                break;
        }
    }

    private void proceedAfterPermission() {

        // Toast.makeText(getContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //  boolean result=Utility.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, MY_CAMERA_REQUEST_CODE);

                } else if (items[item].equals("Choose from Library")) {

                    final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    galleryIntent.setType("image/*");
                    galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), MY_GALLERY_REQUEST_CODE);
                    // startActivityForResult(galleryIntent, MY_GALLERY_REQUEST_CODE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
//        createTempFile();
        if (resultCode == RESULT_OK) {
        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            photo = (Bitmap) result.getExtras().get("data");
            Uri tempUri = getImageUri(getContext(), photo);
            File finalFile = new File(getRealPathFromURI(tempUri));
            Glide.with(getContext()).load(finalFile.getAbsolutePath()).into(imgProfile);
            String path = CompressImage.compressImage(getContext(), finalFile.getAbsolutePath());
            ImagePath = new File(path);


            // CALL THIS METHOD TO GET THE ACTUAL PATH

        } else if (requestCode == REQUEST_CODE_SHIPPING) {

                addressdata = Hawk.get("latest_address");
                txtLocation.setText(addressdata.getShippingAddress());
//                txtBillingAddress.setText(addressBilling.getAlias() + "\n" + addressBilling.getAddress1() + "\n" + addressBilling.getPhone());
//                vieBilling.setBackgroundResource(R.drawable.category_fill_round);

            }

        else if (requestCode == MY_GALLERY_REQUEST_CODE){
//            selectedMediaUri = result.getData();
//            photo = (Bitmap) result.getExtras().get("data");

            selectedMediaUri = result.getData();
//            img_List.add(selectedMediaUri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                Uri tempUri = getImageUri(getContext(), bitmap);
                File finalFile = new File(getRealPathFromURI(tempUri));
                String path = CompressImage.compressImage(getContext(), finalFile.getAbsolutePath());
                Log.e("file_path",path);
                ImagePath = new File(path);
                Glide.with(getContext()).load(finalFile.getAbsolutePath()).into(imgProfile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public String getRealPathFromURI1(Uri contentUri)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void getProfile(){
//        loadingAnimation(avi,true);
        img_loder.setVisibility(View.VISIBLE);
        callLoadProfile = apiInterface.getProfile(userToken);
        callLoadProfile.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

               // response.body();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("banner",jsonElement.toString());
                details = gson.fromJson(jsonElement, LoginModel.class);

                if (details.getSuccess().getStatus()==200){

                    if (details.getSuccess().getData().getName()!=null) {
                        editNmae.setText(details.getSuccess().getData().getName().toString());
                        editPhone.setText(details.getSuccess().getData().getPhone());
                        txtEmail.setText(details.getSuccess().getData().getEmail());
                        editBuilding.setText(details.getSuccess().getData().getBuildingNo());
                        editStreeet.setText(details.getSuccess().getData().getStreet());
                        editZone.setText(details.getSuccess().getData().getZone());
//                    txtLocation.setText(details.getSuccess().getData().getAddress());
                        cityId = details.getSuccess().getData().getCity();
                        if (!details.getSuccess().getData().getPhoto().equals(""))
                            Glide.with(getActivity()).load(util.Imageurl + details.getSuccess().getData().getPhoto()).into(imgProfile);
                    }
                    else {
                        txtEmail.setFocusable(true);
                        txtEmail.setFocusableInTouchMode(true);
                        txtEmail.setEnabled(true);
//                        txtEmail.isFocusableInTouchMode = true;
                    }


                }
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);

                citys();

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
            }
        });
    }

    public  boolean LightPermissionGrand(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(context.getString(R.string.permission_nessary));
                    alertBuilder.setMessage(context.getString(R.string.sotrageis_improtant));
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {



                            requestPermissions(  new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, MainEXTERNAL_STORAGE_PERMISSION_CONSTANT);
                        }
                    });
                    android.app.AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, MainEXTERNAL_STORAGE_PERMISSION_CONSTANT);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void updateProfile(){
//        loadingAnimation(avi,true);
        img_loder.setVisibility(View.VISIBLE);
        MultipartBody.Part user_image=null;

        if (ImagePath != null) {
            // empty or doesn't exist
            user_image = APIClient.getRequest("photo",ImagePath);
        }
        RequestBody name= APIClient.plain(editNmae.getText().toString());
        RequestBody email= APIClient.plain(txtEmail.getText().toString());
        RequestBody phone= APIClient.plain(editPhone.getText().toString());
        RequestBody city= APIClient.plain(cityId);
        RequestBody buildNo= APIClient.plain(editBuilding.getText().toString());
        RequestBody zone= APIClient.plain(editZone.getText().toString());
        RequestBody street= APIClient.plain(editStreeet.getText().toString());

        Call<LoginModel> call = apiInterface.updateProfile(name,email,phone,city,buildNo,zone,street,user_image,userToken);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                LoginModel details = new LoginModel();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(response.body());
                Log.e("response",jsonElement.toString());
                details = gson.fromJson(jsonElement, LoginModel.class);

                if (details.getSuccess().getStatus()==200){
                    Toast.makeText(Application.getContext1(),details.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                    LoginData userDt = new LoginData();
                    userDt = Hawk.get("user_details");
                    userDt.setEmail(details.getSuccess().getData().getEmail());
                    userDt.setPhone(details.getSuccess().getData().getPhone());
                    userDt.setPhoto(details.getSuccess().getData().getPhoto());
                    userDt.setPhoto(details.getSuccess().getData().getPhoto());
                    Hawk.put("user_details",userDt);
                    updateHeader();
                }
                else {
                    Toast.makeText(Application.getContext1(),details.getSuccess().getMessage(),Toast.LENGTH_SHORT).show();
                }
//                loadingAnimation(avi,false);
                img_loder.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {

                img_loder.setVisibility(View.GONE);

            }
        });
    }


}
