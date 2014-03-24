/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacteriasimulator;

import java.util.*;

/**
 * A form of cell which communicates information to a set of data which all other
 * microbes have access. This represents global communication in order
 * to govern activity such as breeding and feeding.
 * @author LAPTOP
 */
public class GlobalCommCell extends Microbe{

    private ArrayList<int[]> foodLocations;
    private int alive;


    /**
     * A type of cell which communicates information globally. Meaning each cell
     * is aware of the nearest food source that has been found and how many
     * cells are alive (for the purposes of breeding).
     * @param x The Cells x coordinate
     * @param y The Cells y coordinate
     * @param foodPher The array of food and pheromones
     * @param bacteria The array of other bacteria
     * @param foodLocs The array to hold found food sources.
     */
    public GlobalCommCell(int x, int y,FoodPhero[][] foodPher,ArrayList<Microbe> bacteria,ArrayList<int[]>foodLocs,int breedRate){
        super(x,y,foodPher,bacteria,foodLocs,breedRate);
        foodLocations = foodLocs;
    }

    /**
     * A method which randomly walks if given to further information
     * but is guided by the discoveries of the other microbe in the colony.
     */
    @Override
    public void search(){
        int rand = new Random().nextInt(8);
        checkForFood();
        if(foodLocations.isEmpty()){
            walk(rand);
            reduceEnergy();
            checkForFood();
        }
        else{
            if(foundFood && energy < 35){
                Feed();
            }
            else{
                if(foundFood && energy > 35 && energy < THRESH){
                    Feed();
                    walk(rand);
                    reduceEnergy();
                }
                else{
                    if(!foundFood && !foodLocations.isEmpty()){
                        moveTowardsFood();
                        reduceEnergy();
                    }
                    else{
                        walk(rand);
                        reduceEnergy();
                    }
                }
            }
        }
    }

    /**
     * A method which assesses the possible food sources of the particular microbe
     * and increases the chances of it approaching the closest one..
     */
    public void moveTowardsFood(){
        int[][] distances = new int[foodLocations.size()][2];
        for(int[] loc:foodLocations){
            distances[foodLocations.indexOf(loc)][0] = (int) Math.sqrt(Math.pow(x-loc[0],2));
            distances[foodLocations.indexOf(loc)][1] = (int) Math.sqrt(Math.pow(y-loc[1],2));
        }
        int[] nearest = distances[0];
        int nearInd = 0;
        for(int i = 0; i < foodLocations.size();i++){
            if(distances[i][0] < nearest[0] && distances[i][1] < nearest[1]){
                nearInd = i;
                nearest[0] = distances[i][0];
                nearest[1] = distances[i][1];
            }
        }
        int closeX = x;
        int closeY = y;
        nearest = foodLocations.get(nearInd);
        if(x - nearest[0] < 0){
          closeX += 1;
        }
        if(x - nearest[0] > 0){
            closeX -= 1;
        }
        if(y - nearest[0] < 0){
            closeY += 1;
        }
        if(y - nearest[1] > 0){
            closeY -= 1;
        }
        if(nearest[0] == x && nearest[1] == y && !foodPher[0][1].isFood()){
            foodLocations.remove(nearInd);
        }
        int prob = new Random().nextInt(2);
        if(prob == 0){
            x = closeX;
            y = closeY;
        }
        else{
            walk(new Random().nextInt(8));
        }
    }

    /**
     * Causes the GlobalCommCell to create a produce offspring.
     * @return The new GlobalCommCell.
     */
    @Override
    public GlobalCommCell breed(){
            int newX = x + 1 < 1000 ? x + 1 : 0;
            int newY = x + 1 < 1000 ? y + 1 : 0;
            energy=energy/2;
            GlobalCommCell child = new GlobalCommCell(newX,newY,foodPher,bacteria,foodLocations,breedRate);
            return child;
    }

    /**
     * The method which checks whether an appropriate time has past since
     * the bacteria last bred and whether it has enough energy to do so.
     * @param time The current time step the simulator is on, if a multiple of 10 it is allowed to breed
     * @return True if the bacteria can reproduce
     */
    @Override
    public boolean canBreed(int time){
        int alive = 0;
        Double aveEn = 0.0;
        for(Microbe bact : bacteria){
            if(bact.isAlive()){
                alive++;
                aveEn += bact.getEnergy();
            }
        }
        aveEn = aveEn/bacteria.size();
        return time % 10 == 0 && energy > aveEn;
        //&& energy > 40;
        //&& alive < 100 
    }


}
