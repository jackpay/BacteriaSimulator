/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacteriasimulator;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The super class microbe contains the fields an methods shared by all the different forms of
 * microbe within the universe.
 * @author LAPTOP
 */
public class Microbe extends Cell implements Search{

    public static final int THRESH = 100;
    public static final int SIZE = 50;
    public int energy;
    public boolean feeding;
    public int[] foodLoc;
    public int[] pheromone;
    public FoodPhero[][] foodPher;
    public boolean canBreed;
    public int status;
    public boolean foundFood;
    public ArrayList<Microbe> bacteria;
    public ArrayList<int[]> foodLocations;
    public int breedRate;
    private boolean dead;


    /**
     * The constructor which takes the x and y coordinate of its starting place in the grid
     * @param x x coordinate The starting x coordinate of the cell
     * @param y y coordinate The starting y coordinate of the cell
     */
    public Microbe(int x,int y, FoodPhero[][] foodPher,ArrayList<Microbe> bacteria, ArrayList<int[]> foodLocs, int breedRate){
        super();
        feeding = false;    //Are they capable of feeding at this point
        foodLoc = new int[2];  //Food Location
        setLocation(x,y);
        this.foodPher = foodPher; //
        energy = 50;
        canBreed = false;
        this.bacteria = bacteria;
        foodLocations = foodLocs;
        this.breedRate = breedRate;
        dead = false;
    }

    /**
     * An empty method created by the interface Search so each cell
     * type can define it's own.
     */
    public void search(){
        
    };


    /**
     * A method which causes the microbe to perform random walk in search of foodLoc
     * providing the location it wishes to travel is not as yet occupied.
     */
    public void walk(int choice){
        int loc = new Random().nextInt(8);
        if(loc == 0){
            x = x - 1 > 0 ? x - 1 : SIZE - 1;
            y = y - 1 > 0 ? y - 1 : SIZE - 1;
        }

        if(loc == 1){
            x = x - 1 > 0 ? x - 1 : SIZE - 1;
        }

        if(loc == 2){
            y = y + 1 < SIZE ? y + 1 : 0;
            x = x - 1 > 0 ? x - 1 : SIZE - 1;
        }

        if(loc == 3){
            y = y - 1 > 0 ? y-1 : SIZE-1;
        }

        if(loc == 4){
            y = y + 1 < SIZE ? y + 1 : 0;
        }

        if(loc == 5){
            x = x + 1 < SIZE ? x + 1: 0;
            y = y - 1 > 0 ? y - 1 : SIZE - 1;
        }

        if(loc == 6){
            x = x + 1 < SIZE ? x + 1: 0;
        }

        if(loc == 7){
            x = x + 1 < SIZE ? x + 1: 0;
            y = y + 1 < SIZE ? y + 1 : 0;
        }
    }

    /**
     * Returns the current energy level of the microbe;
     * @return
     */
    public int getEnergy(){
        return energy;
    }
    
    /**
     * Reduces the energy level of the Microbe by 1.
     */
    public void reduceEnergy(){
        energy--;
        if(energy < 30){
            canBreed = true;
        }
    }

    /**
     * Checks to see if the conditions of having recently canBreed and
     * sufficient energy levels are met for a microbe to breed.
     * @return true if allowed to breed.
     */
    public boolean canBreed(int time){
        return false;
    }

    /**
     * A method which is overriden by the other classes with their own breed
     * method to produce cells of the same species.
     * @return The new cell.
     */
    public Microbe breed(){
        return this;
    }

    /**
     * Increases the energy level of the Microbe by 5.
     */
    public void increaseEnergy(){
        energy += 5;
    }

    public boolean isAlive(){
        return energy > 0;
    }

    public void Feed(){
        if(foodPher[foodLoc[0]][foodLoc[1]].isFood() && foodPher[foodLoc[0]][foodLoc[1]].getAmount() > 0){
            foodPher[foodLoc[0]][foodLoc[1]].removeFood();
            increaseEnergy();
        }
        
    }

    /**
     * Allows the microbe to check for any foodLoc in it's immediate vicinity
     * if there is it is set as feeding and the location of the foodLoc is
     * given.
     */
    public void checkForFood(){
        foundFood = false;
        if(foodPher[x][y].isFood()){
                setFoodLoc(x, y);
                foundFood = true;
            }
        if(x - 1 > 0 && y - 1 > 0){
            if(foodPher[x-1][y-1].isFood()){
                setFoodLoc(x - 1, y - 1);
                foundFood = true;
            }
        }
        if(x - 1 > 0){
            if(foodPher[x-1][y].isFood()){
                setFoodLoc(x - 1, y);
                foundFood = true;
            }
        }
        if(x - 1 > 0 && y + 1 < SIZE){
            if(foodPher[x-1][y+1].isFood()){
                setFoodLoc(x - 1, y+1);
                foundFood = true;
            }
        }
        if(y - 1 > 0){
            if(foodPher[x][y-1].isFood()){
                setFoodLoc(x, y-1);
                foundFood = true;
            }
        }
        if(y + 1 < SIZE){
            if(foodPher[x][y+1].isFood()){
                setFoodLoc(x, y+1);
                foundFood = true;
            }
        }
        if(x + 1 < SIZE && y - 1 > 0){
            if(foodPher[x+1][y-1].isFood()){
                setFoodLoc(x+1, y-1);
                foundFood = true;
            }
        }
        if(x + 1 < SIZE){
            if(foodPher[x+1][y].isFood()){
                setFoodLoc(x+1, y);
                foundFood = true;
            }
        }
        if(x + 1 < SIZE && y + 1 < SIZE){
            if(foodPher[x+1][y+1].isFood()){
                setFoodLoc(x+1, y+1);
                foundFood = true;
            }
        }
    }

    /**
     * Sets the location of food when found
     * @param locX Food x coordinate
     * @param locY Food y coordinate
     */
    public void setFoodLoc(int locX, int locY){
        foodLoc[0] = locX;
        foodLoc[1] = locY;
        feeding = true;
        int[] newFoodLoc = new int[2];
        System.arraycopy(foodLoc, 0, newFoodLoc, 0, 2);
        boolean alexist = false;
        for(int[] loc : foodLocations){
            if(loc[0] == newFoodLoc[0]){
                if(loc[1] == newFoodLoc[1]){
                    alexist = true;
                }
            }
        }
        if(foodLocations != null && !alexist){
               foodLocations.add(newFoodLoc);
               leavePheromone(newFoodLoc[0],newFoodLoc[1],50);
        }
    }

    /**
     * Checks the Microbes surrounding area for any messages left
     * which may indicate what activity to follow.
     * @return An arrayList of all the messages in the surrounding area of the Cell
     */
    public ArrayList checkForPheromones(){
        ArrayList<int[]> messages = new ArrayList();
        if(x - 1 > 0 && y - 1 > 0){
            if(foodPher[x-1][y-1].getMessage() > 0){
                messages.add(newMessage(x-1,y-1,foodPher[x-1][y-1].getMessage()));
            }
        }
        if(x - 1 > 0){
            if(foodPher[x-1][y].getMessage() > 0){
                messages.add(newMessage(x-1,y,foodPher[x-1][y].getMessage()));
            }
        }
        if(x - 1 > 0 && y + 1 < SIZE){
            if(foodPher[x-1][y+1].getMessage() > 0){
                messages.add(newMessage(x-1,y+1,foodPher[x-1][y+1].getMessage()));
            }
        }
        if(y - 1 > 0){
            if(foodPher[x][y-1].getMessage() > 0){
                messages.add(newMessage(x,y-1,foodPher[x][y-1].getMessage()));
            }
        }
        if(y + 1 < SIZE){
            if(foodPher[x][y+1].getMessage() > 0){
                messages.add(newMessage(x,y+1,foodPher[x][y+1].getMessage()));
            }
        }
        if(x + 1 < SIZE && y - 1 > 0){
            if(foodPher[x+1][y-1].getMessage() > 0){
                messages.add(newMessage(x+1,y-1,foodPher[x+1][y-1].getMessage()));
            }
        }
        if(x + 1 < SIZE){
            if(foodPher[x+1][y].getMessage() > 0){
                messages.add(newMessage(x+1,y,foodPher[x+1][y].getMessage()));
            }
        }
        if(x + 1 < SIZE && y + 1 < SIZE){
            if(foodPher[x+1][y+1].getMessage() > 0){
                messages.add(newMessage(x+1,y+1,foodPher[x+1][y+1].getMessage()));
            }
        }
        return messages;
                
    }
    
    /**
     * Creates a new message containing the pheromone message and its
     * location
     */
    public int[] newMessage(int locX, int locY, int mess){
        int[] message = new int[3];
        message[0] = mess;
        message[1] = locX;
        message[2] = locY;
        return message;
    }
        

    /**
     * Leaves pheromone at the given location
     * @param xPos The x location for pheromone position
     * @param yPos The y location for pheromone position
     * @param status The message to be placed
     */
    public void leavePheromone(int xPos, int yPos, int status){
        if(!foodPher[xPos][yPos].isFood){
            foodPher[xPos][yPos] = new Pheromone(xPos,yPos,status);
        }
    }
    
    
    public boolean isDead(){
        return dead;
    }
    
    public void setDead(){
        dead = true;
    }



}

