package shaharben_ezra.moked24;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import static java.lang.Integer.parseInt;

public class editPdf extends AppCompatActivity {

    private EditText pdfFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_pdf_file);
        setTitle(R.string.hmercazName);
        pdfFileName = (EditText) findViewById(R.id.pdfFileName);
    }

    public void getJsonFile(View v) {
        String fileName = pdfFileName.getText().toString();
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
                Intent target = new Intent(editPdf.this, StartCreatePdfFile.class);
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
                pdfObj pdfObj = new pdfObj(summaryObj.getString("propertyDescription"), summaryObj.getString("customerName"),
                        summaryObj.getString("fullAddress"), summaryObj.getString("workersName"),
                        parseInt(summaryObj.getString("callNumber")), summaryObj.getString("email"), summaryObj.getString("waterConclusion"), summaryObj.getString("sewageConclusion"), summaryObj.getString("sealingConclusion"), summaryObj.getString("recommendation"));

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

