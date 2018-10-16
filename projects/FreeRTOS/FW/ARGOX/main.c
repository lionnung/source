/*
    FreeRTOS V7.0.1 - Copyright (C) 2011 Real Time Engineers Ltd.
	

    ***************************************************************************
     *                                                                       *
     *    FreeRTOS tutorial books are available in pdf and paperback.        *
     *    Complete, revised, and edited pdf reference manuals are also       *
     *    available.                                                         *
     *                                                                       *
     *    Purchasing FreeRTOS documentation will not only help you, by       *
     *    ensuring you get running as quickly as possible and with an        *
     *    in-depth knowledge of how to use FreeRTOS, it will also help       *
     *    the FreeRTOS project to continue with its mission of providing     *
     *    professional grade, cross platform, de facto standard solutions    *
     *    for microcontrollers - completely free of charge!                  *
     *                                                                       *
     *    >>> See http://www.FreeRTOS.org/Documentation for details. <<<     *
     *                                                                       *
     *    Thank you for using FreeRTOS, and thank you for your support!      *
     *                                                                       *
    ***************************************************************************


    This file is part of the FreeRTOS distribution.

    FreeRTOS is free software; you can redistribute it and/or modify it under
    the terms of the GNU General Public License (version 2) as published by the
    Free Software Foundation AND MODIFIED BY the FreeRTOS exception.
    >>>NOTE<<< The modification to the GPL is included to allow you to
    distribute a combined work that includes FreeRTOS without being obliged to
    provide the source code for proprietary components outside of the FreeRTOS
    kernel.  FreeRTOS is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
    more details. You should have received a copy of the GNU General Public
    License and the FreeRTOS license exception along with FreeRTOS; if not it
    can be viewed here: http://www.freertos.org/a00114.html and also obtained
    by writing to Richard Barry, contact details for whom are available on the
    FreeRTOS WEB site.

    1 tab == 4 spaces!

    http://www.FreeRTOS.org - Documentation, latest information, license and
    contact details.

    http://www.SafeRTOS.com - A version that is certified for use in safety
    critical systems.

    http://www.OpenRTOS.com - Commercial support, development, porting,
    licensing and training services.
*/

/*
 * Creates all the demo application tasks, then starts the scheduler.  The WEB
 * documentation provides more details of the standard demo application tasks.
 *
 * A "Check" task is created in addition to the standard demo tasks.    This
 * only executes every three seconds but has a high priority to ensure it gets
 * processor time.  Its main function is to check that all the standard demo
 * tasks are still operational.  If everything is running as expected then the
 * check task will toggle an LED every 3 seconds.  An error being discovered in
 * any task will cause the toggle rate to increase to 500ms.
 *
 */

/* FreeRTOS includes. */
#include "FreeRTOS.h"
#include "task.h"

/* Standard includes. */
#include <stdio.h>
extern double fabs (double);

/* Atmel library includes. */
#include <pio/pio.h>
#include <led.h>

/* Stack size. */
#define mainLED1_STACK_SIZE         (4 * 1024)
#define mainLED2_STACK_SIZE         (4 * 1024)
#define comSTACK_SIZE               (4 * 1024)

/* Priorities for the demo application tasks. */
#define mainLED1_TEST_PRIORITY		( tskIDLE_PRIORITY + 1 )
#define mainLED2_TEST_PRIORITY		( tskIDLE_PRIORITY + 2 )
#define mainCOM_TEST_PRIORITY		( tskIDLE_PRIORITY + 0 )

/* The period of the LED task. */
#define mainLED1_PERIOD			    ( 5000 / portTICK_RATE_MS )
#define mainLED2_PERIOD			    ( 500 / portTICK_RATE_MS );

/* Constants used by the ComTest task. */
#define mainCOM_TEST_BAUD_RATE		( 115200 )
#define mainCOM_TEST_BUFFER_LEN	    ( 4096 )

/* We should find that each character can be queued for Tx immediately and we
don't have to block to send. */
#define comNO_BLOCK					( ( portTickType ) 0 )

/* The Rx task will block on the Rx queue for a long period. */
#define comRX_BLOCK_TIME			( ( portTickType ) 0xffff )

/*-----------------------------------------------------------*/

/* Simple hardware setup required by the demo. */
static void prvSetupHardware( void );

/* LED task */
static void prvLED1Task( void *pvParameters );
static void prvLED2Task( void *pvParameters );

/* Handle to the com port. */
typedef void * xComPortHandle;
static xComPortHandle xPort = NULL;

/* The receive task. */
static portTASK_FUNCTION_PROTO( vComRxTask, pvParameters );

xComPortHandle xSerialPortInitMinimal( unsigned long ulWantedBaud, unsigned portBASE_TYPE uxQueueLength );
void vSerialPutString( xComPortHandle pxPort, const signed char * const pcString, unsigned short usStringLength );
signed portBASE_TYPE xSerialGetChar( xComPortHandle pxPort, signed char *pcRxedChar, portTickType xBlockTime );
signed portBASE_TYPE xSerialPutChar( xComPortHandle pxPort, signed char cOutChar, portTickType xBlockTime );

/*-----------------------------------------------------------*/
int main(void)
{
	/* Perform any hardware setup necessary to run the demo. */
	prvSetupHardware();
	
	/* Create the LED task. */
	xTaskCreate( prvLED1Task, ( signed char * ) "LED1", mainLED1_STACK_SIZE, NULL, mainLED1_TEST_PRIORITY, NULL );
	xTaskCreate( prvLED2Task, ( signed char * ) "LED2", mainLED2_STACK_SIZE, NULL, mainLED2_TEST_PRIORITY, NULL );
	
	/* Create the COM Rx task. */
	xTaskCreate( vComRxTask, ( signed char * ) "COMRx", comSTACK_SIZE, NULL, mainCOM_TEST_PRIORITY, ( xTaskHandle * ) NULL );

	/* Start the scheduler.

	NOTE : Tasks run in system mode and the scheduler runs in Supervisor mode.
	The processor MUST be in supervisor mode when vTaskStartScheduler is 
	called.  The demo applications included in the FreeRTOS.org download switch
	to supervisor mode prior to main being called.  If you are not using one of
	these demo application projects then ensure Supervisor mode is used here. */
	vTaskStartScheduler();
	
	/* Will only get here if there was insufficient heap availale for the
	idle task to be created. */
	for( ;; );
}
/*-----------------------------------------------------------*/

static void prvLED1Task( void * pvParameters )
{
portTickType xNextWakeTime, xPeriod = mainLED1_PERIOD;
volatile portDOUBLE d1, d2, d3, d4;
volatile portDOUBLE dAnswer;

	/* Just to remove the compiler warning. */
	( void ) pvParameters;

	LED_Set( LED_POWER );
	print_init();
	start_print();

	d1 = -389.38;
	d2 = 32498.2;
	d3 = -2.0001;

	dAnswer = ( d1 / d2 ) * d3;

	/* Initialise xNextWakeTime prior to its first use.  From this point on
	the value of the variable is handled automatically by the kernel. */
	xNextWakeTime = xTaskGetTickCount();
	
	for( ;; )
	{
		/* Delay until it is time for this task to execute again. */
		vTaskDelayUntil( &xNextWakeTime, xPeriod );
		
		d1 = -389.38;
		d2 = 32498.2;
		d3 = -2.0001;

		d4 = ( d1 / d2 ) * d3;
		if( fabs( d4 - dAnswer ) > 0.001 ) {
		    LED_Set( LED_POWER );
		    while (1) vTaskDelay( xPeriod );
		}

		/* Finally toggle the LED. */
		//LED_Toggle( LED_POWER );
	}
}
/*-----------------------------------------------------------*/

static void prvLED2Task( void * pvParameters )
{
portTickType xNextWakeTime, xPeriod = mainLED2_PERIOD;
volatile portDOUBLE d1, d2, d3, d4;
volatile portDOUBLE dAnswer;

	/* Just to remove the compiler warning. */
	( void ) pvParameters;

	d1 = 123.4567;
	d2 = 2345.6789;
	d3 = -918.222;

	dAnswer = ( d1 + d2 ) * d3;

	/* Initialise xNextWakeTime prior to its first use.  From this point on
	the value of the variable is handled automatically by the kernel. */
	xNextWakeTime = xTaskGetTickCount();
	
	for( ;; )
	{
		/* Delay until it is time for this task to execute again. */
		vTaskDelayUntil( &xNextWakeTime, xPeriod );
		
		d1 = 123.4567;
		d2 = 2345.6789;
		d3 = -918.222;

		d4 = ( d1 + d2 ) * d3;
		if( fabs( d4 - dAnswer ) > 0.001 ) {
		    LED_Set( LED_DS1 );
		    while (1) vTaskDelay( xPeriod );
		}

		/* Finally toggle the LED. */
		LED_Toggle( LED_DS1 );
	}
}
/*-----------------------------------------------------------*/

static portTASK_FUNCTION( vComRxTask, pvParameters )
{
signed char cByteRxed;

	/* Just to stop compiler warnings. */
	( void ) pvParameters;

	for( ;; )
	{
		/* Block on the queue that contains received bytes until a byte is
		available. */
		if( xSerialGetChar( xPort, &cByteRxed, comRX_BLOCK_TIME ) )
		{
		    xSerialPutChar( xPort, cByteRxed, comNO_BLOCK );
		}
	}
} /*lint !e715 !e818 pvParameters is required for a task function even if it is not referenced. */
/*-----------------------------------------------------------*/

static void prvSetupHardware( void )
{
const Pin xPins[] = { PIN_USART0_RXD, PIN_USART0_TXD };

	/* Setup the LED outputs. */
	LED_Configure(LED_POWER);
	LED_Configure(LED_DS1);
	
	/* Setup the pins for the UART. */
	PIO_Configure( xPins, PIO_LISTSIZE( xPins ) );	
	
	xSerialPortInitMinimal( mainCOM_TEST_BAUD_RATE, mainCOM_TEST_BUFFER_LEN );
}
