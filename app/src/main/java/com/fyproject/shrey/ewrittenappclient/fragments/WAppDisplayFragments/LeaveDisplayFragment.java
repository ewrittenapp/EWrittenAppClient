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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fyproject.shrey.ewrittenappclient.R;
import com.fyproject.shrey.ewrittenappclient.activity.NewApplication;
import com.fyproject.shrey.ewrittenappclient.activity.ViewApplicaion;
import com.fyproject.shrey.ewrittenappclient.model.WAppLeave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.net.URI;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;

import static com.fyproject.shrey.ewrittenappclient.activity.ViewApplicaion.info;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeaveDisplayFragment extends Fragment {

    private TextView tvToName;
    private TextView tvFromName;
    private TextView tvFromInfo;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private TextView tvMessage;
    private TextView tvStatus;
    private TextView tvResponse;

    private Button btnFile;
    private Button btnAccept; //Faculty
    private Button btnReject; //Faculty

    private DatabaseReference fbRoot;
    private StorageReference storageRef;
    private FirebaseStorage fbstorage;

    private URI fileUri;

    private WAppLeave leaveApp;

    public String STUDENT;
    public String FACULTY;
    private String CurrentUserID;
    final String TAG = "TAG";


    private void initialization(View v) {
        tvToName = (TextView) v.findViewById(R.id.tvToName);
        tvFromName = (TextView) v.findViewById(R.id.tvFromName);
        tvFromInfo = (TextView) v.findViewById(R.id.tvFromInfo);
        tvStartDate = (TextView) v.findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) v.findViewById(R.id.tvEndDate);
        tvMessage = (TextView) v.findViewById(R.id.tvMessage);
        tvStatus = (TextView) v.findViewById(R.id.tvStatus);
        btnFile = (Button) v.findViewById(R.id.btnFile);
        btnAccept = (Button) v.findViewById(R.id.btnAccept);
        btnReject = (Button) v.findViewById(R.id.btnReject);
        tvResponse = (TextView) v.findViewById(R.id.tvResponse);

        STUDENT = getString(R.string.student);
        FACULTY = getString(R.string.faculty);
        leaveApp = (WAppLeave) ViewApplicaion.info;
        fbRoot = FirebaseDatabase.getInstance().getReference();

        fbstorage = FirebaseStorage.getInstance();

        storageRef = fbstorage.getReference();
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

    private void setUpFacultyGUI() {
        tvToName.setVisibility(View.GONE);
    }

    public LeaveDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_leave, container, false);
        initialization(view);
        downloadAttachment();
        btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leaveApp.attachedFile != "null") {
                    //**** @Shahrukh TO-DO: CHECK THIS FUNCTION AND USE IT as per requirement
                    viewFile(fileUri);
                } else {
                    Toast.makeText(getActivity(), "No file attached", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (ViewApplicaion.USERTYPE.equals(STUDENT)) {
            //STUDENT Display wApp code

            tvToName.append(leaveApp.toName);
            tvFromName.setText(leaveApp.fromName);
            tvFromInfo.setText(leaveApp.classInfo);
            tvStartDate.append(leaveApp.startDate);
            tvEndDate.append(leaveApp.endDate);
            tvMessage.setText(leaveApp.message);
            tvStatus.setText(leaveApp.status.toUpperCase());


        } else if (ViewApplicaion.USERTYPE.equals(FACULTY)) {
            //FACULTY Display wApp code


            tvToName.append(leaveApp.toName);
            tvFromName.setText(leaveApp.fromName);
            tvFromInfo.setText(leaveApp.classInfo);
            tvStartDate.append(leaveApp.startDate);
            tvEndDate.append(leaveApp.endDate);
            tvMessage.setText(leaveApp.message);
            tvStatus.setText(leaveApp.status.toUpperCase());

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateStatus("accepted");
                }
            });

            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateStatus("rejected");
                }
            });

        }

        return view;
    }


    //**** @Shahrukh TO-DO: CHECK THIS FUNCTION AND USE IT as per requirement
    private void downloadAttachment() {
        if (leaveApp.attachedFile != "null") {
            //Code to download file
            File rootPath = new File(Environment.getExternalStorageDirectory(), "EWAPP");
            if (!rootPath.exists()) {
                rootPath.mkdirs();
            }
            final File localFile = new File(rootPath, leaveApp.attachedFile);
            fileUri = localFile.toURI();
            StorageReference ref = storageRef.child(leaveApp.attachedFile);
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

    public void UpdateStatus(String status) {
        String wAppPath1 = "/applicationsNode/" + leaveApp.toUid + "/" + leaveApp.getwAppId();
        String wAppPath2 = "/applicationsNode/" + leaveApp.fromUid + "/" + leaveApp.getwAppId();

        Map<String, Object> updateStatus = new HashMap<String, Object>();

        updateStatus.put(wAppPath1 + "/status/", status);
        updateStatus.put(wAppPath2 + "/status/", status);

        fbRoot.updateChildren(updateStatus, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference databaseReference) {
                if (error == null) { //Success
                    Toast.makeText(getContext(), "application sent!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    return;
                }
                Log.d(TAG, "updateStatus: ERRoR: " + error); //write failure
                Toast.makeText(getContext(), "failed to update status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "LeaveDisplayFragment onStop: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "LeaveDisplayFragment onDetach: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, " LeaveDisplayFragment onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "LeaveDisplayFragment onDestroy: ");
    }
}
