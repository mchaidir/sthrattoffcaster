package net.mchaidir.effectpi.effect.impl;

import com.sparkfun.qwiic.twist.Twist;
import lombok.RequiredArgsConstructor;
import net.mchaidir.effectpi.effect.EffectBase;
import net.mchaidir.effectpi.common.TwistEventListener;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

@RequiredArgsConstructor
public class Clean extends EffectBase {
	
	public Clean(Map<Twist, TwistEventListener> twistMap,
							 ScheduledExecutorService scheduledExecutorService) {
		super(scheduledExecutorService);
	}
	
	public int applyEffect(int inputSignal) {
		return inputSignal;
	}
	
}
