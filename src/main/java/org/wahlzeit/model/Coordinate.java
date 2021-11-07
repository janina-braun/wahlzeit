package org.wahlzeit.model;

public class Coordinate {
    private double x;
    private double y;
    private double z;

    public Coordinate(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getDistance(Coordinate coordinates) {
        double d_x = Math.pow(this.x - coordinates.getX(), 2); //(x1 - x2)^2
        double d_y = Math.pow(this.y - coordinates.getY(), 2); //(y1 - y2)^2
        double d_z = Math.pow(this.z - coordinates.getZ(), 2); //(z1 - z2)^2

        return Math.sqrt(d_x + d_y + d_z);
    }

    public boolean isEqual(Coordinate coordinate) {
        if (this.x == coordinate.getX()) {
            if (this.y == coordinate.getY()) {
                return this.z == coordinate.getZ();
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return this.isEqual((Coordinate) o);
    }
}
