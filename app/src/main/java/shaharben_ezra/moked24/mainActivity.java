package shaharben_ezra.moked24;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class mainActivity extends AppCompatActivity {
    Button showPdf;
    EditText costumerName, address, workers, callNumber;
    ActionBar actionbar;
    TextView textview;

    ActionBar.LayoutParams layoutparams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.hmercazName);
    }
        public void PressBtbActivity(View v) {

        switch (v.getId()){

            case R.id.createPdf:{
                Intent target = new Intent(mainActivity.this, StartCreatePdfFile.class);
                startActivity(target);

                break;
            }
            case R.id.showPdf:{
                if(!finalPdf.targetPdf.isEmpty()) {
                    Intent target = new Intent(mainActivity.this, OpenPdf.class);
                    startActivity(target);
                }
                else {
                    Toast.makeText(this, getString(R.string.firstCreate), Toast.LENGTH_SHORT).show();

                }
                break;
            }
            case R.id.SendPdf:{
                try {
                    if(!finalPdf.targetPdf.isEmpty()) {
                        sendMail();
                    }
                    else{
                        Toast.makeText(this, getString(R.string.firstCreate), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.openFile:{

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
        if(requestCode==10) {
            if (resultCode == RESULT_OK) {
                String dat1a = data.getData().getPath();
                int x = 4;
            }
        }
    }

    public void sendMail() throws IOException {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("application/pdf");

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
        this.startActivity(i);

    }

}