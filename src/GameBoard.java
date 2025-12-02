import java.io.FileWriter;
import java.io.File;
import java.io.BufferedWriter;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Main class that simulates the insect moving game.
 * Reads input data, validates it, simulates insect movements, and writes results to output file.
 */
public class GameBoard {
    private static Board boardData;
    private static List<EliminatedInsect> eliminatedInsects = new ArrayList<>();
    private static int boardSize;

    private static final int MIN_BOARD_SIZE = 4;
    private static final int MAX_BOARD_SIZE = 1000;
    private static final int MIN_INSECT_NUMBER = 1;
    private static final int MAX_INSECT_NUMBER = 16;
    private static final int MIN_FOOD_NUMBER = 1;
    private static final int MAX_FOOD_NUMBER = 200;

    /**
     * Checks if there are duplicate insects (same type and color) on the board.
     *
     * @return true if duplicate insects are found, false otherwise
     */
    private static boolean isDuplicate() {
        for (int i = 0; i < boardData.getLastIdxOfInsect() + 1; ++i) {
            Insect insect = (Insect) boardData.getEntity(i);
            for (int j = i + 1; j < boardData.getLastIdxOfInsect() + 1; ++j) {
                Insect insect2 = (Insect) boardData.getEntity(j);
                if (Insect.defineNameOfInstance(insect).equals(Insect.defineNameOfInstance(insect2))
                        && insect.getColorName().equals(insect2.getColorName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if any two entities share the same position on the board.
     *
     * @return true if entities share the same position, false otherwise
     */
    private static boolean isTheSamePosition() {
        for (int i = 0; i < boardData.getLastIdx() + 1; ++i) {
            EntityPosition entity = boardData.getEntity(i).entityPosition;
            for (int j = i + 1; j < boardData.getLastIdx() + 1; ++j) {
                if (boardData.getEntity(j) == null) {
                    System.out.println("wtf");
                }
                EntityPosition entity2 = boardData.getEntity(j).entityPosition;
                if (entity.getX() == entity2.getX() && entity.getY() == entity2.getY()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Reads and validates input data from input.txt file.
     *
     * @return true if data is successfully read and validated, false if errors occur
     */
    private static boolean readData() {
        try {
            Scanner input = new Scanner(new File("input.txt"));
            boardSize = input.nextInt();
            if (boardSize < MIN_BOARD_SIZE || boardSize > MAX_BOARD_SIZE) {
                throw new InvalidBoardSizeException();
            }
            int insectsNumber = input.nextInt();
            if (insectsNumber < MIN_INSECT_NUMBER || insectsNumber > MAX_INSECT_NUMBER) {
                throw new InvalidNumberOfInsectsException();
            }
            int foodNumber = input.nextInt();
            if (foodNumber < MIN_FOOD_NUMBER || foodNumber > MAX_FOOD_NUMBER) {
                throw new InvalidNumberOfFoodPointsException();
            }
            boardData = new Board(boardSize);

            // Reading insects
            for (int i = 0; i < insectsNumber; ++i) {
                InsectColor color = InsectColor.toColor(input.next());
                if (color == InsectColor.NONE) {
                    throw new InvalidInsectColorException();
                }
                String name = input.next().toLowerCase();
                int y = input.nextInt();
                int x = input.nextInt();
                EntityPosition position = new EntityPosition(x, y);
                switch (name) {
                    case "grasshopper":
                        boardData.addEntity(new Grasshopper(position, color));
                        break;
                    case "butterfly":
                        boardData.addEntity(new Butterfly(position, color));
                        break;
                    case "ant":
                        boardData.addEntity(new Ant(position, color));
                        break;
                    case "spider":
                        boardData.addEntity(new Spider(position, color));
                        break;
                    default:
                        throw new InvalidInsectTypeException();
                }
                if (x < 1 || y < 1 || x > boardSize || y > boardSize) {
                    throw new InvalidEntityPositionException();
                }
            }

            // Reading food
            for (int i = 0; i < foodNumber; ++i) {
                int foodAmount = input.nextInt();
                int y = input.nextInt();
                int x = input.nextInt();
                EntityPosition position = new EntityPosition(x, y);
                boardData.addEntity(new FoodPoint(position, foodAmount));
            }

            if (isDuplicate()) {
                throw new DuplicateInsectException();
            }
            if (isTheSamePosition()) {
                throw new TwoEntitiesOnSamePositionException();
            }

        } catch (Exception e) {
            try {
                BufferedWriter output = new BufferedWriter(new FileWriter("output.txt"));
                output.write(e.getMessage());
                output.close();
                return false;
            } catch (Exception ex) {
                // Ignore
            }
        }
        return true;
    }

    /**
     * Main method that orchestrates the game simulation.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Reading and checking validness
        boolean resultOfDataReading = readData();
        if (!resultOfDataReading) {
            return;
        }

        // Moving
        for (int i = 0; i < boardData.getLastIdxOfInsect() + 1; ++i) {
            Insect myInsect = (Insect) boardData.getEntity(i);
            Direction bestDirection = myInsect.getBestDirection(boardData, boardSize);
            int gatheredValue = myInsect.travelDirection(bestDirection, boardData, boardSize);
            boardData.removeEntity(i);
            eliminatedInsects.add(new EliminatedInsect(myInsect.getColorName(),
                    Insect.defineNameOfInstance(myInsect), bestDirection, gatheredValue));
        }
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter("output.txt"));
            for (EliminatedInsect eliminatedInsect : eliminatedInsects) {
                output.write(eliminatedInsect.getInsectResultString() + '\n');
            }
            output.close();
        } catch (Exception ex) {
            // Ignore
        }
    }
}

// Exceptions

/**
 * Exception thrown when board size is outside valid range.
 */
class InvalidBoardSizeException extends Exception {
    public InvalidBoardSizeException() {
        super("Invalid board size");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

/**
 * Exception thrown when number of insects is outside valid range.
 */
class InvalidNumberOfInsectsException extends Exception {
    public InvalidNumberOfInsectsException() {
        super("Invalid number of insects");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

/**
 * Exception thrown when number of food points is outside valid range.
 */
class InvalidNumberOfFoodPointsException extends Exception {
    public InvalidNumberOfFoodPointsException() {
        super("Invalid number of food points");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

/**
 * Exception thrown when insect color is invalid.
 */
class InvalidInsectColorException extends Exception {
    public InvalidInsectColorException() {
        super("Invalid insect color");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

/**
 * Exception thrown when insect type is invalid.
 */
class InvalidInsectTypeException extends Exception {
    public InvalidInsectTypeException() {
        super("Invalid insect type");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

/**
 * Exception thrown when entity position is outside board boundaries.
 */
class InvalidEntityPositionException extends Exception {
    public InvalidEntityPositionException() {
        super("Invalid entity position");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

/**
 * Exception thrown when duplicate insects (same type and color) are found.
 */
class DuplicateInsectException extends Exception {
    public DuplicateInsectException() {
        super("Duplicate insects");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

/**
 * Exception thrown when two entities occupy the same position.
 */
class TwoEntitiesOnSamePositionException extends Exception {
    public TwoEntitiesOnSamePositionException() {
        super("Two entities in the same position");
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

// Enums

/**
 * Represents the possible colors of insects.
 */
enum InsectColor {
    RED,
    GREEN,
    BLUE,
    YELLOW,
    NONE;

    /**
     * Converts a string to the corresponding InsectColor.
     *
     * @param s the string to convert
     * @return the corresponding InsectColor, or NONE if no match found
     */
    public static InsectColor toColor(String s) {
        s = s.toLowerCase();
        return switch (s) {
            case "red" -> RED;
            case "green" -> GREEN;
            case "blue" -> BLUE;
            case "yellow" -> YELLOW;
            default -> NONE;
        };
    }
}

/**
 * Represents the possible movement directions for insects.
 */
enum Direction {
    N("North", 0, -1),
    E("East", 1, 0),
    S("South", 0, 1),
    W("West", -1, 0),
    NE("North-East", 1, -1),
    SE("South-East", 1, 1),
    SW("South-West", -1, 1),
    NW("North-West", -1, -1);

    private final String textRepresentation;
    private final int xBias;
    private final int yBias;

    Direction(String text, int xBias, int yBias) {
        this.textRepresentation = text;
        this.xBias = xBias;
        this.yBias = yBias;
    }

    /**
     * Moves the given entity position one step in this direction.
     *
     * @param entityPosition the position to move
     */
    public void nextStep(EntityPosition entityPosition) {
        entityPosition.moveX(xBias);
        entityPosition.moveY(yBias);
    }

    /**
     * Gets the text representation of this direction.
     *
     * @return the text representation (e.g., "North", "South-East")
     */
    public String getTextRepresentation() {
        return textRepresentation;
    }
}

// Interfaces

/**
 * Interface for entities that can move in orthogonal directions (N, E, S, W).
 */
interface OrthogonalMoving {
    /**
     * Calculates the total visible food value in the given orthogonal direction.
     *
     * @param dir the direction to check
     * @param entityPosition the starting position
     * @param boardData the game board
     * @param boardSize the size of the board
     * @return the total food value visible in the given direction
     */
    default int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                   Board boardData, int boardSize) {
        int visibleValue = 0;
        EntityPosition curPosition = entityPosition.copy();
        while (0 < curPosition.getX() && curPosition.getX() < boardSize + 1
                && 0 < curPosition.getY() && curPosition.getY() < boardSize + 1) {
            dir.nextStep(curPosition);
            BoardEntity boardEntity = boardData.getEntity(curPosition);
            if (boardEntity instanceof FoodPoint foodPoint) {
                visibleValue += foodPoint.getValue();
            }
        }
        return visibleValue;
    }

    /**
     * Simulates traveling in an orthogonal direction.
     *
     * @param dir the direction to travel
     * @param entityPosition the starting position
     * @param insectColor the color of the insect
     * @param boardData the game board
     * @param boardSize the size of the board
     * @return the amount of food eaten during travel
     */
    default int travelOrthogonally(Direction dir, EntityPosition entityPosition, InsectColor insectColor,
                                   Board boardData, int boardSize) {
        return 1;
    }
}

/**
 * Interface for entities that can move in diagonal directions (NE, SE, SW, NW).
 */
interface DiagonalMoving {
    /**
     * Calculates the total visible food value in the given diagonal direction.
     *
     * @param dir the direction to check
     * @param entityPosition the starting position
     * @param boardData the game board
     * @param boardSize the size of the board
     * @return the total food value visible in the given direction
     */
    default int getDiagonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                 Board boardData, int boardSize) {
        int visibleValue = 0;
        EntityPosition curPosition = entityPosition.copy();
        while (0 < curPosition.getX() && curPosition.getX() < boardSize + 1
                && 0 < curPosition.getY() && curPosition.getY() < boardSize + 1) {
            dir.nextStep(curPosition);
            BoardEntity boardEntity = boardData.getEntity(curPosition);
            if (boardEntity instanceof FoodPoint foodPoint) {
                visibleValue += foodPoint.getValue();
            }
        }
        return visibleValue;
    }

    /**
     * Simulates traveling in a diagonal direction.
     *
     * @param dir the direction to travel
     * @param entityPosition the starting position
     * @param insectColor the color of the insect
     * @param boardData the game board
     * @param boardSize the size of the board
     * @return the amount of food eaten during travel
     */
    default int travelDiagonally(Direction dir, EntityPosition entityPosition, InsectColor insectColor,
                                 Map<String, BoardEntity> boardData, int boardSize) {
        return 1;
    }
}

// Classes

/**
 * Represents a position on the game board with x and y coordinates.
 */
class EntityPosition {
    private int x;
    private int y;

    /**
     * Creates a new EntityPosition with the given coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public EntityPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-coordinate.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Moves the position along the x-axis by the given bias.
     *
     * @param bias the amount to move
     */
    public void moveX(int bias) {
        x += bias;
    }

    /**
     * Moves the position along the y-axis by the given bias.
     *
     * @param bias the amount to move
     */
    public void moveY(int bias) {
        y += bias;
    }

    /**
     * Creates a copy of this position.
     *
     * @return a new EntityPosition with the same coordinates
     */
    public EntityPosition copy() {
        return new EntityPosition(getX(), getY());
    }
}

/**
 * Abstract base class for all entities on the game board.
 */
abstract class BoardEntity {
    protected EntityPosition entityPosition;

    /**
     * Creates a new BoardEntity at the given position.
     *
     * @param entityPosition the position of the entity
     */
    public BoardEntity(EntityPosition entityPosition) {
        this.entityPosition = entityPosition;
    }
}

/**
 * Represents a food point on the game board with a specific value.
 */
class FoodPoint extends BoardEntity {
    private int value;

    /**
     * Creates a new FoodPoint at the given position with the specified value.
     *
     * @param position the position of the food point
     * @param value the food value
     */
    public FoodPoint(EntityPosition position, int value) {
        super(position);
        this.value = value;
    }

    /**
     * Gets the food value of this point.
     *
     * @return the food value
     */
    public int getValue() {
        return value;
    }
}

/**
 * Represents the game board containing all entities.
 */
class Board {
    private Map<Integer, BoardEntity> boardData = new HashMap<>();
    private int size;
    private int firstIdxOfFood = -1;
    private int lastIdxOfInsect = -1;
    private int lastIdx = 0;

    /**
     * Creates a new Board with the specified size.
     *
     * @param boardSize the size of the board
     */
    public Board(int boardSize) {
        this.size = boardSize;
    }

    /**
     * Adds an entity to the board.
     *
     * @param entity the entity to add
     */
    public void addEntity(BoardEntity entity) {
        if (firstIdxOfFood == -1 && entity instanceof FoodPoint) {
            firstIdxOfFood = lastIdx;
            lastIdxOfInsect = lastIdx - 1;
        }
        boardData.put(lastIdx, entity);
        lastIdx += 1;
    }

    /**
     * Gets the entity at the specified position.
     *
     * @param seekPosition the position to check
     * @return the entity at the position, or null if no entity found
     */
    public BoardEntity getEntity(EntityPosition seekPosition) {
        for (int i = 0; i < lastIdx; ++i) {
            BoardEntity boardEntity = boardData.get(i);
            if (boardEntity == null) {
                continue;
            }
            EntityPosition curEntityPosition = boardEntity.entityPosition;
            if (curEntityPosition.getX() == seekPosition.getX()
                    && curEntityPosition.getY() == seekPosition.getY()) {
                return boardData.get(i);
            }
        }
        return null;
    }

    /**
     * Gets the entity at the specified index.
     *
     * @param idx the index of the entity
     * @return the entity at the index
     */
    public BoardEntity getEntity(int idx) {
        return boardData.get(idx);
    }

    /**
     * Removes the entity at the specified index.
     *
     * @param idx the index of the entity to remove
     */
    public void removeEntity(int idx) {
        boardData.remove(idx);
    }

    /**
     * Removes the entity at the specified position.
     *
     * @param seekPosition the position of the entity to remove
     */
    public void removeEntity(EntityPosition seekPosition) {
        for (int i = 0; i < lastIdx; ++i) {
            BoardEntity boardEntity = boardData.get(i);
            if (boardEntity == null) {
                continue;
            }
            EntityPosition curEntityPosition = boardEntity.entityPosition;
            if (curEntityPosition.getX() == seekPosition.getX()
                    && curEntityPosition.getY() == seekPosition.getY()) {
                boardData.remove(i);
                break;
            }
        }
    }

    /**
     * Gets the movement direction for an insect (not implemented).
     *
     * @param insect the insect
     * @return always null
     */
    public Direction getDirection(Insect insect) {
        return null;
    }

    /**
     * Gets the direction sum for an insect (not implemented).
     *
     * @param insect the insect
     * @return always 0
     */
    public int getDirectionSum(Insect insect) {
        return 0;
    }

    /**
     * Gets the first index of food entities.
     *
     * @return the first index of food
     */
    public int getFirstIdxOfFood() {
        return firstIdxOfFood;
    }

    /**
     * Gets the last index of insect entities.
     *
     * @return the last index of insects
     */
    public int getLastIdxOfInsect() {
        return lastIdxOfInsect;
    }

    /**
     * Gets the last index of all entities.
     *
     * @return the last index
     */
    public int getLastIdx() {
        return lastIdx - 1;
    }
}

// Insects

/**
 * Abstract base class for all insect entities.
 */
abstract class Insect extends BoardEntity {
    protected InsectColor insectColor;

    /**
     * Creates a new Insect at the given position with the specified color.
     *
     * @param position the position of the insect
     * @param insectColor the color of the insect
     */
    public Insect(EntityPosition position, InsectColor insectColor) {
        super(position);
        this.insectColor = insectColor;
    }

    /**
     * Determines the best direction for the insect to move based on visible food.
     *
     * @param boardData the game board
     * @param boardSize the size of the board
     * @return the best direction to move
     */
    public abstract Direction getBestDirection(Board boardData, int boardSize);

    /**
     * Simulates the insect traveling in the specified direction.
     *
     * @param dir the direction to travel
     * @param boardData the game board
     * @param boardSize the size of the board
     * @return the amount of food eaten during travel
     */
    public int travelDirection(Direction dir, Board boardData, int boardSize) {
        int gatheredValue = 0;
        EntityPosition curPosition = entityPosition.copy();
        while (0 < curPosition.getX() && curPosition.getX() < boardSize + 1
                && 0 < curPosition.getY() && curPosition.getY() < boardSize + 1) {
            dir.nextStep(curPosition);
            BoardEntity boardEntity = boardData.getEntity(curPosition);
            if (boardEntity instanceof FoodPoint foodPoint) {
                gatheredValue += foodPoint.getValue();
                boardData.removeEntity(curPosition);
            } else if (boardEntity instanceof Insect insect) {
                if (insect.insectColor != this.insectColor) {
                    break;
                }
            }
        }
        return gatheredValue;
    }

    /**
     * Gets the color name of the insect.
     *
     * @return the color name as a string
     */
    public String getColorName() {
        return switch (insectColor) {
            case BLUE -> "Blue";
            case RED -> "Red";
            case GREEN -> "Green";
            case YELLOW -> "Yellow";
            default -> throw new RuntimeException("color did not defined");
        };
    }

    /**
     * Gets the class name of the insect instance.
     *
     * @param inst the insect instance
     * @return the simple class name of the insect
     */
    public static String defineNameOfInstance(Insect inst) {
        return inst.getClass().getSimpleName();
    }
}

/**
 * Represents a Grasshopper insect that moves by jumping orthogonally.
 */
class Grasshopper extends Insect implements OrthogonalMoving {
    /**
     * Creates a new Grasshopper at the given position with the specified color.
     *
     * @param entityPosition the position of the grasshopper
     * @param insectColor the color of the grasshopper
     */
    public Grasshopper(EntityPosition entityPosition, InsectColor insectColor) {
        super(entityPosition, insectColor);
    }

    @Override
    public int getOrthogonalDirectionVisibleValue(Direction dir, EntityPosition entityPosition,
                                                  Board boardData, int boardSize) {
        int visibleValue = 0;
        EntityPosition curPosition = entityPosition.copy();
        while (0 < curPosition.getX() && curPosition.getX() < boardSize + 1
                && 0 < curPosition.getY() && curPosition.getY() < boardSize + 1) {
            dir.nextStep(curPosition);
            dir.nextStep(curPosition);
            BoardEntity boardEntity = boardData.getEntity(curPosition);
            if (boardEntity instanceof FoodPoint foodPoint) {
                visibleValue += foodPoint.getValue();
            }
        }
        return visibleValue;
    }

    @Override
    public Direction getBestDirection(Board boardData, int boardSize) {
        int northVal = getOrthogonalDirectionVisibleValue(Direction.N, entityPosition, boardData, boardSize);
        int eastVal = getOrthogonalDirectionVisibleValue(Direction.E, entityPosition, boardData, boardSize);
        int southVal = getOrthogonalDirectionVisibleValue(Direction.S, entityPosition, boardData, boardSize);
        int westVal = getOrthogonalDirectionVisibleValue(Direction.W, entityPosition, boardData, boardSize);
        int maxValue = Math.max(northVal, Math.max(eastVal, Math.max(southVal, westVal)));

        if (northVal >= maxValue) {
            return Direction.N;
        }
        if (eastVal >= maxValue) {
            return Direction.E;
        }
        if (southVal >= maxValue) {
            return Direction.S;
        }

        return Direction.W;
    }

    @Override
    public int travelDirection(Direction dir, Board boardData, int boardSize) {
        int gatheredValue = 0;
        EntityPosition curPosition = entityPosition.copy();
        while (0 < curPosition.getX() && curPosition.getX() < boardSize + 1
                && 0 < curPosition.getY() && curPosition.getY() < boardSize + 1) {
            dir.nextStep(curPosition);
            dir.nextStep(curPosition);
            BoardEntity boardEntity = boardData.getEntity(curPosition);
            if (boardEntity instanceof FoodPoint foodPoint) {
                gatheredValue += foodPoint.getValue();
                boardData.removeEntity(curPosition);
            } else if (boardEntity instanceof Insect insect) {
                if (insect.insectColor != this.insectColor) {
                    break;
                }
            }
        }
        return gatheredValue;
    }
}

/**
 * Represents a Butterfly insect that moves orthogonally.
 */
class Butterfly extends Insect implements OrthogonalMoving {
    /**
     * Creates a new Butterfly at the given position with the specified color.
     *
     * @param entityPosition the position of the butterfly
     * @param color the color of the butterfly
     */
    public Butterfly(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
    }

    @Override
    public Direction getBestDirection(Board boardData, int boardSize) {
        int northVal = getOrthogonalDirectionVisibleValue(Direction.N, entityPosition, boardData, boardSize);
        int eastVal = getOrthogonalDirectionVisibleValue(Direction.E, entityPosition, boardData, boardSize);
        int southVal = getOrthogonalDirectionVisibleValue(Direction.S, entityPosition, boardData, boardSize);
        int westVal = getOrthogonalDirectionVisibleValue(Direction.W, entityPosition, boardData, boardSize);
        int maxValue = Math.max(northVal, Math.max(eastVal, Math.max(southVal, westVal)));

        if (northVal >= maxValue) {
            return Direction.N;
        }
        if (eastVal >= maxValue) {
            return Direction.E;
        }
        if (southVal >= maxValue) {
            return Direction.S;
        }

        return Direction.W;
    }
}

/**
 * Represents a Spider insect that moves diagonally.
 */
class Spider extends Insect implements DiagonalMoving {
    /**
     * Creates a new Spider at the given position with the specified color.
     *
     * @param entityPosition the position of the spider
     * @param color the color of the spider
     */
    public Spider(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
    }

    @Override
    public Direction getBestDirection(Board boardData, int boardSize) {
        int northEastVal = getDiagonalDirectionVisibleValue(Direction.NE, entityPosition, boardData, boardSize);
        int southEastVal = getDiagonalDirectionVisibleValue(Direction.SE, entityPosition, boardData, boardSize);
        int southWestVal = getDiagonalDirectionVisibleValue(Direction.SW, entityPosition, boardData, boardSize);
        int northWestVal = getDiagonalDirectionVisibleValue(Direction.NW, entityPosition, boardData, boardSize);
        int maxValue = Math.max(northEastVal, Math.max(southEastVal, Math.max(southWestVal, northWestVal)));

        if (northEastVal >= maxValue) {
            return Direction.NE;
        }
        if (southEastVal >= maxValue) {
            return Direction.SE;
        }
        if (southWestVal >= maxValue) {
            return Direction.SW;
        }

        return Direction.NW;
    }
}

/**
 * Represents an Ant insect that can move both orthogonally and diagonally.
 */
class Ant extends Insect implements OrthogonalMoving, DiagonalMoving {
    /**
     * Creates a new Ant at the given position with the specified color.
     *
     * @param entityPosition the position of the ant
     * @param color the color of the ant
     */
    public Ant(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
    }

    @Override
    public Direction getBestDirection(Board boardData, int boardSize) {
        int northVal = getOrthogonalDirectionVisibleValue(Direction.N, entityPosition, boardData, boardSize);
        int eastVal = getOrthogonalDirectionVisibleValue(Direction.E, entityPosition, boardData, boardSize);
        int southVal = getOrthogonalDirectionVisibleValue(Direction.S, entityPosition, boardData, boardSize);
        int westVal = getOrthogonalDirectionVisibleValue(Direction.W, entityPosition, boardData, boardSize);
        int northEastVal = getDiagonalDirectionVisibleValue(Direction.NE, entityPosition, boardData, boardSize);
        int southEastVal = getDiagonalDirectionVisibleValue(Direction.SE, entityPosition, boardData, boardSize);
        int southWestVal = getDiagonalDirectionVisibleValue(Direction.SW, entityPosition, boardData, boardSize);
        int northWestVal = getDiagonalDirectionVisibleValue(Direction.NW, entityPosition, boardData, boardSize);
        int maxValue = Math.max(northVal, Math.max(eastVal, Math.max(southVal, Math.max(westVal,
                Math.max(northEastVal, Math.max(southEastVal, Math.max(southWestVal, northWestVal)))))));

        if (northVal >= maxValue) {
            return Direction.N;
        }
        if (eastVal >= maxValue) {
            return Direction.E;
        }
        if (southVal >= maxValue) {
            return Direction.S;
        }
        if (westVal >= maxValue) {
            return Direction.W;
        }
        if (northEastVal >= maxValue) {
            return Direction.NE;
        }
        if (southEastVal >= maxValue) {
            return Direction.SE;
        }
        if (southWestVal >= maxValue) {
            return Direction.SW;
        }
        return Direction.NW;
    }
}

/**
 * Represents an insect that has been eliminated from the game with its results.
 */
class EliminatedInsect {
    private String colorName;
    private String insectType;
    private Direction dir;
    private int amountOfFoodEaten;

    /**
     * Creates a new EliminatedInsect record.
     *
     * @param colorName the color name of the insect
     * @param insectType the type of the insect
     * @param dir the direction chosen by the insect
     * @param amountOfFoodEaten the amount of food eaten by the insect
     */
    public EliminatedInsect(String colorName, String insectType, Direction dir, int amountOfFoodEaten) {
        this.colorName = colorName;
        this.insectType = insectType;
        this.dir = dir;
        this.amountOfFoodEaten = amountOfFoodEaten;
    }

    /**
     * Generates the result string for the eliminated insect.
     *
     * @return the formatted result string
     */
    public String getInsectResultString() {
        return colorName + " " + insectType + " " + dir.getTextRepresentation() + " " + amountOfFoodEaten;
    }
}
