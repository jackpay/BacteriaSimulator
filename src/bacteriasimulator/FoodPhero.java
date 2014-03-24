/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacteriasimulator;

/**
 * The wrapper class which contains the Food and Pheromone Cells
 * contained in the universe.
 * @author Jack Pay
 */
public class FoodPhero extends Cell{
    
    public boolean isFood; //Indicates if the Cell is food or not
    public int message;    //Indicates the message contained in the cell if it is a pheromone
    public int amount; //amount of food if object is food

    /**
     * The constructor which sets the location of the FoodPhero Cell.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public FoodPhero(int x, int y){
        setLocation(x,y);
        isFood = false;
    }

    /**
     * @return Returns true if the cell contains food.
     */
    public boolean isFood(){
        return isFood;
    }

    /**
     * Returns the pheromone message contained at that location,
     * if 0 is returned there is no message.
     * @return the pheromone message.
     */
    public int getMessage(){
        return message;
    }

    /**
     * Removes an item of food stored.
     */
    public void removeFood(){
        amount-=5;
    }

    /**
     * @return Returns the amount of food stored at a location.
     */
    public int getAmount(){
        return amount;
    }
}
