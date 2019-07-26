package pacman.core;

import static java.lang.Math.sqrt;
import static java.lang.Math.pow;


public class Location {
    public final int x;
    public final int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Location location) {
        int dX = this.x - location.x;
        int dY = this.y - location.y;
        return sqrt(pow(dX,2) + pow(dY,2));
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Location) {
            Location that = (Location) other;
            return (this.x == that.x && this.y == that.y);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
      return ((13 * this.x) + (19  * this.y));
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}