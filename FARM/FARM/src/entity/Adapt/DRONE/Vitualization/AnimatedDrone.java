package entity.adapter.drone.virtual;

import constants.Constants;
import java.lang.IllegalArgumentException;
import java.lang.Math;
import java.util.LinkedList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class AnimatedDrone extends ImageView implements AnimatedDroneInterface {
  private SequentialTransition goToOriginAnimation = new SequentialTransition();
  private SequentialTransition visitLocationAnimation = new SequentialTransition();
  private SequentialTransition scanFarmAnimation = new SequentialTransition();

  public AnimatedDrone(String iconFilePath) {
    super(
      new Image(
        iconFilePath,
        Constants.SCREEN_DRONE_SIZE,
        Constants.SCREEN_DRONE_SIZE,
        true,
        true
      )
    );
  }

  public boolean isDeployed() {
    return (
      goToOriginAnimation.getStatus() == Animation.Status.RUNNING ||
      visitLocationAnimation.getStatus() == Animation.Status.RUNNING ||
      scanFarmAnimation.getStatus() == Animation.Status.RUNNING
    );
  }

  /*
   * aX, aY, bX, bY: pixels
   * speed: pixels per second
   */
  private Duration secondsToTravelFromAToB(
    double aX,
    double aY,
    double bX,
    double bY,
    double speed
  ) {
    return Duration.seconds(Math.abs(Math.hypot(bX - aX, bY - aY) / speed));
  }

  /*
   * aX, aY, bX, bY: pixels
   */
  private double angleFromAToB(double aX, double aY, double bX, double bY) {
    return Math.toDegrees(Math.atan2(bY - aY, bX - aX));
  }

  /*
   * aX, aY, bX, bY: pixels
   */
  private Timeline rotateFromAToBTimeline(
    double aX,
    double aY,
    double bX,
    double bY
  ) {
    KeyValue rotateKeyValue = new KeyValue(
      rotateProperty(),
      angleFromAToB(aX, aY, bX, bY)
    );
    KeyFrame rotateKeyFrame = new KeyFrame(
      Constants.DRONE_ROTATE_DURATION,
      rotateKeyValue
    );
    return new Timeline(rotateKeyFrame);
  }

  private Timeline rotateUpTimeline() {
    return rotateFromAToBTimeline(0, 0, 0, -1);
  }

  private Timeline rotateDownTimeline() {
    return rotateFromAToBTimeline(0, 0, 0, 1);
  }

  private Timeline rotateLeftTimeline() {
    return rotateFromAToBTimeline(0, 0, -1, 0);
  }

  private Timeline rotateRightTimeline() {
    return rotateFromAToBTimeline(0, 0, 1, 0);
  }

  private KeyFrame startAnimationKeyFrame() {
    KeyValue startXKeyValue = new KeyValue(
      translateXProperty(),
      getTranslateX()
    );
    KeyValue startYKeyValue = new KeyValue(
      translateYProperty(),
      getTranslateY()
    );
    return new KeyFrame(Duration.seconds(0), startXKeyValue, startYKeyValue);
  }

  public void goToOrigin() {
    if (getTranslateX() == 0 && getTranslateY() == 0) return;

    // create timeline
    Timeline goToOriginTimeline = new Timeline(
      startAnimationKeyFrame(),
      new KeyFrame(
        secondsToTravelFromAToB(
          getTranslateX(),
          getTranslateY(),
          0,
          0,
          Constants.SCREEN_DRONE_TRAVEL_SPEED
        ),
        new KeyValue(translateXProperty(), 0),
        new KeyValue(translateYProperty(), 0)
      )
    );

    // create animation with timeline
    goToOriginAnimation =
      new SequentialTransition(
        rotateFromAToBTimeline(getTranslateX(), getTranslateY(), 0, 0),
        goToOriginTimeline
      );

    // play animation
    goToOriginAnimation.setCycleCount(1);
    goToOriginAnimation.play();
  }

  /*
   * x, y: feet
   */
  public void visitLocation(int x, int y) throws IllegalArgumentException {
    if (isDeployed()) return;
    if (getTranslateX() == x && getTranslateY() == y) return;
    if (
      x < 0 ||
      y < 0 ||
      x > Constants.REAL_DRONE_X_BOUND ||
      y > Constants.REAL_DRONE_Y_BOUND
    ) throw new IllegalArgumentException("Location is out of bounds!");

    x = x * Constants.PIXELS_PER_FOOT;
    y = y * Constants.PIXELS_PER_FOOT;

    Duration keyFrameDuration = secondsToTravelFromAToB(
      getTranslateX(),
      getTranslateY(),
      x,
      y,
      Constants.SCREEN_DRONE_TRAVEL_SPEED
    );

    // create timelines
    Timeline goToTimeline = new Timeline(
      startAnimationKeyFrame(),
      new KeyFrame(
        keyFrameDuration,
        new KeyValue(translateXProperty(), x),
        new KeyValue(translateYProperty(), y)
      )
    );
    Timeline goBackTimeline = new Timeline(
      new KeyFrame(
        keyFrameDuration,
        new KeyValue(translateXProperty(), getTranslateX()),
        new KeyValue(translateYProperty(), getTranslateY())
      )
    );

    // create new animation with timelines
    visitLocationAnimation =
      new SequentialTransition(
        rotateFromAToBTimeline(getTranslateX(), getTranslateY(), x, y),
        goToTimeline,
        new PauseTransition(Constants.DRONE_STOP_DURATION),
        rotateFromAToBTimeline(x, y, getTranslateX(), getTranslateY()),
        goBackTimeline
      );

    // play animation
    visitLocationAnimation.setCycleCount(1);
    visitLocationAnimation.play();
  }

  public void scanFarm() {
    if (isDeployed()) return;

    goToOrigin();

    int travelVerticalDistance = Constants.SCREEN_DRONE_Y_BOUND;
    int travelLeftDistance = Constants.SCREEN_DRONE_X_BOUND;
    int travelRightDistance = Constants.SCREEN_DRONE_X_BOUND / 5;

    Duration timeToTravelVertical = secondsToTravelFromAToB(
      0,
      0,
      0,
      travelVerticalDistance,
      Constants.SCREEN_DRONE_TRAVEL_SPEED
    );
    Duration timeToTravelLeft = secondsToTravelFromAToB(
      0,
      0,
      travelLeftDistance,
      0,
      Constants.SCREEN_DRONE_TRAVEL_SPEED
    );
    Duration timeToTravelRight = secondsToTravelFromAToB(
      0,
      0,
      travelRightDistance,
      0,
      Constants.SCREEN_DRONE_TRAVEL_SPEED
    );

    // create down timelines
    LinkedList<Timeline> downTimelines = new LinkedList<Timeline>();
    KeyFrame goDownKeyFrame = new KeyFrame(
      timeToTravelVertical,
      new KeyValue(translateYProperty(), Constants.SCREEN_DRONE_Y_BOUND)
    );
    for (int i = 0; i < 3; i++) downTimelines.add(new Timeline(goDownKeyFrame));

    // create right timelines
    LinkedList<Timeline> rightTimelines = new LinkedList<Timeline>();
    for (
      int x = travelRightDistance;
      x <= Constants.SCREEN_DRONE_X_BOUND;
      x += travelRightDistance
    ) rightTimelines.add(
      new Timeline(
        new KeyFrame(timeToTravelRight, new KeyValue(translateXProperty(), x))
      )
    );

    // create up timelines
    LinkedList<Timeline> upTimelines = new LinkedList<Timeline>();
    KeyFrame goUpKeyFrame = new KeyFrame(
      timeToTravelVertical,
      new KeyValue(translateYProperty(), 0)
    );
    for (int i = 0; i < 3; i++) upTimelines.add(new Timeline(goUpKeyFrame));

    // create left timeline
    Timeline leftTimeline = new Timeline(
      new KeyFrame(timeToTravelLeft, new KeyValue(translateXProperty(), 0))
    );

    // create new animation with timelines
    scanFarmAnimation = new SequentialTransition();
    for (int i = 0; i < 3; i++) {
      if (!downTimelines.isEmpty()) scanFarmAnimation
        .getChildren()
        .addAll(rotateDownTimeline(), downTimelines.poll());
      if (!rightTimelines.isEmpty()) scanFarmAnimation
        .getChildren()
        .addAll(rotateRightTimeline(), rightTimelines.poll());
      if (!upTimelines.isEmpty()) scanFarmAnimation
        .getChildren()
        .addAll(rotateUpTimeline(), upTimelines.poll());
      if (!rightTimelines.isEmpty()) scanFarmAnimation
        .getChildren()
        .addAll(rotateRightTimeline(), rightTimelines.poll());
    }
    scanFarmAnimation.getChildren().addAll(rotateLeftTimeline(), leftTimeline);

    // play animation
    scanFarmAnimation.setCycleCount(1);
    scanFarmAnimation.play();
  }
}
