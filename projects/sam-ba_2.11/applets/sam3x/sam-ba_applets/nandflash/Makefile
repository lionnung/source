# ----------------------------------------------------------------------------
#         ATMEL Microcontroller Software Support
# ----------------------------------------------------------------------------
# Copyright (c) 2011, Atmel Corporation
#
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are met:
#
# - Redistributions of source code must retain the above copyright notice,
# this list of conditions and the disclaimer below.
#
# Atmel's name may not be used to endorse or promote products derived from
# this software without specific prior written permission.
#
# DISCLAIMER: THIS SOFTWARE IS PROVIDED BY ATMEL "AS IS" AND ANY EXPRESS OR
# IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT ARE
# DISCLAIMED. IN NO EVENT SHALL ATMEL BE LIABLE FOR ANY DIRECT, INDIRECT,
# INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
# LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
# OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
# LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
# NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
# EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
# ----------------------------------------------------------------------------

# Makefile for compiling applet-nandflash

#-------------------------------------------------------------------------------
# User-modifiable options
#-------------------------------------------------------------------------------


# Chip & board used for compilation
# (can be overriden by adding CHIP=chip and BOARD=board to the command-line)
SERIE = sam3x
CHIP  = $(SERIE)8
BOARD = $(SERIE)_ek

ifeq (sam3x8,$(CHIP))
BOARD_DIR = at91sam3x8-ek
endif

ifeq (sam3x4,$(CHIP))
BOARD_DIR = at91sam3x4-ek
endif

ifeq (sam3x2,$(CHIP))
BOARD_DIR = at91sam3x2-ek
endif

MEMORIES = sram

# Trace level used for compilation
# (can be overriden by adding TRACE_LEVEL=#number to the command-line)
# TRACE_LEVEL_DEBUG      5
# TRACE_LEVEL_INFO       4
# TRACE_LEVEL_WARNING    3
# TRACE_LEVEL_ERROR      2
# TRACE_LEVEL_FATAL      1
# TRACE_LEVEL_NO_TRACE   0
#TRACE_LEVEL = 0    # use dynamic trace level instead

# Optimization level
# -O1 Optimize
# -O2 Optimize even more
# -O3 Optimize yet more
# -O0 Reduce compilation time and make debugging produce the expected results
# -Os Optimize for size

OPTIMIZATION = -Os

#-------------------------------------------------------------------------------
# Path
#-------------------------------------------------------------------------------

# Output file basename
OUTPUT = applet-nandflash-$(CHIP)

# Output directories
BIN = bin
OBJ = obj

# Libraries
PATH_ATML_LIB = ../../libraries
PATH_ATML_LIB_BOARD = $(PATH_ATML_LIB)/libboard_sam3x-ek
PATH_ATML_LIB_CHIP = $(PATH_ATML_LIB)/libchip_sam3x
PATH_ATML_LIB_MEMORIES = $(PATH_ATML_LIB)/memories
PATH_RESOURCES = $(PATH_ATML_LIB_BOARD)/resources/gcc
SAMBA = ..
INSTALLDIR = "../../../../tcl_lib/$(BOARD_DIR)/"

#-------------------------------------------------------------------------------
# Tools
#-------------------------------------------------------------------------------

# Tool suffix when cross-compiling
CROSS_COMPILE = arm-none-eabi-

# Compilation tools
AR = $(CROSS_COMPILE)ar
CC = $(CROSS_COMPILE)gcc
LD = $(CROSS_COMPILE)ld
SIZE = $(CROSS_COMPILE)size
OBJCOPY = $(CROSS_COMPILE)objcopy

# Flags
INCLUDES  = -I$(PATH_ATML_LIB_BOARD)
INCLUDES += -I$(PATH_ATML_LIB_BOARD)/include
INCLUDES += -I$(PATH_ATML_LIB_BOARD)/source
INCLUDES += -I$(PATH_ATML_LIB_CHIP)
INCLUDES += -I$(PATH_ATML_LIB_CHIP)/include
INCLUDES += -I$(PATH_ATML_LIB_CHIP)/source
INCLUDES += -I$(PATH_ATML_LIB_MEMORIES)
INCLUDES += -I$(PATH_ATML_LIB_MEMORIES)/include
INCLUDES += -I$(PATH_ATML_LIB_MEMORIES)/nandflash

CFLAGS += -mcpu=cortex-m3 -mthumb -mlong-calls -ffunction-sections -Wall -DNDEBUG -D__SAMBA__
CFLAGS += -g $(OPTIMIZATION) $(INCLUDES) -D$(CHIP)
CFLAGS += -DDYN_TRACES
#CFLAGS += -DTRACE_LEVEL=$(TRACE_LEVEL)
ASFLAGS = -mcpu=cortex-m3 -mthumb -Wall -g $(OPTIMIZATION) $(INCLUDES) -D$(CHIP) -D__ASSEMBLY__
LDFLAGS = -g $(OPTIMIZATION) -nostartfiles -mcpu=cortex-m3 -mthumb -Wl,-Map=$(OUTPUT).map,--cref,--gc-sections

#-------------------------------------------------------------------------------
# Files
#-------------------------------------------------------------------------------
VPATH += $(PATH_ATML_LIB_BOARD)
VPATH += $(PATH_ATML_LIB_BOARD)/include
VPATH += $(PATH_ATML_LIB_BOARD)/source
VPATH += $(PATH_ATML_LIB_CHIP)
VPATH += $(PATH_ATML_LIB_CHIP)/include
VPATH += $(PATH_ATML_LIB_CHIP)/source
VPATH += $(SAMBA)/common
VPATH += $(PATH_ATML_LIB)/memories/include
VPATH += $(PATH_ATML_LIB)/memories/nandflash

C_OBJECTS += main.o
C_OBJECTS += applet_cstartup.o
C_OBJECTS += applet_stdio.o
C_OBJECTS += applet_string.o
C_OBJECTS += applet_retarget.o

C_OBJECTS += board_lowlevel.o
C_OBJECTS += trace.o
C_OBJECTS += uart_console.o
C_OBJECTS += pmc.o
C_OBJECTS += pio.o
C_OBJECTS += pio_pin.o
C_OBJECTS += wdt.o
C_OBJECTS += smc.o
C_OBJECTS += dmac.o
C_OBJECTS += dmad.o
C_OBJECTS += dma_hardware_interface.o

C_OBJECTS += board_memories.o
C_OBJECTS += hamming.o
C_OBJECTS += EccNandFlash.o
C_OBJECTS += NandFlashModel.o
C_OBJECTS += NandFlashModelList.o
C_OBJECTS += NandSpareScheme.o
C_OBJECTS += NfcRawNandFlash.o
C_OBJECTS += NandFlashDma.o
C_OBJECTS += RawNandFlash.o
C_OBJECTS += SkipBlockNandFlash.o

# Append OBJ and BIN directories to output filename
OUTPUT := $(BIN)/$(OUTPUT)

MEMORIES =sram

#-------------------------------------------------------------------------------
# Rules
#-------------------------------------------------------------------------------

all: $(BIN) $(OBJ) $(MEMORIES)

$(BIN) $(OBJ):
	mkdir $@

define RULES
C_OBJECTS_$(1) = $(addprefix $(OBJ)/$(1)_, $(C_OBJECTS))
ASM_OBJECTS_$(1) = $(addprefix $(OBJ)/$(1)_, $(ASM_OBJECTS))

$(1): $$(ASM_OBJECTS_$(1)) $$(C_OBJECTS_$(1))
	@$(CC) $(LDFLAGS) -T"$(PATH_RESOURCES)/$(CHIP)/$$@_samba.lds" -o $(OUTPUT).elf $$^
	$(OBJCOPY) -O binary $(OUTPUT).elf $(OUTPUT).bin
	$(SIZE) $$^ $(OUTPUT).elf
	@[ -e $(INSTALLDIR) ] || mkdir $(INSTALLDIR)
	@cp -f $(OUTPUT).bin $(INSTALLDIR)

$$(C_OBJECTS_$(1)): $(OBJ)/$(1)_%.o: %.c Makefile $(OBJ) $(BIN)
	@$(CC) $(CFLAGS) -D$(1) -c -o $$@ $$<

$$(ASM_OBJECTS_$(1)): $(OBJ)/$(1)_%.o: %.S Makefile $(OBJ) $(BIN)
	@$(CC) $(ASFLAGS) -D$(1) -c -o $$@ $$<

endef

$(foreach MEMORY, $(MEMORIES), $(eval $(call RULES,$(MEMORY))))

clean:
	-cs-rm -fR $(OBJ) $(BIN)
