package com.fyproject.shrey.ewrittenappclient.fragments.WAppDisplayFragments;


import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.activity.ViewApplicaion;
import com.fyproject.shrey.ewrittenappclient.model.WAppInfrastructure;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfrastructureDisplayFragment extends Fragment {

    private TextView tvToName;
    private TextView tvFromName;
    private TextView tvFromInfo;
    private TextView tvLocationOfProb;
    private TextView tvProblemType;
    private TextView tvDescription;
    private TextView tvStatus;
    private TextView tvResponse;

    private Button btnFile;
    private Button btnAccept; //Faculty
    private Button btnReject; //Faculty

    private DatabaseReference fbRoot;
    private FirebaseStorage fbstorage;
    private StorageReference storageRef;
    private URI fileUri;

    private WAppInfrastructure infrastructureApp;

    public String STUDENT;
    public String FACULTY;
    final String ACCEPT = "accepted";
    final String REJECT = "rejected";
    final String TAG = "TAG";

    private void initialization(View v) {
        tvToName = (TextView) v.findViewById(R.id.tvToName);
        tvFromName = (TextView) v.findViewById(R.id.tvFromName);
        tvFromInfo = (TextView) v.findViewById(R.id.tvFromInfo);
        tvLocationOfProb = (TextView) v.findViewById(R.id.tvLocationOfProb);
        tvProblemType = (TextView) v.findViewById(R.id.tvProblemType);
        tvDescription = (TextView) v.findViewById(R.id.tvDescription);
        tvStatus = (TextView) v.findViewById(R.id.tvStatus);
        btnFile = (Button) v.findViewById(R.id.btnFile);
        btnAccept = (Button) v.findViewById(R.id.btnAccept);
        btnReject = (Button) v.findViewById(R.id.btnReject);
        tvResponse = (TextView) v.findViewById(R.id.tvResponse);

        STUDENT = getString(R.string.student);
        FACULTY = getString(R.string.faculty);

        infrastructureApp = (WAppInfrastructure) ViewApplicaion.info;
        fbRoot = FirebaseDatabase.getInstance().getReference();
        fbstorage = FirebaseStorage.getInstance();
        storageRef = fbstorage.getReference();

        tvToName.append(infrastructureApp.toName);
        tvFromName.setText(infrastructureApp.fromName);
        tvFromInfo.setText(infrastructureApp.classInfo);
        tvLocationOfProb.append(infrastructureApp.location);
        tvProblemType.append(infrastructureApp.issueType);
        tvDescription.append(infrastructureApp.message);
        tvStatus.setText(infrastructureApp.status.toUpperCase());

        //check user type and set UI accordingly
        if (ViewApplicaion.USERTYPE.equals(STUDENT)) {
            setUpStudentGUI(v);
        } else if (ViewApplicaion.USERTYPE.equals(FACULTY)) {
            setUpFacultyGUI();
        }
    }

    private void setUpStudentGUI(View v) {
        btnAccept.setVisibility(View.GONE);
        btnReject.setVisibility(View.GONE);
    }

    private void setUpFacultyGUI() {  //FACULTY Display code
        tvToName.setVisibility(View.GONE);
        //Faculty responded
        if(infrastructureApp.status.equals(ACCEPT) || infrastructureApp.status.equals(REJECT)){
            btnReject.setVisibility(View.GONE);
            btnAccept.setVisibility(View.GONE);
        } else {
            //Faculty Not yet responded
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateStatus(ACCEPT);
                }
            });

            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    View dialogView =  LayoutInflater.from(getContext()).inflate(R.layout.dialog_response,null,false);
                    final EditText etResponseInput = (EditText) dialogView.findViewById(R.id.etResponseInput);

                    builder.setTitle("Confirm reject?");
                    builder.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if( !TextUtils.isEmpty(etResponseInput.getText()) )
                                infrastructureApp.response = etResponseInput.getText().toString();

                            UpdateStatus(REJECT);
                        }
                    });
                    builder.setNegativeButton("Cancel",null);
                    builder.setView(dialogView);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }

    public InfrastructureDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_infrastructure, container, false);
        initialization(view);

        downloadAttachment();

        btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infrastructureApp.attachedFile != "null") {
                    //**** @Shahrukh TO-DO: CHECK THIS FUNCTION AND USE IT as per requirement
                    viewFile(fileUri);
                } else {
                    Toast.makeText(getActivity(), "No file attached", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void downloadAttachment() {
        if (infrastructureApp.attachedFile != "null") {
            //Code to download file
            File rootPath = new File(Environment.getExternalStorageDirectory(), "EWAPP");
            if (!rootPath.exists()) {
                rootPath.mkdirs();
            }
            final File localFile = new File(rootPath, infrastructureApp.attachedFile);
            fileUri = localFile.toURI();
            StorageReference ref = storageRef.child(infrastructureApp.attachedFile);
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.e("firebase ", ";local tem file created  created " + localFile.toString());
                    //  updateDb(timestamp,localFile.toString(),position);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("firebase ", ";local tem file not created  created " + exception.toString());
                }
            });
            //Toast.makeText(getActivity(), localFile.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    private void viewFile(URI fileUri) {
        File file = new File(fileUri.toString());
        String fileExt = FilenameUtils.getExtension(file.getPath());
        Log.d(TAG, "viewPdf: file path = " + file.getPath() + " | file extension = " + fileExt);

        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW);
        if (fileExt.equals("")) {
            intent.setDataAndType(Uri.parse(fileUri.toString()), "image/jpeg");
        } else {
            intent.setDataAndType(Uri.parse(fileUri.toString()), appCallType(fileExt));
        }

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // No application to view, ask to download one
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("No Application Found");
            builder.setMessage("Download one from Android Market?");
            builder.setPositiveButton("Yes, Please",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent
                                    .setData(Uri
                                            .parse("market://details?id=com.adobe.reader"));
                            startActivity(marketIntent);
                        }
                    });
            builder.setNegativeButton("No, Thanks", null);
            builder.create().show();
        }
    }

    private String appCallType(String extension) {
        if (extension.equals("doc") || extension.equals("docx")) {
            return "application/msword";
        } else if (extension.equals("pdf")) {
            // PDF file
            return "application/pdf";
        } else if (extension.equals("ppt") || extension.equals("pptx")) {
            // Powerpoint file
            return "application/vnd.ms-powerpoint";
        } else if (extension.equals("xls") || extension.equals("xlsx")) {
            // Excel file
            return "application/vnd.ms-excel";
        } else if (extension.equals("zip") || extension.equals("rar")) {
            // WAV audio file
            return "application/x-wav";
        } else if (extension.equals("rtf")) {
            // RTF file
            return "application/rtf";
        } else if (extension.equals("wav") || extension.equals("mp3")) {
            // WAV audio file
            return "audio/x-wav";
        } else if (extension.equals("gif")) {
            // GIF file
            return "image/gif";
        } else if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")) {
            // JPG file
            return "image/jpeg";
        } else if (extension.equals("txt")) {
            // Text file
            return "text/plain";
        } else if (extension.equals("3gp") || extension.equals("mpg") || extension.equals("mpeg") || extension.equals("mpe") ||
                extension.equals("mp4") || extension.equals("avi")) {
            // Video files
            return "video/*";
        } else {
            return "*/*";
        }

    }

    public void UpdateStatus(String status){
        String wAppPath1="/applicationsNode/"+infrastructureApp.toUid+"/"+infrastructureApp.getwAppId();
        String wAppPath2="/applicationsNode/"+infrastructureApp.fromUid+"/"+infrastructureApp.getwAppId();

        Map<String, Object> updateStatus = new HashMap<String, Object>();
        infrastructureApp.setStatus(status);
        updateStatus.put(wAppPath1+"/", infrastructureApp);
        updateStatus.put(wAppPath2+"/", infrastructureApp);

        fbRoot.updateChildren(updateStatus, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference databaseReference) {
                if (error == null){ //Success
                    Toast.makeText(getContext(), "application sent!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    return;
                }
                Log.d(TAG, "updateStatus: ERRoR: " + error); //write failure
                Toast.makeText(getContext(), "failed to update status", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
