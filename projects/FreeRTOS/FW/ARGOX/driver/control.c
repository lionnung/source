/*
 * control.c
 *
 *  Created on: 2011/8/11
 *      Author: lion
 */

/* FreeRTOS includes. */
#include <FreeRTOS.h>

#include "control.h"

/* Timer / Counter 2 for motor */
#define MOTOR_TIMER_HW   AT91C_BASE_TC2
#define MOTOR_TIMER_ID   AT91C_ID_TC2
#define MOTOR_TIMER_MODE (TC_CLKS_TIMER_IDX | \
		                  AT91C_TC_CPCSTOP | \
		                  AT91C_TC_CPCDIS | \
		                  AT91C_TC_WAVESEL_UP_AUTO | \
		                  AT91C_TC_WAVE)

/* Timer / Counter 0 for strobe 1 */
#define STB1_TIMER_HW   AT91C_BASE_TC0
#define STB1_TIMER_ID   AT91C_ID_TC0
#define STB1_TIMER_MODE (AT91C_TC_CLKS_TIMER_DIV2_CLOCK | \
		                 AT91C_TC_CPCSTOP | \
		                 AT91C_TC_CPCDIS | \
		                 AT91C_TC_WAVESEL_UP_AUTO | \
		                 AT91C_TC_WAVE | \
		                 AT91C_TC_ACPA_CLEAR | \
		                 AT91C_TC_ACPC_SET)

/* Timer / Counter 1 for strobe 2 */
#define STB2_TIMER_HW   AT91C_BASE_TC1
#define STB2_TIMER_ID   AT91C_ID_TC1
#define STB2_TIMER_MODE (AT91C_TC_CLKS_TIMER_DIV2_CLOCK | \
		                 AT91C_TC_CPCSTOP | \
		                 AT91C_TC_CPCDIS | \
		                 AT91C_TC_WAVESEL_UP_AUTO | \
		                 AT91C_TC_WAVE | \
		                 AT91C_TC_ACPA_CLEAR | \
		                 AT91C_TC_ACPC_SET)

/* SPI for TPH data transfer */
/* SPI clock frequency, in Hz. */
#define TPH_SPCK    12000000
#define TPH_HW      AT91C_BASE_SPI1
#define TPH_ID      AT91C_ID_SPI1
#define TPH_MODE    (AT91C_SPI_MSTR | \
		             AT91C_SPI_MODFDIS)
#define TPH_CS_MODE (AT91C_SPI_NCPHA | \
		             SPID_CSR_SCBR(configCPU_CLOCK_HZ, TPH_SPCK) | \
		             SPID_CSR_DLYBS(configCPU_CLOCK_HZ, 0) | \
		             SPID_CSR_DLYBCT(configCPU_CLOCK_HZ, 0))

/* ADC */
#define ADC_HW   AT91C_BASE_ADC
#define ADC_ID   AT91C_ID_ADC
#define ADC_FREQ 5000000

char *dummy;

void print_init(void)
{

	/* Motor init */
	motor_timer.init(MOTOR_TIMER_HW, MOTOR_TIMER_ID, MOTOR_TIMER_MODE, vMotor_Timer_ISR, &motor_timer);
	motor.motorops = &m63877_ops;
	motor.motorops->init("BD63877", &motor);
	motor.motorops->set_mode(MOTOR_HALF_STEP, &motor);
	motor.motorops->set_direction(MOTOR_BACKWARD, &motor);
	motor.motorops->enable(&motor);

	/* TPH init */
	strobe1_timer.init(STB1_TIMER_HW, STB1_TIMER_ID, STB1_TIMER_MODE, 0, &strobe1_timer);
	strobe2_timer.init(STB2_TIMER_HW, STB2_TIMER_ID, STB2_TIMER_MODE, vHeating_Timer_ISR, &strobe2_timer);
	tph.init(TPH_HW, TPH_ID, "KYOCERA", TPH_MODE, TPH_CS_MODE, vImage_ISR, &tph);
	tph.stb1 = &strobe1_timer;
	tph.stb2 = &strobe2_timer;

	/* ADC init */
	adc.init(ADC_HW, ADC_ID, AT91C_ADC_TRGEN_DIS, 0, AT91C_ADC_SLEEP_NORMAL_MODE, AT91C_ADC_LOWRES_10_BIT, BOARD_MCK, ADC_FREQ, 10 /* us */, 1200 /* ns */, &adc);

	dummy = (char *) 0x20400000;
	tph.buffer = dummy;
}

extern int gap[2000];
extern int ribbon[2000];
extern int finish;

void start_print(void)
{
}
