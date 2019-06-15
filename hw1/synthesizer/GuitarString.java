package synthesizer;

import java.util.HashSet;
import java.util.Set;

//Make sure this class is public
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        buffer=new ArrayRingBuffer<>((int)(SR/frequency));
        for (int i=0; i<buffer.capacity(); i++) {
            buffer.enqueue(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        while(!buffer.isEmpty()){
            buffer.dequeue();
        }
        Set<Double> set=new HashSet<>();
        while(set.size()!=buffer.capacity()){
            int oldSize=set.size();
            double number=Math.random()-0.5;
            set.add(number);
            int newSize=set.size();
            if(oldSize!=newSize){
                buffer.enqueue(number);
            }

        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double first=buffer.dequeue();
        double second=buffer.peek();
        double sample=(first+second)*0.5*DECAY;
        buffer.enqueue(sample);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }
}
