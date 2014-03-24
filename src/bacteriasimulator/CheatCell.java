/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacteriasimulator;

import java.util.*;

/**
 * A Bacteria type which breeds quicker and degrades slower, follows
 * pheromone trails but however does not contribute using pheromones.
 * @author LAPTOP
 */
public class CheatCell extends Microbe{

    public CheatCell(int x, int y, FoodPhero[][] foodPher,ArrayList<Microbe> bacteria,ArrayList<int[]>foodLocs,int breedRate){
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
            Feed();
        }
        if(foundFood && energy > 35 && energy < THRESH){
            Feed();
            Feed();
            walk(rand);
            reduceEnergy();

        }
        if(foundFood && energy >= THRESH){
            Feed();
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
                    if(pherm[0] > pherms.get(max)[0]){
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
                }
                reduceEnergy();
            }


        }

    }


    /**
     * Produces a new instance of the Microbe which is calling the method
     * @return Returns a new copy of the LocalCommCell instance.
     */
    @Override
    public CheatCell breed(){
            int newX = x + 1 < 1000 ? x + 1 : 0;
            int newY = x + 1 < 1000 ? y + 1 : 0;
            CheatCell child = new CheatCell(newX,newY,foodPher,bacteria,foodLocations,breedRate);
            canBreed = false;
            energy=energy/2;
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
        Double newBreed = (Double.valueOf(Integer.toString(breedRate))/3)*2;
        Double breed = Double.valueOf(Integer.toString(THRESH))/100 * newBreed;
        return time % 10 == 0 && energy > breed;
    }

}
