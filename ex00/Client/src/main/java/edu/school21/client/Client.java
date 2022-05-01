package edu.school21.client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import sun.java2d.opengl.OGLRenderQueue;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.net.*;
import java.sql.DriverManager;


public class Client extends Application {

    private double height;
    private double width;
    private Long playerId;
    private Long enemyId;
    private int playerLife;
    private int enemyLife;
    private String request;
    private ResultSet resultSet;

    public Image background(String name) throws IOException {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(name).getFile());
        Image image = new Image(new FileInputStream(file));

        height = image.getHeight();
        width = image.getWidth();
        return image;
    }

    public Image image(String name) throws IOException {

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(name).getFile());
        Image image = new Image(new FileInputStream(file));

        return image;
    }


        @Override
        public void start(Stage stage) throws Exception {

           /* Client client = new Client();
            Socket clientSocket = new Socket("127.0.0.1", 8081);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String driver = reader.readLine();
            String url = reader.readLine();
            String username = reader.readLine();
            String password = reader.readLine();
            Long playerId = Long.parseLong(reader.readLine());
            Long enemyId = Long.parseLong(reader.readLine());
            */
            String driver = "org.postgresql.Driver";
            String url = "jdbc:postgresql://localhost:5432/database";
            String username = "postgres";
            String password = "qwerty007";
            playerId = 1L;
            enemyId = 2L;

            Connection connection = null;
            Statement statement = null;
            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url, username, password);
            } catch (Exception e) {
                System.out.println(e);
            }
            statement = connection.createStatement();




                //Setting the image view
                Image field = background("images/field.png");
                Image player = image("images/player.png");
                Image enemy = image("images/enemy.png");
                Image playerBorder = image("images/border.png");
                Image enemyBorder = image("images/border.png");
                Image playerHealth = image("images/life.png");
                Image enemyHealth = image("images/life.png");
                Image playerBullet = image("images/playerBullet.png");
                Image enemyBullet = image("images/enemyBullet.png");
                //Creating a Group object
                stage.setTitle("Tanks");
                StackPane root = new StackPane();
                Scene scene = new Scene(root, width, height);
                stage.setScene(scene);
                final Canvas canvas = new Canvas(width, height);
                root.getChildren().addAll(canvas);

            ArrayList<String> input = new ArrayList<String>();

            scene.setOnKeyPressed(
                    new EventHandler<KeyEvent>()
                    {
                        public void handle(KeyEvent e)
                        {
                            String code = e.getCode().toString();
                            if ( !input.contains(code) )
                                input.add( code );
                        }
                    });

            scene.setOnKeyReleased(
                    new EventHandler<KeyEvent>()
                    {
                        public void handle(KeyEvent e)
                        {
                            String code = e.getCode().toString();
                            input.remove( code );
                        }
                    });

                GraphicsContext graphicsContext = canvas.getGraphicsContext2D();


                //Creating a scene object
enemyLife = 100;
playerLife = 90;


            new AnimationTimer()
            {
                /*request = "select health from players where id =" + playerId + ";";
                resultSet = statement.executeQuery(request);
            while(resultSet.next())
                playerLife = resultSet.getInt("health");
                request = "select health from players where id = " + enemyId + ";";
                resultSet = statement.executeQuery(request);
            while(resultSet.next())
                enemyLife = resultSet.getInt("health");

                 */



                int x1 = (int)(width / 2);
                int x2 = (int)(width / 2);
                int direction = 0;



                double bulletY = height - 100;
                double bulletX = 0;
                boolean bulletExist = false;

                double enemyBulletY;
                double enemyBulletX;

                public void handle(long currentNanoTime)
                {
                    x2++;
                    if (input.contains("LEFT")) {
                        x1 -= 5;
                        direction = 0;
                    }
                    if (input.contains("RIGHT")) {
                        x1 += 5;
                        direction = 1;
                    }
                    if (input.contains("DOWN")) {
                        if (direction == 0)
                            x1 -= 5;
                        else if (direction == 1)
                            x1 += 5;
                    }

                    if (input.contains("SPACE")) {
                        bulletY = height - 100 - player.getHeight();
                        bulletX = x1 + player.getWidth() * 0.5;
                        bulletExist = true;
                    } else if (bulletExist) {
                        if (bulletY == 0)
                            bulletExist = false;
                        else
                            bulletY -= 5;
                    }
                    if ((enemyBulletX > x1 - player.getWidth() * 0.5 && enemyBulletX < x1 + player.getWidth() * 0.5)
                            && (enemyBulletY > height - 200 - player.getHeight() && enemyBulletY < height - 200 )) {
                        bulletExist = false;
                        playerLife -= 5;
                    }

                    if (x1 <= 10)
                        x1 = 10;
                    if (x1 >= (int)width - 100)
                        x1 = (int)width - 100;
                    if (x2 <= 10)
                        x2 = (int)width - 10;
                    if (x2 >= (int)width - 10)
                        x2 = 10;

                    graphicsContext.drawImage(field, 0, 0);
                    graphicsContext.drawImage(player, x1, height - 200);
                    graphicsContext.drawImage(enemy, x2, 100);
                    graphicsContext.drawImage(playerHealth, 55, height - 45, ((width / 4 - 10) * playerLife) / 100, 30);
                    graphicsContext.drawImage(enemyHealth, width - 300, 15, ((width / 4 - 10) * enemyLife) / 100, 30);
                    graphicsContext.drawImage(playerBorder, 50, height - 50, width / 4, 35);
                    graphicsContext.drawImage(enemyBorder, width - 305, 10, width / 4, 35);
                    if (bulletExist)
                        graphicsContext.drawImage(playerBullet, bulletX, bulletY);
                    if (playerLife == 0) {
                        graphicsContext.setFill(Color.WHITE);
                        graphicsContext.fillRect(width * 0.5 - 300, height * 0.5 - 300, 500.0, 500.0);
                        graphicsContext.setFill(Color.RED);
                        graphicsContext.fillText("YOU LOST!", width * 0.5 - 100, height * 0.5 - 150);
                        graphicsContext.fillText("Shots: ", width * 0.5 - 100, height * 0.5 - 100);
                        graphicsContext.fillText("Hits: ", width * 0.5 - 100, height * 0.5 - 50);
                        graphicsContext.fillText("Misses: ", width * 0.5 - 100, height * 0.5);

                    } else if (enemyLife == 0) {
                        graphicsContext.setFill(Color.WHITE);
                        graphicsContext.fillRect(width * 0.5 - 300, height * 0.5 - 300, 500.0, 500.0);
                        graphicsContext.setFill(Color.BLUE);
                        graphicsContext.fillText("YOU WON!", width * 0.5 - 100, height * 0.5 - 150);
                        graphicsContext.fillText("Shots: ", width * 0.5 - 100, height * 0.5 - 100);
                        graphicsContext.fillText("Hits: ", width * 0.5 - 100, height * 0.5 - 50);
                        graphicsContext.fillText("Misses: ", width * 0.5 - 100, height * 0.5);
                    }

                }
            }.start();
                stage.show();
        }
        public static void main(String args[]){

            launch(args);
        }
    }
