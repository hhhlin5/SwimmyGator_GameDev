import java.awt.*;
import java.awt.geom.Rectangle2D;

//Abstract class for "scripts"
//TODO: Done for you!
public abstract class ScriptableBehavior {
    //All scripts will have some shared attributes and functions

    GameObject gameObject;//the object a script is attached to; allows modification of its data by the script

    ScriptableBehavior(GameObject g){
        gameObject=g;
    }

    //Start(), sets up anything required by the script;
    public abstract void Start();

    //Update(), performs actions each frame
    public abstract void Update();
}

//
//// New ScriptableBehavior class to handle Start button behavior
//class StartButtonScript extends ScriptableBehavior {
//    boolean gameStart = false;
//    public StartButtonScript(GameObject g) {
//        super(g);
//    }
//
//    @Override
//    public void Start() {
//    }
//
//    @Override
//    public void Update() {
//        // Check if mouse is over the button
//        Point mousePosition = Input.GetMousePosition();
//        if (mousePosition != null && gameObject.Contains(mousePosition)) {
//            gameObject.SetObjectMaterial(new Material(new Color(50, 230, 53, 150), null, 0)); // Highlight color
//            if (Input.IsMouseClicked()) {
//                gameStart = true;
//                System.out.println("Game Started!");
//            }
//        }
//        else {
//            gameObject.SetObjectMaterial(new Material(new Color(50, 200, 90, 0), null, 0)); // Normal color
//        }
//
//        if (gameStart) {
//            // Setting up the new game background
//            GameObject gameBackground = new GameObject(0, 0);
//            gameBackground.SetShape(new Rectangle(0, 0, GatorEngine.WIDTH, GatorEngine.HEIGHT));
//            gameBackground.SetObjectMaterial(new Material("resources/Background.png"));
//            GatorEngine.Create(gameBackground);
//
//            // Creating a new game object (The Gator)
//            GameObject playerObject = new GameObject(100, 300);
//            playerObject.SetShape(new Rectangle2D.Double(0, 0, 290, 100));
//            playerObject.SetObjectMaterial(new Material("resources/Gator.png"));
//
//            // Create other Obstacles
//            PlayerMovementScript playerMovementScript = new PlayerMovementScript(playerObject);
//            playerObject.AddScript(playerMovementScript);
//            GatorEngine.Create(playerObject);
//
//            gameStart = false;
//        }
//    }
//}
//
//// New ScriptableBehavior class to handle Start button behavior
//class EndButtonScript extends ScriptableBehavior {
//    boolean gameEnd = false;
//    public EndButtonScript(GameObject g) {
//        super(g);
//    }
//
//    @Override
//    public void Start() {
//    }
//
//    @Override
//    public void Update() {
//        // Check if mouse is over the button
//        Point mousePosition = Input.GetMousePosition();
//        if (mousePosition != null && gameObject.Contains(mousePosition)) {
//            gameObject.SetObjectMaterial(new Material(new Color(50, 230, 53, 150), null, 0)); // Highlight color
//            if (Input.IsMouseClicked()) {
//                gameEnd = true;
//                System.out.println("Game Ended!");
//            }
//        } else {
//            gameObject.SetObjectMaterial(new Material(new Color(50, 200, 90, 0), null, 0)); // Normal color
//        }
//
//        if (gameEnd) {
//            // Setting up the background
//            GameObject background = new GameObject(0, 0);
//            background.SetShape(new Rectangle(0, 0, GatorEngine.WIDTH, GatorEngine.HEIGHT));
//            background.SetObjectMaterial(new Material("resources/Title.png"));
//            GatorEngine.Create(background);
//
//            // Setting up the Start button
//            SwimmyGator.startButton = new GameObject(708, 400);
//            SwimmyGator.startButton.SetShape(new Rectangle2D.Double(0, 0, 300, 140));
//            GatorEngine.Create(SwimmyGator.startButton);
//
//            // Adding StartButtonScript to handle the button behavior
//            StartButtonScript startButtonScript = new StartButtonScript(SwimmyGator.startButton);
//            SwimmyGator.startButton.AddScript(startButtonScript);
//
//            gameEnd = false;
//        }
//    }
//}
//
