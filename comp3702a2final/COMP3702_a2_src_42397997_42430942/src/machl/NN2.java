/**
 * Students: SU Sheng Loong (42397997) and TEE Lip Jian (42430942)
 */
package machl;

import java.util.Random;

public class NN2 extends NN1 {
	// the values produced by hidden units
	double[] hid;			
	// the trainable weight values [to hidden][from input]
	public double[][] hidw;	
	// the trainable bias values for hidden units
	public double[] hidbias;

	/** Constructs a multi-layer neural network structure and 
	 *  initializes weights 
	 *  and bias to small random values. 
	 *  @param  nInput  Number of input nodes
	 *  @param  nOutput Number of output nodes
	 *  @param  nHid	  Number of hidden nodes
	 *  @param  seed    Seed for the random number generator 
	 *  					used for initial weights.
	 */
	public NN2(int nInput, int nOutput, int nHid, int seed) {
		// the super constructor is now responsible for 
		//     setting up the input matrix between hidden 
		//     layer and output layer
		super(nHid, nOutput, seed);
		// if number of hidden units specified is not valid
		//     set the number to the number of inputs * 2/3 
		//	   + number of outputs
		if (nHid <= 0)
			nHid = nInput * 2 / 3 + nOutput;
		// allocate space for hidden units and weight values 
		//      between input and hidden units
		hid = new double[nHid];
		hidw = new double[nHid][nInput];
		hidbias = new double[nHid];
		if (rand == null)
			rand=new Random(seed);
		// initialize weight and bias values between hidden 
		//     and output layer
		for (int j=0; j<nOutput; j++) {
			for (int i=0; i<nHid; i++)
				w[j][i]=rand.nextGaussian()*.1;
			bias[j]=rand.nextGaussian()*.1;
		}
	}

	/** Computes the output values of the output nodes in 
	 *  the network given input values.
	 *  @param  x  The input values.
	 *  @return double[] The vector of computed output values
	 */
	public double[] feedforward(double[] x) {
		// compute the activation of each hidden unit 
		//     based on the input value
		for (int j=0; j<hid.length; j++) {
			double sum=0; // reset summed activation value
			for (int i=0; i<x.length; i++)
				sum+=x[i]*hidw[j][i];
			hid[j]=outputFunction(sum+hidbias[j]);
		}
		// compute the activation of each output unit based 
		//    on the hidden layer value
		for (int j=0; j<y.length; j++) {
			double sum=0; // reset summed activation value
			for (int i=0; i<hid.length; i++)
				sum+=hid[i]*w[j][i];
			y[j]=outputFunction(sum+bias[j]);
		}
		return y;
	}

	/** Adapts weights in the network given the specification 
	 *  of which values that should appear at the output 
	 *  (target) when the input has been presented.
	 *  The procedure is known as error backpropagation. 
	 *  This implementation is "online" rather than 
	 *  "batched", that is, the change is not based on the 
	 *  gradient of the global error, merely the local -- 
	 *  pattern-specific -- error.
	 *  @param  x  The input values.
	 *  @param  d  The desired output values.
	 *  @param  eta     The learning rate, always between 0 and 
	 *  				1, typically a small value, e.g. 0.1
	 *  @return double  An error value 
	 *  				(the root-mean-squared-error).
	 */
	public double train(double[] x, double[] d, double eta) {

		// present the input and calculate the outputs
		feedforward(x);

		// allocate space for errors of output nodes 
		//     and hidden units
		double[] outputerror=new double[y.length];
		double[] hiderror = new double[hid.length];

		// compute the error of output nodes (explicit target 
		//     is available -- so quite simple)
		//     also, calculate the root-mean-squared-error to 
		//     indicate progress
		double rmse=0;
		for (int j=0; j<y.length; j++) {
			double diff=d[j]-y[j];
			outputerror[j]=diff*outputFunctionDerivative(y[j]);
			rmse+=diff*diff;
		}
		rmse=Math.sqrt(rmse/y.length);

		// compute the error of the hidden units since these 
		//     nodes are responsible for some fraction of the 
		// 	   output error
		for (int j=0; j<hid.length; j++) {
			double sum = 0.0;
			for (int i=0; i<y.length; i++) {
				sum+=w[i][j]*outputerror[i];
			}
			hiderror[j]=sum*outputFunctionDerivative(hid[j]);
		}	
		// change output weights according to errors
		for (int j=0; j<y.length; j++) {
			for (int i=0; i<hid.length; i++) {
				w[j][i]+=outputerror[j]*hid[i]*eta;
			}
			// bias can be understood as a weight from a node 
			//     which is always 1.0.
			bias[j]+=outputerror[j]*1.0*eta; 
		}
		// change hidden layer weights according to errors
		for (int j=0; j<hid.length; j++) {
			for (int i=0; i<x.length; i++) {
				hidw[j][i]+=hiderror[j]*x[i]*eta;
			}
			// bias can be understood as a weight from a node 
			//     which is always 1.0.
			hidbias[j]+=hiderror[j]*1.0*eta; 
		}
		return rmse;
	}
}
