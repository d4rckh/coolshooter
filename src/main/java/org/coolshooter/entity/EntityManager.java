package org.coolshooter.entity;

import javax.swing.*;

import org.coolshooter.entity.common.RenderableCollidableEntity;
import org.coolshooter.entity.trait.Collidable;
import org.coolshooter.entity.trait.Controllable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EntityManager {

    private final List<Entity> entities = new ArrayList<>();
    private final List<Entity> entitiesToAdd = new ArrayList<>();

    private final JPanel panel;

    public EntityManager(JPanel panel) {
        this.panel = panel;
    }

    /** Add an entity safely (queued) */
    public void addEntity(Entity e) {
        entitiesToAdd.add(e);

        // register key bindings if controllable
        if (e instanceof Controllable controllable) {
            controllable.registerKeyBindings(panel);
        }
    }

    /** Update all entities and handle queued additions */
    public void update(double delta) {
        // Update entities
        for (Entity e : entities) {
            if (!e.isDestroyed()) {
                e.update(delta);
            }
        }

        // Initialize and add queued entities
        if (!entitiesToAdd.isEmpty()) {
            entitiesToAdd.forEach(Entity::init);
            entities.addAll(entitiesToAdd);
            entitiesToAdd.clear();
        }
    }

    /** Handle collisions between collidable entities */
    public void handleCollisions() {
        List<RenderableCollidableEntity> collidables = entities.stream()
                .filter(e -> e instanceof Collidable && e instanceof RenderableCollidableEntity)
                .filter(e -> !e.isDestroyed())
                .map(e -> (RenderableCollidableEntity) e)
                .collect(Collectors.toList());

        for (int i = 0; i < collidables.size(); i++) {
            RenderableCollidableEntity a = collidables.get(i);
            for (int j = i + 1; j < collidables.size(); j++) {
                RenderableCollidableEntity b = collidables.get(j);
                if (a.collidesWith(b)) {
                    a.onCollision(b);
                    b.onCollision(a);
                }
            }
        }
    }

    /** Destroy all entities */
    public void clearEntities() {
        for (Entity e : entities) {
            if (!e.isDestroyed()) e.destroy();
        }
        for (Entity e : entitiesToAdd) {
            if (!e.isDestroyed()) e.destroy();
        }
        entities.clear();
        entitiesToAdd.clear();
    }

    /** Get current list of entities */
    public List<Entity> getEntities() {
        return Collections.unmodifiableList(new ArrayList<>(entities));
    }
}
