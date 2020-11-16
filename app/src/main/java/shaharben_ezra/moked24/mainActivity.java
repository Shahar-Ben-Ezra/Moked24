package shaharben_ezra.moked24;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.hmercazName);
    }

    public void PressBtbActivity(View v) {

        switch (v.getId()) {
            case R.id.createPdf: {
                Intent target = new Intent(mainActivity.this, StartCreatePdfFile.class);
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
                Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/mypdf/");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(selectedUri, "*/*");
                startActivity(intent);
                break;
            }
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
                    {"me@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                    "המוקד לאיתור נזילות בישראל");
            emailIntent.putExtra(Intent.EXTRA_TEXT,
                    "שלום רב, \n" +
                            "\n" +
                            "לאחר בדיקה שבוצעה מצורפת חוות דעת מקצועית.\n" +
                            "נשמח לעמוד לשירותכם בכל עת ,\n" +
                            "בברכה המוקד לאיתור נזילות בישראל. ");
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(finalPdf.targetPdf));
            Intent i = Intent.createChooser(emailIntent, "Send mail...");
            startActivity(i);
            progressDialog.dismiss();
        }
    }
}
