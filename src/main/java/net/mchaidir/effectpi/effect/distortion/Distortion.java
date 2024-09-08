package net.mchaidir.effectpi.effect.distortion;

import com.sparkfun.qwiic.twist.Twist;
import lombok.Getter;
import lombok.Setter;
import net.mchaidir.effectpi.effect.EffectBase;
import net.mchaidir.effectpi.common.TwistEventListener;
import net.mchaidir.effectpi.common.LayeredTwistEventListener;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

@Getter
@Setter
public class Distortion extends EffectBase {
	
	private int distortionValue;
	private int volumeValue;
	private int lowCutoffValue;
	private int midCutoffValue;
	private int highCutoffValue;
	
	private int gain;
	
	private double mid;
	private double bass;
	private double treble;
	
	private double compressionRatio;
	private int compressionThreshold;
	
	public Distortion(Twist distortionLevelTwist,
										Twist distortionCutoffTwist,
										ScheduledExecutorService scheduledExecutorService) {
		super(scheduledExecutorService);
		
		TwistEventListener distortionValueTwistEventListener =
				new DistortionValueTwistEventListener(this);
		TwistEventListener volumeValueTwistEventListener =
				new VolumeValueTwistEventListener(this);
		TwistEventListener distortionLevelTwistEventListener =
				new LayeredTwistEventListener(
						new TwistEventListener[] {
								distortionValueTwistEventListener,
								volumeValueTwistEventListener
						});
		registerTwist(distortionLevelTwist, distortionLevelTwistEventListener);
		
		TwistEventListener distortionCutoffTwistEventListener =
				new LayeredTwistEventListener(new TwistEventListener[] {
						lowCutoffEventListener,
						midCutoffEventListener,
						highCutoffEventListener
				});
		registerTwist(distortionCutoffTwist, distortionCutoffTwistEventListener);
	}
	
	public Distortion(Map<Twist, TwistEventListener> twistMap,
										ScheduledExecutorService scheduledExecutorService) {
		super(scheduledExecutorService);
	}
	
	public int applyEffect(int inputSignal) {
		// Apply Gain/Drive
		int signal = (int) (inputSignal * gain);
		
		// Apply Clipping
		if (clippingType.equals("soft")) {
			signal = (int) (Math.tanh((double) signal / clippingThreshold) * clippingThreshold);
		} else if (clippingType.equals("hard")) {
			signal = Math.max(-clippingThreshold, Math.min(signal, clippingThreshold));
		} else {
			throw new IllegalArgumentException("clippingType must be 'soft' or 'hard'");
		}
		
		// Apply Tone Control
		signal = toneControl(signal, bass, mid, treble, sampleRate);
		
		// Apply Compression
		signal = compress(signal, compressionThreshold, compressionRatio);
		
		return signal;
	}
	
	// More sophisticated tone control with bass, mid, and treble adjustments
	int toneControl(int signal, double bass, double mid, double treble, int sampleRate) {
		// Apply bass (low-pass filter)
		int bassSignal = applyLowPassFilter(signal, bass, sampleRate);
		
		// Apply mid (band-pass filter)
		int midSignal = applyBandPassFilter(signal, mid, sampleRate);
		
		// Apply treble (high-pass filter)
		int trebleSignal = applyHighPassFilter(signal, treble, sampleRate);
		
		// Combine all three bands (this is a simple additive approach)
		return bassSignal + midSignal + trebleSignal;
	}
	
	// Low-pass filter implementation for bass control
	int applyLowPassFilter(int signal, double cutoff, int sampleRate) {
		// Assuming a very simple low-pass filter (first-order)
		double alpha = Math.exp(-2.0 * Math.PI * cutoff / sampleRate);
		return (int) (alpha * signal);
	}
	
	// Band-pass filter implementation for midrange control
	int applyBandPassFilter(int signal, double centerFreq, int sampleRate) {
		// Simple implementation, more complex methods may be required for accuracy
		double qFactor = 1.0; // Quality factor (determines bandwidth)
		double alpha = Math.exp(-2.0 * Math.PI * centerFreq / sampleRate / qFactor);
		return (int) ((1 - alpha) * signal);  // Simplified for demonstration
	}
	
	// High-pass filter implementation for treble control
	int applyHighPassFilter(int signal, double cutoff, int sampleRate) {
		double alpha = Math.exp(-2.0 * Math.PI * cutoff / sampleRate);
		return signal - (int) (alpha * signal); // Subtract low frequencies to get high-pass effect
	}
	
	// Apply compression
	int compress(int signal, int threshold, double ratio) {
		return signal > threshold ? (int) (threshold + (signal - threshold) / ratio) : signal;
	}
	
}
