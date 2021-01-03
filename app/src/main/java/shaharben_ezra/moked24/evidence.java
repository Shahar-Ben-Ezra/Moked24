package shaharben_ezra.moked24;

import android.graphics.Bitmap;

import java.io.Serializable;

public class evidence implements Serializable {

    private static final long serialVersionUID = 1;
    private Bitmap regularImageView;
    private Bitmap thermalImageView;
    private String description;
    private String regularImageViewPath;
    private String thermalImageViewPath;

    public evidence(Bitmap regularImageView, Bitmap thermalImageView, String description) {
        this.description = description;
        this.thermalImageView = thermalImageView;
        this.regularImageView = regularImageView;
    }

    public evidence(Bitmap regularImageView, Bitmap thermalImageView, String description, String thermalImageViewPath, String regularImageViewPath) {
        this.description = description;
        this.thermalImageView = thermalImageView;
        this.regularImageView = regularImageView;
        this.thermalImageViewPath = thermalImageViewPath;
        this.regularImageViewPath = regularImageViewPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThermalImageViewPath() {
        if (thermalImageViewPath == null) {
            return "";
        }
        return thermalImageViewPath;
    }

    public void setThermalImageViewPath(String thermalImageViewPath) {
        this.thermalImageViewPath = thermalImageViewPath;
    }

    public String getRegularImageViewPath() {
        if (regularImageViewPath == null) {
            return "";
        }
        return regularImageViewPath;
    }

    public void setRegularImageViewPath(String regularImageViewPath) {
        this.regularImageViewPath = regularImageViewPath;
    }

    public void setRegularImageView(Bitmap regularImageView) {
        this.regularImageView = regularImageView;
    }

    public void setThermalImageView(Bitmap thermalImageView) {
        this.thermalImageView = thermalImageView;
    }

    public Bitmap getRegularImageView() {
        return regularImageView;
    }

    public Bitmap getThermalImageView() {
        return thermalImageView;
    }

}
