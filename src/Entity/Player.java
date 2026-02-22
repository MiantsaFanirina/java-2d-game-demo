package Entity;

import Main.GamePanel;
import Main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity {
    GamePanel gamePanel;

    KeyHandler KeyHandler;

    private int targetX = -1;
    private int targetY = -1;
    private boolean hasTarget = false;
    private String targetDirection = "down";

    public Player(GamePanel gamePanel, KeyHandler KeyHandler)
    {
        this.gamePanel = gamePanel;
        this.KeyHandler = KeyHandler;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues()
    {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage () {
        try {
            up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/boy_up_1.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/boy_up_2.png")));
            down1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/boy_down_1.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/boy_down_2.png")));
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/boy_left_1.png")));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/boy_left_2.png")));
            right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/boy_right_1.png")));
            right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Player/boy_right_2.png")));

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setTargetPosition(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.hasTarget = true;
        
        int dx = targetX - (int) x;
        int dy = targetY - (int) y;
        
        if (Math.abs(dx) > Math.abs(dy)) {
            targetDirection = dx > 0 ? "right" : "left";
        } else if (Math.abs(dy) > Math.abs(dx)) {
            targetDirection = dy > 0 ? "down" : "up";
        } else if (dx != 0) {
            targetDirection = dx > 0 ? "right" : "left";
        }
    }

    public void update()
    {
        boolean isMoving = false;

        if (hasTarget) {
            int dx = targetX - (int) x;
            int dy = targetY - (int) y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > speed) {
                double ratioX = (double) dx / distance;
                double ratioY = (double) dy / distance;
                
                direction = targetDirection;
                
                x += ratioX * speed;
                y += ratioY * speed;
                isMoving = true;
            } else {
                x = targetX;
                y = targetY;
                hasTarget = false;
                targetX = -1;
                targetY = -1;
            }
        }
        else if(KeyHandler.upPressed || KeyHandler.downPressed || KeyHandler.leftPressed || KeyHandler.rightPressed)
        {
            if (KeyHandler.upPressed)
            {
                direction = "up";
                y = y - speed;
            }
            if (KeyHandler.downPressed)
            {
                direction = "down";
                y = y + speed;
            }
            if (KeyHandler.leftPressed)
            {
                direction = "left";
                x = x - speed;
            }
            if (KeyHandler.rightPressed)
            {
                direction = "right";
                x = x + speed;
            }
            isMoving = true;
        }

        if (isMoving) {
            spriteCounter = spriteCounter + 1;
            if (spriteCounter > 10)
            {
                if(spriteNum == 1)
                {
                    spriteNum = 2;
                }
                else  if(spriteNum == 2)
                {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2)
    {
        // DRAW
        BufferedImage image = null;
        switch (direction) {
            case "up":
                if (spriteNum == 1)
                {
                    image = up1;
                }

                if (spriteNum == 2)
                {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNum == 1)
                {
                    image = down1;
                }

                if (spriteNum == 2)
                {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNum == 1)
                {
                    image = left1;
                }

                if (spriteNum == 2)
                {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNum == 1)
                {
                    image = right1;
                }

                if (spriteNum == 2)
                {
                    image = right2;
                }
                break;
        }

        g2.drawImage(image, (int) x, (int) y, GamePanel.tileSize, GamePanel.tileSize, null);

    }
}
