package net.mchaidir.effectpi.effect;

import java.util.List;

public class EffectChain {
	
	private List<EffectBase> effects;
	
	public int applyEffects(int inputSignal) {
		int signal = inputSignal;
		for (EffectBase effect : effects) {
			if (effect.isEnabled()) {
				signal = effect.applyEffect(signal);
			}
		}
		
		return signal;
	}
	
}
