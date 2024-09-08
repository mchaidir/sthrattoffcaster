/*
 ******************************************************************************
 * @file    Twist.java
 * @author  SparkFun Electronics
 * @version 1.0.0
 * @date    2024
 * @brief   A Java library for the SparkFun Qwiic Twist Rotary Encoder
 *          This library is a port of the original SparkFun Qwiic Twist library,
 *          implemented in Java for use with Pi4J and I2C.
 *
 * SparkFun Electronics
 * This code is released under the MIT license.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ******************************************************************************
 */

package com.sparkfun.qwiic.twist;

import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;
import com.pi4j.io.i2c.I2CProvider;
import lombok.Getter;

@Getter
public class Twist {
	
	private static final int DEFAULT_ADDRESS = 0x3F;
	private static final int REGISTER_CLEAR_INTERRUPTS = 0x07;
	private static final int REGISTER_COUNT = 0x04;
	private static final int REGISTER_LIMIT = 0x08;
	private static final int REGISTER_DIFF = 0x06;
	private static final int REGISTER_PRESSED = 0x05;
	private static final int REGISTER_RED = 0x09;
	private static final int REGISTER_GREEN = 0x0A;
	private static final int REGISTER_BLUE = 0x0B;
	private static final int REGISTER_COLOR_CYCLE = 0x0C;
	private static final int REGISTER_COLOR_CYCLE_ENABLE = 0x0D;
	private static final int REGISTER_VERSION = 0x02;
	private static final int REGISTER_CLICKED = 0x03;
	private static final int REGISTER_MOVED = 0x0E;
	private static final int REGISTER_SINCE_LAST_MOVEMENT = 0x10;
	private static final int REGISTER_SINCE_LAST_PRESS = 0x11;
	private static final int REGISTER_INT_TIMEOUT = 0x0F;
	private static final int REGISTER_CONNECT_RED = 0x12;
	private static final int REGISTER_CONNECT_GREEN = 0x13;
	private static final int REGISTER_CONNECT_BLUE = 0x14;
	
	private final I2C device;
	
	/**
	 * Constructor to initialize the Qwiic Twist device with the specified I2C provider and configuration.
	 *
	 * @param i2cProvider The I2CProvider used to create the I2C device.
	 * @param config      The I2CConfig containing the configuration for the I2C device.
	 * @throws Exception  If the device initialization fails.
	 */
	public Twist(I2CProvider i2cProvider, I2CConfig config) throws Exception {
		this.device = i2cProvider.create(config);
	}
	
	/**
	 * Checks if the device is connected.
	 *
	 * @return true if the device is connected, false otherwise.
	 */
	public boolean isConnected() {
		try {
			this.device.read();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Initializes the device and checks if it's connected.
	 *
	 * @return true if the device is connected, false otherwise.
	 */
	public boolean begin() {
		return isConnected();
	}
	
	/**
	 * Clears any pending interrupts on the device.
	 */
	public void clearInterrupts() {
		writeByteRegister(REGISTER_CLEAR_INTERRUPTS, (byte) 0x01);
	}
	
	/**
	 * Gets the current count of the rotary encoder.
	 *
	 * @return The current count value.
	 */
	public int getCount() {
		return readTwoByteRegister(REGISTER_COUNT);
	}
	
	/**
	 * Sets the count of the rotary encoder to a specific value.
	 *
	 * @param amount The count value to set.
	 */
	public void setCount(int amount) {
		writeTwoByteRegister(REGISTER_COUNT, amount);
	}
	
	/**
	 * Gets the maximum limit of the rotary encoder count.
	 *
	 * @return The limit value.
	 */
	public int getLimit() {
		return readTwoByteRegister(REGISTER_LIMIT);
	}
	
	/**
	 * Sets the maximum limit of the rotary encoder count.
	 *
	 * @param amount The limit value to set.
	 */
	public void setLimit(int amount) {
		writeTwoByteRegister(REGISTER_LIMIT, amount);
	}
	
	/**
	 * Gets the difference in the encoder position since the last check.
	 *
	 * @param clearValue If true, clears the difference after reading.
	 * @return The difference value.
	 */
	public int getDiff(boolean clearValue) {
		int diff = readTwoByteRegister(REGISTER_DIFF);
		if (clearValue) {
			clearInterrupts();
		}
		return diff;
	}
	
	/**
	 * Checks if the encoder button is currently pressed.
	 *
	 * @return true if the button is pressed, false otherwise.
	 */
	public boolean isPressed() {
		return (readByteRegister(REGISTER_PRESSED) & 0x01) != 0;
	}
	
	/**
	 * Checks if the encoder button was clicked.
	 *
	 * @return true if the button was clicked, false otherwise.
	 */
	public boolean wasClicked() {
		return (readByteRegister(REGISTER_CLICKED) & 0x01) != 0;
	}
	
	/**
	 * Checks if the encoder has been moved.
	 *
	 * @return true if the encoder has been moved, false otherwise.
	 */
	public boolean hasMoved() {
		return (readByteRegister(REGISTER_MOVED) & 0x01) != 0;
	}
	
	/**
	 * Gets the time since the last movement of the encoder.
	 *
	 * @param clearValue If true, clears the time after reading.
	 * @return The time since the last movement.
	 */
	public int sinceLastMovement(boolean clearValue) {
		int value = readTwoByteRegister(REGISTER_SINCE_LAST_MOVEMENT);
		if (clearValue) {
			clearInterrupts();
		}
		return value;
	}
	
	/**
	 * Gets the time since the last press of the encoder button.
	 *
	 * @param clearValue If true, clears the time after reading.
	 * @return The time since the last press.
	 */
	public int sinceLastPress(boolean clearValue) {
		int value = readTwoByteRegister(REGISTER_SINCE_LAST_PRESS);
		if (clearValue) {
			clearInterrupts();
		}
		return value;
	}
	
	/**
	 * Sets the RGB color of the LED on the device.
	 *
	 * @param red   The red component (0-255).
	 * @param green The green component (0-255).
	 * @param blue  The blue component (0-255).
	 */
	public void setColor(int red, int green, int blue) {
		writeByteRegister(REGISTER_RED, (byte) red);
		writeByteRegister(REGISTER_GREEN, (byte) green);
		writeByteRegister(REGISTER_BLUE, (byte) blue);
	}
	
	/**
	 * Sets the red component of the LED color.
	 *
	 * @param red The red component (0-255).
	 */
	public void setRed(int red) {
		writeByteRegister(REGISTER_RED, (byte) red);
	}
	
	/**
	 * Sets the green component of the LED color.
	 *
	 * @param green The green component (0-255).
	 */
	public void setGreen(int green) {
		writeByteRegister(REGISTER_GREEN, (byte) green);
	}
	
	/**
	 * Sets the blue component of the LED color.
	 *
	 * @param blue The blue component (0-255).
	 */
	public void setBlue(int blue) {
		writeByteRegister(REGISTER_BLUE, (byte) blue);
	}
	
	/**
	 * Gets the red component of the LED color.
	 *
	 * @return The red component (0-255).
	 */
	public int getRed() {
		return readByteRegister(REGISTER_RED);
	}
	
	/**
	 * Gets the green component of the LED color.
	 *
	 * @return The green component (0-255).
	 */
	public int getGreen() {
		return readByteRegister(REGISTER_GREEN);
	}
	
	/**
	 * Gets the blue component of the LED color.
	 *
	 * @return The blue component (0-255).
	 */
	public int getBlue() {
		return readByteRegister(REGISTER_BLUE);
	}
	
	/**
	 * Gets the firmware version of the device.
	 *
	 * @return The version number.
	 */
	public int getVersion() {
		return readTwoByteRegister(REGISTER_VERSION);
	}
	
	/**
	 * Sets the RGB color of the connected device.
	 *
	 * @param red   The red component (0-255).
	 * @param green The green component (0-255).
	 * @param blue  The blue component (0-255).
	 */
	public void connectColor(int red, int green, int blue) {
		writeByteRegister(REGISTER_CONNECT_RED, (byte) red);
		writeByteRegister(REGISTER_CONNECT_GREEN, (byte) green);
		writeByteRegister(REGISTER_CONNECT_BLUE, (byte) blue);
	}
	
	/**
	 * Sets the red component of the connected device's color.
	 *
	 * @param red The red component (0-255).
	 */
	public void setConnectRed(int red) {
		writeByteRegister(REGISTER_CONNECT_RED, (byte) red);
	}
	
	/**
	 * Gets the red component of the connected device's color.
	 *
	 * @return The red component (0-255).
	 */
	public int getConnectRed() {
		return readByteRegister(REGISTER_CONNECT_RED);
	}
	
	/**
	 * Sets the green component of the connected device's color.
	 *
	 * @param green The green component (0-255).
	 */
	public void setConnectGreen(int green) {
		writeByteRegister(REGISTER_CONNECT_GREEN, (byte) green);
	}
	
	/**
	 * Gets the green component of the connected device's color.
	 *
	 * @return The green component (0-255).
	 */
	public int getConnectGreen() {
		return readByteRegister(REGISTER_CONNECT_GREEN);
	}
	
	/**
	 * Sets the blue component of the connected device's color.
	 *
	 * @param blue The blue component (0-255).
	 */
	public void setConnectBlue(int blue) {
		writeByteRegister(REGISTER_CONNECT_BLUE, (byte) blue);
	}
	
	/**
	 * Gets the blue component of the connected device's color.
	 *
	 * @return The blue component (0-255).
	 */
	public int getConnectBlue() {
		return readByteRegister(REGISTER_CONNECT_BLUE);
	}
	
	/**
	 * Sets the interrupt timeout value.
	 *
	 * @param timeout The timeout value in milliseconds.
	 */
	public void setIntTimeout(int timeout) {
		writeTwoByteRegister(REGISTER_INT_TIMEOUT, timeout);
	}
	
	/**
	 * Gets the interrupt timeout value.
	 *
	 * @return The timeout value in milliseconds.
	 */
	public int getIntTimeout() {
		return readTwoByteRegister(REGISTER_INT_TIMEOUT);
	}
	
	/**
	 * Sets the color cycle time for the LED.
	 *
	 * @param cycleTime The time in milliseconds for one complete color cycle.
	 */
	public void setColorCycle(int cycleTime) {
		writeTwoByteRegister(REGISTER_COLOR_CYCLE, cycleTime);
	}
	
	/**
	 * Checks if the color cycling feature is enabled.
	 *
	 * @return true if color cycling is enabled, false otherwise.
	 */
	public boolean isColorCycleEnabled() {
		return (readByteRegister(REGISTER_COLOR_CYCLE_ENABLE) & 0x01) != 0;
	}
	
	/**
	 * Enables or disables the color cycling feature.
	 *
	 * @param enable true to enable color cycling, false to disable.
	 */
	public void enableColorCycle(boolean enable) {
		writeByteRegister(REGISTER_COLOR_CYCLE_ENABLE, (byte) (enable ? 0x01 : 0x00));
	}
	
	// Helper method: read a single byte from a register
	private int readByteRegister(int register) {
		try {
			byte[] buffer = new byte[1];
			this.device.readRegister(register, buffer);
			return buffer[0] & 0xFF;
		} catch (Exception e) {
			return 0;
		}
	}
	
	// Helper method: read two bytes from a register
	private int readTwoByteRegister(int register) {
		try {
			byte[] buffer = new byte[2];
			this.device.readRegister(register, buffer);
			return (buffer[1] << 8) | (buffer[0] & 0xFF);
		} catch (Exception e) {
			return 0;
		}
	}
	
	// Helper method: write a single byte to a register
	private void writeByteRegister(int register, byte value) {
		try {
			this.device.writeRegister(register, value);
		} catch (Exception e) {
			// Handle the exception (if needed)
		}
	}
	
	// Helper method: write two bytes to a register
	private void writeTwoByteRegister(int register, int value) {
		try {
			byte[] buffer = new byte[]{
					(byte) (value & 0xFF), (byte) ((value >> 8) & 0xFF)
			};
			this.device.writeRegister(register, buffer);
		} catch (Exception e) {
			// Handle the exception (if needed)
		}
	}
}
