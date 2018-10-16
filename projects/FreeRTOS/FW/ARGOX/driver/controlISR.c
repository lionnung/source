/*
 * controlISR.c
 *
 *  Created on: 2011/8/17
 *      Author: lion
 */

/* FreeRTOS includes. */
#include <FreeRTOS.h>

#include "control.h"

int gap[2000];
int ribbon[2000];
int ccc;
int finish;

void vMotor_Timer_ISR(void)
{
	static int c = 0;
	static unsigned int t = 0;
	unsigned short ips;

	if (t > 6000) ips = IPS12;
	else if (t++ > 5000) ips = IPS11;
	else if (t++ > 4000) ips = IPS10;
	else if (t++ > 3000) ips = IPS9;
	else if (t++ > 2000) ips = IPS8;
	else if (t++ > 1000) ips = IPS7;
	else ips = IPS6;

	motor_timer.stop(&motor_timer);
	motor.motorops->step(&motor);
	motor_timer.start(IPS1, &motor_timer);

	if (++c > 1000) {
		c = 0;
		LED_Toggle( LED_POWER );
	}

	if (motor.phase == 1) {
	    tph.init_print(&tph);
		gap[ccc] = adc.read(ADC_MEDIA, &adc);
		ribbon[ccc] = adc.read(ADC_RIBBON, &adc);
		ccc++;
		if (ccc >= 1999) {
			motor_timer.stop(&motor_timer);
			finish = 1;
		}
	}
	motor.phase = !motor.phase;

	/* End the interrupt in the AIC. */
	AT91C_BASE_AIC->AIC_EOICR = motor_timer.hw->TC_SR;
}

void vHeating_Timer_ISR(void)
{
	static int c = 0;

	tph.stb1->stop(tph.stb1);
	tph.stb2->stop(tph.stb2);

	if (++c > 900) {
		c = 0;
		LED_Toggle( LED_POWER );
	}

	/* End the interrupt in the AIC. */
	AT91C_BASE_AIC->AIC_EOICR = tph.stb2->hw->TC_SR;
}

void vImage_ISR(void)
{
	static int c = 0;

	tph.hw->SPI_IDR = AT91C_SPI_ENDTX;
	tph.hw->SPI_PTCR = AT91C_PDC_TXTDIS;

	if (++c > 800) {
		c = 0;
		LED_Toggle( LED_POWER );
	}

	/* End the interrupt in the AIC. */
	AT91C_BASE_AIC->AIC_EOICR = tph.hw->SPI_SR;
}
