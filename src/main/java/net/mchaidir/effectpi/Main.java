package net.mchaidir.effectpi;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import com.pi4j.io.spi.Spi;
import com.pi4j.plugin.pigpio.provider.i2c.PiGpioI2CProvider;
import com.sparkfun.qwiic.twist.Twist;
import net.mchaidir.effectpi.effect.EffectBase;
import net.mchaidir.effectpi.effect.impl.Clean;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
	
	static final String GPIO_PROVIDER = "pigpio";
	
	public static void main(String[] args) throws Exception {
		//TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
		// to see how IntelliJ IDEA suggests fixing it.
		System.out.printf("Hello and welcome!");
		
		for (int i = 1; i <= 5; i++) {
			//TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
			// for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
			System.out.println("i = " + i);
		}
		
		EffectBase clean = new Clean();
		
		Context pi4j = Pi4J.newAutoContext();
		I2CProvider i2cProvider = pi4j.provider(PiGpioI2CProvider.class);
		
		I2CConfig firstTwistConfig = I2CConfig.newBuilder(pi4j)
				.id("")
				.bus(bus)
				.device(address)
				.build();
		Twist firstTwist = new Twist(i2cProvider, firstTwistConfig);
		
		
		
		
		
    DigitalInput pushButton = createDigitalInput(pi4j);
		
		Spi spiDevice = createSpiDevice();
		spiDevice.
	}
	
	static DigitalInput createDigitalInput(Context pi4jContext) {
		return pi4jContext.create(
				DigitalInput.newConfigBuilder(pi4jContext)
						.id(id)
						.name(id)
						.address(address)
						.provider(GPIO_PROVIDER)
						.build());
	}
	
	Spi createSpiDevice(Context pi4jContext) {
		return pi4jContext.create(
				Spi.newConfigBuilder(pi4jContext)
						.provider(GPIO_PROVIDER)
						.id(id)
						.name()
						.bus()
						.chipSelect()
						.baud()
						.build());
	}
}