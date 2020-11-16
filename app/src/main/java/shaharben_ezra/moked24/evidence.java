package shaharben_ezra.moked24;

import android.graphics.Bitmap;

public class evidence {
    private Bitmap regularImageView;
    private Bitmap thermalImageView;
    private String description ;


    public evidence( Bitmap regularImageView, Bitmap thermalImageView, String description){
        this.description=description;
        this.thermalImageView=thermalImageView;
        this.regularImageView=regularImageView;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getRegularImageView() {
        return regularImageView;
    }

    public Bitmap getThermalImageView()
    {
        return thermalImageView;
    }

}
