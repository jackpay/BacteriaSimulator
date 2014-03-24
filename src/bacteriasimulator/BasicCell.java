/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacteriasimulator;

import java.util.*;

/**
 * A Basic cell which random walks until it find foodLoc.
 * Eats until foodLoc, or foodLoc runs out and the continues to walk.
 * Extends microbe
 * @author LAPTOP
 */
public class BasicCell extends Microbe{

    public BasicCell(int x, int y,FoodPhero[][] foodPher, ArrayList<Microbe> bacteria,ArrayList<int[]>foodLocs,int breedRate){
        super(x,y,foodPher,bacteria,foodLocs,breedRate);
    }

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
            reduceEnergy();

        }
        else{
            walk(rand);
            reduceEnergy();
        }

    }

    @Override
    public BasicCell breed(){
            int newX = x + 1 < 1000 ? x + 1 : 0;
            int newY = x + 1 < 1000 ? y + 1 : 0;
            BasicCell child = new BasicCell(newX,newY,foodPher,bacteria,foodLocations,breedRate);
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
        Double breed = Double.valueOf(Integer.toString(THRESH))/100 * breedRate;
//        System.out.print(breedRate);
        return time % 10 == 0 && energy > breed;
    }
}
