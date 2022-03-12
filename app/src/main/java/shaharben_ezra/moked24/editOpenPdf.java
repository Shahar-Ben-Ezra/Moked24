package shaharben_ezra.moked24;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;

import static java.lang.Integer.parseInt;

public class editOpenPdf extends AppCompatActivity {

    private AutoCompleteTextView pdfFileNameAutoComplete;
    static String fileName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_open_pdf_file);
        setTitle(R.string.hmercazName);
        String directory_path = Environment.getExternalStorageDirectory() + "/MokedApp/PDF_Files/";

        File directory = new File(directory_path);
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            Toast.makeText(this, getString(R.string.noFilesMessage), Toast.LENGTH_SHORT).show();
            finish();
        }

        int myStringArrayLength = files.length / 2;
        if (files.length % 2 != 0) { // odd
            myStringArrayLength++;
        }

        String[] myStringArray = new String[myStringArrayLength];
        Log.d("Files", "Size: " + files.length);
        int count = 0;
        try {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (fileName.contains("pdf")) {
                    myStringArray[count++] = fileName.split("\\.")[0];
                }
            }

            if (count != myStringArrayLength) {
                String[] myNewStringArray = new String[count];
                for (int i = 0; i < count; i++) {
                    myNewStringArray[i] = myStringArray[i];
                }
                myStringArray = myNewStringArray;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        pdfFileNameAutoComplete = (AutoCompleteTextView) findViewById(R.id.pdfFileNameAutoComplete1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(editOpenPdf.this, android.R.layout.simple_dropdown_item_1line, myStringArray);
        pdfFileNameAutoComplete.setAdapter(adapter);

    }


    public void getPdfFilePath(View v) {
        fileName = pdfFileNameAutoComplete.getText().toString();
        if (fileName.isEmpty()) {
            Toast.makeText(this, getString(R.string.didntFound), Toast.LENGTH_SHORT).show();
        } else {
            String directory_path = Environment.getExternalStorageDirectory() + "/MokedApp/PDF_Files/";
            File root = new File(directory_path);
            FilenameFilter beginswithm = new FilenameFilter() {
                public boolean accept(File directory, String currentFilename) {
                    return currentFilename.startsWith(fileName);
                }
            };
            File[] files = root.listFiles(beginswithm);
            for (File f : files) {
                System.out.println(f);//4296116
            }
            String filename = directory_path + fileName + ".pdf";
            try {
                File file = new File(filename);
                if (file.exists()) {
                    finalPdf.targetPdf = filename;
                    Intent target = new Intent(editOpenPdf.this, OpenPdf.class);
                    startActivity(target);
                    finish();
                } else {
                    Toast.makeText(this, getString(R.string.didntFound), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.didntFound), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public void getJsonFile(View v) {
        fileName = pdfFileNameAutoComplete.getText().toString();
        if (fileName.isEmpty()) {
            Toast.makeText(this, getString(R.string.didntFound), Toast.LENGTH_SHORT).show();
        } else {
            String directory_path = Environment.getExternalStorageDirectory() + "/MokedApp/PDF_Files/";
            String filename = directory_path + fileName + ".json";
            try {
                FileInputStream fis = new FileInputStream(filename);
                ObjectInputStream ois = new ObjectInputStream(fis);
                String jsonString = (String) ois.readObject();
                ois.close();
                Intent target = new Intent(editOpenPdf.this, StartCreatePdfFile.class);
                JSONObject obj = new JSONObject(jsonString);
                JSONObject summaryObj = obj.getJSONObject("summary");
                JSONArray arr = obj.getJSONArray("evidences");

                for (int i = 0; i < arr.length(); i++) {
                    String imageBitmapPath = arr.getJSONObject(i).getString("imageBitmap");
                    String imageBitmapthrPath = arr.getJSONObject(i).getString("imageBitmapthr");
                    String imageExp = arr.getJSONObject(i).getString("imageExp");
                    evidence e = new evidence(getBitmap(imageBitmapPath), getBitmap(imageBitmapthrPath), imageExp, imageBitmapthrPath, imageBitmapPath);
                    StartCreatePdfFile.evidenceArrayList.add(e);
                }
                String reasonCall = "";
                try {
                    reasonCall = summaryObj.getString("reasonCall");
                } catch (Exception e) {
                }
                boolean isContractor = false;
                try {
                    isContractor = summaryObj.getBoolean("isContractor");
                } catch (Exception e) {
                    System.out.print(e);
                }
                pdfObj pdfObj = new pdfObj(summaryObj.getString("propertyDescription"), summaryObj.getString("customerName"),
                        summaryObj.getString("fullAddress"), summaryObj.getString("workersName"), reasonCall,
                        parseInt(summaryObj.getString("callNumber")), summaryObj.getString("email"), summaryObj.getString("waterConclusion"), summaryObj.getString("sewageConclusion"), summaryObj.getString("sealingConclusion"), summaryObj.getString("recommendation"), isContractor);

                DatePickerFragment.changeDate = summaryObj.getString("date");
                target.putExtra("PDF", pdfObj);
                startActivity(target);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.didntFound), Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }
        }
    }

    private Bitmap getBitmap(String path) {
        if (path.isEmpty()) {
            return null;
        }
        File imgFile = new File(path);
        if (imgFile.exists()) {
            return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return null;
    }

    protected void onRestart() {
        super.onRestart();
        if (finalPdf.finishFlag) {
            finalPdf.finishFlag = false;
            finish();
        }
    }


}

