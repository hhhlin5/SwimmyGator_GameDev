import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Application {
    //All the objects needed for the game minus the dynamically created obstacles
    static GameObject Gator, Background1, Background2, Floor, GatorHead;
    static GameObject Title, StartButton;
    static GameObject EndPopup, ResetButton;
    static GameObject Controller;
    static GameObject PlayTime, TotalTime;


    static int state = 0; // 0=title, 1=game running, 2=game ended
    static boolean isGameActive = false; // Flag to indicate if the game is active
    static int playTime = 0; // Total playtime in seconds

    public static void Start(){

        //GAME OBJECTS
        Background1 = new GameObject(0,0);
        Background1.SetShape(new Rectangle(0,0, GatorEngine.WIDTH, GatorEngine.HEIGHT));
        Background1.SetObjectMaterial(new Material("resources/Background.png"));
        GatorEngine.Create(Background1);

        Background2 = new GameObject(GatorEngine.WIDTH,0);
        Background2.SetShape(new Rectangle(0,0, GatorEngine.WIDTH, GatorEngine.HEIGHT));
        Background2.SetObjectMaterial(new Material("resources/Background.png"));
        GatorEngine.Create(Background2);

        // Assign BackgroundResetter scripts after both backgrounds are created
        Background1.AddScript(new BackgroundResetter(Background1, Background2));
        Background2.AddScript(new BackgroundResetter(Background2, Background1));

        Floor = new GameObject(0,GatorEngine.HEIGHT);
        Floor.SetShape(new Rectangle(0,0, GatorEngine.WIDTH, 30));
        Floor.SetObjectMaterial(new Material(new Color(244, 250, 252, 255), null, 0)); // Highlight color
        GatorEngine.Create(Floor);

        CreateGator();

        //TITLE OBJECTS
        Title = new GameObject(0,0);
        Title.SetShape(new Rectangle(0,0, GatorEngine.WIDTH, GatorEngine.HEIGHT));
        Title.SetObjectMaterial(new Material("resources/Title.png"));
        GatorEngine.Create(Title);

        EndPopup = new GameObject(0,0);
        EndPopup.SetShape(new Rectangle(0, 0, GatorEngine.WIDTH, GatorEngine.HEIGHT));
        EndPopup.SetObjectMaterial(new Material("resources/End.png"));
        GatorEngine.Create(EndPopup);

        StartButton = new GameObject(708, 400);
        StartButton.SetShape(new Rectangle2D.Double(0, 0, 300, 140));
        StartButton.SetObjectMaterial(new Material(new Color(50, 230, 53, 0), null, 0));
        StartButton.AddScript(new Button(StartButton, new Color(50, 230, 53, 0), new Color(50, 230, 53, 150), () -> GameScreen()));
        GatorEngine.Create(StartButton);

        ResetButton = new GameObject(705, 515);
        ResetButton.SetShape(new Rectangle2D.Double(0, 0, 300, 145));
        ResetButton.SetObjectMaterial(new Material(new Color(50, 230, 53, 0), null, 0));
        ResetButton.AddScript(new Button(ResetButton, new Color(50, 230, 53, 0), new Color(50, 230, 53, 150), () -> TitleScreen()));
        GatorEngine.Create(ResetButton);

        Controller = new GameObject(-10,-10);
        Controller.AddScript(new SwimmyGatorController(Controller));
        GatorEngine.Create(Controller);

        PlayTime = new GameObject(10,10);
        PlayTime.SetShape(new Rectangle2D.Double(0, 0, 200, 50));
        PlayTime.SetObjectMaterial(new Material(new Color(255, 255, 255, 0), null, 0));
        PlayTime.AddScript(new TimerScript(PlayTime));
        GatorEngine.Create(PlayTime);

        TotalTime = new GameObject(GatorEngine.WIDTH / 2 - 250, GatorEngine.HEIGHT / 2 - 50);
        TotalTime.SetShape(new Rectangle2D.Double(0, 0, 500, 100));
        TotalTime.SetObjectMaterial(new Material(new Color(255, 255, 255, 0), null, 0));
        GatorEngine.Create(TotalTime);

        TitleScreen();
    }

    private static void CreateGator(){
        GatorEngine.Delete(Gator);
        GatorEngine.Delete(GatorHead);

        Gator = new GameObject(100, GatorEngine.HEIGHT / 2);
        Gator.SetShape(new Rectangle2D.Double(0, 0, 290, 100));
        Gator.SetObjectMaterial(new Material("resources/Gator.png"));
        Gator.AddScript(new GatorController(Gator));
        GatorEngine.Create(Gator);

        GatorHead = new GameObject(110, GatorEngine.HEIGHT / 2);
        GatorHead.SetShape(new Rectangle2D.Double(228,20,50, 50));
        GatorHead.SetObjectMaterial(new Material(new Color(0,0,0,0), null, 0));
        Gator.AddScript(new GatorController(GatorHead));
        GatorEngine.Create(GatorHead);
    }

    private static GameObject CreateObstacle() {
        Random random = new Random();
        int obstacleType = random.nextInt(3);
        GameObject obstacle;

        switch (obstacleType) {
            case 0:
                obstacle = CreateGarbage();
                break;
            case 1:
                obstacle = CreateRock();
                break;
            case 2:
                obstacle = CreateSwimmer();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + obstacleType);
        }

        return obstacle;
    }

    private static GameObject CreateGarbage() {
        GameObject Garbage = new GameObject(GatorEngine.WIDTH,GatorEngine.HEIGHT/2 - 176/4);
        Garbage.SetShape(new Rectangle2D.Double(0, 0, 70, 70));
        Garbage.SetObjectMaterial(new Material("resources/Garbage.png"));
        Garbage.AddScript(new HorizontalScroll(Garbage));
        Garbage.AddScript(new HorizontalDestroy(Garbage));
        Garbage.AddScript(new GatorCollideable(Garbage));
        GatorEngine.Create(Garbage);
        return Garbage;
    }

    private static GameObject CreateRock() {
        Shape s = new Polygon(new int[]{77,155,0},new int[]{0,255,255},3);

        GameObject Rock = new GameObject(GatorEngine.WIDTH,GatorEngine.HEIGHT-275);
        Rock.SetShape(s);
        Rock.SetObjectMaterial(new Material("resources/Rock.png"));
        Rock.AddScript(new HorizontalScroll(Rock));
        Rock.AddScript(new HorizontalDestroy(Rock));
        Rock.AddScript(new GatorCollideable(Rock));
        GatorEngine.Create(Rock);
        return Rock;
    }

    private static GameObject CreateSwimmer() {
        Shape s = new Polygon(new int[]{0,363,200,50},new int[]{0,0,260,230},4);
        GameObject Swimmer = new GameObject(GatorEngine.WIDTH,0);
        Swimmer.SetShape(s);
        Swimmer.SetObjectMaterial(new Material("resources/Swimmer.png"));
        Swimmer.AddScript(new HorizontalScroll(Swimmer));
        Swimmer.AddScript(new HorizontalDestroy(Swimmer));
        Swimmer.AddScript(new GatorCollideable(Swimmer));
        GatorEngine.Create(Swimmer);
        return Swimmer;
    }

    //BELOW: scripts I used for my solution+brief(!) description
    //TODO: implement these and use to setup objects

    //horizontally translates the gameObject based on speed
    public static class HorizontalScroll extends ScriptableBehavior{
        static float speed = 5; // Speed at which the object moves horizontally
        HorizontalScroll(GameObject g) {
            super(g);
        }

        @Override
        public void Start(){
        }

        @Override
        public void Update(){
            if (!isGameActive) {
                return;
            }

            // Translate the gameObject to the left by the specified speed
            gameObject.Translate(-speed, 0);

            // If the object moves completely off-screen, we can optionally deactivate or destroy it
            if (gameObject.GetObjectTransform().getTranslateX() + gameObject.GetWidth() < 0) {
                gameObject.SetActive(false);
            }
        }
    }

    //deletes the game object when it is off screen
    public static class HorizontalDestroy extends ScriptableBehavior{
        HorizontalDestroy(GameObject g) {
            super(g);
        }

        @Override
        public void Start(){
        }

        @Override
        public void Update(){
            // Check if the game object is completely off the screen
            if (gameObject.GetObjectTransform().getTranslateX() + gameObject.GetWidth() < 0) {
                GatorEngine.Delete(gameObject);
            }
        }
    }

    //moves the gator, and looks for input to make it "jump"
    public static class GatorController extends ScriptableBehavior{
        float vY = 0;
        float gravity = 1.5f;
        float impulseForce = 8;

        GatorController(GameObject g) {
            super(g);
        }

        @Override
        public void Start() {
        }

        @Override
        public void Update() {
            if (!isGameActive || !gameObject.IsActive(true)) {
                return;
            }

            // Check if the space bar is being held
            if (Input.GetKeyHeld(' ')) {
                vY = -impulseForce;
            }
            else {
                vY += gravity;
            }
            // Move the player object based on the current vertical velocity
            gameObject.Translate(0, vY);


            // Check if the gator head hits the floor
            if (GatorHead.GetObjectTransform().getTranslateY() >= Floor.GetObjectTransform().getTranslateY() ) {
                System.out.println("Gator hit the floor! Game Over.");
                End();
            }
        }
    }

    //ends the game if the object collides with the gator head
    public static class GatorCollideable extends ScriptableBehavior{
        GatorCollideable(GameObject g) {
            super(g);
        }

        @Override
        public void Start() {
        }

        @Override
        public void Update() {
            if (!isGameActive || !gameObject.IsActive(true)) {
                return;
            }

            if (GatorHead.CollidesWith(gameObject)) {
                System.out.println("Collision detected with obstacle! Game Over.");
                End();
            }
        }
    }

    // checks when the backgrounds move off screen, and moves them to align with the other one
    public static class BackgroundResetter extends ScriptableBehavior {
        GameObject otherBackground;

        BackgroundResetter(GameObject g, GameObject otherBackground) {
            super(g);
            this.otherBackground = otherBackground;
        }

        @Override
        public void Start() {
        }

        @Override
        public void Update() {
            if (!isGameActive) {
                return;
            }

            gameObject.Translate(-HorizontalScroll.speed, 0);

            // Check if the background is completely off-screen and reset its position
            if (gameObject.GetObjectTransform().getTranslateX() + GatorEngine.WIDTH <= 0) {
                float otherBackgroundX = (float) otherBackground.GetObjectTransform().getTranslateX();
                gameObject.MoveTo(otherBackgroundX + GatorEngine.WIDTH - HorizontalScroll.speed, 0);
            }
        }
    }

    //if the game has started, counts frames to spawn objects periodically
    public static class SwimmyGatorController extends ScriptableBehavior{
        static ArrayList<GameObject> Obstacles = new ArrayList<>();
        static float FRAMECOUNT = 0;
        static int RATE = (int) GatorEngine.FRAMERATE;
        static float speedIncrement = 0.03f; // Increment in speed over time
        static float difficultyIncrement = 0.95f;

        SwimmyGatorController(GameObject g) {
            super(g);
        }

        @Override
        public void Start() {
        }

        @Override
        public void Update() {
            if (!isGameActive) {
                return;
            }
            FRAMECOUNT++;

            if (FRAMECOUNT >= RATE) {
                FRAMECOUNT = 0;
                GameObject newObstacle = CreateObstacle();
                Obstacles.add(newObstacle);

                // Gradually reduce the RATE to spawn obstacles more frequently
                RATE = Math.max((int) (RATE * difficultyIncrement), 10);
            }

            // Increment obstacle speed over time to increase difficulty
            HorizontalScroll.speed += speedIncrement;
        }

        //resets variables changes during the game
        public static void Reset(){
            for (GameObject obstacle : Obstacles) {
                GatorEngine.Delete(obstacle);
            }
            Obstacles.clear();
            FRAMECOUNT = 0;
            HorizontalScroll.speed = 5; // Reset speed to initial value
            RATE = (int) GatorEngine.FRAMERATE;
        }
    }

    //runs the runnable when the button is clicked
    //changes color when the button is hovered over by the mouse
    public static class Button extends ScriptableBehavior{
        Color c1, c2;
        Runnable r;


        Button(GameObject g, Color nohover, Color hover, Runnable r) {
            super(g);
            c1 = nohover;
            c2 = hover;
            this.r = r;
        }

        @Override
        public void Start() {
        }

        @Override
        public void Update() {
            Point2D mousePosition = Input.GetMousePosition();
            if (mousePosition != null && gameObject.Contains(mousePosition)) {
                gameObject.SetObjectMaterial(new Material(c2, null, 0));
                if (Input.IsMouseClicked()) {
                    r.run();
                    System.out.println("Game Started!");
                }
            }
            else {
                gameObject.SetObjectMaterial(new Material(c1, null, 0));
            }
        }
    }

    public static class TimerScript extends ScriptableBehavior {
        private long lastUpdateTime;

        TimerScript(GameObject g) {
            super(g);
        }

        @Override
        public void Start() {
            lastUpdateTime = System.currentTimeMillis();
        }

        @Override
        public void Update() {
            if (!isGameActive) {
                return;
            }

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime >= 1000) {
                playTime++;
                lastUpdateTime = currentTime;

                // Update timer display by creating a new material with the timer text as an image
                BufferedImage timerImage = createTimerImage("Time: " + playTime + "s");
                Material timerMaterial = new Material();
                timerMaterial.SetMaterialImage(timerImage);
                gameObject.SetObjectMaterial(timerMaterial);
            }
        }
        private BufferedImage createTimerImage(String text) {
            // Set the dimensions for the image
            int width = 200;  // Adjust as needed
            int height = 50;  // Adjust as needed
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            // Create a Graphics2D object to draw the text
            Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.BLACK); // Background color
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.WHITE); // Text color
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics metrics = g2d.getFontMetrics();
            int x = (width - metrics.stringWidth(text)) / 2;
            int y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();
            g2d.drawString(text, x, y);

            // Dispose of the Graphics2D object to release resources
            g2d.dispose();

            return image;
        }
    }


    //THESE ARE USED TO ACTIVATE/DEACTIVATE THE APPROPRIATE OBJECTS FOR EACH VIEW
    //TODO: implement

    //turns off all objects not on the title screen, and turns those ones on
    //deletes things made during the game
    //changes state
    public static void TitleScreen(){
        state = 0;
        isGameActive = false;
        Title.SetActive(true);
        StartButton.SetActive(true);

        EndPopup.SetActive(false);
        ResetButton.SetActive(false);
        Background1.SetActive(false);
        Background2.SetActive(false);
        Gator.SetActive(false);
        GatorHead.SetActive(false);
        PlayTime.SetActive(false);
        TotalTime.SetActive(false);

        SwimmyGatorController.Reset();
        playTime = 0; // Reset playtime
    }

    //turns off all objects not on the game screen, and turns those ones on
    //changes state
    public static void GameScreen(){
        state = 1;
        isGameActive = true;
        Title.SetActive(false);
        StartButton.SetActive(false);
        EndPopup.SetActive(false);
        ResetButton.SetActive(false);
        Background1.SetActive(true);
        Background2.SetActive(true);
        Gator.SetActive(true);
        GatorHead.SetActive(true);
        PlayTime.SetActive(true);
        TotalTime.SetActive(false);

        SwimmyGatorController.Reset();
        Controller.SetActive(true);
        SwimmyGatorController.Obstacles.clear();
    }

    //turns on the popup, changes state
    public static void End(){
        state = 2;
        isGameActive = false;

        // Update TotalTime with the final playTime value
        BufferedImage totalTimeImage = createTimerImage("Total Time: " + playTime + "s", false);
        Material totalTimeMaterial = new Material();
        totalTimeMaterial.SetMaterialImage(totalTimeImage);
        TotalTime.SetObjectMaterial(totalTimeMaterial);

        // Activate TotalTime
        TotalTime.SetActive(true);

        EndPopup.SetActive(true);
        ResetButton.SetActive(true);
        Gator.SetActive(true);
        GatorHead.SetActive(true);
        Background1.SetActive(true);
        Background2.SetActive(true);
        PlayTime.SetActive(false);
    }
    private static BufferedImage createTimerImage(String text, boolean withBackground) {
        // Set the dimensions for the image
        int width = 300;  // Adjust as needed
        int height = 50;  // Adjust as needed
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Create a Graphics2D object to draw the text
        Graphics2D g2d = image.createGraphics();

        if (withBackground) {
            g2d.setColor(Color.BLACK); // Background color
            g2d.fillRect(0, 0, width, height);
        }

        g2d.setColor(Color.WHITE); // Text color
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics metrics = g2d.getFontMetrics();
        int x = (width - metrics.stringWidth(text)) / 2;
        int y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();
        g2d.drawString(text, x, y);

        // Dispose of the Graphics2D object to release resources
        g2d.dispose();

        return image;
    }
}
