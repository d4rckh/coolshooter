package org.coolshooter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {

    // Store loaded sprites by name
    private final Map<String, BufferedImage[]> sprites = new HashMap<>();
    // Later you can add: Map<String, Clip> sounds = new HashMap<>();

    public class Sprite {
        private final BufferedImage[] frames;

        public Sprite(BufferedImage[] frames) {
            this.frames = frames;
        }

        public BufferedImage getFrame(int index) {
            return frames[index % frames.length];
        }

        public int getFrameCount() {
            return frames.length;
        }
    }

    /**
     * Loads a sprite sheet and slices it into frames.
     * 
     * @param path resource path (inside resources folder, e.g. "/user_player.png")
     * @param cols number of columns
     * @param rows number of rows
     */
    private Sprite loadSpriteSheet(String path, int cols, int rows) {
        try (InputStream is = ResourceManager.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("Resource not found: " + path);
            }

            BufferedImage sheet = ImageIO.read(is);
            int frameWidth = sheet.getWidth() / cols;
            int frameHeight = sheet.getHeight() / rows;

            BufferedImage[] frames = new BufferedImage[cols * rows];
            int index = 0;
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < cols; x++) {
                    frames[index++] = sheet.getSubimage(
                            x * frameWidth,
                            y * frameHeight,
                            frameWidth,
                            frameHeight);
                }
            }
            return new Sprite(frames);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sprite sheet: " + path, e);
        }
    }

    /**
     * Initialize all game resources.
     */
    public void init() {
        // Load user_player.png (2 columns × 1 row)
        Sprite player = loadSpriteSheet("/user_player.png", 2, 1);
        sprites.put("player", player.frames);
        Sprite enemy1 = loadSpriteSheet("/enemy.png", 1, 2);
        sprites.put("enemy1", enemy1.frames);
        Sprite tank = loadSpriteSheet("/tank.png", 1, 2);
        sprites.put("tank", tank.frames);
        Sprite fast_chaser = loadSpriteSheet("/fast_chaser.png", 1, 2);
        sprites.put("fast_chaser", fast_chaser.frames);
    }

    /**
     * Retrieve a sprite by name.
     */
    public Sprite getSprite(String name) {
        BufferedImage[] frames = sprites.get(name);
        if (frames == null) {
            throw new RuntimeException("Sprite not loaded: " + name);
        }
        return new Sprite(frames);
    }
}
