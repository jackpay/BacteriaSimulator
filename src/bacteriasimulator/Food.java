/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacteriasimulator;

import java.util.*;

/**
 *
 * @author LAPTOP
 */
public class Food extends FoodPhero{

    

    public Food(int x, int y, int amount){
        super(x,y);
        this.amount = amount;
        isFood = true;
        message = 0;
    }
}
