import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Material {
    Color Fill, Stroke; //The fill and stroke colors for the GameObject
    int StrokeWidth; //The stroke width for the GameObject
    boolean IsShape = true; //If the material displays an image or not
    BufferedImage MaterialImage; //The image displayed on the object.

    //TODO: Create default
    // black fill
    // black stroke
    // zero stroke width
    // is a shape
    Material(){
        Fill = Color.BLACK;
        Stroke = Color.BLACK;
        StrokeWidth = 0;
        IsShape = true;
    }

    //TODO: Sets the appropriate fields.
    public Material(Color fill, Color border, int borderWidth) {
        this.Fill = fill;
        this.Stroke = border;
        this.StrokeWidth = borderWidth;
        IsShape = true;
    }

    //TODO: Load the image at the path and set isShape flag to false
    public Material(String path){
        IsShape = false;
        try{
            MaterialImage = ImageIO.read(new File(path));
        }
        catch (IOException e) {}
    }

    //TODO: Loads an image and updates this Material's image;
    public void SetMaterialImage(String path){
        this.IsShape = false;
        try{
            MaterialImage = ImageIO.read(new File(path));
        }
        catch (IOException e) {
            System.out.println("error");
        }
    }

    //Getters and Setters, done for you!
    public Color GetFill() {
        return Fill;
    }

    public void SetFill(Color fill) {
        this.Fill = fill;
    }

    public Color GetBorder() {
        return Stroke;
    }

    public void SetBorder(Color border) {
        this.Stroke = border;
    }

    public int GetBorderWidth() {
        return StrokeWidth;
    }

    public void SetBorderWidth(int stroke_width) {
        this.StrokeWidth = stroke_width;
    }

    public BufferedImage GetMaterialImage(){return MaterialImage;}

    public void SetMaterialImage(BufferedImage img){this.MaterialImage =img;this.IsShape =false;}

}
