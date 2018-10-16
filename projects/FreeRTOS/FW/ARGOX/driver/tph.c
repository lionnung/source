/*
 * tph.c
 *
 *  Created on: 2011/8/11
 *      Author: lion
 */

/* FreeRTOS includes. */
#include <FreeRTOS.h>

/* Atmel library includes. */
#include <irq/irq.h>
#include <pio/pio.h>
#include <pmc/pmc.h>
#include <spi/spi.h>
#include <tc/tc.h>

#include "control.h"

static const Pin xPins[] = {PIN_TPH};

static int strobe1_timer_init(AT91S_TC *hw, char id, unsigned int mode, void (*handler)(void), struct timer_counter *tc)
{
	if (tc->open == 0) {
	    tc->hw = hw;
	    tc->id = id;

	    PMC_EnablePeripheral(id);
	    portENTER_CRITICAL();

	    /* Configure Timer / Counter for strobe 1 */
	    TC_Configure(hw, mode);

	    /* Enable the interrupts in the AIC. */
	    if (handler) {
	    	hw->TC_IER = AT91C_TC_CPCS;
	        IRQ_ConfigureIT(id, configKERNEL_INTERRUPT_PRIORITY + 2, (void (*)(void)) handler);
	        IRQ_EnableIT(id);
	    }
	    portEXIT_CRITICAL();

	    tc->open = 1;
	}

	return 0;
}

static int strobe1_timer_release(struct timer_counter *tc)
{
	TC_Stop(tc->hw);
	IRQ_DisableIT(tc->id);
	PMC_DisablePeripheral(tc->id);
	tc->open = 0;

	return 0;
}

static int strobe1_timer_start(unsigned short period, struct timer_counter *tc)
{
	if (tc->open != 1)
		return -1;

	tc->hw->TC_RA = 1;
	tc->hw->TC_RC = period;
	TC_Start(tc->hw);

	return 0;
}

static int strobe1_timer_stop(struct timer_counter *tc)
{
	if (tc->open != 1)
		return -1;

	TC_Stop(tc->hw);

	return 0;
}

static int strobe2_timer_init(AT91S_TC *hw, char id, unsigned int mode, void (*handler)(void), struct timer_counter *tc)
{
	if (tc->open == 0) {
	    tc->hw = hw;
	    tc->id = id;

	    PMC_EnablePeripheral(id);
	    portENTER_CRITICAL();

	    /* Configure Timer / Counter for strobe 2 */
	    TC_Configure(hw, mode);

	    /* Enable the interrupts in the AIC. */
	    if (handler) {
	    	hw->TC_IER = AT91C_TC_CPCS;
	        IRQ_ConfigureIT(id, configKERNEL_INTERRUPT_PRIORITY + 2, (void (*)(void)) handler);
	        IRQ_EnableIT(id);
	    }
	    portEXIT_CRITICAL();

	    tc->open = 1;
	}

	return 0;
}

static int strobe2_timer_release(struct timer_counter *tc)
{
	TC_Stop(tc->hw);
	IRQ_DisableIT(tc->id);
	PMC_DisablePeripheral(tc->id);
	tc->open = 0;

	return 0;
}

static int strobe2_timer_start(unsigned short period, struct timer_counter *tc)
{
	if (tc->open != 1)
		return -1;

	tc->hw->TC_RA = 1;
	tc->hw->TC_RC = period;
	TC_Start(tc->hw);

	return 0;
}

static int strobe2_timer_stop(struct timer_counter *tc)
{
	if (tc->open != 1)
		return -1;

	TC_Stop(tc->hw);

	return 0;
}

static int tph_init(AT91S_SPI *hw, char id, char *name, unsigned int mode, unsigned int cs_mode, void (*handler)(void), struct tph_info *info)
{
	if (info->open == 0) {
		info->hw = hw;
		info->id = id;

		strcpy(info->name, name);
		PIO_Configure(xPins, PIO_LISTSIZE(xPins));
	    PMC_EnablePeripheral(id);
	    portENTER_CRITICAL();

	    /* Configure SPI for tph data transfer */
	    SPI_Configure(hw, id, mode);
	    SPI_ConfigureNPCS(hw, 0, cs_mode); /* CS 0 */

	    /* Disables the receiver PDC transfer requests */
	    /* Disables the transmitter PDC transfer requests */
	    hw->SPI_PTCR = AT91C_PDC_RXTDIS | AT91C_PDC_TXTDIS;

	    SPI_Enable(hw);

	    /* Enable the interrupts in the AIC. */
	    IRQ_ConfigureIT(id, configKERNEL_INTERRUPT_PRIORITY + 2, (void (*)(void)) handler);
	    IRQ_EnableIT(id);
	    portEXIT_CRITICAL();

	    info->open = 1;
	}

	return 0;
}

static int tph_release(struct tph_info *info)
{
	info->stb1->release(info->stb1);
	info->stb2->release(info->stb2);
	SPI_Disable(info->hw);
	IRQ_DisableIT(info->id);
	PMC_DisablePeripheral(info->id);
	info->open = 0;

	return 0;
}

static int tph_enable(struct tph_info *info)
{
	if (info->open != 1)
		return -1;

	PIO_Set(&xPins[PIN_ON_24V_IDX]);
	return 0;
}

static int tph_disable(struct tph_info *info)
{
	if (info->open != 1)
		return -1;

	PIO_Clear(&xPins[PIN_ON_24V_IDX]);
	return 0;
}

static int tph_latch(struct tph_info *info)
{
	if (info->open != 1)
		return -1;

	PIO_Clear(&xPins[PIN_TPH_NLAT_IDX]);
	PIO_Clear(&xPins[PIN_TPH_NLAT_IDX]);
	PIO_Clear(&xPins[PIN_TPH_NLAT_IDX]);
	PIO_Set(&xPins[PIN_TPH_NLAT_IDX]);
	PIO_Set(&xPins[PIN_TPH_NLAT_IDX]);
	PIO_Set(&xPins[PIN_TPH_NLAT_IDX]);

	return 0;
}

static int tph_image_trans(char *buffer, unsigned short size, struct tph_info *info)
{
	if (info->open != 1)
		return -1;

	/* DMA */
	info->hw->SPI_PTCR = AT91C_PDC_TXTDIS;
	info->hw->SPI_TPR = (unsigned int) buffer;
	info->hw->SPI_TCR = size;
	info->hw->SPI_IER = AT91C_SPI_ENDTX;
	info->hw->SPI_PTCR = AT91C_PDC_TXTEN;

	return 0;
}

static int tph_init_strobe(unsigned short period, struct tph_info *info)
{
	if (info->open != 1)
		return -1;

	info->stb1->start(period, info->stb1);
	info->stb2->start(period, info->stb2);

	return 0;
}

static int tph_init_print(struct tph_info *info)
{
	if (info->open != 1)
		return -1;

	info->latch(info);
	info->image_trans(info->buffer, 108, info);
	info->init_strobe(350 * (BOARD_MCK / 1000000) / TC_CLKS_TIMER_DIV2, info);

	/*{
	    int i, c;
	    static int j = 0, k = 0;
	    static char buf[2000];

	    c = 0;
    	for (i = 0; i < 16; i++) {
    		sprintf(&buf[j++ * 3], "%02X ", info->buffer[c++]);
	    }
    	sprintf(&buf[j++ * 3], "\r\n");
    	if (k++ >= 32) {
    		for (i = 0; i < 32; i++)
    		    TRACE_INFO("%s", &buf[i * 17 * 3]);
    	}
	}*/

	info->buffer += 108;

	return 0;
}

struct timer_counter strobe1_timer = {
	init:    strobe1_timer_init,
	release: strobe1_timer_release,
	start:   strobe1_timer_start,
	stop:    strobe1_timer_stop,
};

struct timer_counter strobe2_timer = {
	init:    strobe2_timer_init,
	release: strobe2_timer_release,
	start:   strobe2_timer_start,
	stop:    strobe2_timer_stop,
};

struct tph_info tph = {
	init:        tph_init,
	release:     tph_release,
	enable:      tph_enable,
	disable:     tph_disable,
	latch:       tph_latch,
	image_trans: tph_image_trans,
	init_strobe: tph_init_strobe,
	init_print:  tph_init_print,
};
