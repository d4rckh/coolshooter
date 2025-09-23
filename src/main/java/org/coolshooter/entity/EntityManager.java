package org.coolshooter.entity;

import javax.swing.*;

import org.coolshooter.entity.common.BasicShapeCollidableEntity;
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
        // Make a snapshot of current entities
        List<Entity> snapshot = new ArrayList<>(entities);

        // Update all entities
        for (Entity e : snapshot) {
            if (!e.isDestroyed()) {
                e.update(delta);
            }
        }

        // Remove destroyed entities
        entities.removeIf(Entity::isDestroyed);

        // Initialize and add queued entities
        if (!entitiesToAdd.isEmpty()) {
            entitiesToAdd.forEach(Entity::init);
            entities.addAll(entitiesToAdd);
            entitiesToAdd.clear();
        }
    }

    /** Handle collisions between collidable entities */
    public void handleCollisions() {
        List<Collidable> collidables = entities.stream()
                .filter(e -> e instanceof Collidable && e instanceof Collidable)
                .filter(e -> !e.isDestroyed())
                .map(e -> (Collidable) e)
                .collect(Collectors.toList());

        for (int i = 0; i < collidables.size(); i++) {
            Collidable a = collidables.get(i);
            for (int j = i + 1; j < collidables.size(); j++) {
                Collidable b = collidables.get(j);
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
            if (!e.isDestroyed())
                e.destroy();
        }
        for (Entity e : entitiesToAdd) {
            if (!e.isDestroyed())
                e.destroy();
        }
        entities.clear();
        entitiesToAdd.clear();
    }

    /** Get current list of entities */
    public List<Entity> getEntities() {
        return Collections.unmodifiableList(new ArrayList<>(entities));
    }
}
