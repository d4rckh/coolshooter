package org.coolshooter.entity.player;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

import org.coolshooter.Game;
import org.coolshooter.domain.GameScene;
import org.coolshooter.entity.common.RenderableCollidableEntity;
import org.coolshooter.entity.gun.Gun;

@Slf4j
@Getter
public abstract class PlayerEntity extends RenderableCollidableEntity {
    @Setter
    protected int speed = 600;
    protected double velX = 0;
    protected double velY = 0;
    @Setter
    protected int hp = 100;
    @Setter
    protected int maxHp = 100;
    protected Gun gun;
    private double knockbackVX = 0;
    private double knockbackVY = 0;

    public PlayerEntity(Game game, double worldX, double worldY) {
        super(game, worldX, worldY);
    }

    @Override
    public void init() {
    }

    public void knockback(double vx, double vy, double strength) {
        this.knockbackVX = vx * strength;
        this.knockbackVY = vy * strength;
    }

    public boolean heal(int hp) {
        if (this.getHp() + hp > this.getMaxHp())
            return false;

        this.setHp(this.getHp() + hp);
        return true;
    }

    @Override
    public void update(double delta) {
        double vx = velX;
        double vy = velY;

        // Normalize diagonal movement
        double length = Math.sqrt(vx * vx + vy * vy);
        if (length > 0) {
            vx /= length;
            vy /= length;
        }

        // Apply movement
        double dx = (vx * speed + knockbackVX) * delta;
        double dy = (vy * speed + knockbackVY) * delta;

        getPosition().setX(getPosition().getX() + dx);
        getPosition().setY(getPosition().getY() + dy);

        // Decay knockback (friction)
        double decay = 5.0; // higher = stops faster
        knockbackVX -= knockbackVX * decay * delta;
        knockbackVY -= knockbackVY * decay * delta;

        // Gun update
        if (gun != null)
            gun.update(delta);
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (hp <= 0) {
            this.destroy();

            if (this instanceof UserPlayerEntity) {
                this.getGame().setScene(GameScene.MENU);
            }
        }
    }

    @Override
    public void onCollision(RenderableCollidableEntity entity) {
    }

    @Override
    public void render(Graphics g) {
        super.render(g); // draw the player

        // Health bar dimensions
        int barWidth = (int) (getWidth() * getGame().getCamera().getZoom());
        int barHeight = (int) (7 * getGame().getCamera().getZoom());

        // Position above the player's sprite
        int x = (int) getScreenPosition().getX();
        int y = (int) (getScreenPosition().getY() - 15);

        // Background (grey)
        g.setColor(Color.GRAY);
        g.fillRect(x, y, barWidth, barHeight);

        // Foreground (green -> red depending on hp)
        float healthRatio = Math.max(0, (float) this.getHp() / this.getMaxHp());
        g.setColor(new Color(1 - healthRatio, healthRatio, 0)); // red to green
        g.fillRect(x, y, (int) (barWidth * healthRatio), barHeight);

        // Optional border
        g.setColor(Color.BLACK);
        g.drawRect(x, y, barWidth, barHeight);
    }
}
