#  ----------------------------------------------------------------------------
#          ATMEL Microcontroller Software Support
#  ----------------------------------------------------------------------------
#  Copyright (c) 2011, Atmel Corporation
#
#  All rights reserved.
#
#  Redistribution and use in source and binary forms, with or without
#  modification, are permitted provided that the following conditions are met:
#
#  - Redistributions of source code must retain the above copyright notice,
#  this list of conditions and the disclaimer below.
#
#  Atmel's name may not be used to endorse or promote products derived from
#  this software without specific prior written permission.
#
#  DISCLAIMER: THIS SOFTWARE IS PROVIDED BY ATMEL "AS IS" AND ANY EXPRESS OR
#  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
#  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT ARE
#  DISCLAIMED. IN NO EVENT SHALL ATMEL BE LIABLE FOR ANY DIRECT, INDIRECT,
#  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
#  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
#  OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
#  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
#  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
#  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
#  ----------------------------------------------------------------------------

if { [ catch { source "$libPath(extLib)/common/generic.tcl"} errMsg] } {
    if {$commandLineMode == 0} {
        tk_messageBox -title "File not found" -message "Common library file not found:\n$errMsg" -type ok -icon error
    } else {
        puts "-E- Common library file not found:\n$errMsg"
        puts "-E- Connection abort"
    }
    exit
}

namespace eval BOARD {
    variable sramSize         0x00008000
    variable maxBootSize      0
    # Vdd Memory 1.8V = 0 / Vdd Memory 3.3V = 1
    variable extRamVdd 1
    # External SDRAM = 0 / External DDR = 1 External PSRAM = 2
    variable extRamType 0
    # Set bus width (16 or 32)
    variable extRamDataBusWidth 16
    # DDRAM Model (not used)
    variable extDDRamModel 0
}

################################################################################
## PROCEDURES FOR COMPATIBILITY WITH OLDER SAM-BA VERSIONS AND USER DEFINED
################################################################################
if { [ catch { source "$libPath(extLib)/common/functions.tcl"} errMsg] } {
    if {$commandLineMode == 0} {
        tk_messageBox -title "File not found" -message "Function file not found:\n$errMsg" -type ok -icon error
    } else {
        puts "-E- Function file not found:\n$errMsg"
        puts "-E- Connection abort"
    }
    exit
}

array set memoryAlgo {
    "SRAM 0"       "::at91sam3x2_sram0"
    "SRAM 1"       "::at91sam3x2_sram1"
	"SDRAM"        "::at91sam3x2_sdram"
	"Flash"        "::at91sam3x2_flash"
    "Peripheral"   "::at91sam3x2_peripheral"
    "REMAP"        "::at91sam3x2_remap"
}


################################################################################
## Low Level Initialization
################################################################################
if { [ catch { source "$libPath(extLib)/$target(board)/lowlevelinit.tcl"} errMsg] } {
    set continue no
    if {$commandLineMode == 0} {
        set continue [tk_messageBox -title "File not found" -message "Low level initialization file not found.\nContinue anyway ?" -icon warning -type yesno]
    } else {
        puts "-E- Low level initialization file not found."
        puts "-E- Connection abort!"
    }

    if {$continue == no} {
        TCL_Close $target(handle)
        exit
    }
}
LOWLEVEL::Init

################################################################################
## SRAM
################################################################################
array set at91sam3x2_sram0 {
	dftDisplay  1
    dftDefault  0
	dftAddress  0x20000000
	dftSize     0x4000
	dftSend     "RAM::sendFile"
	dftReceive  "RAM::receiveFile"
	dftScripts  ""
}

array set at91sam3x2_sram1 {
	dftDisplay  1
    dftDefault  0
	dftAddress  0x20080000
	dftSize     0x4000
	dftSend     "RAM::sendFile"
	dftReceive  "RAM::receiveFile"
	dftScripts  ""
}

################################################################################
## SDRAM
################################################################################
array set at91sam3x2_sdram {
    dftDisplay  1
    dftDefault  0
    dftAddress  0x70000000
    dftSize     "$GENERIC::memorySize"
    dftSend     "RAM::sendFile"
    dftReceive  "RAM::receiveFile"
    dftScripts  "::at91sam3x2_sdram_scripts"
}

puts "-I- External RAM Settings :  extRamVdd=$BOARD::extRamVdd, extRamType=$BOARD::extRamType, extRamDataBusWidth=$BOARD::extRamDataBusWidth, extDDRamModel=$BOARD::extDDRamModel"

set RAM::appletAddr          0x20001000
set RAM::appletMailboxAddr   0x20001040
set RAM::appletFileName      "$libPath(extLib)/$target(board)/applet-extram-sam3x2.bin"

array set at91sam3x2_sdram_scripts {
    "Enable SDRAM"   "GENERIC::Init $RAM::appletAddr $RAM::appletMailboxAddr $RAM::appletFileName [list $::target(comType) $::target(traceLevel) $BOARD::extRamVdd $BOARD::extRamType $BOARD::extRamDataBusWidth $BOARD::extDDRamModel]"
}

################################################################################
## FLASH
################################################################################
array set at91sam3x2_flash {
	dftDisplay  1
    dftDefault  1
    dftAddress  0x80000
    dftSize     0x20000
    dftSend     "FLASH::SendFile"
    dftReceive  "FLASH::ReceiveFile"
    dftScripts  "::at91sam3x2_flash_scripts"
}
array set at91sam3x2_flash_scripts {
        "Enable Security Bit (GPNVM0)"         "FLASH::ScriptGPNMV 0"
        "Boot from Flash (GPNVM1)"             "FLASH::ScriptGPNMV 2"
        "Boot from ROM (GPNVM1)"               "FLASH::ScriptGPNMV 3"
		"Select Flash0 for boot (GPNVM2)"      "FLASH::ScriptGPNMV 5"
        "Select Flash1 for boot (GPNVM2)"      "FLASH::ScriptGPNMV 4"
        "Erase All Flash"                      "FLASH::EraseAll"
		"Enable Flash access"                  "FLASH::Init "
        "Read unique ID"   	                   "FLASH::ReadUniqueID"
}

set FLASH::appletAddr             0x20001000
set FLASH::appletMailboxAddr      0x20001040
set FLASH::appletFileName         "$libPath(extLib)/$target(board)/applet-flash-sam3x2.bin"

# Initialize SDRAM
if {[catch {GENERIC::Init $RAM::appletAddr $RAM::appletMailboxAddr $RAM::appletFileName [list $::target(comType) $::target(traceLevel) $BOARD::extRamVdd $BOARD::extRamType $BOARD::extRamDataBusWidth $BOARD::extDDRamModel]} dummy_err] } {
    set continue no
    if {$commandLineMode == 0} {
        set continue [tk_messageBox -title "External RAM init" -message "External RAM initialization failed.\nExternal RAM access is required to run applets.\nContinue anyway ?" -icon warning -type yesno]
    } else {
        puts "-E- Error during external RAM initialization."
        puts "-E- External RAM access is required to run applets."
        puts "-E- Connection abort"
    }
    # Close link
    if {$continue == no} {
        TCL_Close $target(handle)
        exit
    }
} else {
    puts "-I- External RAM initialized"
}

# Initialize FLASH
if {[catch {FLASH::Init} dummy_err]} {
    if {$commandLineMode == 0} {
        tk_messageBox -title "FLASH init" -message "Failed to initialize FLASH access" -icon error -type ok
    } else {
        puts "-E- Error during FLASH initialization"
        puts "-E- Connection abort"
    }
    # Close link
    TCL_Close $target(handle)
    exit
} else {
    puts "-I- FLASH initialized"
}

################################################################################
array set at91sam3x2_peripheral {
	dftDisplay  0
    dftDefault  0
	dftAddress  0x40000000
	dftSize     0x10000000
	dftSend     ""
	dftReceive  ""
	dftScripts  ""
}

array set at91sam3x2_remap {
	dftDisplay  0
    dftDefault  0
	dftAddress  0x00000000
	dftSize     0x40000
	dftSend     ""
	dftReceive  ""
	dftScripts  ""
}

