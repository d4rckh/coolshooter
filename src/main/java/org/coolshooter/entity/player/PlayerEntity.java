package org.coolshooter.entity.player;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

import org.coolshooter.Game;
import org.coolshooter.domain.GameState;
import org.coolshooter.entity.Gun;
import org.coolshooter.entity.common.RenderableCollidableEntity;

public abstract class PlayerEntity extends RenderableCollidableEntity {
    @Setter
    protected int speed = 500;
    protected double velX = 0;
    protected double velY = 0;
    @Setter
    @Getter
    protected int hp = 100;
    @Getter
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
        this.gun = new Gun(this);
    }

    public void knockback(double vx, double vy, double strength) {
        this.knockbackVX = vx * strength;
        this.knockbackVY = vy * strength;
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
                this.getGame().setState(GameState.MENU);
            }
        }
    }

    @Override
    public void onCollision(RenderableCollidableEntity entity) {
    }

    @Override
    public void render(Graphics g) {
        super.render(g); // draw the player

        // Draw health bar
        int barWidth = getWidth();
        int barHeight = 5;
        int x = (int) getScreenPosition().getX();
        int y = (int) getScreenPosition().getY() - barHeight - 2; // slightly above player

        // Background (grey)
        g.setColor(Color.GRAY);
        g.fillRect(x, y, barWidth, barHeight);

        // Foreground (green -> red depending on hp)
        float healthRatio = Math.max(0, (float) this.getHp() / this.getMaxHp());
        g.setColor(new Color(1 - healthRatio, healthRatio, 0)); // green to red
        g.fillRect(x, y, (int) (barWidth * healthRatio), barHeight);

        // Optional border
        g.setColor(Color.BLACK);
        g.drawRect(x, y, barWidth, barHeight);
    }
}
