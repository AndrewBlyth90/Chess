package com.chess.engine.board;


import java.util.Map;

public class BoardUtils {


    //Variables determining tiles for different columns
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] THIRD_COLUMN = initColumn(2);
    public static final boolean[] FOURTH_COLUMN = initColumn(3);
    public static final boolean[] FIFTH_COLUMN = initColumn(4);
    public static final boolean[] SIXTH_COLUMN = initColumn(5);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    //Variables determining tiles for different rows
    public static final boolean[] EIGHTH_RANK = initRow(0);
    public static final boolean[] SEVENTH_RANK = initRow(8);
    public static final boolean[] SIXTH_RANK = initRow(16);
    public static final boolean[] FIFTH_RANK = initRow(24);
    public static final boolean[] FOURTH_RANK = initRow(32);
    public static final boolean[] THIRD_RANK = initRow(40);
    public static final boolean[] SECOND_RANK = initRow(48);
    public static final boolean[] FIRST_RANK = initRow(56);


    public static final String[] ALGEBRAIC_NOTATION = initialiseAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initalisePositionToCoordinateMap();


    //Variables for constant numbers.
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;


    /**
     * Creates a column.
     *
     * @param columnNumber
     * @return
     */
    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[NUM_TILES];
        do {
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        }
        while (columnNumber < NUM_TILES);
        return column;
    }


    /**
     * Creates a row
     *
     * @param rowNumber
     * @return
     */
    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_TILES];
        do {
            row[rowNumber] = true;
            rowNumber++;

        } while (rowNumber % NUM_TILES_PER_ROW != 0);
        return row;

    }


    /**
     * Constructor for BoardUtils throwing error.
     */
    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate me!");
    }


    /**
     * Checks Coordinate is valid. Ie. Under 64
     *
     * @param coordinate
     * @return
     */
    public static boolean isValidTileCoordinate(final int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }

    public static int getCoordinateAtPosition(final String position){
        return POSITION_TO_COORDINATE.get(position);

    }

    public static int getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION[coordinate];
    }
}
