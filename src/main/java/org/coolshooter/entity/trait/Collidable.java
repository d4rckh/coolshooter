package org.coolshooter.entity.trait;

import java.awt.Point;
import java.awt.Shape;

import org.coolshooter.entity.common.BasicShapeCollidableEntity;

public interface Collidable {
    void onCollision(Collidable entity);

    boolean collidesWith(Collidable other);

    Shape getCollisionShape();

    Point getIntersectionPoint(Collidable other);
}
