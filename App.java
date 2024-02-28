import doodlepad.*;

import java.awt.Color;
import java.util.*;
import java.util.Collections;

public class App {

    private static Window gameWindow;

    public static void main(String[] args) throws Exception {
        
        //runSimulation();
        Menu m = new Menu();
    }

    public static void runSimulation() { //setting up the particle system
        System.out.println("Particle Simulation Starting!");
        gameWindow = new Window(1280, 720, 500, 10, 120); //500 by 500 display with 10 particles
        gameWindow.createParticles();

        //add another window to controll options in the 
    }
}

class Window extends Pad { //base class for the particle system

    private int numParticles;
    private ArrayList<Particle> particles;
    private int xSize;
    private int ySize;
    private int particleSize;
    private int tickRate;

    private boolean isPaused = true;

    public Window(int x, int y, int p, int ps, int tr) {
        super("Display", x, y);

        //background
        Rectangle r = new Rectangle(0, 0, x, y);
        r.setFillColor(46, 51, 61);

        //particles
        numParticles = p;
        xSize = x;
        ySize = y;
        particleSize = ps;
        tickRate = tr;
        particles = new ArrayList<Particle>(); //list to store particle objects

        //tick rate stuff
        this.setTickRate(tr); //set tick rate
        this.setTickHandler(this::updateParticles);
        this.startTimer();
        isPaused = false;
    }

    @Override
    public void onKeyPressed(String keyText, String keyModifiers) { //if keyboard button is pressed

        if(!isPaused) {
            //pause
            this.stopTimer();
            isPaused = true;
            System.out.println("PAUSED");
        } else {
            this.startTimer();
            isPaused = false;
            System.out.println("UNPAUSED");
        }
    }

    public void createParticles() { //creating and adding particles to the "particles" arraylist
        for(int i = 0; i < numParticles; i++) { //adding particles to list

            //generate random position for particle
            int randomX = (int) (Math.random() * (xSize + 1 - particleSize));
            int randomY = (int) (Math.random() * (ySize + 1 - particleSize));

            //adding particles to arraylist
            Particle temp = new Particle(randomX, randomY, particleSize, xSize, ySize, 1, 1);
            particles.add(temp);

        }
    }

    public void updateParticles(Pad pad, Long lon) { //this is called every tick
    
        for(int i = 0; i < particles.size(); i++) { //calling move methods on all particles
            for(int j = i + 1; j < particles.size() - 1; j++) {
                Particle p = particles.get(i);
                Particle o = particles.get(j);
                p.checkCollisions(o);
            }

            particles.get(i).moveParticle(); //moving individual particle
        }
    }
}

class Particle extends Oval {

    private int xPos;
    private int yPos;
    private double velocityX;
    private double velocityY;
    private int xSize; //size of the window
    private int ySize; //size of the window
    private int particleSize; //diameter of the particle
    private double mass = 1.0;
    
    public Particle(int x, int y, int s, int cX, int cY, int xV, int yV) {
        super(x, y, s, s);
        xPos = x; //particle position
        yPos = y;
        particleSize = s; //particle diameter
        xSize = cX; //window size
        ySize = cY;
        velocityX = 1; //(Math.random() * 3) - 1;; //random value between -1 and 1 (int) (Math.random() * (max - min + 1)) + min;
        velocityY = -1; //(Math.random() * 3) - 1;
        //super.setFillColor(252, 127, 3);

        int R = (int)(Math.random()*256);
        int G = (int)(Math.random()*256);
        int B = (int)(Math.random()*256);
        Color color = new Color(R, G, B); //random color, but can be bright or dull
        super.setFillColor(color);
    }

    public double getXPos() {
        return super.getX();
    }
    public double getYPos() {
        return super.getY();
    }

    public double getXVelocity() {
        return velocityX;
    }
    public double getYVelocity() {
        return velocityY;
    }

    public void setXVelocity(double x) {
        velocityX = x;
    }
    public void setYVelocity(double y) {
        velocityY = y;
    }
    
    public void bounce(Particle other) {

        int x1 = (int) (this.getXPos() + (particleSize/2.0));
        int y1 = (int) (this.getYPos() + (particleSize/2.0));
        int x2 = (int) (other.getXPos() + (particleSize/2.0));
        int y2 = (int) (other.getYPos() +  (particleSize/2.0));
        
        double v1x = this.getXVelocity();
        double v1y = this.getYVelocity();
        double v2x = other.getXVelocity();
        double v2y = other.getYVelocity();

        double n2x = x2 - x1;
        double n2y = y2 - y1;
        double n1x = x1 - x2;
        double n1y = y1 - y2;

        double r1x = v1x + n1x;
        double r1y = v1y + n1y;
        double r2x = v2x + n2x;
        double r2y = v2y + n2y; 

        double m1 = Math.sqrt((r1x * r1x) + (r1y * r1y)) - Math.sqrt((v1x * v1x) + (v1y * v1y));
        double m2 = Math.sqrt((r2x * r2x) + (r2y * r2y)) - Math.sqrt((v2x * v2x) + (v2y * v2y));

        this.setXVelocity(r1x / m1);
        this.setYVelocity(r1y / m1);
        other.setXVelocity(r2x / m2);
        other.setYVelocity(r2y / m2);
    }

    public void checkCollisions(Particle other) { //x and y pos in this class don't move with the particle, they are just the original spawn locations
        
        //particles need to bounce when colliding with each other
        int x1 = (int) (this.getXPos() + (particleSize/2.0));
        int y1 = (int) (this.getYPos() + (particleSize/2.0));
        int x2 = (int) (other.getXPos() + (particleSize/2.0));
        int y2 = (int) (other.getYPos() +  (particleSize/2.0));

        double distance = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));

        if(distance <= particleSize) {
            //reverse particle direction
            //calculateMoveParticle(other);
            this.bounce(other);

            /*
            this.velocityX = -this.velocityX;
            this.velocityY = -this.velocityY;

            other.velocityX = -other.velocityX;
            other.velocityY = -other.velocityY;
            */
            int R = (int)(Math.random()*256);
            int G = (int)(Math.random()*256);
            int B = (int)(Math.random()*256);
            Color color = new Color(R, G, B); //random color, but can be bright or dull
            this.setFillColor(color);
            int R2 = (int)(Math.random()*256);
            int G2 = (int)(Math.random()*256);
            int B2 = (int)(Math.random()*256);
            Color color2 = new Color(R2, G2, B2); //random color, but can be bright or dull
            other.setFillColor(color2);
        }
    }

    public void moveParticle() {

        //movement checks for walls
        if(super.getX() > xSize - particleSize || super.getX() < 0) {
            velocityX = -velocityX;
        } 
        if(super.getY() > ySize - particleSize || super.getY() < 0) {
            velocityY = -velocityY;
        } 
        //particle move call
        this.move(velocityX, velocityY);
    }
}