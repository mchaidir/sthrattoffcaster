package net.mchaidir.effectpi.common;

public class LayeredTwistEventListener implements TwistEventListener {
	
	private int activeLayer;
	
	private final int layerCount;
	private final TwistEventListener[] eventListenerLayers;
	
//	private final Twist controlTwist;
//	private final TwistEventListener controlEventListener;
	
	public LayeredTwistEventListener(TwistEventListener[] eventListenerLayers) {
		this.activeLayer = 0;
		this.eventListenerLayers = eventListenerLayers;
		this.layerCount = this.eventListenerLayers.length;
	}
	
	void toNextLayer() {
		activeLayer = (activeLayer + 1) % layerCount;
	}
	
	void toPreviousLayer() {
		activeLayer = (activeLayer - 1 + layerCount) % layerCount;
	}
	
	@Override
	public void onButtonPressed() {
		eventListenerLayers[activeLayer].onButtonPressed();
	}
	
	@Override
	public void onEncoderTurned(int encoderDelta) {
		eventListenerLayers[activeLayer].onEncoderTurned(encoderDelta);
	}
}
