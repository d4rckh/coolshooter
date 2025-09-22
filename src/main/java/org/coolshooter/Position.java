package org.coolshooter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.function.DoubleUnaryOperator;

@AllArgsConstructor
@Getter
@Setter
public class Position {
    private double x;
    private double y;

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(DoubleUnaryOperator updater) {
        this.setX(updater.applyAsDouble(this.x));
    }

    public void setY(DoubleUnaryOperator updater) {
        this.setY(updater.applyAsDouble(this.y));
    }
}
