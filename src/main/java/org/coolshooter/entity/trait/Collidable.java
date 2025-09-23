package org.coolshooter.entity.trait;

import java.awt.Point;

import org.coolshooter.entity.common.RenderableCollidableEntity;

public interface Collidable {
    void onCollision(RenderableCollidableEntity entity);

    boolean collidesWith(RenderableCollidableEntity other);

    Point getIntersectionPoint(RenderableCollidableEntity other);
}
