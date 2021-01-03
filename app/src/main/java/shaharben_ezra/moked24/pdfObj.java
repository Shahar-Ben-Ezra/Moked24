package shaharben_ezra.moked24;

import java.io.Serializable;

public class pdfObj implements Serializable {

    private static final long serialVersionUID = 1;
    private String propertyDescription;
    private String customerName;
    private String fullAddress;
    private String workersName;
    private String email;
    private int callNumber;
    private String recommendation;
    private String waterConclusion;
    private String sealingConclusion;
    private String sewageConclusion;

    public pdfObj(String propertyDescription, String customerName, String fullAddress,
                  String workersName, int callNumber, String email) {
        this.propertyDescription = propertyDescription;
        this.customerName = customerName;
        this.fullAddress = fullAddress;
        this.workersName = workersName;
        this.callNumber = callNumber;
        this.email = email;
        this.waterConclusion = "";
        this.sewageConclusion = "";
        this.sealingConclusion = "";
        this.recommendation = "";
    }

    public pdfObj(String propertyDescription, String customerName, String fullAddress,
                  String workersName, int callNumber, String email, String waterConclusion, String sewageConclusion, String sealingConclusion, String recommendation) {
        this.propertyDescription = propertyDescription;
        this.customerName = customerName;
        this.fullAddress = fullAddress;
        this.workersName = workersName;
        this.callNumber = callNumber;
        this.email = email;
        this.waterConclusion = waterConclusion;
        this.sewageConclusion = sewageConclusion;
        this.sealingConclusion = sealingConclusion;
        this.recommendation = recommendation;
    }

    public String getPropertyDescription() {
        return propertyDescription;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getEmail() {
        return email.isEmpty() ? "@gmail.com" : email;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getWorkersName() {
        return workersName;
    }

    public int getCallNumber() {
        return callNumber;
    }

    public void setWaterConclusion(String waterConclusion) {
        this.waterConclusion = waterConclusion;
    }

    public String getWaterConclusion() {
        return waterConclusion;
    }

    public void setSealingConclusion(String sealingConclusion) {
        this.sealingConclusion = sealingConclusion;
    }

    public String getSealingConclusion() {
        return sealingConclusion;
    }

    public void setSewageConclusion(String sewageConclusion) {
        this.sewageConclusion = sewageConclusion;
    }

    public String getSewageConclusion() {
        return sewageConclusion;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getRecommendation() {
        return recommendation;
    }

}
