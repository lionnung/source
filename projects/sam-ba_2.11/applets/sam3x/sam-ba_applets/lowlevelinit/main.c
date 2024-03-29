/* ----------------------------------------------------------------------------
 *         ATMEL Microcontroller Software Support
 * ----------------------------------------------------------------------------
 * Copyright (c) 2011, Atmel Corporation
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

/*----------------------------------------------------------------------------
 *        Headers
 *----------------------------------------------------------------------------*/
#include "../common/applet.h"
#include <board.h>
#include <string.h>

/*----------------------------------------------------------------------------
 *        Local definitions
 *----------------------------------------------------------------------------*/
/* Initialization mode */
#define EK_MODE 0
#define USER_DEFINED_CRYSTAL 1
#define BYASS_MODE 2

/*----------------------------------------------------------------------------
 *        Local structures
 *----------------------------------------------------------------------------*/
/** 
 * \brief Structure for storing parameters for each command that can be performed by the applet.
 */
struct _Mailbox {

    /* Command send to the monitor to be executed. */
    uint32_t command;
    /* Returned status, updated at the end of the monitor execution. */
    uint32_t status;

    /* Input Arguments in the argument area */
    union {

        /* Input arguments for the Init command. */
        struct {

            /* Communication link used. */
            uint32_t comType;
            /* Trace level. */
            uint32_t traceLevel;
            /* Low initialization mode */
            uint32_t mode;
            /* frequency of user-defined crystal */
            uint32_t crystalFreq;
            /* frequency of external clock in bypass mode */
            uint32_t extClk;

        } inputInit;

        /* Output arguments for the Init command. */
        /* None */

        /* Input arguments for the Write command. */
        /* None */

        /* Output arguments for the Write command. */
        /* None */

        /* Input arguments for the Read command. */
        /* None */

        /* Output arguments for the Read command. */
        /* None */

        /* Input arguments for the Full Erase command. */
        /* NONE */

        /* Output arguments for the Full Erase command. */
        /* NONE */

        /* Input arguments for the Buffer Erase command. */
        /* None */

        /* Output arguments for the Buffer Erase command. */
        /* NONE */
    } argument;
};
extern uint32_t end;

/*----------------------------------------------------------------------------
 *         Global variables
 *----------------------------------------------------------------------------*/

/* End of program space (code + data). */
extern uint32_t end;
extern uint32_t _sstack;

/* Startup time of main oscillator (in number of slow clock ticks). */
#define USER_OSCOUNT(a)           ((a) << 8)
/* USB PLL divisor value. */
#define USER_USBDIV(a)            ((a) << 8)
/* PLL frequency range. */
#define USER_CKGR_PLL(a)          ((a) << 14)
/* PLL startup time (in number of slow clock ticks). */
#define USER_PLLCOUNT(a)          ((a) << 8)
/* PLL MUL value. */
#define USER_MUL(a)               ((a) << 16)
/* PLL DIV value. */
#define USER_DIV(a)               (a)
/* Master clock prescaler value. */
#define USER_PRESCALER(a)         ((a) << 4)
/* Define clock timeout */
#define CLOCK_TIMEOUT             0xFFFFFFFF

/**
 * \brief  Configure the PMC if the frequency of the external oscillator is different from the one mounted on EK
 * \param crystalFreq  The frequency of the external oscillator
 */
static void user_defined_LowlevelInit (uint32_t crystalFreq)
{
    uint32_t dwTimeout = 0;
    uint32_t oscount, pllcount, mul, div, prescaler;

    /* Set 3 WS for Embedded Flash Access for 84 MHz */
    EFC0->EEFC_FMR = EEFC_FMR_FWS( 3 );
    EFC1->EEFC_FMR = EEFC_FMR_FWS( 3 );

    switch (crystalFreq) {
    /* When crystal frequency is 12MHz */
    case 12000000:
        /* Master Clock = crystalFreq * (mul + 1) / div = 12MHz * (13 + 1) / 1 = 168MHz */
        mul = USER_MUL(13);
        div = USER_DIV(1);
        /* Processor Clock = Master Clock / prescaler = 168MHz / 2 = 84MHz */
        prescaler = USER_PRESCALER(1);
        /* Please refer to Power Management Controller of the product datasheet */
        pllcount = USER_PLLCOUNT(2);
        oscount = USER_OSCOUNT(8);
        break;
    
    /* other master clock configurations */
    
    default:
        break;
    }
    
   
    PMC->PMC_MCKR = (PMC->PMC_MCKR & (uint32_t)~PMC_MCKR_CSS_Msk) | PMC_MCKR_CSS_MAIN_CLK;
    for ( dwTimeout = 0; !(PMC->PMC_SR & PMC_SR_MCKRDY) && (dwTimeout++ < CLOCK_TIMEOUT) ; );

    /* Switch to 3-20MHz Xtal oscillator */
    PMC->CKGR_MOR |= CKGR_MOR_KEY(0x37) | oscount | CKGR_MOR_MOSCXTEN ;
    dwTimeout = 0;
    while (!(PMC->PMC_SR & PMC_SR_MOSCXTS) && (dwTimeout++ < CLOCK_TIMEOUT));

    PMC->CKGR_MOR |= CKGR_MOR_KEY(0x37) | CKGR_MOR_MOSCSEL;
    dwTimeout = 0;
    while (!(PMC->PMC_SR & PMC_SR_MOSCSELS) && (dwTimeout++ < CLOCK_TIMEOUT));
    dwTimeout = 0;
    
    /* Initialize PLLA */
    PMC->CKGR_PLLAR = CKGR_PLLAR_STUCKTO1 | mul | div | pllcount;
    dwTimeout = 0;
    while (!(PMC->PMC_SR & PMC_SR_LOCKA) && (dwTimeout++ < CLOCK_TIMEOUT));
    for ( dwTimeout = 0; !(PMC->PMC_SR & PMC_SR_MCKRDY) && (dwTimeout++ < CLOCK_TIMEOUT) ; );

    /* Switch to main clock */
    PMC->PMC_MCKR = prescaler | PMC_MCKR_CSS_MAIN_CLK;
    for ( dwTimeout = 0; !(PMC->PMC_SR & PMC_SR_MCKRDY) && (dwTimeout++ < CLOCK_TIMEOUT) ; );

    /* Switch to plla clock */
    PMC->PMC_MCKR = prescaler | PMC_MCKR_CSS_PLLA_CLK;
    for ( dwTimeout = 0; !(PMC->PMC_SR & PMC_SR_MCKRDY) && (dwTimeout++ < CLOCK_TIMEOUT) ; );
}

/**
 * \brief  Configure the PMC in bypass mode. An external clock should be input to XIN as the source clock.
 *
 * \param extClk  The frequency of the external clock
 */
static void bypass_LowLevelInit (uint32_t extClk)
{
    uint32_t dwTimeout = 0;
    uint32_t oscount, pllcount, mul, div, prescaler;

    /* Set 3 WS for Embedded Flash Access for 84 MHz */
    EFC0->EEFC_FMR = EEFC_FMR_FWS( 3 );
    EFC1->EEFC_FMR = EEFC_FMR_FWS( 3 );

    switch (extClk) {
    /* When the frequency of external clock is 12MHz */
    case 12000000:
        /* PLL Clock = extClk * (mul + 1) / div = 12MHz * (13 + 1) / 1 = 168MHz */
        mul = USER_MUL(13);
        div = USER_DIV(1);
        /* Processor Clock = PLL Clock / prescaler = 168MHz / 2 = 84MHz */
        prescaler = USER_PRESCALER(1);
        /* Please refer to Power Management Controller of the product datasheet */
        pllcount = USER_PLLCOUNT(2);
        oscount = USER_OSCOUNT(8);
        break;
    
    /* other master clock configurations */
    
    default:
        break;
    }
    
    /* Initialize main oscillator */
    if ( !(PMC->CKGR_MOR & CKGR_MOR_MOSCSEL) )  /* Main Oscillator Selection */
    {
        /* The Main Crystal Oscillator is enabled */
        PMC->CKGR_MOR = CKGR_MOR_KEY(0x37) | oscount | CKGR_MOR_MOSCRCEN | CKGR_MOR_MOSCXTBY;
        dwTimeout = 0;
        while ( !(PMC->PMC_SR & PMC_SR_MOSCXTS) && (dwTimeout++ < CLOCK_TIMEOUT) );
    }

    /* Switch to 3-20MHz Xtal oscillator */
    /* The Main Crystal Oscillator is enabled */
    PMC->CKGR_MOR = CKGR_MOR_KEY(0x37) | oscount | CKGR_MOR_MOSCRCEN | CKGR_MOR_MOSCXTBY;
    PMC->CKGR_MOR |= CKGR_MOR_KEY(0x37) | CKGR_MOR_MOSCSEL;
    dwTimeout = 0;
    /* Wait Main XTAL Oscillator Status */
    while (!(PMC->PMC_SR & PMC_SR_MOSCXTS) && (dwTimeout++ < CLOCK_TIMEOUT));

    /* configure PLLA to 168 MHz */
    PMC->CKGR_PLLAR = CKGR_PLLAR_STUCKTO1 | mul | pllcount | div;
    dwTimeout = 0;
    /* Wait PLL A Lock Status */
    while (!(PMC->PMC_SR & PMC_SR_LOCKA) && (dwTimeout++ < CLOCK_TIMEOUT));

    PMC->PMC_MCKR = prescaler | PMC_MCKR_CSS_MAIN_CLK;
    dwTimeout = 0;
    /* Wait Master Clock Status */
    while (!(PMC->PMC_SR & PMC_SR_MCKRDY) && (dwTimeout++ < CLOCK_TIMEOUT));

    PMC->PMC_MCKR = prescaler | PMC_MCKR_CSS_PLLA_CLK;
    dwTimeout = 0;
    /* Wait Master Clock Status */
    while (!(PMC->PMC_SR & PMC_SR_MCKRDY) && (dwTimeout++ < CLOCK_TIMEOUT));
}

/**
 * \brief  Configure the PMC as EK setting
 */
static void EK_LowLevelInit (void)
{
    LowLevelInit();
}

/*----------------------------------------------------------------------------
 *         Global functions
 *----------------------------------------------------------------------------*/
/**
 * \brief  Applet main entry. This function decodes received command and executes it.
 *
 * \param argc  always 1
 * \param argv  Address of the argument area..
 */
int main(int argc, char **argv)
{
    struct _Mailbox *pMailbox = (struct _Mailbox *) argv;
    uint32_t mode, crystalFreq, extClk;
    uint32_t comType;

    // ----------------------------------------------------------
    // INIT:
    // ----------------------------------------------------------
    if (pMailbox->command == APPLET_CMD_INIT) {

        mode = pMailbox->argument.inputInit.mode;
        crystalFreq = pMailbox->argument.inputInit.crystalFreq;
        extClk = pMailbox->argument.inputInit.extClk;
        comType = pMailbox->argument.inputInit.comType;

        switch (mode) {
        case EK_MODE: 
            EK_LowLevelInit();
            pMailbox->status = APPLET_SUCCESS;
            break;
        case USER_DEFINED_CRYSTAL:
            user_defined_LowlevelInit(crystalFreq);
            pMailbox->status = APPLET_SUCCESS;
            break;
        case BYASS_MODE:
            bypass_LowLevelInit(extClk);
            pMailbox->status = APPLET_SUCCESS;
            break;
        default:
            pMailbox->status = APPLET_DEV_UNKNOWN;
            break;
        }
    } else {
        pMailbox->status = APPLET_DEV_UNKNOWN;
    }

    UART_Configure(115200, BOARD_MCK);

    /* Notify the host application of the end of the command processing */
    pMailbox->command = ~(pMailbox->command);
    if (comType == DBGU_COM_TYPE) {
        UART_PutChar(0x6);
    }

    return 0;
}

