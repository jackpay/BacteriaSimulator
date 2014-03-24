/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacteriasimulator;

/**
 * Represents an empty cell within the universe of the simulation.
 * @author Jack Pay
 */
public class EmptyCell extends FoodPhero{

    /**
     * An empty cell constructor which stores its location and nothing else
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public EmptyCell(int x, int y){
        super(x,y);
        isFood = false;
        message = 0;
        amount = 0;
    }

}
