package in.co.sdrc.mychat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static in.co.sdrc.mychat.MainActivity.signedInUser;
import static in.co.sdrc.mychat.R.id.activity_main;
import static in.co.sdrc.mychat.R.id.user_list_view;

public class User_List extends AppCompatActivity {

    ListView userList;
    ArrayList<String> users;
    Map<String, String> availableUsers;
    RelativeLayout activity_user_list;
    private static int SIGN_IN_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__list);
        userList = (ListView) findViewById(user_list_view);
        availableUsers = new HashMap<>();
        activity_user_list = (RelativeLayout) findViewById(R.id.user_list_layout);
        users = new ArrayList<>();


        //Check if not sign-in then navigate Signin page
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        } else {
            Snackbar.make(activity_user_list,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            //signedInUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            //Load content
            displayUserList();
        }
     //   FirebaseIDService idService = new FirebaseIDService();
       this.startService(new Intent(this, FirebaseIDService.class));


    }

    public void displayUserList(){
        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("User");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, User> td = (HashMap<String, User>) dataSnapshot.getValue();
                if (td != null) {
                    /*for (User user : td.values()) {
                        availableUsers.put(user.name, user.name);
                    }*/
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        User user = postSnapshot.getValue(User.class);
                        availableUsers.put(user.name, user.name);

                    }
                }
                users.clear();
                for (String uname : availableUsers.values()){
                    users.add(uname);
                }
                users.remove(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                if (!availableUsers.containsKey(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    usersRef.push().setValue(new User(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                }

                CustomListAdapter adapter = new CustomListAdapter(users, getApplicationContext());
                userList.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.activity_user__list)
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                Snackbar.make(activity_user_list,"Successfully signed in.Welcome!", Snackbar.LENGTH_SHORT).show();
                displayUserList();
            }
            else{
                Snackbar.make(activity_user_list,"We couldn't sign you in.Please try again later", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_user_list,"You have been signed out.", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true;
    }
}
