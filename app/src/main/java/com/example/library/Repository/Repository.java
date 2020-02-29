package com.example.library.Repository;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.example.library.Models.Course;
import com.example.library.Models.File;
import com.example.library.Models.Level;
import com.example.library.UploadFileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class Repository {
    Level level = UploadFileFragment.level;
    Course course = UploadFileFragment.subject;
    File file = UploadFileFragment.file;

    List<Course> courses;
    List<File> files;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    public Repository() {
    }


    public String uploadFile(Uri fileUri) {
        StorageReference storageReference = storage.getReference();

        Log.d("THE level", level.getLevelname());
        Log.d("THE course", course.getCourseName());
        Log.d("THE file", file.getFileName());
        storageReference.child(level.getLevelname()).child(course.getCourseName()).child(file.getFileName()).putFile(fileUri);
        StorageReference downloadref = storageReference.child(level.getLevelname()).child(course.getCourseName()).child(file.getFileName());
        String downloadUrl = downloadref.getDownloadUrl().toString();
        return downloadUrl;

    }

    public void uploadLevel(Level level) {
        firestore.collection("Uploads").document(level.getLevelname()).collection(course.getCourseName()).document(file.getFileName()).set(file).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });



    }

    public List<Course> getCourses(Level level) {
        firestore.collection("Uploads")
                .document(level.getLevelname())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            courses = new ArrayList<>();
                            Course course = task.getResult().toObject(Course.class);
                            courses.add(course);
                        }
                    }

                });

        return courses;

    }

    public List<File> getFiles(Course course) {
        files = new ArrayList<>();
        files = course.getFiles();

        return files;

    }

    public String getFileUrl(File file) {

        String url = file.getFileUrl();
        return url;
    }


}
