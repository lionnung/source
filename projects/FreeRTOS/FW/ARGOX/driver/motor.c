/*
 * motor.c
 *
 *  Created on: 2011/6/10
 *      Author: lion
 */

/* FreeRTOS includes. */
#include <FreeRTOS.h>

/* Atmel library includes. */
#include <irq/irq.h>
#include <pio/pio.h>
#include <pmc/pmc.h>
#include <tc/tc.h>

#include "control.h"

static const Pin xPins[] = {PIN_MOTOR};

static int motor_timer_init(AT91S_TC *hw, char id, unsigned int mode, void (*handler)(void), struct timer_counter *tc)
{
	if (tc->open == 0) {
	    tc->hw = hw;
	    tc->id = id;

	    PMC_EnablePeripheral(id);
	    portENTER_CRITICAL();

	    /* Configure Timer / Counter for motor */
	    TC_Configure(hw, mode);
	    hw->TC_IER = AT91C_TC_CPCS;

	    /* Enable the interrupts in the AIC. */
	    IRQ_ConfigureIT(id, configKERNEL_INTERRUPT_PRIORITY + 2, (void (*)(void)) handler);
	    IRQ_EnableIT(id);
	    portEXIT_CRITICAL();

	    tc->open = 1;
	}

	return 0;
}

static int motor_timer_release(struct timer_counter *tc)
{
	TC_Stop(tc->hw);
	IRQ_DisableIT(tc->id);
	PMC_DisablePeripheral(tc->id);
	tc->open = 0;

	return 0;
}

static int motor_timer_start(unsigned short period, struct timer_counter *tc)
{
	if (tc->open != 1)
		return -1;

	tc->hw->TC_RC = period;
	TC_Start(tc->hw);

	return 0;
}

static int motor_timer_stop(struct timer_counter *tc)
{
	if (tc->open != 1)
		return -1;

	TC_Stop(tc->hw);

	return 0;
}

static int m63877_init(char *name, struct motor_info *info)
{

	/* Setup the pins for the motor. */
	if (info->open == 0) {
		strcpy(info->name, name);
	    PIO_Configure(xPins, PIO_LISTSIZE(xPins));

	    /*
		 * ENABLE = 0, MTP_TREN = 0 => U10 ON (default)
		 * ENABLE = 1, MTP_TREN = 1 => U30 ON (for high speed)
		 */
		PIO_Clear(&xPins[PIN_MOTOR_ENABLE_IDX]);
		PIO_Clear(&xPins[PIN_MOTOR_MTP_TREN_IDX]);

		PIO_Set(&xPins[PIN_MOTOR_RESET_IDX]);       /* Disable reset */
		PIO_Clear(&xPins[PIN_MOTOR_MT_ENABLE_IDX]); /* Disable motor */

		info->open = 1;
	}

	return 0;
}

static int m63877_release(struct motor_info *info)
{
	PIO_Clear(&xPins[PIN_MOTOR_MT_ENABLE_IDX]);
	PIO_Clear(&xPins[PIN_MOTOR_RESET_IDX]);
	info->open = 0;

	return 0;
}

static int m63877_enable(struct motor_info *info)
{
	if (info->open != 1)
		return -1;

	PIO_Set(&xPins[PIN_MOTOR_MT_ENABLE_IDX]);
	return 0;
}

static int m63877_disable(struct motor_info *info)
{
	if (info->open != 1)
		return -1;

	PIO_Clear(&xPins[PIN_MOTOR_MT_ENABLE_IDX]);
	return 0;
}

static int m63877_step(struct motor_info *info)
{
	if (info->open != 1)
		return -1;

	PIO_Set(&xPins[PIN_MOTOR_STEP_IDX]);
	PIO_Clear(&xPins[PIN_MOTOR_STEP_IDX]);

	return 0;
}

static int m63877_set_mode(int mode, struct motor_info *info)
{
	if (info->open != 1)
		return -1;

	info->mode = mode;

	switch (mode) {
	case MOTOR_FULL_STEP:
		PIO_Clear(&xPins[PIN_MOTOR_MS1_IDX]);
		PIO_Clear(&xPins[PIN_MOTOR_MS2_IDX]);
		break;

	case MOTOR_HALF_STEP:
	default:
		PIO_Set(&xPins[PIN_MOTOR_MS1_IDX]);
		PIO_Clear(&xPins[PIN_MOTOR_MS2_IDX]);
		break;

	case MOTOR_HALF_STEP_B:
		PIO_Clear(&xPins[PIN_MOTOR_MS1_IDX]);
		PIO_Set(&xPins[PIN_MOTOR_MS2_IDX]);
		break;

	case MOTOR_QUARTER_STEP:
		PIO_Set(&xPins[PIN_MOTOR_MS1_IDX]);
		PIO_Set(&xPins[PIN_MOTOR_MS2_IDX]);
		break;
	}

	return 0;
}

static int m63877_set_direction(int direction, struct motor_info *info)
{
	if (info->open != 1)
		return -1;

	info->direction = direction;

	switch (direction) {
	case MOTOR_FORWARD:
	default:
		PIO_Clear(&xPins[PIN_MOTOR_DIR_IDX]);
		break;

	case MOTOR_BACKWARD:
		PIO_Set(&xPins[PIN_MOTOR_DIR_IDX]);
		break;
	}

	return 0;
}

static int m63877_set_voltage(int voltage, struct motor_info *info)
{
	if (info->open != 1)
		return -1;

	return 0;
}

static int m63877_set_current(int current, struct motor_info *info)
{
	if (info->open != 1)
		return -1;

	return 0;
}

struct timer_counter motor_timer = {
	init:    motor_timer_init,
	release: motor_timer_release,
	start:   motor_timer_start,
	stop:    motor_timer_stop,
};

struct motor_ops m63877_ops = {
    init:          m63877_init,
    release:       m63877_release,
    enable:        m63877_enable,
    disable:       m63877_disable,
    step:          m63877_step,
    set_mode:      m63877_set_mode,
    set_direction: m63877_set_direction,
    set_voltage:   m63877_set_voltage,
    set_current:   m63877_set_current,
};

struct motor_info motor;
