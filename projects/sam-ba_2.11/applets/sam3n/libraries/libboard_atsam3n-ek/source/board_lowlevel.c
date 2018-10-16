/* ----------------------------------------------------------------------------
 *         ATMEL Microcontroller Software Support
 * ----------------------------------------------------------------------------
 * Copyright (c) 2010, Atmel Corporation
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the disclaimer below.
 *
 * Atmel's name may not be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * DISCLAIMER: THIS SOFTWARE IS PROVIDED BY ATMEL "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT ARE
 * DISCLAIMED. IN NO EVENT SHALL ATMEL BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ----------------------------------------------------------------------------
 */

/**
 * \file
 *
 * Provides the low-level initialization function that called on chip startup.
 */

/*----------------------------------------------------------------------------
 *        Headers
 *----------------------------------------------------------------------------*/

#include <board.h>

/*----------------------------------------------------------------------------
 *        Local definitions
 *----------------------------------------------------------------------------*/

/* Define shift bits of CKGR_PLLR */
#define CKGR_MUL_SHIFT         16
#define CKGR_PLLCOUNT_SHIFT     8
#define CKGR_DIV_SHIFT          0

/* Clock settings at 48MHz */
#if (BOARD_MCK == 48000000)
#define BOARD_OSCOUNT   (CKGR_MOR_MOSCXTST & (0x8 << 8))
#define BOARD_PLLR      ((1 << 29) | (0x7 << CKGR_MUL_SHIFT) \
                      | (0x1 << CKGR_PLLCOUNT_SHIFT) \
                      | (0x1 << CKGR_DIV_SHIFT))
#define BOARD_MCKR      (PMC_MCKR_PRES_CLK_2 | PMC_MCKR_CSS_PLL_CLK)
#else
    #error "No settings for current BOARD_MCK."
#endif

/* Define clock timeout */
#define CLOCK_TIMEOUT    0xFFFFFFFF

/*----------------------------------------------------------------------------
 *        Exported functions
 *----------------------------------------------------------------------------*/

/**
 * \brief Performs the low-level initialization of the chip.
 * This includes EFC and master clock configuration.
 * It also enable a low level on the pin NRST triggers a user reset.
 */
void LowLevelInit (void)
{
    uint32_t timeout = 0;
    
    { asm volatile ("cpsid i"); }

    /* Set 3 FWS for Embedded Flash Access */
    EFC->EEFC_FMR = (6 << 8);

    /* Enable NRST reset */
    //RSTC->RSTC_MR |= RSTC_MR_URSTEN;
#if 0
    /* Select external slow clock */
    
    if ((SUPC->SUPC_SR & SUPC_SR_OSCSEL) != SUPC_SR_OSCSEL_CRYST) {
        SUPC->SUPC_CR = SUPC_CR_XTALSEL_CRYSTAL_SEL | ((uint32_t)0xA5 << 24);
        timeout = 0;
        while (!(SUPC->SUPC_SR & SUPC_SR_OSCSEL_CRYST) );
    }

    /* Initialize main oscillator */
    if(!(PMC->CKGR_MOR & CKGR_MOR_MOSCSEL)) {
        PMC->CKGR_MOR = (0x37 << 16) | BOARD_OSCOUNT | CKGR_MOR_MOSCRCEN | CKGR_MOR_MOSCXTEN;
        timeout = 0;
        while (!(PMC->PMC_SR & PMC_SR_MOSCXTS) && (timeout++ < CLOCK_TIMEOUT));
    }

    /* Switch to 3-20MHz Xtal oscillator */
    PMC->CKGR_MOR = (0x37 << 16) | BOARD_OSCOUNT | CKGR_MOR_MOSCRCEN | CKGR_MOR_MOSCXTEN | CKGR_MOR_MOSCSEL;
    timeout = 0;
    while (!(PMC->PMC_SR & PMC_SR_MOSCSELS) && (timeout++ < CLOCK_TIMEOUT));
    PMC->PMC_MCKR = (PMC->PMC_MCKR & ~(uint32_t)PMC_MCKR_CSS) | PMC_MCKR_CSS_MAIN_CLK;
    timeout = 0;
    while (!(PMC->PMC_SR & PMC_SR_MCKRDY) && (timeout++ < CLOCK_TIMEOUT));

    /* Initialize PLL */
    PMC->CKGR_PLLR = BOARD_PLLR;
    timeout = 0;
    while (!(PMC->PMC_SR & PMC_SR_LOCK) && (timeout++ < CLOCK_TIMEOUT));

    /* Switch to fast clock */
    PMC->PMC_MCKR = (BOARD_MCKR & ~PMC_MCKR_CSS) | PMC_MCKR_CSS_MAIN_CLK;
    timeout = 0;
    while (!(PMC->PMC_SR & PMC_SR_MCKRDY) && (timeout++ < CLOCK_TIMEOUT));

    PMC->PMC_MCKR = BOARD_MCKR;
    timeout = 0;
    while (!(PMC->PMC_SR & PMC_SR_MCKRDY) && (timeout++ < CLOCK_TIMEOUT));
#endif    
}
