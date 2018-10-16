/*
 * io.c
 *
 *  Created on: 2011/10/12
 *      Author: lion
 */

/* FreeRTOS includes. */
#include <FreeRTOS.h>

/* Atmel library includes. */
#include <adc/adc.h>
#include <pio/pio.h>

#include "control.h"

static const Pin xPins[] = {PIN_ADC};

static int adc_init(AT91S_ADC *hw, char id,
                    unsigned char trgEn,
                    unsigned char trgSel,
                    unsigned char sleepMode,
                    unsigned char resolution,
                    unsigned int mckClock,
                    unsigned int adcClock,
                    unsigned int startupTime,
                    unsigned int sampleAndHoldTime,
		            struct adc_info *info)
{
	if (info->open == 0) {
	    info->hw = hw;
	    info->id = id;

		PIO_Configure(xPins, PIO_LISTSIZE(xPins));

		/*
		 * ADC1 = 0, ADC2 = 0 => MEDIA & RIBBON (default)
		 * ADC1 = 1, ADC2 = 0 => TM & VSENS
		 * ADC1 = 0, ADC2 = 1 => TPHELEM & PEELER
		 * ADC1 = 1, ADC2 = 1 => TM1
		 */
		PIO_Clear(&xPins[PIN_ADC1_IDX]);
		PIO_Clear(&xPins[PIN_ADC2_IDX]);

	    ADC_Initialize(hw, id, trgEn, trgSel, sleepMode, resolution, mckClock, adcClock, startupTime, sampleAndHoldTime);
	    ADC_EnableChannel(hw, ADC_CHANNEL_0);
	    ADC_EnableChannel(hw, ADC_CHANNEL_1);

		info->open = 1;
	}

	return 0;
}

static int adc_release(struct adc_info *info)
{
	ADC_DisableChannel(info->hw, ADC_CHANNEL_0);
	ADC_DisableChannel(info->hw, ADC_CHANNEL_1);
	info->open = 0;

	return 0;
}

static int adc_select(int device, struct adc_info *info)
{
	if (info->open != 1)
		return -1;

    switch (device) {
    case ADC_MEDIA:
    case ADC_RIBBON:
    default:
		PIO_Clear(&xPins[PIN_ADC1_IDX]);
		PIO_Clear(&xPins[PIN_ADC2_IDX]);
		break;

    case ADC_TM:
    case ADC_VSENS:
		PIO_Set(&xPins[PIN_ADC1_IDX]);
		PIO_Clear(&xPins[PIN_ADC2_IDX]);
		break;

    case ADC_TPHELEM:
    case ADC_PEELER:
		PIO_Clear(&xPins[PIN_ADC1_IDX]);
		PIO_Set(&xPins[PIN_ADC2_IDX]);
		break;

    case ADC_TM1:
		PIO_Set(&xPins[PIN_ADC1_IDX]);
		PIO_Set(&xPins[PIN_ADC2_IDX]);
		break;
    }

    return 0;
}

static int adc_read(int device, struct adc_info *info)
{
	int result;

	if (info->open != 1)
		return -1;

	ADC_StartConversion(info->hw);

	switch (device) {
	case ADC_MEDIA:
	case ADC_TM:
	case ADC_TPHELEM:
	default:
		while (!(ADC_GetStatus(info->hw) & (1 << ADC_CHANNEL_0))) ;
		result = (int) (info->hw->ADC_CDR0 & 0x3ff); // Channel 0, 10 bits
		break;

	case ADC_RIBBON:
	case ADC_VSENS:
	case ADC_PEELER:
	case ADC_TM1:
		while (!(ADC_GetStatus(info->hw) & (1 << ADC_CHANNEL_1))) ;
		result = (int) (info->hw->ADC_CDR1 & 0x3ff); // Channel 1, 10 bits
		break;
	}

	return result;
}

struct adc_info adc = {
	init:     adc_init,
	release:  adc_release,
	select:   adc_select,
	read:     adc_read,
};
