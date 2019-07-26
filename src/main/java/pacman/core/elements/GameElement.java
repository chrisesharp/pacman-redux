package pacman.core.elements;

import pacman.core.Location;
import pacman.core.Colour;

public abstract class GameElement {
    private Location location;
    private String icon;
    private Colour colour = Colour.WHITE;

    public GameElement(Location location, String icon) {
        this.location = location;
        this.icon = icon;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Colour getColour() {
        return colour;
    }

    public Colour getEffect() {
        return Colour.DEFAULT;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isPassable(GameElement element) {
        return true;
    }

    public boolean playSound() {
        return false;
    }

}