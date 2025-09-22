package org.coolshooter;

import java.util.function.Function;
import javax.swing.JPanel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicPosition extends Position {

    private final Function<JPanel, Double> xSupplier;
    private final Function<JPanel, Double> ySupplier;
    private final JPanel panel;

    public DynamicPosition(JPanel panel,
                           Function<JPanel, Double> xSupplier,
                           Function<JPanel, Double> ySupplier) {
        super(0, 0);
        this.panel = panel;
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
    }

    @Override
    public double getX() {
        return xSupplier.apply(panel);
    }

    @Override
    public double getY() {
        return ySupplier.apply(panel);
    }

    @Override
    public void setX(double x) {
        throw new RuntimeException("Can't update dynamic position");
    }

    @Override
    public void setY(double y) {
        throw new RuntimeException("Can't update dynamic position");
    }
}
