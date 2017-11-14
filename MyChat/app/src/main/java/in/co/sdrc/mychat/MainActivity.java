package in.co.sdrc.mychat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    RelativeLayout activity_main;
    private FirebaseListAdapter<ChatMessage> adapter;

    //Add Emojicon
    EmojiconEditText emojiconEditText;
    ImageView emojiButton,submitButton, chooseImageButton;
    EmojIconActions emojIconActions;
    public static String signedInUser;
    String user;
    TextView title;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    Dialog chooseImageDialog, imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = getIntent().getStringExtra("user");
        title = (TextView) findViewById(R.id.title_user_name);
        title.setText(user);
        activity_main = (RelativeLayout)findViewById(R.id.activity_main);

        //Add Emoji
        emojiButton = (ImageView)findViewById(R.id.emoji_button);
        submitButton = (ImageView)findViewById(R.id.submit_button);
        emojiconEditText = (EmojiconEditText)findViewById(R.id.emojicon_edit_text);
        chooseImageButton = (ImageView) findViewById(R.id.choose_image_button);
        emojIconActions = new EmojIconActions(getApplicationContext(),activity_main,emojiButton,emojiconEditText);
        emojIconActions.ShowEmojicon();

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Chat").push().setValue(new ChatMessage(emojiconEditText.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail(),user, false, null));
                emojiconEditText.setText("");
                emojiconEditText.requestFocus();
            }
        });

        displayChatMessage();

        /*//Check if not sign-in then navigate Signin page
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        } else {
            Snackbar.make(activity_main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            signedInUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            //Load content
            displayChatMessage();
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //imageView.setImageBitmap(bitmap);
                chooseImageDialog = new Dialog(MainActivity.this);
                chooseImageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                chooseImageDialog.setContentView(R.layout.upload_image_dialog);

                ImageView thumnail = (ImageView) chooseImageDialog.findViewById(R.id.image_preview);
                thumnail.setImageBitmap(bitmap);

                Button upload = (Button) chooseImageDialog.findViewById(R.id.upload_button);
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uploadTask(bitmap);
                        chooseImageDialog.dismiss();
                    }
                });

                chooseImageDialog.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean uploadTask(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
        /*Byte[] byteObjects = new Byte[byteArray.length];

        int i=0;
                // Associating Byte array values with bytes. (byte[] to Byte[])
        for(byte b: byteArray)
            byteObjects[i++] = b;

        String upl = byteObjects.toString();*/

        FirebaseDatabase.getInstance().getReference().child("Chat").push().setValue(new ChatMessage(emojiconEditText.getText().toString(),
                FirebaseAuth.getInstance().getCurrentUser().getEmail(),user, true, encodedImage));
        emojiconEditText.setText("");
        emojiconEditText.requestFocus();
        return true;
    }
    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void displayChatMessage() {

        ListView listOfMessage = (ListView)findViewById(R.id.list_of_message);
        adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.list_item, FirebaseDatabase.getInstance().getReference().child("Chat"))
        {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                if ((model.getMessageUser().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) || model.getMessageUser().equals(user))
                         && (model.getMessageReceiver().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) || model.getMessageReceiver().equals(user)))
                {

                    if (model.getMessageUser().equals(user)) {
                        LinearLayout senderLayout, receiverLayout;

                        senderLayout = (LinearLayout) v.findViewById(R.id.sender_layout);
                        receiverLayout = (LinearLayout) v.findViewById(R.id.receiver_layout);
                        senderLayout.setVisibility(View.VISIBLE);
                        receiverLayout.setVisibility(View.GONE);
                        //senderLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGreen));

                        ImageView senderSentImage;
                        TextView messageText, messageUser, messageTime;
                        messageText = (EmojiconTextView) v.findViewById(R.id.message_text_sender);
                        messageTime = (TextView) v.findViewById(R.id.message_time_sender);

                        senderSentImage = (ImageView) v.findViewById(R.id.sender_sentImage);

                        //if sender sent an image
                        if (model.isImage()){
                            senderSentImage.setVisibility(View.VISIBLE);
                            messageText.setVisibility(View.GONE);

                            final Bitmap btmp = BitmapFactory.decodeByteArray(Base64.decode(model.getImageFile(), Base64.DEFAULT), 0, Base64.decode(model.getImageFile(), Base64.DEFAULT).length);

                            senderSentImage.setImageBitmap(btmp);

                            senderSentImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    viewImage(btmp);
                                }
                            });

                        } else {
                            //Get references to the views of list_item.xml
                            senderSentImage.setVisibility(View.GONE);
                            messageText.setVisibility(View.VISIBLE);
                            messageText.setText(model.getMessageText());
                        }

                        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
                    } else {
                        //Get references to the views of list_item.xml
                        LinearLayout senderLayout, receiverLayout;
                        TextView messageText, messageUser, messageTime;
                        ImageView receiverSentImage;
                        senderLayout = (LinearLayout) v.findViewById(R.id.sender_layout);
                        receiverLayout = (LinearLayout) v.findViewById(R.id.receiver_layout);

                        senderLayout.setVisibility(View.GONE);
                        receiverLayout.setVisibility(View.VISIBLE);
                        //receiverLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLightGreen));

                        messageText = (EmojiconTextView) v.findViewById(R.id.message_text_receiver);
                        messageTime = (TextView) v.findViewById(R.id.message_time_receiver);

                        receiverSentImage = (ImageView) v.findViewById(R.id.receiver_sentImage);

                        if (model.isImage()) {

                            receiverSentImage.setVisibility(View.VISIBLE);
                            messageText.setVisibility(View.GONE);

                            final Bitmap btmp = BitmapFactory.decodeByteArray(Base64.decode(model.getImageFile(), Base64.DEFAULT), 0, Base64.decode(model.getImageFile(), Base64.DEFAULT).length);

                            receiverSentImage.setImageBitmap(btmp);

                            receiverSentImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    viewImage(btmp);
                                }
                            });

                        } else {
                            receiverSentImage.setVisibility(View.GONE);
                            messageText.setVisibility(View.VISIBLE);

                            messageText.setText(model.getMessageText());
                        }
                        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
                    }
                }

            }
        };
        listOfMessage.setAdapter(adapter);
    }

    public void viewImage(Bitmap bitmap){
        imageView = new Dialog(MainActivity.this);
        imageView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imageView.setContentView(R.layout.view_image);

        ImageView fullImage = (ImageView) imageView.findViewById(R.id.image_view);
        fullImage.setImageBitmap(bitmap);

        imageView.show();
    }

    /*@Override
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
                    Snackbar.make(activity_main,"You have been signed out.", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }else if(item.getItemId() == R.id.list_of_users) {
            Intent intent = new Intent(MainActivity.this,User_List.class);
            startActivity(intent);
        }
        return true;
    }*/

}
