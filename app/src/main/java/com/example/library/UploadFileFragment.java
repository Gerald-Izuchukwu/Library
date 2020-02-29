package com.example.library;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.Models.Course;
import com.example.library.Models.File;
import com.example.library.Models.Level;
import com.example.library.Repository.Repository;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class UploadFileFragment extends Fragment {
    Spinner levelspinner;
    EditText courseName;
    TextView fileName;
    Button upload, uploadFile;
    int FILE_SELECT_CODE = 0;
    Uri uri;
    FileViewModel fileViewModel;
    public static Course subject;
    public static Level level;
    public static File file;
    String name;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootview = inflater.inflate(R.layout.fragment_upload_file, container, false);

        levelspinner = rootview.findViewById(R.id.spinnerLevel);
        courseName = rootview.findViewById(R.id.courseET);
        fileName = rootview.findViewById(R.id.filenameTV);
        upload = rootview.findViewById(R.id.uploadBut);
        uploadFile = rootview.findViewById(R.id.uploadFileBut);
        file = new File();
        subject = new Course();
        level = new Level();
        fileViewModel = new FileViewModel();




        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Year one");
        spinnerArray.add("Year two");
        spinnerArray.add("Year three");
        spinnerArray.add("Year four");
        spinnerArray.add("Year five");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        levelspinner.setAdapter(adapter);
        final String selectedLevel = levelspinner.getSelectedItem().toString();
        final String course = courseName.getText().toString();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFile();
            }
        });

        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("THE URI", uri.toString());
                Log.d("THE Name", name);


                file.setFileName(name);


                subject.setCourseName(courseName.getText().toString());


                level.setLevel(selectedLevel);


                String url = fileViewModel.uploadFile(uri);
                file.setFileUrl(url);
                subject.setFile(file);
                level.setSubject(subject);


                fileViewModel.uploadLevel(level);


            }
        });


        return rootview;


    }


    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {

                    // Get the Uri of the selected file
                    uri = data.getData();
                    name = getFilename(uri);
                    fileName.setText(name);
                    Log.d("THE URI", uri.toString());


                }
                break;
        }
    }


    public String getFilename(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;

    }
}
