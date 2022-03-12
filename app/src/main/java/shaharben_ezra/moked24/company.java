package shaharben_ezra.moked24;

import android.graphics.Bitmap;

public class company {
    private String companyName;
    private String authorizedDealer;
    private String phonNumber;
    private String website;
    private String mail;
    private Bitmap scaledBmp;

    public company(String companyName, String authorizedDealer, String phonNumber, String website, String mail, Bitmap scaledBmp) {
        this.companyName = companyName;
        this.authorizedDealer = authorizedDealer;
        this.phonNumber = phonNumber;
        this.website = website;
        this.mail = mail;
        this.scaledBmp = scaledBmp;
    }

    public Bitmap getScaledBmp() {
        return scaledBmp;
    }

    public void setScaledBmp(Bitmap scaledBmp) {
        this.scaledBmp = scaledBmp;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAuthorizedDealer() {
        return authorizedDealer;
    }

    public void setAuthorizedDealer(String authorizedDealer) {
        this.authorizedDealer = authorizedDealer;
    }

    public String getPhonNumber() {
        return phonNumber;
    }

    public void setPhonNumber(String phonNumber) {
        this.phonNumber = phonNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}





