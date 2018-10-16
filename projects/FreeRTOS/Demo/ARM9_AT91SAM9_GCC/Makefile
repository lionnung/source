#/*
#    FreeRTOS V7.0.1 - Copyright (C) 2011 Real Time Engineers Ltd.
#	
#
#    ***************************************************************************
#     *                                                                       *
#     *    FreeRTOS tutorial books are available in pdf and paperback.        *
#     *    Complete, revised, and edited pdf reference manuals are also       *
#     *    available.                                                         *
#     *                                                                       *
#     *    Purchasing FreeRTOS documentation will not only help you, by       *
#     *    ensuring you get running as quickly as possible and with an        *
#     *    in-depth knowledge of how to use FreeRTOS, it will also help       *
#     *    the FreeRTOS project to continue with its mission of providing     *
#     *    professional grade, cross platform, de facto standard solutions    *
#     *    for microcontrollers - completely free of charge!                  *
#     *                                                                       *
#     *    >>> See http://www.FreeRTOS.org/Documentation for details. <<<     *
#     *                                                                       *
#     *    Thank you for using FreeRTOS, and thank you for your support!      *
#     *                                                                       *
#    ***************************************************************************
#
#
#    This file is part of the FreeRTOS distribution.
#
#    FreeRTOS is free software; you can redistribute it and/or modify it under
#    the terms of the GNU General Public License (version 2) as published by the
#    Free Software Foundation AND MODIFIED BY the FreeRTOS exception.
#    >>>NOTE<<< The modification to the GPL is included to allow you to
#    distribute a combined work that includes FreeRTOS without being obliged to
#    provide the source code for proprietary components outside of the FreeRTOS
#    kernel.  FreeRTOS is distributed in the hope that it will be useful, but
#    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
#    or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
#    more details. You should have received a copy of the GNU General Public
#    License and the FreeRTOS license exception along with FreeRTOS; if not it
#    can be viewed here: http://www.freertos.org/a00114.html and also obtained
#    by writing to Richard Barry, contact details for whom are available on the
#    FreeRTOS WEB site.
#
#    1 tab == 4 spaces!
#
#    http://www.FreeRTOS.org - Documentation, latest information, license and
#    contact details.
#
#    http://www.SafeRTOS.com - A version that is certified for use in safety
#    critical systems.
#
#    http://www.OpenRTOS.com - Commercial support, development, porting,
#    licensing and training services.
#*/

#
# FreeRTOS Real Time Kernel Forums
# http://sourceforge.net/projects/freertos/forums/forum/382005
#
# arm-none-eabi-objdump -x -S -t -r obj_file > output.txt
# gcc with -g option, Intermix source code with disassembly
#

RTOS_SOURCE_DIR=../../Source
DEMO_COMMON_DIR=../Common/Minimal
DEMO_INCLUDE_DIR=../Common/include

BOARD=at91sam9260-ek
CHIP=at91sam9260
CPU=arm926ej-s

AT91LIB_SOURCE_DIR=../Common/drivers/Atmel/at91libv18
AT91LIB_BOARD_DIR=$(AT91LIB_SOURCE_DIR)/boards/$(BOARD)
AT91LIB_PERIPHERALS_DIR=$(AT91LIB_SOURCE_DIR)/peripherals
AT91LIB_UTILITY_DIR=$(AT91LIB_SOURCE_DIR)/utility

CPP=arm-none-eabi-cpp
CC=arm-none-eabi-gcc
OBJCOPY=arm-none-eabi-objcopy
CRT0=$(CHIP)_boot.x
LDSCRIPT=$(CHIP)_sdram.lds

LINKER_FLAGS=-mthumb -nostartfiles -Xlinker -oRTOSDemo.elf -Xlinker -M -Xlinker -Map=rtosdemo.map

DEBUG=-g
OPTIM=-Os
WARNINGS=-Wall -Wextra -Wshadow -Wpointer-arith -Wbad-function-cast -Wcast-align -Wsign-compare \
		 -Waggregate-return -Wstrict-prototypes -Wunused
#		 -Waggregate-return -Wstrict-prototypes -Wmissing-prototypes -Wmissing-declarations -Wunused


AFLAGS= -D __ASSEMBLY__

CFLAGS= $(DEBUG) \
		$(OPTIM) \
		$(WARNINGS) \
		-T$(LDSCRIPT) \
		-I . \
		-I $(RTOS_SOURCE_DIR)/include \
		-I $(RTOS_SOURCE_DIR)/portable/GCC/ARM9_AT91SAM9 \
		-I $(DEMO_INCLUDE_DIR) \
		-I $(AT91LIB_SOURCE_DIR) \
		-I $(AT91LIB_BOARD_DIR) \
		-I $(AT91LIB_BOARD_DIR)/$(CHIP) \
		-I $(AT91LIB_PERIPHERALS_DIR) \
		-I $(AT91LIB_UTILITY_DIR) \
		-D SAM9_GCC \
		-D $(CHIP) \
		-mcpu=$(CPU) \
		-fno-dwarf2-cfi-asm \
#		-fomit-frame-pointer \
#		-fno-strict-aliasing \

THUMBFLAGS= -D THUMB_INTERWORK \
            -mthumb \
            -mthumb-interwork

THUMB_SOURCE= \
		main.c \
		libc_stubs.c \
		serial/serial.c \
		ParTest/ParTest.c \
		$(DEMO_COMMON_DIR)/BlockQ.c \
		$(DEMO_COMMON_DIR)/blocktim.c \
		$(DEMO_COMMON_DIR)/flash.c \
		$(DEMO_COMMON_DIR)/integer.c \
		$(DEMO_COMMON_DIR)/GenQTest.c \
		$(DEMO_COMMON_DIR)/QPeek.c \
		$(DEMO_COMMON_DIR)/dynamic.c \
		$(DEMO_COMMON_DIR)/countsem.c \
		$(DEMO_COMMON_DIR)/death.c \
		$(DEMO_COMMON_DIR)/PollQ.c \
		$(DEMO_COMMON_DIR)/recmutex.c \
		$(DEMO_COMMON_DIR)/semtest.c \
		$(DEMO_COMMON_DIR)/comtest.c \
		$(RTOS_SOURCE_DIR)/list.c \
		$(RTOS_SOURCE_DIR)/queue.c \
		$(RTOS_SOURCE_DIR)/tasks.c \
		$(RTOS_SOURCE_DIR)/portable/GCC/ARM9_AT91SAM9/port.c \
		$(RTOS_SOURCE_DIR)/portable/MemMang/heap_2.c \
		$(AT91LIB_BOARD_DIR)/board_lowlevel.c \
		$(AT91LIB_BOARD_DIR)/board_memories.c \
		$(AT91LIB_PERIPHERALS_DIR)/irq/aic.c \
		$(AT91LIB_PERIPHERALS_DIR)/pio/pio.c \
		$(AT91LIB_PERIPHERALS_DIR)/pit/pit.c \
		$(AT91LIB_PERIPHERALS_DIR)/pmc/pmc.c \
		$(AT91LIB_PERIPHERALS_DIR)/usart/usart.c \
		$(AT91LIB_UTILITY_DIR)/led.c

ARM_SOURCE= \
		$(RTOS_SOURCE_DIR)/portable/GCC/ARM9_AT91SAM9/portISR.c \
		serial/serialISR.c
		
ASM_SOURCE= \
        $(CRT0)

THUMB_OBJS = $(THUMB_SOURCE:.c=.o)
ARM_OBJS = $(ARM_SOURCE:.c=.o)
CRT0_OBJS = $(ASM_SOURCE:.x=.s)


all: RTOSDemo.bin

RTOSDemo.bin : RTOSDemo.hex
	$(OBJCOPY) RTOSDemo.elf -O binary RTOSDemo.bin
	
RTOSDemo.hex : RTOSDemo.elf
	$(OBJCOPY) RTOSDemo.elf -O ihex RTOSDemo.hex

RTOSDemo.elf : $(CRT0_OBJS) $(THUMB_OBJS) $(ARM_OBJS) Makefile
	$(CC) $(CFLAGS) $(CRT0_OBJS) $(ARM_OBJS) $(THUMB_OBJS) $(LIBS) $(LINKER_FLAGS)

$(THUMB_OBJS) : %.o : %.c Makefile FreeRTOSConfig.h
	$(CC) -c $(CFLAGS) $(THUMBFLAGS) $< -o $@

$(ARM_OBJS) : %.o : %.c Makefile FreeRTOSConfig.h
	$(CC) -c $(CFLAGS) $< -o $@

$(CRT0_OBJS) : %.s : %.x Makefile
	$(CPP) $(AFLAGS) $(CFLAGS) $< -o $@

clean:
	cs-rm -f $(THUMB_OBJS)
	cs-rm -f $(ARM_OBJS)
	cs-rm -f $(CRT0_OBJS)
	cs-rm -f RTOSDemo.elf
	cs-rm -f RTOSDemo.hex
	cs-rm -f RTOSDemo.bin
	cs-rm -f RTOSDemo.map
	