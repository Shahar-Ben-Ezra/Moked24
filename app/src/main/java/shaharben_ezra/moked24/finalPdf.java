package shaharben_ezra.moked24;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static shaharben_ezra.moked24.DatePickerFragment.changeDate;

public class finalPdf extends AppCompatActivity {
    public static String emailName = "";
    private static pdfObj pdfObj;
    private static final int STORAGE_CODE = 1000;
    private static final int PAGE_WIDTH = 612;
    private static final int PAGE_HEIGHT = 792;
    private Bitmap bmp, scaledBmp;
    public static String targetPdf = "";
    public static String pdfFileName = "";
    private EditText waterSystem, SealingSystem, SewageSystem, RecommendationsForMaking;
    public static boolean finishFlag = false;
    private static String watersS = "";
    private static String SewageS = "";
    private static String SealingS = "";
    private static String Recommendations = "";
    private static boolean finishingPdf = false;
    public static JSONArray jsonArray = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_pdf);
        Intent intent = getIntent();
        pdfObj = (pdfObj) intent.getSerializableExtra("PDF");
        emailName = pdfObj.getEmail();
        waterSystem = (EditText) findViewById(R.id.waterSystem);
        SealingSystem = (EditText) findViewById(R.id.SealingSystem);
        SewageSystem = (EditText) findViewById(R.id.SewageSystem);
        RecommendationsForMaking = (EditText) findViewById(R.id.RecommendationsForMaking);
        waterSystem.setText(pdfObj.getWaterConclusion().isEmpty() ? watersS : pdfObj.getWaterConclusion());
        SealingSystem.setText(pdfObj.getSealingConclusion().isEmpty() ? SealingS : pdfObj.getSealingConclusion());
        SewageSystem.setText(pdfObj.getSewageConclusion().isEmpty() ? SewageS : pdfObj.getSewageConclusion());
        RecommendationsForMaking.setText(pdfObj.getRecommendation().isEmpty() ? Recommendations : pdfObj.getRecommendation());
    }

    public void PressButtonActivity(View v) {
        if (v.getId() == R.id.create) {
            pdfObj.setWaterConclusion(waterSystem.getText().toString());
            pdfObj.setSewageConclusion(SewageSystem.getText().toString());
            pdfObj.setSealingConclusion(SealingSystem.getText().toString());
            pdfObj.setRecommendation(RecommendationsForMaking.getText().toString());

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {
                    String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permission, STORAGE_CODE);
                } else {
                    createPdf(pdfObj.getPropertyDescription(), pdfObj.getCustomerName(),
                            pdfObj.getFullAddress(), pdfObj.getWorkersName(), pdfObj.getReasonCall(),
                            pdfObj.getCallNumber());
                }
            } else {
                createPdf(pdfObj.getPropertyDescription(), pdfObj.getCustomerName(),
                        pdfObj.getFullAddress(), pdfObj.getWorkersName(), pdfObj.getReasonCall(),
                        pdfObj.getCallNumber());
            }
        }
    }

    private void SymbolsEachPage(Canvas canvas, Paint paint, int number, boolean isContractor) {
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setFakeBoldText(true);
        paint.setTextSize(13);
        if (isContractor) {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ies);
            scaledBmp = Bitmap.createScaledBitmap(bmp, 100, 100, false);

            company company = new company(getString(R.string.contractorName), getString(R.string.MursaContractor), getString(R.string.phoneNumberContractor), getString(R.string.websiteContractor), getString(R.string.mailContractor), scaledBmp);
            drawSymbols(canvas, paint, company);

        } else {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.hmoked);
            scaledBmp = Bitmap.createScaledBitmap(bmp, 100, 100, false);

            company company = new company(getString(R.string.hmercazName), getString(R.string.Mursa), getString(R.string.phonNumber), getString(R.string.website), getString(R.string.mail), scaledBmp);
            drawSymbols(canvas, paint, company);

        }
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(number), PAGE_WIDTH / 2, 30, paint);
    }

    private void drawSymbols(Canvas canvas, Paint paint, company c) {
        canvas.drawBitmap(c.getScaledBmp(), 0, 0, paint);
        canvas.drawText(c.getCompanyName(), PAGE_WIDTH - 10, 30, paint);
        canvas.drawText(c.getAuthorizedDealer(), PAGE_WIDTH - 10, 50, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(c.getPhonNumber(), 10, PAGE_HEIGHT - 60, paint);
        canvas.drawText(c.getWebsite(), 10, PAGE_HEIGHT - 40, paint);
        canvas.drawText(c.getMail(), 10, PAGE_HEIGHT - 20, paint);
    }

    private void createPdf(String propertyDescription, String customerName, String fullAddress, String workersName, String reason, int cn) {
        PdfDocument document = new PdfDocument();  // create a new document
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);// start a page
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        SymbolsEachPage(canvas, paint, 1, pdfObj.getIsContractor());
        String sCertDate = changeDate;
        if (changeDate == "") {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            sCertDate = dateFormat.format(new Date());

        }
        changeDate = "";
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setFakeBoldText(true);
        paint.setTextSize(13);
        // headlines
        canvas.drawText(getString(R.string.headlineInHonorOf) + " " + customerName, PAGE_WIDTH - 50, 160, paint);
        canvas.drawText(getString(R.string.headlineNumber) + " " + cn, PAGE_WIDTH - 50, 180, paint);
        canvas.drawText(getString(R.string.headlineAuditDate) + " " + sCertDate, PAGE_WIDTH - 50, 200, paint);
        canvas.drawText(getString(R.string.headlineWorkersName) + " " + workersName, PAGE_WIDTH - 50, 220, paint);
        canvas.drawText(getString(R.string.reason) + " " + reason, PAGE_WIDTH - 50, 240, paint);
        canvas.drawText(getString(R.string.headineAddress) + " " + fullAddress, PAGE_WIDTH - 50, 260, paint);
        paint.setTextSize(11);
        //certificate of approval
        canvas.drawText(getString(R.string.certificateOfApproval), PAGE_WIDTH - 85, 300, paint);
        canvas.drawText("• מצלמה תרמית מסוג x 360x280  flir ex series משמשת לצילום תרמי+דיגיטלי במקביל ", PAGE_WIDTH - 95, 320, paint);
        canvas.drawText("• סיב אופטי לצילום פנים צנרת ולאבחון כשלים במערכות ניקוז וביוב מסוג mc 30 mincam  ", PAGE_WIDTH - 95, 340, paint);
        canvas.drawText(" protimeter mini c  מד לחות • ", PAGE_WIDTH - 95, 360, paint);

//        canvas.drawText("ניסיון מקצועי של הבודק: ", PAGE_WIDTH - 85, 430, paint);
        canvas.drawText("ניסיון מקצועי של 3 דורות באיתור נזקי צנרת ומתן פתרונות טכנולוגים. ", PAGE_WIDTH - 85, 410, paint);
        canvas.drawText("גילוי תרמי, מערכות מים, אינסטלציה,  ומערכות איטום. לאורך השנים סיפקנו שירות לאלפי מקרים. ", PAGE_WIDTH - 85, 430, paint);
//        canvas.drawText("8 שנות ניסיון בגילוי תרמי, מערכות מים ואינסטלציה, ומערכות איטום,  ", PAGE_WIDTH - 85, 455, paint);
//        canvas.drawText(" . TQP לאחר הכשרה של שנה באבחון נזקי מים בסידני אוסטרליה תחת חברת", PAGE_WIDTH - 85, 475, paint);
//        canvas.drawText("למעלה מ4000 נכסים שאובחנו בארץ. ", PAGE_WIDTH - 85, 495, paint);


//        canvas.drawText("*אני נותן חוות דעתי זו במקום עדות בבית המשפט ואני מצהיר בזאת כי ידוע לי היטב, שלעניין ", PAGE_WIDTH - 85, 575, paint);
//        canvas.drawText("הוראות החוק הפלילי בדבר עדות שקר בשבועה בבית המשפט, דין חוות דעתי זו כשהיא חתומה ", PAGE_WIDTH - 85, 595, paint);
//        canvas.drawText("על ידי כדין עדות בשבועה שנתתי בבית המשפט.", PAGE_WIDTH - 85, 615, paint);
        canvas.drawText("אני נותן חוות דעתי זו במקום עדות בבית המשפט ואני מצהיר בזאת כי ידוע לי היטב, שלעניין ", PAGE_WIDTH - 85, 480, paint);
        canvas.drawText("הוראות החוק הפלילי בדבר עדות שקר בשבועה בבית המשפט, דין חוות דעתי זו כשהיא חתומה ", PAGE_WIDTH - 85, 500, paint);
        canvas.drawText("על ידי כדין עדות בשבועה שנתתי בבית המשפט.", PAGE_WIDTH - 85, 520, paint);
        document.finishPage(page);

        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 2).create();
        page = document.startPage(pageInfo);
        Paint paint2 = new Paint();
        canvas = page.getCanvas();
        paint2.setFakeBoldText(true);
        SymbolsEachPage(canvas, paint2, 2, pdfObj.getIsContractor());
        // headlines finding

        paint2.setTextAlign(Paint.Align.CENTER);
        paint2.setTextSize(17);
        paint2.setUnderlineText(true);
        canvas.drawText(getString(R.string.headlineFinding), PAGE_WIDTH / 2, 150, paint2);
        paint2.setUnderlineText(false);
        paint2.setTextAlign(Paint.Align.RIGHT);
        paint2.setTextSize(11);
        canvas.drawText(getString(R.string.headlineInspection), PAGE_WIDTH - 50, 170, paint2);
        canvas.drawText(getString(R.string.headlinePropertyExplanation) + " " + propertyDescription, PAGE_WIDTH - 50, 190, paint2);
        canvas.drawText(getString(R.string.headlineCheckingElements) + " מערכות מים, ניקוז, ביוב, ואיטום. ", PAGE_WIDTH - 50, 210, paint2);
        canvas.drawText(getString(R.string.headlineResonForChecking) + " נזקי מים.", PAGE_WIDTH - 50, 230, paint2);
        canvas.drawText(getString(R.string.headlineBeyondTheFindings), PAGE_WIDTH - 50, 250, paint2);
        TextPaint mTextPaint = new TextPaint();
        mTextPaint.setTextSize(11);
        StaticLayout mTextLayout = new StaticLayout(getString(R.string.explainEvidence), mTextPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.save();
        canvas.translate(-150, 240);
        mTextLayout.draw(canvas);
        canvas.restore();
        // Table
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(20, 400, PAGE_WIDTH - 50, 660, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(14);
        paint.setUnderlineText(true);
        canvas.drawText(getString(R.string.digital), PAGE_WIDTH - 60, 430, paint);
        canvas.drawText(getString(R.string.thermal), PAGE_WIDTH - 280, 430, paint);
        canvas.drawText(getString(R.string.evidences), PAGE_WIDTH - 450, 430, paint);
        paint.setUnderlineText(false);
        canvas.drawLine(PAGE_WIDTH - 430, 400, PAGE_WIDTH - 430, 660, paint);
        canvas.drawLine(PAGE_WIDTH - 240, 400, PAGE_WIDTH - 240, 660, paint);
        canvas.drawLine(20, 440, PAGE_WIDTH - 50, 440, paint);
        paint.setTextSize(11);
        createJson(sCertDate, customerName);
        if (StartCreatePdfFile.evidenceArrayList.size() != 0) {
            evidence evidence = StartCreatePdfFile.evidenceArrayList.get(0);
            if (evidence.getRegularImageView() != null) {
                scaledBmp = Bitmap.createScaledBitmap(evidence.getRegularImageView(), 170, 200, false);
                canvas.drawBitmap(scaledBmp, 380, 450, paint);
            }
            if (evidence.getThermalImageView() != null) {
                scaledBmp = Bitmap.createScaledBitmap(evidence.getThermalImageView(), 170, 200, false);
                canvas.drawBitmap(scaledBmp, 190, 450, paint);
            }
            paint.setTextSize(13);
            if (!evidence.getDescription().isEmpty()) {
                drawLongTxt(canvas, evidence.getDescription(), 3, paint, 180, 485);
            }
            StartCreatePdfFile.evidenceArrayList.remove(0);
        }
        document.finishPage(page);///finish page 2
        int ppnumber = 3;
        int numberOfPages = StartCreatePdfFile.evidenceArrayList.size() / 3;
        for (int i = 0; i <= numberOfPages; i++) {
            paint.setStrokeWidth(2);
            paint.setStyle(Paint.Style.STROKE);
            if (i == numberOfPages) {
                // Table
                int numberOfImages = StartCreatePdfFile.evidenceArrayList.size();
                if (numberOfImages == 1) {
                    pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, ppnumber).create();
                    page = document.startPage(pageInfo);
                    Paint paint4 = new Paint();
                    canvas = page.getCanvas();
                    paint.setFakeBoldText(false);
                    SymbolsEachPage(canvas, paint4, ppnumber++, pdfObj.getIsContractor());

                    canvas.drawRect(20, 120, PAGE_WIDTH - 50, 310, paint);
                    paint.setStyle(Paint.Style.FILL);

                    canvas.drawLine(PAGE_WIDTH - 430, 120, PAGE_WIDTH - 430, 310, paint);
                    canvas.drawLine(PAGE_WIDTH - 240, 120, PAGE_WIDTH - 240, 310, paint);
                    int y = 160;
                    for (int j = 0; j < 1; j++) {
                        evidence evidence = StartCreatePdfFile.evidenceArrayList.get(0);
                        if (evidence.getThermalImageView() != null) {
                            scaledBmp = Bitmap.createScaledBitmap(evidence.getThermalImageView(), 170, 180, false);
                            canvas.drawBitmap(scaledBmp, 190, y - 35, paint);
                        }
                        if (evidence.getRegularImageView() != null) {
                            scaledBmp = Bitmap.createScaledBitmap(evidence.getRegularImageView(), 170, 180, false);
                            canvas.drawBitmap(scaledBmp, 380, y - 35, paint);
                        }
                        if (!evidence.getDescription().isEmpty()) {
                            drawLongTxt(canvas, evidence.getDescription(), 3, paint, 180, y);
                        }
                        StartCreatePdfFile.evidenceArrayList.remove(0);
                    }
                    document.finishPage(page);
                } else if (numberOfImages == 2) {
                    pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, ppnumber).create();
                    page = document.startPage(pageInfo);
                    Paint paint4 = new Paint();
                    canvas = page.getCanvas();
                    paint.setFakeBoldText(false);
                    SymbolsEachPage(canvas, paint4, ppnumber++, pdfObj.getIsContractor());
                    canvas.drawRect(20, 120, PAGE_WIDTH - 50, 500, paint);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawLine(PAGE_WIDTH - 430, 120, PAGE_WIDTH - 430, 500, paint);
                    canvas.drawLine(PAGE_WIDTH - 240, 120, PAGE_WIDTH - 240, 500, paint);
                    canvas.drawLine(20, 310, PAGE_WIDTH - 50, 310, paint);///1 width line
                    int y = 160;
                    for (int j = 0; j < 2; j++) {
                        evidence evidence = StartCreatePdfFile.evidenceArrayList.get(0);
                        if (evidence.getThermalImageView() != null) {
                            scaledBmp = Bitmap.createScaledBitmap(evidence.getThermalImageView(), 170, 180, false);
                            canvas.drawBitmap(scaledBmp, 190, y - 35, paint);
                        }
                        if (evidence.getRegularImageView() != null) {
                            scaledBmp = Bitmap.createScaledBitmap(evidence.getRegularImageView(), 170, 180, false);
                            canvas.drawBitmap(scaledBmp, 380, y - 35, paint);
                        }
                        if (!evidence.getDescription().isEmpty()) {
                            drawLongTxt(canvas, evidence.getDescription(), 3, paint, 180, y);
                        }
                        y += 190;
                        StartCreatePdfFile.evidenceArrayList.remove(0);
                    }
                    document.finishPage(page);
                }
            } else {
                pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, ppnumber).create();
                page = document.startPage(pageInfo);
                Paint paint4 = new Paint();
                canvas = page.getCanvas();
                paint.setFakeBoldText(false);
                SymbolsEachPage(canvas, paint4, ppnumber++, pdfObj.getIsContractor());
                canvas.drawRect(20, 120, PAGE_WIDTH - 50, 690, paint);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawLine(PAGE_WIDTH - 430, 120, PAGE_WIDTH - 430, 690, paint);
                canvas.drawLine(PAGE_WIDTH - 240, 120, PAGE_WIDTH - 240, 690, paint);
                canvas.drawLine(20, 310, PAGE_WIDTH - 50, 310, paint);///1 width line
                canvas.drawLine(20, 500, PAGE_WIDTH - 50, 500, paint);///2 width line
                int y = 160;
                for (int j = 0; j < 3; j++) {
                    evidence evidence = StartCreatePdfFile.evidenceArrayList.get(0);
                    if (evidence.getThermalImageView() != null) {
                        scaledBmp = Bitmap.createScaledBitmap(evidence.getThermalImageView(), 170, 180, false);
                        canvas.drawBitmap(scaledBmp, 190, y - 35, paint);
                    }
                    if (evidence.getRegularImageView() != null) {
                        scaledBmp = Bitmap.createScaledBitmap(evidence.getRegularImageView(), 170, 180, false);
                        canvas.drawBitmap(scaledBmp, 380, y - 35, paint);
                    }
                    if (!evidence.getDescription().isEmpty()) {
                        drawLongTxt(canvas, evidence.getDescription(), 3, paint, 180, y);
                    }
                    y += 190;
                    StartCreatePdfFile.evidenceArrayList.remove(0);

                }
                document.finishPage(page);
            }
        }
        // Create final Page
        pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, ppnumber).create();
        page = document.startPage(pageInfo);
        Paint paint3 = new Paint();
        canvas = page.getCanvas();
        paint3.setFakeBoldText(true);
        SymbolsEachPage(canvas, paint3, ppnumber++, pdfObj.getIsContractor());
        // headlines
        paint3.setTextAlign(Paint.Align.CENTER);
        paint3.setTextSize(18);
        paint3.setUnderlineText(true);
        canvas.drawText(getString(R.string.conclusion), PAGE_WIDTH / 2, 150, paint3);
        paint3.setUnderlineText(false);
        paint3.setTextAlign(Paint.Align.RIGHT);
        paint3.setTextSize(13);

        paint3.setUnderlineText(true);
        canvas.drawText(getString(R.string.waterSystem) + ":", PAGE_WIDTH - 50, 190, paint3);
        int y = 190;
        paint3.setUnderlineText(false);
        y = drawLongTxt(canvas, pdfObj.getWaterConclusion(), 12, paint3, PAGE_WIDTH - 150, y);

        y = y + 20;
        paint3.setUnderlineText(true);
        canvas.drawText(getString(R.string.sealingSystem) + ":", PAGE_WIDTH - 50, y, paint3);
        paint3.setUnderlineText(false);
        y = drawLongTxt(canvas, pdfObj.getSealingConclusion(), 12, paint3, PAGE_WIDTH - 150, y);

        y = y + 20;
        paint3.setUnderlineText(true);
        canvas.drawText(getString(R.string.sewageSystem) + ":", PAGE_WIDTH - 50, y, paint3);
        paint3.setUnderlineText(false);
        y = drawLongTxt(canvas, pdfObj.getSewageConclusion(), 12, paint3, PAGE_WIDTH - 150, y);

        y = y + 80;
        paint3.setTextAlign(Paint.Align.CENTER);
        paint3.setTextSize(18);
        paint3.setUnderlineText(true);
        canvas.drawText(getString(R.string.recommendationsForMaking), PAGE_WIDTH / 2, y, paint3);
        paint3.setTextAlign(Paint.Align.RIGHT);
        paint3.setUnderlineText(false);
        paint3.setTextSize(13);
        y += 20;
        y = drawLongTxt(canvas, pdfObj.getRecommendation(), 15, paint3, PAGE_WIDTH - 75, y);

        paint3.setTextSize(18);
        canvas.drawText("ההמלצה שעבודות יבוצעו על ידי בעל מקצוע מוסמך בנוסף,מומלץ שבעל", PAGE_WIDTH - 50, y + 90, paint3);
        canvas.drawText("המקצוע שמבצע את העבודות יתעדכן עם הבודק מחברתנו.", PAGE_WIDTH - 50, y + 110, paint3);
        canvas.drawText("כך שהעבודה תתנהל בצורה מקצועית ובשקיפות מלאה.", PAGE_WIDTH - 50, y + 130, paint3);

        document.finishPage(page);
        // write the document content

        String directory_path = Environment.getExternalStorageDirectory() + "/MokedApp/PDF_Files/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy_MM_dd");
        pdfFileName = customerName.trim() + ft.format(dNow);
        targetPdf = directory_path + pdfFileName + ".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, getString(R.string.donePdf), Toast.LENGTH_LONG).show();
            finishFlag = true;
            finishingPdf = true;
            finish();
        } catch (Exception e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        document.close();
    }

    private void createJson(String sCertDate, String customerName) {
        try {
            JSONObject objToSave = new JSONObject();
            objToSave.put("callNumber", pdfObj.getCallNumber());
            objToSave.put("customerName", pdfObj.getCustomerName());
            objToSave.put("email", pdfObj.getEmail());
            objToSave.put("fullAddress", pdfObj.getFullAddress());
            objToSave.put("propertyDescription", pdfObj.getPropertyDescription());
            objToSave.put("sealingConclusion", pdfObj.getSealingConclusion());
            objToSave.put("recommendation", pdfObj.getRecommendation());
            objToSave.put("sewageConclusion", pdfObj.getSewageConclusion());
            objToSave.put("waterConclusion", pdfObj.getWaterConclusion());
            objToSave.put("workersName", pdfObj.getWorkersName());
            objToSave.put("reasonCall", pdfObj.getReasonCall());
            objToSave.put("isContractor", pdfObj.getIsContractor());
            objToSave.put("date", sCertDate);
            JSONObject finalJson = new JSONObject();
            finalJson.put("summary", objToSave);
            for (int i = 0; i < StartCreatePdfFile.evidenceArrayList.size(); i++) {
                evidence ev = StartCreatePdfFile.evidenceArrayList.get(i);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("imageBitmap", ev.getRegularImageViewPath());
                    obj.put("imageBitmapthr", ev.getThermalImageViewPath());
                    obj.put("imageExp", ev.getDescription());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finalPdf.jsonArray.put(obj);
            }
            finalJson.put("evidences", jsonArray);
            File storageDir = new File(Environment.getExternalStorageDirectory() + "/MokedApp/PDF_Files");
            storageDir.mkdirs();
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy_MM_dd");
            String filename = Environment.getExternalStorageDirectory() + "/MokedApp/PDF_Files/" + pdfObj.getCustomerName().trim() + ft.format(dNow) + ".json";
            FileOutputStream file1 = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file1);
            out.writeObject(finalJson.toString());
            out.close();
            file1.close();
            jsonArray = new JSONArray();
        } catch (Exception ex) {
            System.out.println("IOException is caught");
        }
    }

    private int drawLongTxt(Canvas canvas, String input, int space, Paint paint, int x, int y) {

        String DiscripWithN = "";
        if (input != null) {
            int count = 0;
            int countlength = 0;
            String[] description = input.split(" ");
            for (int i = 0; i < description.length; i++) {
                String disc = description[i];
                if (count == space || disc.contains("\n") || (space == 3 && i + 1 < description.length &&
                        countlength + disc.length() + description[i + 1].length() > 23)
                        || (space == 15 && i + 1 < description.length &&
                        countlength + disc.length() + description[i + 1].length() > 72) || (
                        space == 12 && i + 1 < description.length &&
                                countlength + disc.length() + description[i + 1].length() > 63)) {
                    canvas.drawText(DiscripWithN + disc, x, y, paint);
                    y += 15;
                    count = 0;
                    DiscripWithN = "";
                    countlength = 0;
                } else {
                    DiscripWithN += disc + " ";
                    countlength += disc.length() + 1;
                    count++;
                }
            }
            canvas.drawText(DiscripWithN, x, y, paint);
            y += 15;
        }
        return y;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createPdf(pdfObj.getPropertyDescription(), pdfObj.getCustomerName(),
                            pdfObj.getFullAddress(), pdfObj.getWorkersName(), pdfObj.getReasonCall(),
                            pdfObj.getCallNumber());
                } else {
                    Toast.makeText(this, "permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (finishingPdf) {
            watersS = "";
            SewageS = "";
            SealingS = "";
            Recommendations = "";
            finishingPdf = false;
        } else {
            watersS = waterSystem.getText().toString();
            SewageS = SewageSystem.getText().toString();
            SealingS = SealingSystem.getText().toString();
            Recommendations = RecommendationsForMaking.getText().toString();
        }
    }
}
