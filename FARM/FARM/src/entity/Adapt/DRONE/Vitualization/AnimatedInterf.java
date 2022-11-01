package entity.adapter.drone.virtual;

public interface AnimatedDroneInterface {
  public boolean isDeployed();

  /*
   * x, y: pixels
   */
  public void visitLocation(int x, int y) throws IllegalArgumentException;

  public void scanFarm();
}
