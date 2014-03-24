/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacteriasimulator;

/**
 * A Class which indicates whether a Pheromone is present at a particular
 * Cell in the universe and if so what message it contains.
 * @author Jack Pay
 */
public class Pheromone extends FoodPhero{

    /**
     * The constructor sets the location of the pheromone and
     * @param x the x coordinate
     * @param y the y coordinate
     * @param mess The pheromone message
     */
    public Pheromone(int x, int y, int mess){
        super(x,y);
        message = mess;
        isFood = false;
        amount = 0;
    }
}
