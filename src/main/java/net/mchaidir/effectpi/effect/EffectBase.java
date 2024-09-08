package net.mchaidir.effectpi.effect;

import com.sparkfun.qwiic.twist.Twist;
import net.mchaidir.effectpi.common.TwistEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class EffectBase {
	
	private boolean enabled;
	boolean selected;
	
	private static final int POLLING_INTERVAL_MS = 100;
	
	private final Map<Twist, TwistEventListener> twistMap;
	
	public EffectBase(ScheduledExecutorService scheduledExecutorService) {
		this.twistMap = new HashMap<>();
		
		scheduledExecutorService.scheduleAtFixedRate(
				this::pollEvent,
				0,
				POLLING_INTERVAL_MS,
				TimeUnit.MILLISECONDS);
	}
	
	void pollEvent() {
		if (selected) {
			for (Map.Entry<Twist, TwistEventListener> twistEntry : twistMap.entrySet()) {
				Twist twist = twistEntry.getKey();
				TwistEventListener eventListener = twistEntry.getValue();
				
				if (twist.isPressed()) {
					eventListener.onButtonPressed();
				}
				
				if (twist.hasMoved()) {
					eventListener.onEncoderTurned(twist.getDiff(true));
				}
			}
		}
	}
	
	protected void registerTwist(Twist twist, TwistEventListener eventListener) {
		twistMap.put(twist, eventListener);
	}
	
	protected abstract int applyEffect(int inputSignal);
	
}
