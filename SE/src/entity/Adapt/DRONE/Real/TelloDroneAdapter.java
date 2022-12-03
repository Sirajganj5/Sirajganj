package entity.Adapt.DRONE.Real;

import constants.Constants;
import entity.Adapt.DRONE.Vitualization.AnimatedDroneInterface;
import tellolib.communication.TelloConnection;
import tellolib.control.TelloControl;
import tellolib.drone.TelloDrone;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TelloDroneAdapter implements AnimatedDroneInterface {
//  private TelloDrone telloDrone; // physical drone object
  private int flightFloor; // feet

  public TelloDroneAdapter() {
//    this.telloDrone = telloDrone;
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
//    telloDrone.activateSDK();
    System.out.println("The drone takes off");
//    telloDrone.takeoff();
    System.out.println("The drone rises 5 feet above the flight floor");
//    telloDrone.increaseAltitude(feetToCentimeters(flightFloor + 5));
  }

  private void endFlight() throws IOException {
    System.out.println("The drone lands");
//    telloDrone.land();
  }

  private double angleFromAToB(double aX, double aY, double bX, double bY) {
    return Math.toDegrees(Math.atan2(bY - aY, bX - aX));
  }

  /*
   * x, y: feet
   */
  public void visitLocation(int x, int y) throws IllegalArgumentException {
//    if (isDeployed()) return;
//    if (x == 0 && y == 0) return;
//    if (
//      x < 0 ||
//      y < 0 ||
//      x > Constants.REAL_FARM_LENGTH ||
//      y > Constants.REAL_FARM_WIDTH
//    ) throw new IllegalArgumentException("Location is out of bounds!");
//
//    int turnValue = (int) Math.round(angleFromAToB(0, 0, x, y));
//    int distanceToTravel = feetToCentimeters(
//      (int) Math.round(Math.hypot(x, y))
//    );
//
//    try {
//      startFlight();
//
//      // travel to
//      System.out.println("The drone turns to face the specified location");
//      telloDrone.turnCW(turnValue);
//      System.out.println("The drone flies to the specified location");
//      telloDrone.flyForward(distanceToTravel);
//      System.out.println("The drone hovers over the specified location");
//      telloDrone.hoverInPlace((int) Constants.DRONE_STOP_DURATION.toSeconds());
//
//      // travel back
//      System.out.println("The drone turns to face the starting location");
//      telloDrone.turnCW(180);
//      System.out.println("The drone flies to the starting location");
//      telloDrone.flyForward(distanceToTravel);
//
//      endFlight();
//    } catch (IOException | InterruptedException e) {
//      e.printStackTrace();
//    }
  }
  
  private final Logger logger = Logger.getGlobal(); 

	public void execute()
	{
		logger.info("start");
		
	    TelloControl telloControl = TelloControl.getInstance();
	    
	    TelloDrone drone = TelloDrone.getInstance();

	    telloControl.setLogLevel(Level.FINE);

	    try 
	    {
		    telloControl.connect();
		    
		    telloControl.enterCommandMode();
		    
		    // Now we will fly. This command will cause the drone to take
		    // off to its default initial altitude. Note that taking off
		    // can take some time and we wait in this method until the
		    // signals completion of take off.
		    
		    telloControl.takeOff();
		    
		    telloControl.startStatusMonitor();
		    		    
		    // Now we will execute a series of movement commands.
		    // Distances in centimeters.
		   
	    	telloControl.forward(150);
	    	 telloControl.rotateRight(360);
		    telloControl.backward(150);
		    
//		    telloControl.up(50);
		    
//		    telloControl.down(50);
		    
		    telloControl.left(150);
		    
		    telloControl.right(150);
		    
		    telloControl.rotateLeft(150);
		    
		    telloControl.rotateRight(150);
		    
		    // fly a curve to a point 3.25 feet in front of the drone and 
		    // 1.5 feet higher.
//		    telloControl.curve(25, 25, 0, 100, 0, 50, 20);
		    
		    //telloControl.doFlip(TelloFlip.backward);
	    }	
	    catch (Exception e) {
	    	e.printStackTrace();
	    } finally 
	    {
	    	if (telloControl.getConnection() == TelloConnection.CONNECTED && drone.isFlying())
	    	{
	    		try
	    		{telloControl.land();}
	    		catch(Exception e) { e.printStackTrace();}
	    	}
	    }
	    
  	telloControl.disconnect();
	    
	    logger.info("end");
	}
	
	public void visitor()
	{
		logger.info("start");
		
	    TelloControl telloControl = TelloControl.getInstance();
	    
	    TelloDrone drone = TelloDrone.getInstance();

	    telloControl.setLogLevel(Level.FINE);

	    try 
	    {
		    telloControl.connect();
		    
		    telloControl.enterCommandMode();
		    
		    // Now we will fly. This command will cause the drone to take
		    // off to its default initial altitude. Note that taking off
		    // can take some time and we wait in this method until the
		    // signals completion of take off.
		    
		    telloControl.takeOff();
		    
		    telloControl.startStatusMonitor();
		    		    
		    // Now we will execute a series of movement commands.
		    // Distances in centimeters.
		   
	    	telloControl.forward(50);
	    	 telloControl.rotateRight(90);
		    telloControl.forward(100);
		    
		    // fly a curve to a point 3.25 feet in front of the drone and 
		    // 1.5 feet higher.
//		    telloControl.curve(25, 25, 0, 100, 0, 50, 20);
		    
		    //telloControl.doFlip(TelloFlip.backward);
	    }	
	    catch (Exception e) {
	    	e.printStackTrace();
	    } finally 
	    {
	    	if (telloControl.getConnection() == TelloConnection.CONNECTED && drone.isFlying())
	    	{
	    		try
	    		{telloControl.land();}
	    		catch(Exception e) { e.printStackTrace();}
	    	}
	    }
	    
  	telloControl.disconnect();
	    
	    logger.info("end");
	}

  public void scanFarm() {
//    if (isDeployed()) return;
//
//    int vDistance = feetToCentimeters(Constants.REAL_DRONE_Y_BOUND);
//    int rDistance = feetToCentimeters(Constants.REAL_DRONE_X_BOUND / 5);
//    int lDistance = feetToCentimeters(Constants.REAL_DRONE_X_BOUND);
//
//    try {
//      startFlight();
//      try {
//			TimeUnit.SECONDS.sleep(1);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//      telloDrone.turnCW(180);
//      try {
//			TimeUnit.SECONDS.sleep(2);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//      for (int i = 0; i < 1; i++) {
//        // down
//        System.out.println("The drone turns 90 degrees clockwise");
//        
//        System.out.printf("The drone flies forward %d cm\n", vDistance);
//        telloDrone.flyForward(100);
//        try {
//			TimeUnit.SECONDS.sleep(2);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        // right
//        System.out.println("The drone turns 90 degrees counter clockwise");
//        telloDrone.turnCCW(90);
//        try {
//			TimeUnit.SECONDS.sleep(1);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        System.out.printf("The drone flies forward %d cm\n", rDistance);
//        telloDrone.flyForward(100);
//        try {
//			TimeUnit.SECONDS.sleep(1);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        // up
//        System.out.println("The drone turns 90 degrees counter clockwise");
//        telloDrone.turnCCW(90);
//        try {
//			TimeUnit.SECONDS.sleep(1);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        System.out.printf("The drone flies forward %d cm\n", vDistance);
//        telloDrone.flyForward(100);
//        try {
//			TimeUnit.SECONDS.sleep(1);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        System.out.println("The drone turns 90 degrees counter clockwise");
//        telloDrone.turnCCW(90); 
//        try {
//			TimeUnit.SECONDS.sleep(1);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        System.out.printf("The drone flies forward %d cm\n", vDistance);
//        telloDrone.flyForward(100);
////        if (i < 2) {
////          // right
////          System.out.println("The drone turns 90 degrees clockwise");
////          telloDrone.turnCW(90);
////          System.out.printf("The drone flies forward %d cm\n", rDistance);
////          telloDrone.flyForward(rDistance);
////        }
//      }
//
//      // left
////      System.out.println("The drone turns 90 degrees counter clockwise");
////      telloDrone.turnCCW(90);
////      System.out.printf("The drone flies forward %d cm\n", lDistance);
////      telloDrone.flyForward(lDistance);
//
//      endFlight();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
  }
}
