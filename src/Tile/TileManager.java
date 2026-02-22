package Tile;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class TileManager {

    GamePanel gamePanel;
    Tile[] tile;

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        tile = new Tile[10];
        getTileImage();
    }

    public void getTileImage() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Tiles/grass.png")));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Tiles/wall.png")));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Tiles/water.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        // DRAW GRASS BACKGROUND
        for (int i = 0; i < GamePanel.maxScreenRow + 1; i++) {
            for (int j = 0; j < GamePanel.maxScreenCol + 1; j++) {
                int tileNum = 0;
                g2.drawImage(tile[tileNum].image, j * GamePanel.tileSize, i * GamePanel.tileSize, GamePanel.tileSize, GamePanel.tileSize, null);
            }
        }

    }

}
