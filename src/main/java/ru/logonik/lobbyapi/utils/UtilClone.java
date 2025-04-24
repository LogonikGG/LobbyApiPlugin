package ru.logonik.lobbyapi.utils;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class UtilClone {

    public static List<List<Location>> deepCopySpawns(List<List<Location>> original) {
        List<List<Location>> copy = new ArrayList<>();
        for (List<Location> team : original) {
            List<Location> teamCopy = new ArrayList<>();
            for (Location loc : team) {
                teamCopy.add(loc.clone());
            }
            copy.add(teamCopy);
        }
        return copy;
    }
}