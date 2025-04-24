package ru.logonik.lobbyapi.utils;

import java.util.ArrayList;
import java.util.List;

public class UtilList {
    public static <O> void addMissingLists(List<List<O>> list, int size) {
        while (list.size() < size) {
            list.add(new ArrayList<>());
        }
    }
}
