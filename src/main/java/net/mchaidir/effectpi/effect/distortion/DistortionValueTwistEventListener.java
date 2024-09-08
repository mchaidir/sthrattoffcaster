package net.mchaidir.effectpi.effect.distortion;

import net.mchaidir.effectpi.common.TwistEventListener;

public class DistortionValueTwistEventListener implements TwistEventListener {
	
	private final Distortion distortion;
	
	public DistortionValueTwistEventListener(Distortion distortion) {
		this.distortion = distortion;
	}
	
	@Override
	public void onButtonPressed() {
	
	}
	
	@Override
	public void onEncoderTurned(int encoderDelta) {
		distortion.setDistortionValue(distortion.getDistortionValue() + encoderDelta);
	}
}
