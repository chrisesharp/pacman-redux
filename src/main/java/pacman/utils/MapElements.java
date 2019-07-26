package pacman.utils;

import pacman.core.Location;
import pacman.core.elements.*;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Objects;

public class MapElements {

    public final Collection<MapElement> gates;
    public final Collection<MapElement> ghosts;
    public final Collection<MapElement> pacman;
    public final Collection<MapElement> pills;
    public final Collection<MapElement> powerPills;
    public final Collection<MapElement> tunnels;
    public final Collection<MapElement> walls;
    public final int width;
    public final int height;

    public MapElements(Collection<MapElement> elements, int width, int height) {
        this.gates = filter(elements, Gate::isGate);
        this.ghosts = filter(elements, Ghost::isGhost);
        this.pacman = filter(elements, Pacman::isPacman);
        this.pills = filter(elements, Pill::isPill);
        this.powerPills = filter(elements, PowerPill::isPowerPill);
        this.tunnels = filter(elements, Tunnel::isTunnel);
        this.walls = filter(elements, Wall::isWall);
        this.width = width;
        this.height = height;
    }

    private static Collection<MapElements.MapElement> filter(Collection<MapElements.MapElement> elements, Predicate<String> filterFunc) {
        return elements.stream()
                .filter(element -> filterFunc.test(element.icon))
                .collect(Collectors.toSet());
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
