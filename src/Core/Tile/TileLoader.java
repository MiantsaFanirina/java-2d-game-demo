package Core.Tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;

public class TileLoader {

    private static final String DATA_PREFIX = "data:image/png;base64,";

    public Tile[] load(String filePath, int maxTiles) {
        Tile[] tiles = new Tile[maxTiles];

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean inTilesSection = false;

            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;

                if (trimmed.equals("TILES")) {
                    inTilesSection = true;
                    continue;
                }

                if (inTilesSection) {
                    Tile tile = parseTileLine(trimmed);
                    if (tile != null) {
                        tiles[tile.getId()] = tile;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tiles;
    }

    private Tile parseTileLine(String line) {
        String[] parts = line.split(":");
        if (parts.length < 4) return null;

        try {
            int tileId = Integer.parseInt(parts[0]);
            String name = parts[2];
            String base64Data = extractBase64(parts);

            BufferedImage image = decodeImage(base64Data);
            boolean hasCollision = name.toLowerCase().contains("wall") || 
                                  name.toLowerCase().contains("water");

            return new Tile(tileId, name, image, hasCollision);

        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String extractBase64(String[] parts) {
        StringBuilder builder = new StringBuilder();
        for (int i = 3; i < parts.length; i++) {
            if (i > 3) builder.append(":");
            builder.append(parts[i]);
        }
        String data = builder.toString();
        if (data.startsWith(DATA_PREFIX)) {
            data = data.substring(DATA_PREFIX.length());
        }
        return data;
    }

    private BufferedImage decodeImage(String base64Data) throws IOException {
        byte[] bytes = Base64.getDecoder().decode(base64Data);
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        BufferedImage image = ImageIO.read(input);
        input.close();
        return image;
    }
}
