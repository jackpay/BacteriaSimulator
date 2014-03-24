/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacteriasimulator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.*;

import java.util.Random;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * The GUI which displays the options available to the user and the simulator universe.
 * @author Jack Pay
 */
public class GUI extends JFrame{

    private static final int SIZE = 500;

    private Simulation sim;
    private GUI gui;

    private ArrayList<Shape> shapes;

    private Container pane = this.getContentPane(); //Content Pane for GUI
    private JPanel top; // The top JPanel to store Components
    private JPanel middle;//The middle JPanel to store Components

    private FoodPhero[][] foodPhero;
    private ArrayList<Microbe> bacteria;

    private boolean started;

    private int step;

    private PaintSurface universe;
    private Graphics2D g2;

    private JLabel timeCount;
    private JLabel bacteriaCount;
    private JLabel deadBacteria;
    private JLabel food;

    private JComboBox amountFood;
    private JComboBox numberBac;
    private JComboBox speed;
    private JComboBox bacType;
    private JComboBox breedRate;


    /**
     * The constructor which produces the simulator universe option buttons and stats panel.
     */
    public GUI(){

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Bacteria Simulator");

        top = new JPanel();
        middle = new JPanel();
        shapes = new ArrayList();
        started = false;
        gui = this;

        top.setLayout(new FlowLayout());

        middle.setLayout(new FlowLayout());
        middle.setSize(SIZE,SIZE);

        foodPhero = new FoodPhero[SIZE][SIZE];
        bacteria = new ArrayList();

        setupFood(50);
        setupBacteria(1,0,2);

        sim = new Simulation(50,20,0,shapes,foodPhero,bacteria,this);

        pane.setLayout(new BorderLayout());
        
        universe = new PaintSurface();

        pane.add(universe,BorderLayout.CENTER);

        for(int i = 0; i < SIZE; i+=10){
            for(int j = 0; j < SIZE;j+=10){
                Shape s = new Rectangle2D.Float(j,i,10,10);
                shapes.add(s);
            }
        }

        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(!started){
                    sim.runSimulation();
                    started = true;
                }
                else{
                }

            }
        });


        JButton stop = new JButton("Stop");
        stop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                sim.pauseSim();
            }
        });

        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                started = false;
                bacteria = new ArrayList();
                int numFd = Integer.parseInt((String)amountFood.getSelectedItem());
                setupFood(numFd);
                int numBac = Integer.parseInt((String)numberBac.getSelectedItem());
                setupBacteria(numBac,bacType.getSelectedIndex(),breedRate.getSelectedIndex());
                System.out.println(breedRate.getSelectedIndex());
                sim = new Simulation(speed.getSelectedIndex(),20,0,shapes,foodPhero,bacteria,gui);
                upDateGUI();
            }
        });

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1,3));
        buttons.add(start);
        buttons.add(stop);
        buttons.add(reset);
        buttons.setSize(200,200);
        buttons.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(49, 79, 79)));

        JPanel options = new JPanel();
        options.setLayout(new FlowLayout());
        JLabel amountFoodLab = new JLabel (" Amount of food: ");
        String[] foodOps = {"1","10","20","50","100"};
        amountFood = new JComboBox(foodOps);
        JLabel numBacLabel = new JLabel(" Number of Bacteria: ");
        String[] bacOps = {"1","10","20","50","100"};
        numberBac = new JComboBox(bacOps);
        String[] speeds = {"Very Fast","Fast","Normal","Slow"};
        JLabel speedLabel = new JLabel("Speed: ");
        speed = new JComboBox(speeds);
        String[] bactType = {"Basic","Local Comm","Global Comm","Cheaters"};
        JLabel bacLabel = new JLabel("Bacteria");
        String[] breedOps = {"10%","40%","60%","80%","100%"};
        breedRate = new JComboBox(breedOps);
        JLabel breedOp = new JLabel("Breed Rate: ");
        bacType = new JComboBox(bactType);
        options.add(bacLabel);
        options.add(bacType);
        options.add(speedLabel);
        options.add(speed);
        options.add(numBacLabel);
        options.add(numberBac);
        options.add(amountFoodLab);
        options.add(amountFood);
        options.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(49, 79, 79)));
        options.add(breedOp);
        options.add(breedRate);
        options.setSize(100,100);


        JPanel info = new JPanel();
        info.setLayout(new GridLayout(5,2));
        JLabel  timeLabel = new JLabel(" Time: ");
        timeCount = new JLabel();
        JLabel bacteriaLabel = new JLabel(" Live Bacteria: ");
        bacteriaCount = new JLabel();
        JLabel deadBactLabel = new JLabel(" Dead Bacteria: ");
        deadBacteria = new JLabel();
        JLabel foodLabel = new JLabel(" Food: ");
        food = new JLabel();
        info.add(timeLabel);
        info.add(timeCount);
        info.add(bacteriaLabel);
        info.add(bacteriaCount);
        info.add(deadBactLabel);
        info.add(deadBacteria);
        info.add(foodLabel);
        info.add(food);
        info.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(49, 79, 79)));

        top.setLayout(new BorderLayout());
        //top.add(info,BorderLayout.EAST);
        top.add(buttons,BorderLayout.NORTH);
        top.add(options,BorderLayout.SOUTH);
        middle.add(info);
        //top.add(reset);
        //top.add(time);

        pane.add(top, BorderLayout.SOUTH);
        pane.add(middle,BorderLayout.EAST);
        Dimension all = new Dimension(800,632);
        this.setSize(all);
        this.setVisible(true);

    }

    /**
     * Paints the universe and is recalled at the end of every iteration
     * over the cells to update the universe
     */
    private class PaintSurface extends JComponent{

       public PaintSurface(){

       }
       public void paint(Graphics g){
        g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        
        for(Shape s:shapes){
            g2.draw(s);
        }
        for(int j = 0; j < SIZE; j++){
                for(int p = 0; p < SIZE; p++){
                    if(foodPhero[j][p].getMessage() > 0){
                        addPheromone(j,p,foodPhero[j][p].getMessage());
                    }
                    if(foodPhero[j][p].isFood()){
                        if(foodPhero[j][p].getAmount() <= 0){
                            foodPhero[j][p] = new EmptyCell(j,p);
                        }
                        else{
                            addFood(j,p,foodPhero[j][p].getAmount());
                        }
                    }
                }
            }
        for(Microbe bact : bacteria){
            if(!bact.isAlive() && !bact.isDead()){
                bact.setDead();
                foodPhero[bact.getX()][bact.getY()] = new Food(bact.getX(),bact.getY(),5);
                addFood(bact.getX(),bact.getY(),5);
            }
            else{
                if(!bact.isDead()){
                    addBacteria(bact.getX(),bact.getY(),bact.getEnergy());
                }
            }
        }

        repaint();
    }
    }

    /**
     * Updates the statistics on the GUI
     * @param time The time that has passed
     * @param liveBac The bacteria which are alive
     * @param deadBac The bacteria which are dead
     */
    public void updateStats(int time, int liveBac, int deadBac){
        timeCount.setText(Integer.toString(time));
        bacteriaCount.setText(Integer.toString(liveBac));
        deadBacteria.setText(Integer.toString(deadBac));
        int foodAmount = 0;
        for(int i = 0; i < SIZE; i++){
            for(int j = 0; j < SIZE; j++){
                if(foodPhero[i][j].isFood){
                    foodAmount += foodPhero[i][j].getAmount();
                }
            }
        }
        food.setText(Integer.toString(foodAmount));
    }

    /**
     * Repaints the universe.
     */
    public void upDateGUI(){
        universe.repaint();
    }

    /**
     * Validates the GUI to ensure the changes to the GUI are displayed
     */
    public void validateGUI(){
        middle.validate();
        top.validate();
        pane.validate();
    }

    /**
     * Removes all of the elements from the GUI
     */
    public void cleanGUI(){
        top.removeAll();
        middle.removeAll();
        middle.repaint();
        top.repaint();
    }

    /**
     * Adds a food amount to the location in the universe
     * specified by the parameters
     * @param x The coordinate in the x direction
     * @param y The coordinate in the y direction
     */
    public void addFood(int x, int y, int energy){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
        Double e = Double.parseDouble(Integer.toString(energy));
        Double col = (double )(e/100) * 100;
        int newCol = (int) ((int) (255/100) * col);
        newCol = newCol <= 0 ? 0 : newCol;
        g2.setColor(new Color(0, newCol, 0));
        Shape s = new Rectangle2D.Float(x*10,y*10,10,10);
        g2.fill(s);
        int place = SIZE - 1 * y;
        place = place + x;
        shapes.set((place+y), s);
    }

    /**
     * Adds a pheromone to the visual universe
     * @param x x coordinate
     * @param y y coordinate
     * @param energy indicator of pheromone strength
     */
    public void addPheromone(int x, int y, int energy){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50F));
        if(energy == 100){
            g2.setColor(Color.RED);
        }
        else{
            Double e = Double.parseDouble(Integer.toString(energy));
            Double col = (double )(e/100) * 100;
            int newCol = (int) ((int) (100/255) * col);
            newCol = newCol <= 0 ? 0 : newCol;
            g2.setColor(new Color(216,191,newCol));
        }
        Shape s = new Rectangle2D.Float(x*10,y*10,10,10);
        g2.fill(s);
        int place = SIZE - 1 * y;
        place = place + x;
        shapes.set((place+y), s);
    }

    /**
     * Used by paint to add a bacteria to the universe according to it's
     * coordinates.
     * @param x coordinate in x direction
     * @param y coordinate in y location
     */
    public void addBacteria(int x , int y, int energy){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
        Double e = Double.parseDouble(Integer.toString(energy));
        if(e >= 100){
            e = 100.0;
        }
        Double col = (double )(e/100) * 100;
        int newCol = (int) ((int) (255/100) * col);
        newCol = newCol <= 0 ? 0 : newCol;
        g2.setColor(new Color(0, 0, newCol));
        Shape s = new Rectangle2D.Float(x*10,y*10,10,10);
        g2.fill(s);
        int place = SIZE - 1 * y;
        place = place + x;
        shapes.set((place+y), s);
    }

    /**
     * Sets up a number of clusters of food within the universe.
     * @param foodClust The number of food clusters to be present.
     */
    public void setupFood(int foodClust){

        for(int i = 0; i < SIZE; i++){
            for(int j = 0; j < SIZE; j++){
                foodPhero[i][j] = new EmptyCell(i,j);
            }
        }
        for(int i = 0; i < foodClust; i++){
            Random rand = new Random();
            int x = rand.nextInt(50);
            int y = rand.nextInt(50);
            x = x > 20 && x < 30 ? x - 10 : x;
            x = x > 30 && x < 40 ? x + 10 : x;
            y = y > 20 && y < 30 ? y - 10 : y;
            y = y > 30 && y < 40 ? y + 10 : y;

            foodPhero[x][y] = new Food(x,y,100);
            if(y +  1 < SIZE/10){
                foodPhero[x][y+1] = new Food(x,y+1,100);
            }
            if(x + 1 < SIZE/10){
                foodPhero[x+1][y] = new Food(x+1,y,100);
            }
            if(x + 1 > 0 && y + 1 < SIZE/10){
                foodPhero[x+1][y+1] = new Food(x+1,y+1,100);
            }
            if(x - 1 > 0 && y - 1 > 0){
                foodPhero[x-1][y-1] = new Food(x-1,y-1,50);
            }
            if(x - 1 > 0  && y + 2 < SIZE/10){
                foodPhero[x-1][y+2] = new Food(x-1,y+2,50);
            }
            if(x + 2 < SIZE/10  && y - 1 > 0){
                foodPhero[x+2][y-1] = new Food(x+1,y-1,50);
            }
            if(x + 2 < SIZE/10  && y + 2 < SIZE/10){
                foodPhero[x+2][y+2] = new Food(x+2,y+2,50);
            }
        }



    }

    /**
     * Sets up an array list of microbes to act in the simulation
     * @param numBact Number of microbes.
     * @param type Type of microbes.
     */
    public void setupBacteria(int numBact, int type, int breed){

            int min = 20;
            int max = 30;
            int breR = 0;
            Random rand = new Random();
            int x = 0;
            int y = 0;
            int numCheat = numBact/5;
            if(breed == 0){
                breR = 10;
            }
            if(breed == 1){
                breR = 40;
            }
            if(breed == 2){
                breR = 60;
            }
            if(breed == 3){
                breR = 80;
            }
            if(breed == 4){
                breR = 100;
            }

            ArrayList<int[]> foodLocs = new ArrayList();
            for(int i = 0; i < numBact; i++){
                x = rand.nextInt(max - min) + min;
                y = rand.nextInt(max - min) + min;
                if(type == 0){
                    bacteria.add(new BasicCell(x,y,foodPhero,bacteria,foodLocs,breR));
                }
                if(type == 1){
                    bacteria.add(new LocalCommCell(x,y,foodPhero,bacteria,foodLocs,breR));
                }
                if(type == 2){
                    bacteria.add(new GlobalCommCell(x,y,foodPhero,bacteria,foodLocs,breR));
                }
                if(type == 3){
                    if(i < numCheat){
                        bacteria.add(new CheatCell(x,y,foodPhero,bacteria,foodLocs,breR));
                    }
                    else{
                        bacteria.add(new LocalCommCell(x,y,foodPhero,bacteria,foodLocs,breR));
                    }
                }
            }
    }
}
