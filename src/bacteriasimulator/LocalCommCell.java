/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacteriasimulator;

import java.util.*;

/**
 * A Microbe which performs random walk but leaves a pheromone trail
 * indicating its current status. If well fed gives a message of
 * well being and therefore close to food. If not in a good state
 * communicates not to breed or follow.
 * @author Jack Pay
 */
public class LocalCommCell extends Microbe{

    public LocalCommCell(int x, int y, FoodPhero[][] foodPher,ArrayList<Microbe> bacteria,ArrayList<int[]>foodLocs,int breedRate){
        super(x,y,foodPher,bacteria,foodLocs,breedRate);
    }

    /**
     * The search method for a cell which communicates locally via leaving pheromone
     * signatures.
     * One indicates if it has found food, with a stronger degree of pheromone
     * when close to food.
     * Another to indicate starvation, to indicate to other cells to move away.
     */
    @Override
    public void search(){
        int rand = new Random().nextInt(8);
        checkForFood();
        if(foundFood && energy < 35){
            Feed();
        }
        if(foundFood && energy > 35 && energy < THRESH){
            Feed();
            walk(rand);
            leavePheromone(x,y,energy);
            reduceEnergy();

        }
        if(foundFood && energy >= THRESH){
            walk(rand);
            leavePheromone(x,y,THRESH);
            reduceEnergy();

        }
        if(!foundFood && energy < 3){
            leavePheromone(x,y,1000);
            walk(rand);
            reduceEnergy();
        }
        else{
            ArrayList<int[]> pherms = checkForPheromones();
            if(pherms.isEmpty()){
                walk(rand);
                reduceEnergy();
            }
            else{
                int max = 0;
                for(int[] pherm : pherms){
                    if(pherm[0] > pherms.get(max)[0] && pherm[0] != 1000){
                        max = pherms.indexOf(pherm);
                    }
                }
                int move = new Random().nextInt(2);
                if(move == 0){
                    x = pherms.get(max)[1];
                    y = pherms.get(max)[2];
                }
                else{
                    walk(rand);
                    reduceEnergy();
                }
            }
        }

    }


    /**
     * Produces a new instance of the Microbe which is calling the method
     * @return Returns a new copy of the LocalCommCell instance.
     */
    @Override
    public LocalCommCell breed(){
            int newX = x + 1 < 1000 ? x + 1 : 0;
            int newY = x + 1 < 1000 ? y + 1 : 0;
            LocalCommCell child = new LocalCommCell(newX,newY,foodPher,bacteria,foodLocations,breedRate);
            energy=energy/2;
            canBreed = false;
            return child;
    }

    /**
     * Checks the surrounding area for inhibitory pheromones left by
     * bacteria close to death
     * @return Returns true if an inhibitor is found.
     */
    public boolean checkForInhibitor(){
        boolean inhibit = false;
        ArrayList<int[]> pherms = checkForPheromones();
        for(int[] pherm : pherms){
            if(pherm[0] == 1000){
                inhibit = true;
            }
        }
        return inhibit;
    }


    /**
     * The method which checks whether an appropriate time has past since
     * the bacteria last bred and whether it has enough energy to do so.
     * @param time The current time step the simulator is on, if a multiple of 10 it is allowed to breed
     * @return True if the bacteria can reproduce
     */
    @Override
    public boolean canBreed(int time){
        Double breed = Double.valueOf(Integer.toString(THRESH))/100 * breedRate;
        return time % 10 == 0 && energy > breed && !checkForInhibitor();
    }

}
