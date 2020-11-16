package shaharben_ezra.moked24;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class StartCreatePdfFile extends AppCompatActivity  {

    private EditText propertyDescriptionText, costumerName, address, workers,email;
    private TextView textViewNumberOfImages;
    public static ArrayList<evidence> evidenceArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pdf_file);
        setTitle(R.string.hmercazName);
        propertyDescriptionText = (EditText) findViewById(R.id.propertyDescription);
        costumerName = (EditText) findViewById(R.id.costumerName);
        email = (EditText) findViewById(R.id.email);

        address = (EditText) findViewById(R.id.address);
        workers = (EditText) findViewById(R.id.workers);
        textViewNumberOfImages=(TextView) findViewById(R.id.textViewNumberOfImages);
        textViewNumberOfImages.setText("מספר התמונות שהוספת עד כה :"+evidenceArrayList.size());
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public void PressActivity(View v) {

        switch (v.getId()) {
            case R.id.continueRecommendation: {
                Intent target = new Intent(StartCreatePdfFile.this, finalPdf.class);
                Random random = new Random();
                // generate a random integer from 0 to 8999, then add 1000
                int callNumber = random.nextInt(8999) + 1000;
                pdfObj pdfObj=new pdfObj(propertyDescriptionText.getText().toString(), costumerName.getText().toString(),
                        address.getText().toString(), workers.getText().toString(),
                        callNumber,email.getText().toString());
                target.putExtra("PDF",  pdfObj);
                startActivity(target);
                break;

            }
            case R.id.addPhoto: {
                Intent target = new Intent(StartCreatePdfFile.this, addImage.class);
                startActivity(target);
                break;
            }
        }
    }

    protected void onRestart() {
        super.onRestart();
        if(finalPdf.finishFlag){
            finalPdf.finishFlag=false;
            finish();
        }
        else
        textViewNumberOfImages.setText("מספר התמונות שהוספת עד כה :"+evidenceArrayList.size());
    }
}
