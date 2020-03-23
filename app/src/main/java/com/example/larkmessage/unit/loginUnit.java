package com.example.larkmessage.unit;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.larkmessage.MainActivity;
import com.example.larkmessage.R;
import com.example.larkmessage.WelcomeActivity;
import com.example.larkmessage.entity.UserItem;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class loginUnit {
    private static final  int RC_SIGN_IN =123;
    private FirebaseAuth mAuth;
    public  loginUnit()
    {
        mAuth = FirebaseAuth.getInstance();
    }
    public void loginFcn(Activity activity)
    {
        List<AuthUI.IdpConfig> list= Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());
        activity.startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(list).build(),RC_SIGN_IN
        );
    }
    public void checkExist(final UserItem u, final Activity activity)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserItem userItem=null;

        DocumentReference docRef = db.collection("UserList").document(u.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(activity, "already register", Toast.LENGTH_SHORT).show();
                    } else {
                        createAccount(u,activity);
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });
    }
    public  void createAccount(final UserItem u, final Activity activity)
    {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(u.getEmail(), u.getPassword())
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            u.setUserId(user.getUid());
                            new userUnit().createAccountInfo(user,u,activity);
                        } else {
                            Toast.makeText(activity, "already registered or no internet",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void recheckUser()
    {
        final Boolean[] result = {null};
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // Get auth credentials from the user for re-authentication. The example below shows
    // email and password credentials but there are multiple possible providers,
    // such as GoogleAuthProvider or FacebookAuthProvider.
    AuthCredential credential = EmailAuthProvider
            .getCredential("user@example.com", "password1234");

    // Prompt the user to re-provide their sign-in credentials
    user.reauthenticate(credential)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "createUserWithEmail:success");
                        //FirebaseUser user = mAuth.getCurrentUser();

                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                        //        Toast.LENGTH_SHORT).show();
                        //updateUI(null);

                    }
                }
            });

    }
    public static int getRcSignIn() {
        return RC_SIGN_IN;
    }
    public void logoutFcn(final Activity activity)
    {
        AuthUI.getInstance()
                .signOut(activity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(activity, WelcomeActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                        } else {
                            Toast.makeText(activity, "sign out failed.", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                });

    }
    public void delete(Activity activity)
    {
        AuthUI.getInstance()
                .delete(activity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                            //        Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void themeAndLogo(Activity activity) {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();

        // [START auth_fui_theme_logo]
        activity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.nn1)      // Set logo drawable
                        .setTheme(R.style.AppTheme)      // Set theme
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_theme_logo]
    }
    public void signInWithEmailAndPassword(String email, String password , final Activity activity)
    {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            System.out.println("successful"+user.getDisplayName()+user.getEmail()+user.getPhoneNumber());

                            //intent.putExtra(USERCODE,auth);
                            readUserInfo(user,activity);
                        } else {
                            Toast.makeText(activity, "sign in failed.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // ...
                    }
                });
    }


    public void readUserInfo(FirebaseUser user , final Activity activity)
    {
        if(user==null ) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        UserItem userItem=null;

        DocumentReference docRef = db.collection("UserList").document(user.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String,Object> m = document.getData();
                        Intent intent = new Intent(activity, MainActivity.class);
                        try {
                            UserItem u = new UserItem.Builder(m.get("UserName").toString(),m.get("Email").toString())
                                    .phoneNumber(m.get("PhoneNumber").toString())
                                    .password(m.get("Password").toString())
                                    .userId(m.get("UserId").toString())
                                    .time(m.get("RegisterDate").toString())
                                    .bgColor(Integer.parseInt(m.get("BackgroundColor").toString()))
                                    .textSize(Integer.parseInt(m.get("TextSize").toString()))
                                    .textStyle(Integer.parseInt(m.get("TextStyle").toString()))
                                    .Build();
                            intent.putExtra("userInfo", (Serializable) u);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        activity.startActivity(intent);
                        activity.finish();
                    } else {
                        Log.d("error", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });
    }

}
