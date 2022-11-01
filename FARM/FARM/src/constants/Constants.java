package constants;

import javafx.util.Duration;

public class Constants {
  // screen farm constants
  public static final int SCREEN_FARM_LENGTH = 800; // pixels
  public static final int SCREEN_FARM_WIDTH = 600; // pixels

  public static final int SCREEN_ITEM_LENGTH_MIN = 50; // pixels
  public static final int SCREEN_ITEM_LENGTH_MAX = 800; // pixels
  public static final int SCREEN_ITEM_WIDTH_MIN = 50; // pixels
  public static final int SCREEN_ITEM_WIDTH_MAX = 600; // pixels
  public static final int SCREEN_ITEM_HEIGHT_MIN = 0; // pixels
  public static final int SCREEN_ITEM_HEIGHT_MAX = 1000; // pixels
  public static final int SCREEN_ITEM_X_BOUND = 550; // pixels
  public static final int SCREEN_ITEM_Y_BOUND = 750; // pixels

  public static final int SCREEN_DRONE_SIZE = 50; // pixels^2
  public static final int SCREEN_DRONE_TRAVEL_SPEED = 100; // pixels per second
  public static final int SCREEN_DRONE_X_BOUND = 550; // pixels
  public static final int SCREEN_DRONE_Y_BOUND = 750; // pixels

  // real farm constants
  public static final int PIXELS_PER_FOOT = 25; // screen farm pixels per real farm feet
  public static final int CENTIMETERS_PER_FOOT = 30;

  public static final int REAL_FARM_LENGTH = 32; // feet
  public static final int REAL_FARM_WIDTH = 24; // feet

  public static final int REAL_ITEM_LENGTH_MIN = 2; // feet
  public static final int REAL_ITEM_LENGTH_MAX = 32; // feet
  public static final int REAL_ITEM_WIDTH_MIN = 2; // feet
  public static final int REAL_ITEM_WIDTH_MAX = 24; // feet
  public static final int REAL_ITEM_HEIGHT_MIN = 0; // feet
  public static final int REAL_ITEM_HEIGHT_MAX = 40; // feet
  public static final int REAL_ITEM_X_BOUND = 22; // feet
  public static final int REAL_ITEM_Y_BOUND = 30; // feet

  public static final int REAL_DRONE_SIZE = 2; // feet^2
  public static final int REAL_DRONE_TRAVEL_SPEED = 4; // feet per second
  public static final int REAL_DRONE_X_BOUND = 22; // feet
  public static final int REAL_DRONE_Y_BOUND = 30; // feet

  // constants that apply to both
  public static final Duration DRONE_ROTATE_DURATION = Duration.seconds(1.5); // seconds
  public static final Duration DRONE_STOP_DURATION = Duration.seconds(1.5); // seconds
}
