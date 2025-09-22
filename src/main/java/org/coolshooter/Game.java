package org.coolshooter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

import org.coolshooter.domain.GameScene;
import org.coolshooter.entity.Entity;
import org.coolshooter.entity.EntityManager;
import org.coolshooter.entity.EntitySpawner;
import org.coolshooter.entity.player.UserPlayerEntity;
import org.coolshooter.entity.ui.UIButtonEntity;
import org.coolshooter.entity.ui.UIFpsText;
import org.coolshooter.entity.ui.UIInputFieldEntity;
import org.coolshooter.entity.ui.UILabelEntity;
import org.coolshooter.entity.ui.UIPlayerHealthText;
import org.coolshooter.timer.GameTimer;
import org.coolshooter.timer.TimerManager;

import java.awt.Color;

@Slf4j
public class Game {

    private final JPanel panel;

    @Getter
    private final Camera camera;

    @Getter
    private final EntityManager entityManager;

    @Getter
    private final EntitySpawner entitySpawner;

    @Getter
    private final TimerManager timerManager;

    private final int TARGET_FPS = 500;
    private final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS; // ns

    @Getter
    private GameScene scene = GameScene.MENU;

    @Getter
    private UserPlayerEntity userPlayerEntity;

    @Getter
    private final GameSettings gameSettings;

    public Game(JFrame frame) {
        this.panel = new GamePanel(this);
        this.camera = new Camera(0, 0);
        this.entityManager = new EntityManager(panel);
        this.entitySpawner = new EntitySpawner(this);
        this.timerManager = new TimerManager();
        this.gameSettings = new GameSettings();

        panel.setBackground(Color.BLACK);

        frame.add(panel);
        frame.setSize(900, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void setScene(GameScene newState) {
        this.scene = newState;

        if (newState == GameScene.PLAYING) {
            startGame();
        } else if (newState == GameScene.MENU) {
            showMenu();
        } else if (newState == GameScene.CUSTOMIZATION) {
            showCustomization();
        }
    }

    public void showCustomization() {
        // Clear old UI/game entities
        entityManager.clearEntities();
        timerManager.clearTimers();

        // === Title ===
        UILabelEntity title = new UILabelEntity(this, new DynamicPosition(
                panel,
                p -> p.getWidth() / 2.0 - 80,
                p -> p.getHeight() / 2.0 - 160), "Customization");
        entityManager.addEntity(title);

        // === NPC Spawn Interval ===
        UILabelEntity npcSpawnLabel = new UILabelEntity(this, new DynamicPosition(
                panel,
                p -> p.getWidth() / 2.0 - 180,
                p -> p.getHeight() / 2.0 - 100), "NPC Spawn Interval (s):");
        entityManager.addEntity(npcSpawnLabel);

        UIInputFieldEntity npcSpawnField = new UIInputFieldEntity(this, new DynamicPosition(
                panel,
                p -> p.getWidth() / 2.0 + 20,
                p -> p.getHeight() / 2.0 - 100), 100, UIInputFieldEntity.InputType.INTEGER);
        npcSpawnField.setText(String.valueOf(this.gameSettings.getNpcSpawnRate()));
        entityManager.addEntity(npcSpawnField);

        // === Knockback Strength ===
        UILabelEntity knockbackLabel = new UILabelEntity(this, new DynamicPosition(
                panel,
                p -> p.getWidth() / 2.0 - 180,
                p -> p.getHeight() / 2.0 - 40), "Knockback Strength:");
        entityManager.addEntity(knockbackLabel);

        UIInputFieldEntity knockbackField = new UIInputFieldEntity(this, new DynamicPosition(
                panel,
                p -> p.getWidth() / 2.0 + 20,
                p -> p.getHeight() / 2.0 - 40), 100, UIInputFieldEntity.InputType.INTEGER);
        entityManager.addEntity(knockbackField);

        // === Gun Cooldown ===
        UILabelEntity cooldownLabel = new UILabelEntity(this, new DynamicPosition(
                panel,
                p -> p.getWidth() / 2.0 - 180,
                p -> p.getHeight() / 2.0 + 20), "Gun Cooldown (ms):");
        entityManager.addEntity(cooldownLabel);

        UIInputFieldEntity cooldownField = new UIInputFieldEntity(this, new DynamicPosition(
                panel,
                p -> p.getWidth() / 2.0 + 20,
                p -> p.getHeight() / 2.0 + 20), 100, UIInputFieldEntity.InputType.INTEGER);
        entityManager.addEntity(cooldownField);

        // === Apply Button ===
        UIButtonEntity applyButton = new UIButtonEntity(
                this,
                new DynamicPosition(
                        panel,
                        p -> p.getWidth() / 2.0 - 60, // button width 120 -> center
                        p -> p.getHeight() / 2.0 + 80),
                "Apply",
                () -> {
                    try {
                        int npcInterval = Integer.parseInt(npcSpawnField.getText());
                        // double knockback = Double.parseDouble(knockbackField.getText());
                        // int cooldown = Integer.parseInt(cooldownField.getText());

                        // Apply settings to gameSettings
                        this.gameSettings.setNpcSpawnRate(npcInterval);
                        // this.gameSettings.setKnockbackStrength(knockback);
                        // this.gameSettings.setGunCooldown(cooldown);

                        log.info("Customization applied: NPC interval=" + npcInterval +
                                "s, Knockback=" + 0 + ", Cooldown=" + 0 + "ms");

                        this.setScene(GameScene.MENU);
                    } catch (NumberFormatException e) {
                        log.warn("Invalid customization input", e);
                    }
                });
        entityManager.addEntity(applyButton);
    }

    /** Returns mouse position in panel coordinates */
    public Position getPanelMouse() {
        var mouse = panel.getMousePosition();
        return (mouse != null) ? new Position(mouse.getX(), mouse.getY()) : null;
    }

    /** Initialize the game */
    void init() {
        camera.adjustZoom(panel.getWidth(), panel.getHeight(), 1000, 1000);

        panel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                camera.adjustZoom(panel.getWidth(), panel.getHeight(), 1000, 1000);
            }
        });

        showMenu();
        updateLoop();
    }

    private void showMenu() {
        entityManager.clearEntities();
        timerManager.clearTimers();

        // UISliderEntity volumeSlider = new UISliderEntity(this, new Position(200,
        // 400), 300);
        // this.entityManager.addEntity(volumeSlider);

        // UIInputFieldEntity nameField = new UIInputFieldEntity(this, new Position(200,
        // 300), 250);
        // this.entityManager.addEntity(nameField);

        entityManager.addEntity(new UIButtonEntity(
                this,
                new DynamicPosition(
                        panel,
                        p -> p.getWidth() / 2.0 - 100, // X: centered minus half button width
                        p -> p.getHeight() / 2.0 - 100 // Y: centered minus half button height
                ),
                "Play",
                () -> setScene(GameScene.PLAYING)));

        entityManager.addEntity(new UIButtonEntity(
                this,
                new DynamicPosition(
                        panel,
                        p -> p.getWidth() / 2.0 - 100, // X: centered minus half button width
                        p -> p.getHeight() / 2.0 - 25 // Y: centered minus half button height
                ),
                "Customize",
                () -> setScene(GameScene.CUSTOMIZATION)));

        entityManager.addEntity(new UIButtonEntity(
                this,
                new DynamicPosition(
                        panel,
                        p -> p.getWidth() / 2.0 - 100, // X stays same (aligned with "Start Game")
                        p -> p.getHeight() / 2.0 + 50 // Y: offset lower
                ),
                "Exit",
                () -> System.exit(0)));
    }

    private void startGame() {
        entityManager.clearEntities();
        timerManager.clearTimers();

        this.userPlayerEntity = new UserPlayerEntity(this);

        this.entitySpawner.spawnNPC();
        this.timerManager.addTimer(
                new GameTimer(() -> (double)this.gameSettings.getNpcSpawnRate(), true, e -> this.entitySpawner.spawnNPC()));

        entityManager.addEntity(userPlayerEntity);
        entityManager.addEntity(new UIPlayerHealthText(this));
        entityManager.addEntity(new UIFpsText(this));
    }

    /** Main game loop */
    private void updateLoop() {
        new Thread(() -> {
            long lastTime = System.nanoTime();

            while (true) {
                long now = System.nanoTime();
                double delta = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                update(delta);
                panel.repaint();

                long elapsed = System.nanoTime() - now;
                long sleepTime = (OPTIMAL_TIME - elapsed) / 1_000_000;
                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        log.error("FPS cap sleep interrupted", e);
                    }
                }
            }
        }).start();
    }

    /** Update all entities and handle collisions */
    private void update(double delta) {
        entityManager.update(delta);
        entityManager.handleCollisions();

        if (scene == GameScene.PLAYING && userPlayerEntity != null) {
            camera.centerOn(userPlayerEntity.getPosition(), panel.getWidth(), panel.getHeight());
        }

        this.timerManager.update(delta);
    }

    /** Queue a new entity safely */
    public void addEntity(Entity e) {
        entityManager.addEntity(e);
    }
}
