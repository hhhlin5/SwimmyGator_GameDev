import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class GameObject {
    private AffineTransform ObjectTransform; //the location/scale/rotation of our object
    private Shape ObjectShape; //the collider/rendered shape of this object
    private Material ObjectMaterial; //data about the fill color, border color, and border thickness
    private ArrayList<ScriptableBehavior> ObjectScripts = new ArrayList<>(); //all scripts attached to the object
    private boolean IsActive = true; //whether this gets Updated() and Draw()

    //TODO: Create the default GameObject using:
    //TODO: a default AffineTransform
    //TODO: a default Material
    //TODO: a 10x10 pixel Rectangle2D.Float Shape at coordinate (0,0)
    public GameObject(){
        ObjectTransform = new AffineTransform();
        ObjectMaterial = new Material();
        ObjectShape = new Rectangle2D.Double(0, 0, 10, 10);
    }

    //TODO: Create the default GameObject, but with its AffineTransform translated to the coordinate (x,y)
    public GameObject(int x, int y){
        ObjectTransform = new AffineTransform();
        ObjectTransform.translate(x, y); // Translate to the specified coordinates
        ObjectMaterial = new Material();
        ObjectShape = new Rectangle2D.Float(0, 0, 10, 10);
    }

    //Engine Methods

    //TODO: Start all scripts on the object
    public void Start(){
        for (ScriptableBehavior script : ObjectScripts){
            script.Start();
        }
    }

    //TODO: Update all scripts on the object
    public void Update(){
        if (!IsActive) return;
        for (ScriptableBehavior script : ObjectScripts) {
            script.Update();
        }
    }

    //TODO: Adds a new scripted behavior at runtime.
    // Start the script and add it to the list.
    public void AddScript(ScriptableBehavior NewScript){
        ObjectScripts.add(NewScript);
        NewScript.Start();
    }

    //TODO: Draw the object by
    // 1) saving the renderer's old transform.
    // 2) transforming the renderer based on the GameObject's transform.
    // 3) based on the type of material, drawing either the styled shape or the image scaled to the bounds of the shape.
    // 4) returning the old transform to the renderer.
    public void Draw(Graphics2D renderer) {
        if (!IsActive) {return;}

        // 1) saving the renderer's old transform.
        AffineTransform oldTransform = renderer.getTransform();

        // 2) transforming the renderer based on the GameObject's transform.
        renderer.transform(ObjectTransform);

        // 3) Check if the material is a shape or an image and draw accordingly
        if (ObjectMaterial.IsShape){
            // Draw the shape with the style specified in Material
            renderer.setColor(ObjectMaterial.GetFill());
            renderer.fill(ObjectShape);
            renderer.setStroke(new BasicStroke(ObjectMaterial.GetBorderWidth())); // Set border thickness.
            renderer.setColor(ObjectMaterial.GetBorder()); // Set border color.
            renderer.draw(ObjectShape);
        }
        else { // material is not just a simple shape, but instead, it should be rendered as an image.
            // Draw the BufferedImage scaled to match the bounds of the Shape
            float sX = (float)ObjectShape.getBounds().width / ObjectMaterial.MaterialImage.getWidth();
            float sY = (float)ObjectShape.getBounds().height / ObjectMaterial.MaterialImage.getHeight();
            renderer.scale(sX, sY);
            renderer.drawImage(ObjectMaterial.MaterialImage, 0, 0, null);
//            renderer.scale(1/sX, 1/sY);
        }

//        if (GatorEngine.DEBUG){
//            renderer.setStroke(new BasicStroke(4));
//            renderer.setColor(Color.BLACK);
//            renderer.draw(ObjectShape);
//        }

        // 4) Reset the pen's AffineTransform back to the value saved earlier.
        renderer.setTransform(oldTransform);
    }

    //Movement/Collision Methods
    //TODO: Move the GameObject's transform by the provided values.
    public void Translate(float dX, float dY){
        ObjectTransform.translate(dX/ObjectTransform.getScaleX(), dY/ObjectTransform.getScaleY());
    }

    //TODO:
    public void MoveTo(float x, float y){
        float dX = x - (float) ObjectTransform.getTranslateX();
        float dY = y - (float) ObjectTransform.getTranslateY();
        Translate(dX, dY);
    }

    //TODO: Scale the GameObject's transform around the CENTER of its transformed shape.
    public void Scale(float sX, float sY){
       Shape s = ObjectTransform.createTransformedShape(ObjectShape);
       float w = (float) s.getBounds().getWidth();
       float h = (float) s.getBounds().getHeight();

       ObjectTransform.scale(sX, sY);

       Shape s2 = ObjectTransform.createTransformedShape(ObjectShape);
       float w2 = (float) s2.getBounds().getWidth();
       float h2 = (float) s2.getBounds().getHeight();

       ObjectTransform.translate(-(w2-w)/ObjectTransform.getScaleX()/2, -(h2-h)/ObjectTransform.getScaleY()/2);
    }

    //TODO: Returns true if the two objects are touching
    // i.e., the intersection of their Areas is not empty)
    public boolean CollidesWith(GameObject other){
        Area a1 = new Area(ObjectShape);
        a1.transform(ObjectTransform);
        Area a2 = new Area(other.ObjectShape);
        a2.transform(other.ObjectTransform);
        a1.intersect(a2);
        return !a1.isEmpty();
    }

    //TODO:Should return true if the transformed shape contains the point
    public boolean Contains(Point2D point){
        Area a1 = new Area(ObjectShape);
        a1.transform(ObjectTransform);
        return a1.contains(point);
    }

    //Getters and Setters
    //TODO: Done for you!
    public AffineTransform GetObjectTransform() {
        return ObjectTransform;
    }

    public void SetObjectTransform(AffineTransform objectTransform) {
        this.ObjectTransform = objectTransform;
    }

    public Shape GetShape() {
        return ObjectShape;
    }

    public void SetShape(Shape shape) {
        this.ObjectShape = shape;
    }

    public Material GetObjectMaterial() {
        return ObjectMaterial;
    }

    public void SetObjectMaterial(Material objectMaterial) {
        this.ObjectMaterial = objectMaterial;
    }

    public ArrayList<ScriptableBehavior> GetObjectScripts() {
        return ObjectScripts;
    }

    public void SetObjectScripts(ArrayList<ScriptableBehavior> objectScripts) {
        this.ObjectScripts = objectScripts;
    }

    public boolean IsActive(boolean b) {
        return IsActive;
    }

    public void SetActive(boolean active) {
        this.IsActive = active;
    }

    public double GetWidth() {
        Shape transformedShape = ObjectTransform.createTransformedShape(ObjectShape);
        Rectangle2D bounds = transformedShape.getBounds2D();
        return bounds.getWidth();
    }
}