package com.halfnet.tickeytackeytoe.game;

public enum TilePosition {
    TOP_LEFT, TOP_MIDDLE, TOP_RIGHT,
    MIDDLE_LEFT, CENTER, MIDDLE_RIGHT,
    BOTTOM_LEFT, BOTTOM_MIDDLE, BOTTOM_RIGHT;

    public static TilePosition getByOrdinal(int ord) {
        for (TilePosition tp : TilePosition.values()) {
            if (tp.ordinal() == ord) {
                return tp;
            }
        }
        throw new IllegalArgumentException("Invalid Ordinal");
    }
}
