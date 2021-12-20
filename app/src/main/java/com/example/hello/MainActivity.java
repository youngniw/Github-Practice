package com.example.hello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    UserInfo userInfo;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        userInfo = new UserInfo(context, "테이비", "정자동");
        new DownloadFilesTask().execute("https://img1.daumcdn.net/thumb/R720x0.q80/?scode=mtistory2&fname=http%3A%2F%2Fcfile7.uf.tistory.com%2Fimage%2F24283C3858F778CA2EFABE");
    }

    private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // doInBackground 에서 받아온 total 값 사용 장소
            Log.i("확인용", userInfo.getUserName());

            result = resizeBitmap(result);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            userInfo.setUserProfile(byteArray);
            ImageView iv = findViewById(R.id.mainIv);
            iv.setImageBitmap(result);

            Intent intent = new Intent(context, SubActivity.class);
            Bundle bundle = new Bundle();
                bundle.putParcelable("userInfo", userInfo);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public Bitmap resizeBitmap(Bitmap original) {
        int resizeMin = 300;        //300으로 크기 조정
        Bitmap result;

        if (original.getHeight() > original.getWidth()) {
            double aspectRatio = (double) original.getHeight() / (double) original.getWidth();
            int targetHeight = (int) (resizeMin * aspectRatio);
            result = Bitmap.createScaledBitmap(original, resizeMin, targetHeight, false);
        }
        else {
            double aspectRatio = (double) original.getWidth() / (double) original.getHeight();
            int targetWidth = (int) (resizeMin * aspectRatio);
            result = Bitmap.createScaledBitmap(original, targetWidth, resizeMin, false);
        }

        if (result != original) {
            original.recycle();
        }
        return result;
    }

    public static class UserInfo implements Parcelable {
        private byte[] userProfile;
        private String userName = "";
        private String userTown = "";       //XX동

        UserInfo(Context context, String userName, String userTown) {
            Bitmap sendBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.user_img);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            userProfile = stream.toByteArray();

            this.userName = userName;
            this.userTown = userTown;
        }


        protected UserInfo(Parcel in) {
            userProfile = in.createByteArray();
            userName = in.readString();
            userTown = in.readString();
        }

        public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
            @Override
            public UserInfo createFromParcel(Parcel in) {
                return new UserInfo(in);
            }

            @Override
            public UserInfo[] newArray(int size) {
                return new UserInfo[size];
            }
        };

        public byte[] getUserProfile() { return userProfile; }
        //public Bitmap getUserProfile() { return userProfile; }
        public String getUserName() { return userName; }
        public String getUserTown() { return userTown; }

        public void setUserProfile(byte[] userProfile) { this.userProfile = userProfile; }
        //public void setUserProfile(Bitmap userProfile) { this.userProfile = userProfile; }
        public void setUserName(String userName) { this.userName = userName; }
        public void setUserTown(String userTown) { this.userTown = userTown; }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByteArray(userProfile);
            //dest.writeParcelable(userProfile, flags);
            dest.writeString(userName);
            dest.writeString(userTown);
        }

        /*
        public Bitmap getBitmapProfile() {      //TODO: Bitmap 가능한지 확인하기(스레드로 만들어야 함)
            URL imgUrl = null;
            HttpURLConnection connection = null;
            Bitmap retBitmap = null;

            try {
                imgUrl = new URL(userProfileUrl);
                connection = (HttpURLConnection) imgUrl.openConnection();
                connection.setDoInput(true);        //url로 input받는 flag 허용
                connection.connect();

                retBitmap = BitmapFactory.decodeStream(connection.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection!=null)
                    connection.disconnect();

                return retBitmap;
            }
        }

         */
    }
}