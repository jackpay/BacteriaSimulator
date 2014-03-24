/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacteriasimulator;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class which sets up and holds the thread for the simulated universe.
 * @author Jack Pay
 */
public class RunSim implements Runnable{

    public static final int SIZE = 50;

    public FoodPhero[][] foodPher;
    public ArrayList<Microbe> bacteria;

    public int step;
    public Boolean proceed;
    public int speed;
    public int time;
    public ArrayList<Shape> shapes;
    public GUI gui;

    /**
     * Creates a simulation taking a number of parameters for the bacteria universe to
     * be instantiated.
     * @param foodClust Number of food clusters
     * @param time Length of time to run for
     * @param numBact Number of microbes to be present at start
     * @param type Type of bacteria to be present at the start
     * @param shapes An array list of the shapes representing cells on the grid
     */
    public RunSim(ArrayList<Shape> shapes,int speed,ArrayList<Microbe> bacteria, FoodPhero[][]foodPher, GUI gui){
        this.speed = speed;
        time = 0;
        this.proceed = proceed;
        this.gui = gui;
        step = 0;
        this.shapes = shapes;
        this.bacteria = bacteria;
        this.foodPher = foodPher;
    }

    /**
     * Runs a thread and causes the simulation to run.
     */
    public void run() {
      int dead = 0;
      while(dead < bacteria.size()){
        dead = 0;
        Random rand = new Random();
        ArrayList<Microbe> children = new ArrayList();
        Iterator it = bacteria.iterator();
        for(Microbe bac:bacteria){
            if(bac.isAlive()){
                bac.search();
            }
            else{
                dead++;
            }
            if(bac.canBreed(time)){
                children.add(bac.breed());
            }
            
        }
        try {
                Thread.sleep(speed);
                //gui.upDateGUI();
                } catch (InterruptedException ex) {
                    Logger.getLogger(RunSim.class.getName()).log(Level.SEVERE, null, ex);
                }
        Thread.yield();


        if(children.size() > 0){
            bacteria.addAll(children);
        }
        time++;
        gui.updateStats(time,bacteria.size()-dead,dead);

                    }
            
            //}
        //gui.test();
        //gui.upDateGUI();
        }

    


}
