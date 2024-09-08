package net.mchaidir.effectpi.effect.distortion;

import net.mchaidir.effectpi.common.TwistEventListener;

public class VolumeValueTwistEventListener implements TwistEventListener {
	
	private final Distortion distortion;
	
	public VolumeValueTwistEventListener(Distortion distortion) {
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
