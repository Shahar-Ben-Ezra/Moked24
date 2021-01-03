package shaharben_ezra.moked24;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class OpenPdf extends AppCompatActivity {
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opdf);
        pdfView = findViewById(R.id.pdfView);
        File filePath = new File(finalPdf.targetPdf);
        pdfView.fromFile(filePath).load();
    }
}
