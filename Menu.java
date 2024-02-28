import java.util.*;

public class Menu {
    
    private int width;
    private int height;
    private int numParticles;
    private int particleSize;
    private int tickRate;


    public Menu() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Totally Accurate Particle Simulator!");

        System.out.print("Particle System width:");
        width = sc.nextInt();
        System.out.print("Particle System height:");
        height = sc.nextInt();
        System.out.print("Number of particles:");
        numParticles = sc.nextInt();
        System.out.print("Particle Size (even number):");
        particleSize = sc.nextInt();
        System.out.print("Particle System framerate (ex. 60fps):");
        tickRate = sc.nextInt();

        System.out.println("Particle Simulation Starting!");
        Window window = new Window(width, height, numParticles, particleSize, tickRate);
        window.createParticles();
    }
     
}