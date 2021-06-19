package shaharben_ezra.moked24;

import android.Manifest;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class mainActivity extends AppCompatActivity {
    Button showPdf;
    EditText costumerName, address, workers, callNumber;
    ActionBar actionbar;
    TextView textview;
    ProgressDialog progressDialog;
    private static final int STORAGE_CODE = 1000;

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
//            case R.id.openSpecificFile: {
//                Intent target = new Intent(mainActivity.this, openSpecificPdfByName.class);
//                startActivity(target);
//                break;
//            }
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
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(filePDF));
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]
                    {finalPdf.emailName});
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
