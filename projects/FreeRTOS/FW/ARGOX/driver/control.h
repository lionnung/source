/*
 * control.h
 *
 *  Created on: 2011/6/9
 *      Author: lion
 */

#ifndef CONTROL_H_
#define CONTROL_H_

/* Standard includes. */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* Motor PIN definition */
/* Motor voltage adjustment pin definition. */
#define PIN_MOTOR_MTVBB_REF  {1 << 10, AT91C_BASE_PIOA, AT91C_ID_PIOA, PIO_OUTPUT_0, PIO_DEFAULT}
/* Motor extra high voltage pin definition. */
#define PIN_MOTOR_MTP_TREN   {1 << 15, AT91C_BASE_PIOB, AT91C_ID_PIOB, PIO_OUTPUT_0, PIO_DEFAULT}
/* Motor current adjustment pin definition. */
#define PIN_MOTOR_MTVREF_ADJ {1 << 13, AT91C_BASE_PIOC, AT91C_ID_PIOC, PIO_OUTPUT_0, PIO_DEFAULT}
/* Motor step pin definition. */
#define PIN_MOTOR_STEP       {1 << 16, AT91C_BASE_PIOC, AT91C_ID_PIOC, PIO_OUTPUT_0, PIO_DEFAULT}
/* Motor microstep select 2 pin definition. */
#define PIN_MOTOR_MS2        {1 << 17, AT91C_BASE_PIOC, AT91C_ID_PIOC, PIO_OUTPUT_0, PIO_DEFAULT}
/* Motor direction pin definition. */
#define PIN_MOTOR_DIR        {1 << 18, AT91C_BASE_PIOC, AT91C_ID_PIOC, PIO_OUTPUT_0, PIO_DEFAULT}
/* Motor microstep select 1 pin definition. */
#define PIN_MOTOR_MS1        {1 << 19, AT91C_BASE_PIOC, AT91C_ID_PIOC, PIO_OUTPUT_0, PIO_DEFAULT}
/* Motor enable pin definition. */
#define PIN_MOTOR_MT_ENABLE  {1 << 20, AT91C_BASE_PIOC, AT91C_ID_PIOC, PIO_OUTPUT_0, PIO_DEFAULT}
/* Motor enable pin definition. */
#define PIN_MOTOR_ENABLE     {1 << 21, AT91C_BASE_PIOC, AT91C_ID_PIOC, PIO_OUTPUT_0, PIO_DEFAULT}
/* Motor reset pin definition. */
#define PIN_MOTOR_RESET      {1 << 23, AT91C_BASE_PIOC, AT91C_ID_PIOC, PIO_OUTPUT_0, PIO_DEFAULT}
/* All of the motor pin definition. */
#define PIN_MOTOR            PIN_MOTOR_MTVBB_REF, PIN_MOTOR_MTP_TREN, PIN_MOTOR_MTVREF_ADJ, PIN_MOTOR_STEP, PIN_MOTOR_MS2, PIN_MOTOR_DIR, PIN_MOTOR_MS1, PIN_MOTOR_MT_ENABLE, PIN_MOTOR_ENABLE, PIN_MOTOR_RESET

/* TPH PIN definition */
/* TPH 24V pin definition. */
#define PIN_ON_24V           {1 << 6, AT91C_BASE_PIOA, AT91C_ID_PIOA, PIO_OUTPUT_0, PIO_DEFAULT}
/* TPH strobe 1 pin definition. */
#define PIN_TPH_STB1         {1 << 26, AT91C_BASE_PIOA, AT91C_ID_PIOA, PIO_PERIPH_A, PIO_DEFAULT}
/* TPH strobe 2 pin definition. */
#define PIN_TPH_STB2         {1 << 27, AT91C_BASE_PIOA, AT91C_ID_PIOA, PIO_PERIPH_A, PIO_DEFAULT}
/* TPH ref gadj pin definition. */
#define PIN_REF_GADJ         {1 << 28, AT91C_BASE_PIOA, AT91C_ID_PIOA, PIO_OUTPUT_0, PIO_DEFAULT}
/* TPH latch */
#define PIN_TPH_NLAT         {1 << 29, AT91C_BASE_PIOA, AT91C_ID_PIOA, PIO_OUTPUT_1, PIO_DEFAULT}
/* TPH data out pin definition. */
#define PIN_TPH_DO           {1 << 0, AT91C_BASE_PIOB, AT91C_ID_PIOB, PIO_PERIPH_A, PIO_DEFAULT}
/* TPH data in pin definition. */
#define PIN_TPH_DI           {1 << 1, AT91C_BASE_PIOB, AT91C_ID_PIOB, PIO_PERIPH_A, PIO_DEFAULT}
/* TPH clock pin definition. */
#define PIN_TPH_CLK          {1 << 2, AT91C_BASE_PIOB, AT91C_ID_PIOB, PIO_PERIPH_A, PIO_DEFAULT}
/* TPH black dot count pin definition. */
#define PIN_BLACK_DOT_CNT    {1 << 22, AT91C_BASE_PIOC, AT91C_ID_PIOC, PIO_INPUT, PIO_DEFAULT}
/* All of the tph pin definition. */
#define PIN_TPH              PIN_ON_24V, PIN_TPH_STB1, PIN_TPH_STB2, PIN_REF_GADJ, PIN_TPH_NLAT, PIN_TPH_DO, PIN_TPH_DI, PIN_TPH_CLK, PIN_BLACK_DOT_CNT

/* ADC PIN definition */
/* ADC ADC1 pin definition. */
#define PIN_ADC1             {1 << 24, AT91C_BASE_PIOB, AT91C_ID_PIOB, PIO_OUTPUT_0, PIO_DEFAULT}
/* ADC ADC2 pin definition. */
#define PIN_ADC2             {1 << 25, AT91C_BASE_PIOB, AT91C_ID_PIOB, PIO_OUTPUT_0, PIO_DEFAULT}
/* ADC media pin definition. */
#define PIN_MEDIA            {1 << 0, AT91C_BASE_PIOC, AT91C_ID_PIOC, PIO_INPUT, PIO_DEFAULT}
/* ADC ribbon pin definition. */
#define PIN_RIBBON           {1 << 1, AT91C_BASE_PIOC, AT91C_ID_PIOC, PIO_INPUT, PIO_DEFAULT}
/* All of the adc pin definition. */
#define PIN_ADC              PIN_ADC1, PIN_ADC2, PIN_MEDIA, PIN_RIBBON

/* SPI configuration */
/// Calculates the value of the SCBR field of the Chip Select Register given
/// MCK and SPCK.
#define SPID_CSR_SCBR(mck, spck) ((((mck) / (spck)) << 8) & AT91C_SPI_SCBR)

/// Calculates the value of the DLYBS field of the Chip Select Register given
/// the delay in ns and MCK.
#define SPID_CSR_DLYBS(mck, delay) \
    ((((((delay) * ((mck) / 1000000)) / 1000) + 0) << 16) & AT91C_SPI_DLYBS)

/// Calculates the value of the DLYBCT field of the Chip Select Register given
/// the delay in ns and MCK.
#define SPID_CSR_DLYBCT(mck, delay) \
    ((((((delay) / 32 * ((mck) / 1000000)) / 1000) + 0) << 24) & AT91C_SPI_DLYBCT)

/* Motor step */
#define FULL_STEP            1
#define HALF_STEP            2
#define QUARTER_STEP         4
#define EIGHTH_STEP          8

#define MOTOR_DPI            203
#define MOTOR_STEP           HALF_STEP

/* TC DIV */
#define TC_CLKS_TIMER_DIV1   2
#define TC_CLKS_TIMER_DIV2   8
#define TC_CLKS_TIMER_DIV3   32
#define TC_CLKS_TIMER_DIV4   128
#if 0
/* From slow clock (SLCK) */
#if defined(at91sam9260)
#define TC_CLKS_TIMER_DIV5   (BOARD_MCK / 32768)
#else
#define TC_CLKS_TIMER_DIV5   1024
#endif
#endif

#define TC_CLKS_TIMER_DIV    TC_CLKS_TIMER_DIV4
#define TC_CLKS_TIMER_IDX    AT91C_TC_CLKS_TIMER_DIV4_CLOCK

/* IPS */
#define IPS0_5               ((BOARD_MCK / TC_CLKS_TIMER_DIV) / (MOTOR_DPI * HALF_STEP * 0.5))
#define IPS1                 ((BOARD_MCK / TC_CLKS_TIMER_DIV) / (MOTOR_DPI * HALF_STEP * 1.0))
#define IPS2                 ((BOARD_MCK / TC_CLKS_TIMER_DIV) / (MOTOR_DPI * HALF_STEP * 2.0))
#define IPS3                 ((BOARD_MCK / TC_CLKS_TIMER_DIV) / (MOTOR_DPI * HALF_STEP * 3.0))
#define IPS4                 ((BOARD_MCK / TC_CLKS_TIMER_DIV) / (MOTOR_DPI * HALF_STEP * 4.0))
#define IPS5                 ((BOARD_MCK / TC_CLKS_TIMER_DIV) / (MOTOR_DPI * HALF_STEP * 5.0))
#define IPS6                 ((BOARD_MCK / TC_CLKS_TIMER_DIV) / (MOTOR_DPI * HALF_STEP * 6.0))
#define IPS7                 ((BOARD_MCK / TC_CLKS_TIMER_DIV) / (MOTOR_DPI * HALF_STEP * 7.0))
#define IPS8                 ((BOARD_MCK / TC_CLKS_TIMER_DIV) / (MOTOR_DPI * HALF_STEP * 8.0))
#define IPS9                 ((BOARD_MCK / TC_CLKS_TIMER_DIV) / (MOTOR_DPI * HALF_STEP * 9.0))
#define IPS10                ((BOARD_MCK / TC_CLKS_TIMER_DIV) / (MOTOR_DPI * HALF_STEP * 10.0))
#define IPS11                ((BOARD_MCK / TC_CLKS_TIMER_DIV) / (MOTOR_DPI * HALF_STEP * 11.0))
#define IPS12                ((BOARD_MCK / TC_CLKS_TIMER_DIV) / (MOTOR_DPI * HALF_STEP * 12.0))

/* PIO index, same the PIN_MOTOR order. */
enum {
	PIN_MOTOR_MTVBB_REF_IDX = 0,
	PIN_MOTOR_MTP_TREN_IDX,
	PIN_MOTOR_MTVREF_ADJ_IDX,
	PIN_MOTOR_STEP_IDX,
	PIN_MOTOR_MS2_IDX,
	PIN_MOTOR_DIR_IDX,
	PIN_MOTOR_MS1_IDX,
	PIN_MOTOR_MT_ENABLE_IDX,
	PIN_MOTOR_ENABLE_IDX,
	PIN_MOTOR_RESET_IDX
};

/* Motor status */
enum {
	MOTOR_START,
	MOTOR_STOP,
	MOTOR_FULL_STEP,
	MOTOR_HALF_STEP,
	MOTOR_HALF_STEP_B,
	MOTOR_QUARTER_STEP,
	MOTOR_EIGHTH_STEP,
	MOTOR_FORWARD,
	MOTOR_BACKWARD,
	MOTOR_HIGH_VOLTAGE,
	MOTOR_MIDDLE_VOLTAGE,
	MOTOR_LOW_VOLTAGE,
	MOTOR_HIGH_CURRENT,
	MOTOR_MIDDLE_CURRENT,
	MOTOR_LOW_CURRENT
};

/* PIO index, same the PIN_TPH order. */
enum {
	PIN_ON_24V_IDX = 0,
	PIN_TPH_STB1_IDX,
	PIN_TPH_STB2_IDX,
	PIN_REF_GADJ_IDX,
	PIN_TPH_NLAT_IDX,
	PIN_TPH_DO_IDX,
	PIN_TPH_DI_IDX,
	PIN_TPH_CLK_IDX,
	PIN_BLACK_DOT_CNT_IDX
};

/* PIO index, same the PIN_ADC order. */
enum {
	PIN_ADC1_IDX = 0,
	PIN_ADC2_IDX,
	PIN_MEDIA_IDX,
	PIN_RIBBON_IDX
};

/* ADC device */
enum {
    ADC_MEDIA,
    ADC_RIBBON,
    ADC_TM,
    ADC_VSENS,
    ADC_TPHELEM,
    ADC_PEELER,
    ADC_TM1
};

/* Timer / Counter control */
struct timer_counter {
	AT91S_TC *hw;                             /* TC hardware registers */
	char id;                                  /* TC ID */
	int open;
	int (*init)(AT91S_TC *hw,                 /* Initial and set up interrupt handle */
			    char id,
			    unsigned int mode,
			    void (*handler)(void),
			    struct timer_counter *tc);
	int (*release)(struct timer_counter *tc); /* Release */
	int (*start)(unsigned short period,       /* Timer / Counter start */
			     struct timer_counter *tc);
	int (*stop)(struct timer_counter *tc);    /* Timer / Counter stop */
};

/* Motor driver information */
struct motor_info {
	char name[32];                     /* Motor driver name */
	int open;                          /* Has this been open already? */
	struct motor_ops *motorops;
	unsigned short start_period;       /* Start speed */
	unsigned short target_period;      /* Target speed */
	unsigned short step_period;        /* Linear speed up value */
	unsigned short *step_period_array; /* Non-linear speed up array */
	unsigned short instant_period;     /* Current speed */
	int mode;                          /* Motor mode */
	int direction;                     /* Motor direction */
	int phase;                         /* Motor interrupt phase */
	int sequence;                      /* Motor operating sequence */
	int status;                        /* Motor status */

	/* From here on everything is device dependent */
	void *priv;
};

/* Motor driver operations */
struct motor_ops {
	int (*init)(char *name,                        /* Initial motor driver */
			    struct motor_info *info);
	int (*release)(struct motor_info *info);       /* Release motor driver */
	int (*enable)(struct motor_info *info);        /* Enable motor driver */
	int (*disable)(struct motor_info *info);       /* Disable motor driver */
	int (*step)(struct motor_info *info);          /* Motor step */
	int (*set_mode)(int mode,                      /* Set mode, half step, full step... */
			        struct motor_info *info);
	int (*set_direction)(int direction,            /* Set direction */
			             struct motor_info *info);
	int (*set_voltage)(int voltage,                /* Set voltage */
			           struct motor_info *info);
	int (*set_current)(int current,                /* Set current */
			           struct motor_info *info);

	/* From here on everything is device dependent */
};

/* TPH control */
struct tph_info {
	AT91S_SPI *hw;                             /* SPI hardware registers */
	char id;                                   /* SPI ID */
	struct timer_counter *stb1;                /* Heating control 1 */
	struct timer_counter *stb2;                /* Heating control 2 */
	char name[32];                             /* TPH name */
	int open;
	char *buffer;                              /* data */
	int (*init)(AT91S_SPI *hw,                 /* Initial and set up interrupt handle */
			    char id,
			    char *name,
			    unsigned int mode,
			    unsigned int cs_mode,
			    void (*handler)(void),
			    struct tph_info *info);
	int (*release)(struct tph_info *info);     /* Release tph driver */
	int (*enable)(struct tph_info *info);      /* Enable tph 24V */
	int (*disable)(struct tph_info *info);     /* Disable tph 24V */
	int (*latch)(struct tph_info *info);       /* Data latch */
	int (*image_trans)(char *buffer,           /* Data transfer */
			           unsigned short size,
			           struct tph_info *info);
	int (*init_strobe)(unsigned short period,  /* Heating */
			           struct tph_info *info);
	int (*init_print)(struct tph_info *info);  /* Heating method */

	/* From here on everything is device dependent */
};

/* ADC control */
struct adc_info {
	AT91S_ADC *hw;                              /* ADC hardware registers */
	char id;                                    /* ADC ID */
	int open;
	int (*init)(AT91S_ADC *hw,                  /* Initial */
			    char id,
                unsigned char trgEn,
                unsigned char trgSel,
                unsigned char sleepMode,
                unsigned char resolution,
                unsigned int mckClock,
                unsigned int adcClock,
                unsigned int startupTime,
                unsigned int sampleAndHoldTime,
			    struct adc_info *info);
	int (*release)(struct adc_info *info);      /* Release */
	int (*select)(int device,                   /* Select device */
			      struct adc_info *info);
	int (*read)(int device,                     /* Read */
			    struct adc_info *info);
};

extern void vMotor_Timer_ISR(void) __attribute__ ((interrupt("IRQ")));
extern void vHeating_Timer_ISR(void) __attribute__ ((interrupt("IRQ")));
extern void vImage_ISR(void) __attribute__ ((interrupt("IRQ")));

extern struct timer_counter motor_timer;
extern struct motor_ops m63877_ops;
extern struct motor_info motor;
extern struct timer_counter strobe1_timer;
extern struct timer_counter strobe2_timer;
extern struct tph_info tph;
extern struct adc_info adc;

#endif /* CONTROL_H_ */
