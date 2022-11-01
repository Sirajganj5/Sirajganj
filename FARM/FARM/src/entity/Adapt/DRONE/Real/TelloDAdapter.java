package entity.adapter.drone.physical;

import constants.Constants;
import entity.adapter.drone.virtual.AnimatedDroneInterface;
import java.io.IOException;

public class TelloDroneAdapter implements AnimatedDroneInterface {
  private TelloDrone telloDrone; // physical drone object
  private int flightFloor; // feet

  public TelloDroneAdapter(TelloDrone telloDrone) {
    this.telloDrone = telloDrone;
    this.flightFloor = 0;
  }

  /*
   * flightFloor: feet
   */
  public void setFlightFloor(int flightFloor) {
    this.flightFloor = flightFloor;
  }

  public int getFlightFloor() {
    return flightFloor;
  }

  /*
   * Assuming visitLocation and scanFarm are blocking until completion,
   * this method can only be called when the drone is not deployed.
   * Therefore, it always returns false.
   */
  public boolean isDeployed() {
    return false;
  }

  private int feetToCentimeters(int feet) {
    return feet * Constants.CENTIMETERS_PER_FOOT;
  }

  private void startFlight() throws IOException {
    telloDrone.activateSDK();
    System.out.println("The drone takes off");
    telloDrone.takeoff();
    System.out.println("The drone rises 5 feet above the flight floor");
    telloDrone.increaseAltitude(feetToCentimeters(flightFloor + 5));
  }

  private void endFlight() throws IOException {
    System.out.println("The drone lands");
    telloDrone.land();
  }

  private double angleFromAToB(double aX, double aY, double bX, double bY) {
    return Math.toDegrees(Math.atan2(bY - aY, bX - aX));
  }

  /*
   * x, y: feet
   */
  public void visitLocation(int x, int y) throws IllegalArgumentException {
    if (isDeployed()) return;
    if (x == 0 && y == 0) return;
    if (
      x < 0 ||
      y < 0 ||
      x > Constants.REAL_FARM_LENGTH ||
      y > Constants.REAL_FARM_WIDTH
    ) throw new IllegalArgumentException("Location is out of bounds!");

    int turnValue = (int) Math.round(angleFromAToB(0, 0, x, y));
    int distanceToTravel = feetToCentimeters(
      (int) Math.round(Math.hypot(x, y))
    );

    try {
      startFlight();

      // travel to
      System.out.println("The drone turns to face the specified location");
      telloDrone.turnCW(turnValue);
      System.out.println("The drone flies to the specified location");
      telloDrone.flyForward(distanceToTravel);
      System.out.println("The drone hovers over the specified location");
      telloDrone.hoverInPlace((int) Constants.DRONE_STOP_DURATION.toSeconds());

      // travel back
      System.out.println("The drone turns to face the starting location");
      telloDrone.turnCW(180);
      System.out.println("The drone flies to the starting location");
      telloDrone.flyForward(distanceToTravel);

      endFlight();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void scanFarm() {
    if (isDeployed()) return;

    int vDistance = feetToCentimeters(Constants.REAL_DRONE_Y_BOUND);
    int rDistance = feetToCentimeters(Constants.REAL_DRONE_X_BOUND / 5);
    int lDistance = feetToCentimeters(Constants.REAL_DRONE_X_BOUND);

    try {
      startFlight();

      for (int i = 0; i < 3; i++) {
        // down
        System.out.println("The drone turns 90 degrees clockwise");
        telloDrone.turnCW(90);
        System.out.printf("The drone flies forward %d cm\n", vDistance);
        telloDrone.flyForward(vDistance);

        // right
        System.out.println("The drone turns 90 degrees counter clockwise");
        telloDrone.turnCCW(90);
        System.out.printf("The drone flies forward %d cm\n", rDistance);
        telloDrone.flyForward(rDistance);

        // up
        System.out.println("The drone turns 90 degrees counter clockwise");
        telloDrone.turnCCW(90);
        System.out.printf("The drone flies forward %d cm\n", vDistance);
        telloDrone.flyForward(vDistance);

        if (i < 2) {
          // right
          System.out.println("The drone turns 90 degrees clockwise");
          telloDrone.turnCW(90);
          System.out.printf("The drone flies forward %d cm\n", rDistance);
          telloDrone.flyForward(rDistance);
        }
      }

      // left
      System.out.println("The drone turns 90 degrees counter clockwise");
      telloDrone.turnCCW(90);
      System.out.printf("The drone flies forward %d cm\n", lDistance);
      telloDrone.flyForward(lDistance);

      endFlight();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
