package shaharben_ezra.moked24;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class StartCreatePdfFile extends AppCompatActivity {

    private EditText propertyDescriptionText, costumerName, address, workers, email;
    private TextView textViewNumberOfImages;
    public static ArrayList<evidence> evidenceArrayList = new ArrayList<>();
    private pdfObj editPdf;
    Spinner spinnerInputType;

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
        textViewNumberOfImages = (TextView) findViewById(R.id.textViewNumberOfImages);
        textViewNumberOfImages.setText("מספר התמונות שהוספת עד כה :" + evidenceArrayList.size());
        spinnerInputType = findViewById(R.id.spinnerInput);
        Intent intent = getIntent();
        editPdf = (pdfObj) intent.getSerializableExtra("PDF");
        if (editPdf != null) {
            propertyDescriptionText.setText(editPdf.getPropertyDescription());
            costumerName.setText(editPdf.getCustomerName());
            email.setText(editPdf.getEmail());
            address.setText(editPdf.getFullAddress());
            workers.setText(editPdf.getWorkersName());
        }
        String[] arrayItems = {"0",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "11"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter(getBaseContext(), R.layout.simple_spinner_item, arrayItems);
        //Dropdown layout style
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        //attaching data adapter to spinner
        spinnerInputType.setAdapter(dataAdapter);
        spinnerInputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position != 0 && evidenceArrayList.size() >= position) {
                        Intent target = new Intent(StartCreatePdfFile.this, addImage.class);
                        target.putExtra("position", position);
                        startActivity(target);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                pdfObj pdfObj;
                if (editPdf != null) {
                    pdfObj = new pdfObj(propertyDescriptionText.getText().toString(), costumerName.getText().toString(),
                            address.getText().toString(), workers.getText().toString(),
                            callNumber, email.getText().toString(), editPdf.getWaterConclusion(), editPdf.getSewageConclusion(), editPdf.getSealingConclusion(), editPdf.getRecommendation());
                } else {
                    pdfObj = new pdfObj(propertyDescriptionText.getText().toString(), costumerName.getText().toString(),
                            address.getText().toString(), workers.getText().toString(),
                            callNumber, email.getText().toString());
                }
                target.putExtra("PDF", pdfObj);

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

    protected void onDestroy() {
        super.onDestroy();
        evidenceArrayList = new ArrayList<>();
    }

    protected void onRestart() {
        super.onRestart();
        if (finalPdf.finishFlag) {
            finalPdf.finishFlag = false;
            finish();
        } else
            textViewNumberOfImages.setText("מספר התמונות שהוספת עד כה :" + evidenceArrayList.size());
    }
}
