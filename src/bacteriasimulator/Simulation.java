/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacteriasimulator;

import java.awt.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LAPTOP
 */
public class Simulation {

    public static final int SIZE = 50;

    public Thread sim;
    public FoodPhero[][] foodPher;
    public ArrayList<Microbe> bacteria;

    public int step;
    public int speed;
    public ArrayList<Shape> shapes;

    public Simulation(int speed, int numBact, int type, ArrayList<Shape> shapes,FoodPhero[][] foodPher, ArrayList<Microbe> bacteria,GUI gui){
        this.shapes = shapes;
        this.foodPher = foodPher;
        this.bacteria = bacteria;
        if(speed == 0){
            speed = 10;
        }
        else{
            if(speed == 1){
                speed = 50;
            }
            else{
                if(speed == 2){
                    speed = 100;
                }
                else{
                    speed = 500;
                }
            }
        }
        sim = new Thread(new RunSim(shapes,speed,bacteria,foodPher,gui));
        sim.setPriority(Thread.MIN_PRIORITY);
        
    }

    /**
     * Calls the runnable object RunSim and
     * executes the thread.
     */
    public void runSimulation(){
        sim.start();
    }

    /**
     * Pauses the simulation thread
     */
    public void pauseSim(){
            sim.stop();
    }   
}
