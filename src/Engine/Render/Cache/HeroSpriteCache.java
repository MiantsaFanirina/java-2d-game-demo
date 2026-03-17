package Engine.Render.Cache;

import Core.Database.model.Hero;
import Core.Entity.Direction;
import Engine.Render.Utilities.ImageLoader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class HeroSpriteCache {
    private final java.util.Map<String, BufferedImage> cache = new java.util.HashMap<>();

    public BufferedImage getSprite(Hero hero, Direction direction, int frame) {
        String key = hero.getCharacterRow() + "_" + hero.getHairRow() + "_" + 
                      hero.getOutfitFile() + "_" + direction + "_" + frame;
        BufferedImage cached = cache.get(key);
        if (cached != null) return cached;

        BufferedImage composite = composeHeroSprite(hero, direction, frame);
        if (composite != null) {
            cache.put(key, composite);
        }
        return composite;
    }

    private BufferedImage composeHeroSprite(Hero hero, Direction direction, int frame) {
        // Base character body
        BufferedImage base = loadPart(
            "src/Resource/Characters/CharacterModel/Character Model.png",
            hero.getCharacterRow(), direction, frame
        );
        if (base == null) {
            base = loadPart(
                "src/Resource/Characters/CharacterModel/Character Model.png",
                0, direction, frame
            );
        }

        // Hair - load from RIGHT column and flip for LEFT direction
        Direction hairDir = direction;
        if (direction == Direction.LEFT) {
            hairDir = Direction.RIGHT;
        }
        BufferedImage hair = loadPart(
            "src/Resource/Characters/Hair/Hairs.png",
            hero.getHairRow(), hairDir, frame
        );
        if (hair == null) {
            hair = loadPart(
                "src/Resource/Characters/Hair/Hairs.png",
                0, hairDir, frame
            );
        }
        
        // Flip for LEFT direction
        if (hair != null && direction == Direction.LEFT) {
            hair = flipHorizontal(hair);
        }

        // Outfit (mapped to one of the six standard outfits)
        String outfitFile = mapOutfit(hero.getOutfitFile());
        BufferedImage outfit = loadOutfit(outfitFile, direction, frame);
        if (outfit == null) outfit = loadOutfit("Outfit1.png", direction, frame);

        // Composite: base -> outfit -> hair
        BufferedImage composite = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = composite.createGraphics();
        g.drawImage(base, 0, 0, null);
        if (outfit != null) g.drawImage(outfit, 0, 0, null);
        if (hair != null) g.drawImage(hair, 0, 0, null);
        g.dispose();
        return composite;
    }

    private BufferedImage loadPart(String path, int row, Direction direction, int frame) {
        BufferedImage sheet = ImageLoader.loadImage(path);
        if (sheet == null) return null;
        
        int spriteSize = 32;
        int maxRows = sheet.getHeight() / spriteSize;
        if (row < 0 || row >= maxRows) {
            // Try to find a valid row, else return null
            if (maxRows > 0) row = 0;
            else return null;
        }

        int colStart = getColumnOffset(direction);
        int x = (colStart + (frame - 1)) * spriteSize;
        int y = row * spriteSize;

        if (x + spriteSize > sheet.getWidth()) return null;
        return sheet.getSubimage(x, y, spriteSize, spriteSize);
    }

    private BufferedImage loadOutfit(String outfitFile, Direction direction, int frame) {
        BufferedImage sheet = ImageLoader.loadImage("src/Resource/Characters/Outfits/" + outfitFile);
        if (sheet == null) return null;
        
        int spriteSize = 32;
        int colStart = getColumnOffset(direction);
        int x = (colStart + (frame - 1)) * spriteSize;
        return sheet.getSubimage(x, 0, spriteSize, spriteSize);
    }

    private int getColumnOffset(Direction direction) {
        return switch (direction) {
            case DOWN -> 0;
            case RIGHT -> 6;
            case UP -> 12;
            case LEFT -> 18;
        };
    }

    private String mapOutfit(String outfitFile) {
        // Always use the generic outfits, mapping based on outfitFile string to ensure
        // different heroes get different outfits
        String[] outfits = {"Outfit1.png", "Outfit2.png", "Outfit3.png", "Outfit4.png", "Outfit5.png", "Outfit6.png"};
        if (outfitFile == null) return "Outfit1.png";
        int hash = Math.abs(outfitFile.hashCode());
        return outfits[hash % outfits.length];
    }
    
    private BufferedImage flipHorizontal(BufferedImage src) {
        BufferedImage flipped = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = flipped.createGraphics();
        g.drawImage(src, src.getWidth(), 0, -src.getWidth(), src.getHeight(), null);
        g.dispose();
        return flipped;
    }
}
