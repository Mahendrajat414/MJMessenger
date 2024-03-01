package com.example.mjmessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatWin extends AppCompatActivity {


    String reciverimg, reciverUid, reciverName, senderUID;

    CircleImageView profile;

    TextView reciverNName;
    CardView sendbtn;
    EditText textmsg;
    ImageView imagePicker;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;

    // ... (existing code)

    private static final int REQUEST_IMAGE_CAPTURE = 1;


    public static String senderImg;
    public static String reciverIImg;
    String senderRoom, reciverRoom;
    RecyclerView messangesAdpter;
    ArrayList<msgModelclass> messagessArrayList;
    messagesAdpter messagesAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        reciverName = getIntent().getStringExtra("nameeee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        messagessArrayList = new ArrayList<>();


        sendbtn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.textmsg);
        reciverNName = findViewById(R.id.recivername);
        profile = findViewById(R.id.profileimgg);
        messangesAdpter = findViewById(R.id.msgadpter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messangesAdpter.setLayoutManager(linearLayoutManager);
        messagesAdpter = new messagesAdpter(chatWin.this, messagessArrayList);
        messangesAdpter.setAdapter(messagesAdpter);


        Picasso.get().load(reciverimg).into(profile);
        reciverNName.setText("" + reciverName);

        senderUID = firebaseAuth.getUid();
        senderRoom = senderUID + reciverUid;
        reciverRoom = reciverUid + senderUID;



        DatabaseReference reference = database.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagessArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    msgModelclass messages = dataSnapshot.getValue(msgModelclass.class);
                    messagessArrayList.add(messages);
                }
                messagesAdpter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Toast.makeText(chatWin.this, "Error retrieving messages. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("profilepic").getValue().toString();
                reciverIImg = reciverimg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Toast.makeText(chatWin.this, "Error retrieving user information. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        // Your existing code...

        imagePicker = findViewById(R.id.imagePicker);

        // Set a click listener for the profile image
        imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Your existing code...



        // ... (initialize UI elements and Firebase references)

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Determine whether to send text or image message
                if (isTextMessage()) {
                    sendTextMessage();
                }
            }
        });
    }

    private boolean isTextMessage() {
        return !textmsg.getText().toString().trim().isEmpty();
    }

    private void sendTextMessage() {
        String message = textmsg.getText().toString();
        if (message.isEmpty()) {
            Toast.makeText(chatWin.this, "Enter The Message First", Toast.LENGTH_SHORT).show();
            return;
        }
        textmsg.setText("");

        // Create a text message object
        Date date = new Date();
        msgModelclass messageObject = new msgModelclass(message, senderUID, date.getTime());

        // Send text message to the sender's room
        sendMessageToRoom(senderRoom, messageObject);

        // Send text message to the receiver's room
        sendMessageToRoom(reciverRoom, messageObject);
    }

    private void openImagePicker() {
        // Create an Intent to pick an image from the gallery or take a photo using the camera
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        // Check if there's a camera app available, and if yes, include it as an option
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create a chooser intent that includes both the image picker and camera options
            Intent chooserIntent = Intent.createChooser(intent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

            // Start the chooser activity
            startActivityForResult(chooserIntent, REQUEST_IMAGE_PICKER);
        } else {
            // If no camera app is found, just open the image picker
            startActivityForResult(intent, REQUEST_IMAGE_PICKER);
        }
    }
    // Add this constant at the class level
    private static final int REQUEST_IMAGE_PICKER = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            // Handle the selected image URI
            Uri selectedImageUri = data.getData();
            sendImageMessage(selectedImageUri);
        }
    }


    private void sendImageMessage(Uri imageUri) {
        // Create an image message object
        Date date = new Date();
        msgModelclass imageMessage = new msgModelclass(senderUID, date.getTime(), imageUri.toString());

        // Upload the image to Firebase Storage and get the download URL
        uploadImageToStorage(imageUri, imageMessage);
    }

    private void uploadImageToStorage(Uri imageUri, msgModelclass imageMessage) {
        // Create a reference to the Firebase Storage location
        String imageName = "image_" + System.currentTimeMillis() + ".jpg"; // You can create a unique name for each image
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("chat_images").child(imageName);

        // Convert the image URI to a byte array
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            // Upload the byte array to Firebase Storage
            UploadTask uploadTask = storageReference.putBytes(data);

            // Listen for the success/failure of the upload
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Image uploaded successfully, get the download URL
                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    // Set the download URL to the image message
                                    Uri downloadUri = task.getResult();
                                    if (downloadUri != null) {
                                        imageMessage.setImageUrl(downloadUri.toString());

                                        // Send image message to the sender's room
                                        sendMessageToRoom(senderRoom, imageMessage);

                                        // Send image message to the receiver's room
                                        sendMessageToRoom(reciverRoom, imageMessage);
                                    } else {
                                        // Handle the error
                                        Toast.makeText(chatWin.this, "Error getting download URL.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Handle the error
                                    Toast.makeText(chatWin.this, "Error uploading image.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        // Handle the error
                        Toast.makeText(chatWin.this, "Error uploading image.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error
            Toast.makeText(chatWin.this, "Error processing image.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessageToRoom(String room, msgModelclass message) {
        DatabaseReference messagesRef = database.getReference().child("chats").child(room).child("messages");

        messagesRef.push().setValue(message)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Message sent successfully
                        } else {
                            // Handle the error
                            Toast.makeText(chatWin.this, "Error sending message. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // ... (existing code)
}

