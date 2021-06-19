package shaharben_ezra.moked24;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.FilenameFilter;
import java.io.File;
import androidx.appcompat.app.AppCompatActivity;


public class openSpecificPdfByName extends AppCompatActivity {

    private EditText pdfFileName;
    static String fileName ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_specific_pdf_file);
        setTitle(R.string.hmercazName);
        pdfFileName = (EditText) findViewById(R.id.pdfFileName);
    }

    public void getPdfFilePath(View v) {
        fileName = pdfFileName.getText().toString().trim();
        if (fileName.isEmpty()) {
            Toast.makeText(this, getString(R.string.didntFound), Toast.LENGTH_SHORT).show();
        } else {
            String directory_path = Environment.getExternalStorageDirectory() + "/MokedApp/PDF_Files/";
            File root = new File(directory_path);
            FilenameFilter beginswithm = new FilenameFilter()
            {
                public boolean accept(File directory, String currentFilename) {
                    return currentFilename.startsWith(fileName);
                }
            };
            File[] files = root.listFiles(beginswithm);
            for (File f: files)
            {
                System.out.println(f);//4296116
            }
            String filename = directory_path + fileName + ".pdf";
            try {
                File file = new File(filename);
                if (file.exists()) {
                    finalPdf.targetPdf = filename;
                    Intent target = new Intent(openSpecificPdfByName.this, OpenPdf.class);
                    startActivity(target);
                    finish();
                }else{
                    Toast.makeText(this, getString(R.string.didntFound), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.didntFound), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    protected void onRestart() {
        super.onRestart();
        finish();
    }
}

