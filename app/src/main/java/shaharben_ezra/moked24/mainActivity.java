package shaharben_ezra.moked24;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.File;
import java.util.Collections;

import static androidx.core.content.FileProvider.getUriForFile;

public class mainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 2;

    Button showPdf;
    EditText costumerName, address, workers, callNumber;
    ActionBar actionbar;
    TextView textview;
    ProgressDialog progressDialog;
    private static final int STORAGE_CODE = 1000;
    private DriveServiceHelper mDriveServiceHelper;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.hmercazName);
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permission, STORAGE_CODE);
        }
        requestSignIn();
    }


    /**
     * Starts a sign-in activity using {@link #REQUEST_CODE_SIGN_IN}.
     */
    private void requestSignIn() {
        Log.d(TAG, "Requesting sign-in");

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                        .build();
        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);

        // The result of the sign-in Intent is handled in onActivityResult.
        startActivityForResult(client.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    /**
     * Handles the {@code result} of a completed sign-in activity initiated from {@link
     * #requestSignIn()}.
     */
    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                    Log.d(TAG, "Signed in as " + googleAccount.getEmail());

                    // Use the authenticated account to sign in to the Drive service.
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    this, Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName("Drive API Migration")
                                    .build();

                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
                    mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
                })
                .addOnFailureListener(exception -> Log.e(TAG, "Unable to sign in.", exception));
    }

    public void PressBtbActivity(View v) {

        switch (v.getId()) {
            case R.id.createPdf: {
                Intent target = new Intent(mainActivity.this, StartCreatePdfFile.class);
                startActivity(target);
                break;
            }
            case R.id.editFile: {
                Intent target = new Intent(mainActivity.this, editOpenPdf.class);
                startActivity(target);
                break;
            }
            case R.id.showPdf: {
                if (!finalPdf.targetPdf.isEmpty()) {
                    Intent target = new Intent(mainActivity.this, OpenPdf.class);
                    startActivity(target);
                } else {
                    Toast.makeText(this, getString(R.string.firstCreate), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.SendPdf: {
                if (!finalPdf.targetPdf.isEmpty()) {
                    MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                    myAsyncTasks.execute();
                } else {
                    Toast.makeText(this, getString(R.string.firstCreate), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.openFile: {
                Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/MokedApp/PDF_Files/");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(selectedUri, "*/*");
                startActivity(intent);
                break;
            }
            case R.id.proposal_report: {
                Intent target = new Intent(mainActivity.this, proposalReport.class);
                startActivity(target);
                break;
            }
            case R.id.saveToGoogleDrive: {
                if (!finalPdf.targetPdf.isEmpty()) {
                    progressDialog = new ProgressDialog(mainActivity.this);

                    progressDialog.setTitle("Uploading to google drive");
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();

                    mDriveServiceHelper.createFile(finalPdf.targetPdf, finalPdf.pdfFileName)
                            .addOnSuccessListener(nameAndContent -> {
                                progressDialog.dismiss();

                                Toast.makeText(this, getString(R.string.successCreateGD), Toast.LENGTH_SHORT).show();

                            })
                            .addOnFailureListener(exception -> {
                                progressDialog.dismiss();
                                Log.e(TAG, "Unable to open file from picker.", exception);
                            });

                }
                break;
            }
        }
    }

    /**
     * Opens a file from its {@code uri} returned from the Storage Access Framework file picker
     * initiated by {@link #()}.
     */
    private void openFileFromFilePicker(Uri uri) {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Opening " + uri.getPath());

            mDriveServiceHelper.openFileUsingStorageAccessFramework(getContentResolver(), uri)
                    .addOnSuccessListener(nameAndContent -> {
                        String name = nameAndContent.first;
                        String content = nameAndContent.second;

//                        mFileTitleEditText.setText(name);
//                        mDocContentEditText.setText(content);
//
//                        // Files opened through SAF cannot be modified.
//                        setReadOnlyMode();
                    })
                    .addOnFailureListener(exception ->
                            Log.e(TAG, "Unable to open file from picker.", exception));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                String dat1a = data.getData().getPath();
                int x = 4;
            }
        }

        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    handleSignInResult(data);
                }
                break;

            case REQUEST_CODE_OPEN_DOCUMENT:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        openFileFromFilePicker(uri);
                    }
                }
                break;
        }
    }


    public class MyAsyncTasks extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mainActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            } catch (Exception ERR) {
                System.out.println(ERR);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("application/pdf");
            File filePDF = new File(finalPdf.targetPdf);
            emailIntent.putExtra(Intent.EXTRA_STREAM, getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", filePDF));

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]
                    {finalPdf.emailName});
            emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                    "המוקד לאיתור נזילות בישראל");
            emailIntent.putExtra(Intent.EXTRA_TEXT,
                    "שלום רב, \n" +
                            "\n" +
                            "לאחר בדיקה שבוצעה מצורפת חוות דעת מקצועית.\n" +
                            "נשמח לעמוד לשירותכם בכל עת ,\n" +
                            "בברכה המוקד לאיתור נזילות בישראל. ");
            Intent i = Intent.createChooser(emailIntent, "Send mail...");
            startActivity(i);
            progressDialog.dismiss();
        }
    }
}
