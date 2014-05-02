package com.moto.user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.loopj.android.http.RequestParams;
import com.moto.constant.Constant;
import com.moto.constant.DialogMethod;
import com.moto.constant.ImageMethod;
import com.moto.img.MyImageView;
import com.moto.main.R;
import com.moto.model.MeNetworkModel;
import com.moto.model.NetWorkModelListener;
import com.moto.myactivity.MyActivity;
import com.moto.mymap.MyMapApplication;
import com.moto.qiniu.img.Image;
import com.moto.select_morephoto.AlbumActivity;
import com.moto.select_morephoto.ImageManager2;
import com.moto.toast.ToastClass;
import com.moto.tryimage.FileUtils;
import com.moto.tryimage.PhotoUtils;
import com.moto.utils.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class User_EditUserMassage extends MyActivity implements OnClickListener,NetWorkModelListener{
	private ImageView user_edit_return;
	private SharedPreferences mshared;
	private String token;
	private MyImageView  user_img;
	private ImageView user_add_Love_car;
	private LinearLayout moto_photo;
	private TextView user_edit_success;
	private TextView user_nickname_edit;
	private EditText user_signature_edit;
	private EditText user_models_edit;
	private EditText user_displacement_edit;
	private RelativeLayout edit_user_img;
	private DisplayImageOptions options;
	private DisplayImageOptions Originaloptions;
	private ArrayList<String> dataList = new ArrayList<String>();
	private ArrayList<String> ownphotoMessage = new ArrayList<String>();
	private ArrayList<String> carList = new ArrayList<String>();
    //	public ProgressDialog progressDialog;
	
	private Handler handler;
	private String nickname = "";
	private RadioGroup radioGroup;
	private RadioButton radioButtonMale;
	private RadioButton radioButtonFemale;
	private String gender = "1";
	private String mototype = "";
	private String outcapacity = "";
	private String photoString = "";
	private boolean photoChange = false;
	private boolean lovecarChange = false;
	private ArrayList<Image> lovecarImage = new ArrayList<Image>();
	private String profile = "";
	private Image photofiles;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_edit_massage);
		intent = getIntent();
		init();
		SetMessage();
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId)
				{
					case R.id.user_edit_radio_male:
						gender = "1";
						break;
					case R.id.user_edit_radio_female:
						gender = "2";
						break;
				}
			}
		});
		handler = new Handler(){
            
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				switch(msg.what)
				{
                        //获取成功
                    case Constant.MSG_SUCCESS:
                        ToastClass.SetImageToast(User_EditUserMassage.this,"成功修改资料");
                        setResult(302);
                        User_EditUserMassage.this.finish();
                        break;
				}
				super.handleMessage(msg);
			}
			
		};
	}
	private void init() {
		// TODO Auto-generated method stub
		mshared = getSharedPreferences("usermessage", 0);
		token = mshared.getString("token", "");
		options = ImageMethod.GetOptions();
		Originaloptions = ImageMethod.GetOriginalOptions();
		moto_photo = (LinearLayout)findViewById(R.id.user_edit_moto_photo);
		user_edit_return = (ImageView)findViewById(R.id.user_edit_return);
		user_img = (MyImageView)findViewById(R.id.user_img);
		user_add_Love_car = (ImageView)findViewById(R.id.user_add_Love_car);
		user_nickname_edit = (TextView)findViewById(R.id.user_nickname_edit);
		user_signature_edit = (EditText)findViewById(R.id.user_signature_edit);
		user_models_edit = (EditText)findViewById(R.id.user_models_edit);
		user_displacement_edit = (EditText)findViewById(R.id.user_displacement_edit);
		user_edit_success = (TextView)findViewById(R.id.user_edit_success);
		edit_user_img = (RelativeLayout)findViewById(R.id.edit_user_img);
		radioGroup = (RadioGroup)findViewById(R.id.user_edit_gender);
		radioButtonFemale = (RadioButton)findViewById(R.id.user_edit_radio_female);
		radioButtonMale = (RadioButton)findViewById(R.id.user_edit_radio_male);
		edit_user_img.setOnClickListener(this);
		user_edit_success.setOnClickListener(this);
		user_edit_return.setOnClickListener(this);
		user_add_Love_car.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == user_edit_return)
		{
			User_EditUserMassage.this.finish();
		}
		if(v == user_edit_success)
		{
			nickname = user_nickname_edit.getText().toString();
			mototype = user_models_edit.getText().toString();
			outcapacity = user_displacement_edit.getText().toString();
			profile = user_signature_edit.getText().toString();
			if(nickname.replaceAll(" ", "").equals(""))
			{
                DialogMethod.dialogShow(User_EditUserMassage.this,"请输入昵称!");
			}
			else if(profile.replaceAll(" ", "").equals(""))
			{
                DialogMethod.dialogShow(User_EditUserMassage.this,"请填写个性签名!");
			}
			else if(mototype.replaceAll(" ", "").equals(""))
			{
                DialogMethod.dialogShow(User_EditUserMassage.this,"请输入爱骑车型!");
			}
			else if(outcapacity.replaceAll(" ", "").equals(""))
			{
                DialogMethod.dialogShow(User_EditUserMassage.this,"请输入爱骑排量!");
			}
			else {
				try {
					new AsyncTask<Integer, String, Integer>(){
                        
                        @Override
                        protected Integer doInBackground(Integer... params) {
                            // TODO Auto-generated method stub
                            try {
                                SetAsyMessageData();
                                Thread.sleep(40 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
							return null;
						}
						@Override
						protected void onCancelled() {
							DialogMethod.stopProgressDialog();
							super.onCancelled();
                        }
                        @Override
                        protected void onPreExecute() {
							DialogMethod.startProgressDialog(User_EditUserMassage.this,"正在提交");
                            //		progressDialog = new ProgressDialog(User_EditUserMassage.this);
                            //		progressDialog.setMessage("正在提交,请稍候...");
                            //		progressDialog.show();
                        }
                        
                        @Override
                        protected void onPostExecute(Integer result) {
                            DialogMethod.stopProgressDialog();
                            //			DialogMethod.dialogShow(User_EditUserMassage.this, "修改失败!");
                        }
                        
					}.execute();
				} catch (Exception e) {
					// TODO: handle exception
					
				}
			}
			
		}
		if(v == edit_user_img)
		{
            //			intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //			startActivityForResult(intent, 2);
			PhotoUtils.selectPhoto(User_EditUserMassage.this);
		}
		if(v == user_add_Love_car)
		{
			Intent intent = new Intent(User_EditUserMassage.this,
                                       AlbumActivity.class);
			Bundle bundle = new Bundle();
			// intent.putArrayListExtra("dataList", dataList);
			bundle.putStringArrayList("dataList",
                                      StringUtils.getIntentArrayList(dataList));
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
		}
	}
	private void SetMessage(){
		if(intent.getStringExtra("is").equals("1"))
		{
			ownphotoMessage = intent.getStringArrayListExtra("motophoto");
			int num = ownphotoMessage.size();
			if(num > 0)
			{
				for(int i = 0; i < num; i++)
				{
					String imageUrl = imgPath+ownphotoMessage.get(i)+"?imageView2/1/w/100/h/100";
					carList.add(imageUrl) ;
				}
				SetMotoPhoto();
			}
			photoString = intent.getStringExtra("avatar");
			MyMapApplication.imageLoader.displayImage(imgPath+photoString+"?imageView2/1/w/50/h/50",  user_img,options,null);
			user_nickname_edit.setText(intent.getStringExtra("username"));
			if(intent.getStringExtra("gender").equals("1"))
				radioButtonMale.setChecked(true);
			else
				radioButtonFemale.setChecked(true);
			user_models_edit.setText(intent.getStringExtra("mototype"));
			user_displacement_edit.setText(intent.getStringExtra("outputvolume"));
			user_signature_edit.setText(intent.getStringExtra("profile"));
		}
	}
	
	private void SetMotoPhoto()
	{
		int num1 = carList.size();
		int num2 = dataList.size();
		if(num2 > 0)
		{
			moto_photo.setVisibility(View.VISIBLE);
			moto_photo.removeAllViews();
			for (int i = 0; i < num2; i++) {
				View mLayout;
				ImageView mPhoto;
				mLayout = LayoutInflater.from(User_EditUserMassage.this).inflate(
                                                                                 R.layout.user_moto_photo_image_item, null);
				mPhoto = (ImageView) mLayout
                .findViewById(R.id.user_moto_photo_img);
				moto_photo.addView(mLayout);
				moto_photo.invalidate();
				ImageManager2.from(this).displayImage(mPhoto, dataList.get(i),R.drawable.default_add_img);
			}
		}
		else if (num1 > 0) {
			moto_photo.setVisibility(View.VISIBLE);
			moto_photo.removeAllViews();
			for (int i = 0; i < num1; i++) {
				View mLayout;
				ImageView mPhoto;
				mLayout = LayoutInflater.from(User_EditUserMassage.this).inflate(
                                                                                 R.layout.user_moto_photo_image_item, null);
				mPhoto = (ImageView) mLayout
                .findViewById(R.id.user_moto_photo_img);
				moto_photo.addView(mLayout);
				moto_photo.invalidate();
				MyMapApplication.imageLoader.displayImage(carList.get(i),  mPhoto,Originaloptions,null);
				
			}
		} else {
			moto_photo.setVisibility(View.GONE);
		}
	}
	
	private void SetAsyMessageData() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("token", token);
		param.put("username", nickname);
		param.put("gender", gender);
		param.put("mototype", mototype);
		param.put("outcapacity", outcapacity);
		param.put("profile", profile);
		MeNetworkModel meNetworkModel = new MeNetworkModel(this, this);
		try {
			if (photoChange && !lovecarChange) {
				photofiles = new Image(photoString, "file");
				meNetworkModel.updateProfile(param, photofiles, new JSONArray(ownphotoMessage).toString(), "motophoto", "updateprofile");
			}
			else if(!photoChange && !lovecarChange){
				meNetworkModel.updateProfile(param, photoString, new JSONArray(ownphotoMessage).toString(), "updateprofile");
			}
			else if (lovecarChange && !photoChange) {
				lovecarImage.clear();
				int num1 = dataList.size();
				for (int i = 0; i < num1; i++) {
					lovecarImage.add(new Image(
                                               dataList.get(i), "file"));
				}
				meNetworkModel.updateProfile(param, photoString, lovecarImage, "motophoto", "updateprofile");
			}
			else {
				photofiles = new Image(photoString, "file");
				lovecarImage.clear();
				int num1 = dataList.size();
				for (int i = 0; i < num1; i++) {
					lovecarImage.add(new Image(
                                               dataList.get(i), "file"));
				}
				meNetworkModel.updateProfile(param, photofiles, lovecarImage, "motophoto", "updateprofile");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//调用相册返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 0) {
            
            if (data == null) {
                return;
            }
            if (resultCode == RESULT_OK) {
                if (data.getData() == null) {
                    return;
                }
                if (!FileUtils.isSdcardExist()) {
                    ToastClass.SetToast(User_EditUserMassage.this, "SD卡不可用");
                    return;
                }
                Uri uri = data.getData();
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                if (cursor != null) {
                    int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                        String path = cursor.getString(column_index);
                        Bitmap bitmap = PhotoUtils.getBitmapFromFile(path);
                        if (PhotoUtils.bitmapIsLarge(bitmap)) {
                            PhotoUtils.cropPhoto(this, this, path);
                        } else {
                            if (path != null) {
                                photoString = path;
                                photoChange = true;
                                Bitmap b = BitmapFactory.decodeFile(path);
                                user_img.setImageDrawable(new BitmapDrawable(b));
                            }
                        }
                    }
                }
            }
        }
        else if(requestCode == 2 && resultCode == RESULT_OK){
            String path = data.getStringExtra("path");
            if(path != null)
            {
                photoString = path;
                photoChange = true;
                Bitmap b = BitmapFactory.decodeFile(path);
                user_img.setImageDrawable(new BitmapDrawable(b));
            }
        }
        else if(requestCode == 3 && resultCode == RESULT_OK)
        {
            if (data != null) {
                final String path = data.getStringExtra("path");
                photoString = path;
                photoChange = true;
                Bitmap b = BitmapFactory.decodeFile(path);
                user_img.setImageDrawable(new BitmapDrawable(b)); 
            }
        }
        else if(requestCode == 1){
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            Bundle bundle = data.getExtras();
            ArrayList<String> tDataList = (ArrayList<String>)bundle.getSerializable("dataList");
            dataList.clear();
            dataList.addAll(tDataList);
            lovecarChange = true;
            SetMotoPhoto();
        }
    }
	@Override
	public void handleNetworkDataWithSuccess(JSONObject JSONObject)
    throws JSONException {
		// TODO Auto-generated method stub		
        DialogMethod.stopProgressDialog();
        //				progressDialog.dismiss();
        handler.obtainMessage(Constant.MSG_SUCCESS)
        .sendToTarget();
		
	}
	@Override
	public void handleNetworkDataWithFail(JSONObject jsonObject)
    throws JSONException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void handleNetworkDataWithUpdate(float progress)
    throws JSONException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void handleNetworkDataGetFail(String message) throws JSONException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void handleNetworkDataStart() throws JSONException {
		// TODO Auto-generated method stub
		
	}
    
}