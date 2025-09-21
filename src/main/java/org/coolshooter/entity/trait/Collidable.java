package org.coolshooter.entity.trait;

import org.coolshooter.entity.common.RenderableCollidableEntity;

public interface Collidable {
    void onCollision(RenderableCollidableEntity entity);

    boolean collidesWith(RenderableCollidableEntity other);
}
