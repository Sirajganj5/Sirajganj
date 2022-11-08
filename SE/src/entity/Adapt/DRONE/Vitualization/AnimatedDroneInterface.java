package entity.Adapt.DRONE.Vitualization;

public interface AnimatedDroneInterface {
  public boolean isDeployed();

  /*
   * x, y: pixels
   */
  public void visitLocation(int x, int y) throws IllegalArgumentException;

  public void scanFarm();
}
