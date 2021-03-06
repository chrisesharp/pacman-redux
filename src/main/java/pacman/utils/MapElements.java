package pacman.utils;

import pacman.core.Location;
import java.util.Collection;
import java.util.Objects;

public class MapElements {
    public final Collection<MapElement> elements;
    public final int width;
    public final int height;

    public MapElements(Collection<MapElement> elements, int width, int height) {
        this.elements = elements;
        this.width = width;
        this.height = height;
    }

    public static class MapElement {
        public final Location location;
        public final String icon;

        public MapElement(Location location, String icon) {
            this.location = location;
            this.icon = icon;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MapElement that = (MapElement) o;
            return Objects.equals(location, that.location) &&
                    Objects.equals(icon, that.icon);
        }

        @Override
        public int hashCode() {
            return Objects.hash(location, icon);
        }

        @Override
        public String toString() {
            return "MapElement{" +
                    "location=" + location +
                    ", icon='" + icon + '\'' +
                    '}';
        }
    }
}
