package shaharben_ezra.moked24;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.CheckBox;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class proposalReport extends AppCompatActivity {

    private EditText customerNameText;
    static String customerName = "";
    static CheckBox signature;
    Spinner spinnerInputType;
    String reportName;
    private static final int PAGE_WIDTH = 612;
    private static final int PAGE_HEIGHT = 792;
    private Bitmap bmp, scaledBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proposal_report);
        setTitle(R.string.hmercazName);
        customerNameText = (EditText) findViewById(R.id.pdfFileName);
        signature = findViewById(R.id.signature);
        spinnerInputType = findViewById(R.id.spinnerInputReports);
        final String[] arrayItems = {"איתור נזילה תת קרקעית",
                "בדיקה לאיתור נזילות"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter(this, R.layout.simple_spinner_item, arrayItems);
        //Dropdown layout style
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
        //attaching data adapter to spinner
        spinnerInputType.setAdapter(dataAdapter);
        spinnerInputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reportName = arrayItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getPdfFilePath(View v) {

        customerName = customerNameText.getText().toString().trim();
        System.out.print(signature.isChecked());
        createPdf(customerName);
    }

    private void SymbolsEachPage(Canvas canvas, Paint paint, int number) {
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setFakeBoldText(true);
        paint.setTextSize(13);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hmoked);
        scaledBmp = Bitmap.createScaledBitmap(bmp, 100, 100, false);
        canvas.drawBitmap(scaledBmp, 0, 0, paint);
        canvas.drawText(getString(R.string.hmercazName), PAGE_WIDTH - 10, 30, paint);//
        canvas.drawText(getString(R.string.Mursa), PAGE_WIDTH - 10, 50, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(getString(R.string.phonNumber), 10, PAGE_HEIGHT - 60, paint);
        canvas.drawText(getString(R.string.website), 10, PAGE_HEIGHT - 40, paint);
        canvas.drawText(getString(R.string.mail), 10, PAGE_HEIGHT - 20, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(number), PAGE_WIDTH / 2, 30, paint);
    }

    private void createPdf(String customerName) {
        PdfDocument document = new PdfDocument();  // create a new document
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);// start a page
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        SymbolsEachPage(canvas, paint, 1);
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setFakeBoldText(true);
        paint.setTextSize(13);
        // headlines
        canvas.drawText(getString(R.string.headlineInHonorOf) + " " + customerName, PAGE_WIDTH - 50, 160, paint);
        canvas.drawText("על מנת לבצע איתור נזילה באופן מקצועי ומדויק יש לערך בהתאם. ", PAGE_WIDTH - 85, 270, paint);
        paint.setTextSize(11);

        if (reportName == "בדיקה לאיתור נזילות") {
            paint.setUnderlineText(true);
            canvas.drawText(getString(R.string.checkLeak), PAGE_WIDTH / 2 + 70, 190, paint);
            paint.setUnderlineText(false);
            canvas.drawText("1) גישה לכל הדירות אשר קשורות באופן כזה או אחר לנזילה, הגישה תהיה לכל משך הבדיקה.  ", PAGE_WIDTH - 95, 300, paint);
            canvas.drawText("2) דירה אשר גורמת רטיבות נדרש להדליק את הדוד לפחות שעה לפני הגעתנו.", PAGE_WIDTH - 95, 320, paint);
            canvas.drawText("3) בתום הבדיקה ינתן הסבר בעל פה על ממצאי הבדיקה אך באופן חלקי בלבד.", PAGE_WIDTH - 95, 340, paint);
            canvas.drawText("לאחר חמישה ימי עסקים, יופק דוח ממצאים מפורט.", PAGE_WIDTH - 100, 360, paint);
            canvas.drawText("הדוח יועבר רק לאחר העברת תשלום.  ", PAGE_WIDTH - 85, 380, paint);
            canvas.drawText("אופן התשלום: שיק או העברה בנקאית, תישלח חשבונית מס קבלה. ", PAGE_WIDTH - 85, 400, paint);
        } else {
            paint.setUnderlineText(true);
            canvas.drawText(getString(R.string.checkLeakUnder), PAGE_WIDTH / 2 + 70, 190, paint);
            paint.setUnderlineText(false);
            canvas.drawText("1) משך הבדיקה עד 3 שעות. ", PAGE_WIDTH - 95, 300, paint);
            canvas.drawText("2) הבדיקה דורשת הפסקת מים עד 3 שעות בהתאם לכך יש לעדכן את הדיירים.", PAGE_WIDTH - 95, 320, paint);
            canvas.drawText("3) נצטרך גישה לכל הקומות שיש בהן אספקת מים כולל הגג.", PAGE_WIDTH - 95, 340, paint);
            canvas.drawText("4) אנו מבצעים סריקה  במכשיר אלקטרו אקוסטי, בשילוב עם גלאי גז באמצעות החדרת גז לצנרת ", PAGE_WIDTH - 95, 360, paint);
            canvas.drawText(" שמופק מ95% חנקן ו5% מימן,גז שמאושר במכון התקנים הישראלי ומשרד הבריאות. ", PAGE_WIDTH - 100, 380, paint);
            canvas.drawText("5) סימון הנזילה, במקרים חריגים קיימת סטייה של האיתור ברדיוס של עד 2 מטר. ", PAGE_WIDTH - 95, 400, paint);
            canvas.drawText("תלוי במשטח הקרקעי שקיים באזור. ", PAGE_WIDTH - 100, 420, paint);
            canvas.drawText("הצעת מחיר לתיקון הנזילה תינתן רק לאחר איתור והערכה מדויקת של הכשל.", PAGE_WIDTH - 85, 440, paint);
            canvas.drawText("תשלום יתבצע בתום הבדיקה. אופן  התשלום: שיק או העברה בנקאית.", PAGE_WIDTH - 85, 460, paint);
        }
        document.finishPage(page);

        String directory_path = Environment.getExternalStorageDirectory() + "/MokedApp/PDF_Files/WorkOrders/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + "הזמנת עבודה " + reportName + customerName.trim() + ".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, getString(R.string.donePdf), Toast.LENGTH_LONG).show();
            finish();
            finalPdf.targetPdf = targetPdf;
        } catch (Exception e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        document.close();
    }

    protected void onRestart() {
        super.onRestart();
        finish();
    }
}

