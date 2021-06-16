package com.example.integrate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2 extends AppCompatActivity {

    Button signoutBtn;
    CallbackManager callbackManager;
    ImageView imageView;
    TextView textview;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        signoutBtn=findViewById(R.id.signoutBtn);
        textview=findViewById(R.id.username);
        textView2=findViewById(R.id.gender);
        imageView=findViewById(R.id.profilepic);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager=CallbackManager.Factory.create();

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(MainActivity2.this,SignIn.class));
                finish();
            }
        });



        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }


    AccessTokenTracker t = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){
                textview.setText("");
                Toast.makeText(MainActivity2.this,"logout", Toast.LENGTH_SHORT).show();
            }
            else{
                loaduserProfile(currentAccessToken);
            }
        }
    };
    private void loaduserProfile(AccessToken newAccessToken){
        GraphRequest request= GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if(object!= null){
                    try{
                        String email= object.getString("email");
                        String id= object.getString("id");
                        String image_url= "https.//graph.facebook.com/"+id+"/picture?type=normal";
                        textview.setText(email);
                        Glide.with(MainActivity2.this).load(image_url).into(imageView);
                    }
                    catch (JSONException ex){
                        ex.printStackTrace();
                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }


}