package com.st17.culturemap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersonActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    private final static int RC_SIGN_IN = 2;
    private FirebaseAuth mAuth;

    TextView textView;
    TextView textViewName;
    ImageView imageViewUser;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        textView = findViewById(R.id.textView_gmail);
        textViewName = findViewById(R.id.textView_name);
        imageViewUser = findViewById(R.id.imageView_user);
        if (user != null){
            textView.setText(user.getEmail());
            textViewName.setText(user.getDisplayName());

            Glide.with(this).load(user.getPhotoUrl()).into(imageViewUser);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_person);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        createRequest();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser!=null){
            checkAndCreateAcc(currentUser);
        }

        findViewById(R.id.auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user!=null){
                    logOut();
                }
                else{
                    signIn();
                }
            }
        });

        //bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.person);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        Intent intentMain = new Intent(PersonActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        return true;
                    case R.id.map:
                        Intent intentMap = new Intent(PersonActivity.this, MapActivity.class);
                        startActivity(intentMap);
                        return true;
                    case R.id.person:
                        return true;
                }
                return false;
            }
        });
    }

    private void createRequest() {
        // Configure Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(),PersonActivity.class);
        gso = null;
        startActivity(i);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(),PersonActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(PersonActivity.this, "Sorry auth failed.", Toast.LENGTH_SHORT).show();


                        }

                    }
                });
    }

    private void checkAndCreateAcc(FirebaseUser currentUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference dbUser = db.collection("users").document(currentUser.getUid());
        dbUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "tag";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");
                    } else {
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", currentUser.getDisplayName());
                        user.put("favourites", new ArrayList<String>());
                        user.put("wantVisit", new ArrayList<String>());
                        user.put("visited", new ArrayList<String>());


                        db.collection("users").document(currentUser.getUid())
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                    }
                                });
                        Log.d(TAG, "Document does not exist!");
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
    }

    public void OpenFavouritesClick(View view) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Toast.makeText(this, "Войдите в аккаунт", Toast.LENGTH_SHORT).show();
        }else{
            RVObjectsActivity.type = null;
            RVObjectsActivity.collection = null;
            RVObjectsActivity.title = "Любимые";
            RVObjectsActivity.lastPage = "person";
            RVObjectsActivity.userList = "favourites";
            Intent intent = new Intent(this, RVObjectsActivity.class);
            startActivity(intent);
        }
    }

    public void OpenWantVisitClick(View view) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Toast.makeText(this, "Войдите в аккаунт", Toast.LENGTH_SHORT).show();
        }else{
            RVObjectsActivity.type = null;
            RVObjectsActivity.collection = null;
            RVObjectsActivity.title = "Хочу посетить";
            RVObjectsActivity.lastPage = "person";
            RVObjectsActivity.userList = "wantVisit";
            Intent intent = new Intent(this, RVObjectsActivity.class);
            startActivity(intent);
        }
    }

    public void OpenVisitedClick(View view) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Toast.makeText(this, "Войдите в аккаунт", Toast.LENGTH_SHORT).show();
        }else{
            RVObjectsActivity.type = null;
            RVObjectsActivity.collection = null;
            RVObjectsActivity.title = "Посещёные";
            RVObjectsActivity.lastPage = "person";
            RVObjectsActivity.userList = "visited";
            Intent intent = new Intent(this, RVObjectsActivity.class);
            startActivity(intent);
        }
    }
}