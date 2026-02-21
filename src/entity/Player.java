package entity;

import Main.GamePanel;
import Main.KeyHandler;

import java.awt.*;

public class Player extends Entity {
    GamePanel gamePanel;

    KeyHandler KeyHandler;

    public Player(GamePanel gamePanel, KeyHandler KeyHandler)
    {
        this.gamePanel = gamePanel;
        this.KeyHandler = KeyHandler;

        setDefaultValues();
    }

    public void setDefaultValues()
    {
        x = 100;
        y = 100;
        speed = 4;
    }

    public void update()
    {
        if (KeyHandler.upPressed)
        {
            y = y - speed;
        }
        if (KeyHandler.downPressed)
        {
            y = y + speed;
        }
        if (KeyHandler.leftPressed)
        {
            x = x - speed;
        }
        if (KeyHandler.rightPressed)
        {
            x = x + speed;
        }
    }

    public void draw(Graphics2D g2)
    {
        // DRAW
        g2.setColor(Color.white);
        g2.fillRect(x, y, GamePanel.tileSize, GamePanel.tileSize);
    }
}
