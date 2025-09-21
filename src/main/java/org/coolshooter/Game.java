package org.coolshooter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

import org.coolshooter.domain.GameState;
import org.coolshooter.entity.Entity;
import org.coolshooter.entity.EntityManager;
import org.coolshooter.entity.EntitySpawner;
import org.coolshooter.entity.player.UserPlayerEntity;
import org.coolshooter.entity.ui.UIButtonEntity;
import org.coolshooter.entity.ui.UIFpsText;
import org.coolshooter.entity.ui.UIInputFieldEntity;
import org.coolshooter.entity.ui.UIPlayerHealthText;
import org.coolshooter.entity.ui.UISliderEntity;
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

    private final int TARGET_FPS = 200;
    private final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS; // ns

    @Getter
    private GameState state = GameState.MENU;

    @Getter
    private UserPlayerEntity userPlayerEntity;

    public Game(JFrame frame) {
        this.panel = new GamePanel(this);
        this.camera = new Camera(0, 0);
        this.entityManager = new EntityManager(panel);
        this.entitySpawner = new EntitySpawner(this);
        this.timerManager = new TimerManager();

        panel.setBackground(Color.BLACK);

        frame.add(panel);
        frame.setSize(900, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void setState(GameState newState) {
        this.state = newState;

        if (newState == GameState.PLAYING) {
            startGame();
        } else if (newState == GameState.MENU) {
            showMenu();
        }
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

        UISliderEntity volumeSlider = new UISliderEntity(this, new Position(200, 400), 300);
        this.entityManager.addEntity(volumeSlider);

        UIInputFieldEntity nameField = new UIInputFieldEntity(this, new Position(200, 300), 250);
        this.entityManager.addEntity(nameField);

        entityManager.addEntity(new UIButtonEntity(
                this,
                new Position(panel.getWidth() / 2 - 100, panel.getHeight() / 2 - 25),
                "Start Game",
                () -> setState(GameState.PLAYING)));

        entityManager.addEntity(new UIButtonEntity(
                this,
                new Position(panel.getWidth() / 2 - 100, panel.getHeight() / 2 + 50),
                "Exit",
                () -> {
                    System.exit(0);
                }));
    }

    private void startGame() {
        entityManager.clearEntities();
        timerManager.clearTimers();

        this.userPlayerEntity = new UserPlayerEntity(this);
        
        this.timerManager.addTimer(
            new GameTimer(2, true, e -> this.entitySpawner.spawnNPC())
        );

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

        if (state == GameState.PLAYING && userPlayerEntity != null) {
            camera.centerOn(userPlayerEntity.getPosition(), panel.getWidth(), panel.getHeight());
        }

        this.timerManager.update(delta);
    }

    /** Queue a new entity safely */
    public void addEntity(Entity e) {
        entityManager.addEntity(e);
    }
}
