/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacteriasimulator;

/**
 * An empty class used to encompass all other objects within the environment
 * @author LAPTOP
 */
public class Cell {
    
    public int x;
    public int y;


    /**
     * An empty constructor which simply creates an object of Cell.
     */
    public Cell(){
    }

    /**
     * Sets the location of the Cell object within the simulation universe.
     * @param x
     * @param y
     */
    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

}
