/* ----------------------------------------------------------------------------
 *         ATMEL Microcontroller Software Support
 * ----------------------------------------------------------------------------
 * Copyright (c) 2011, Atmel Corporation

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

//------------------------------------------------------------------------------
//         Headers
//------------------------------------------------------------------------------

#include "../common/applet.h"
#include <board.h>
#include <string.h>

//------------------------------------------------------------------------------
//         Local definitions
//------------------------------------------------------------------------------

/** TWI clock frequency in Hz. */
#define TWCK            100000

/** Slave address of AT24C chips.*/
#define AT24C_ADDRESS   0x50

/** Page size of an AT24C512 chip (in bytes)*/
#define PAGE_SIZE       128

/** Eeprom Pins definition */
#define BOARD_PINS_TWI_EEPROM PINS_TWI0

/** TWI0 peripheral ID for eeprom device*/
#define BOARD_ID_TWI_EEPROM   ID_TWI0

/** TWI0 base address for eeprom device */
#define BOARD_BASE_TWI_EEPROM TWI0

#define BUFFER_NB_PAGE       64

/*----------------------------------------------------------------------------
 *        Local structures
 *----------------------------------------------------------------------------*/


/** Structure for storing parameters for each command that can be performed by the applet.*/
struct _Mailbox {

    /** Command send to the monitor to be executed. */
    unsigned int command;
    /** Returned status, updated at the end of the monitor execution.*/
    unsigned int status;

    /** Input Arguments in the argument area*/
    union {

        /** Input arguments for the Init command.*/
        struct {

            /** Communication link used.*/
            unsigned int comType;
            /** Trace level.*/
            unsigned int traceLevel;
            /** Atmel AT24 serial EEPROM index.*/
            unsigned int at24DeviceIndex;
            /** Device address */
            unsigned int devAddr;

        } inputInit;

        /** Output arguments for the Init command.*/
        struct {

            /** Memory size.*/
            unsigned int memorySize;
            /** Buffer address.*/
            unsigned int bufferAddress;
            /** Buffer size.*/
            unsigned int bufferSize;

        } outputInit;

        /** Input arguments for the Write command.*/
        struct {

            /** Buffer address.*/
            unsigned int bufferAddr;
            /** Buffer size.*/
            unsigned int bufferSize;
            /** Memory offset.*/
            unsigned int memoryOffset;

        } inputWrite;

        /** Output arguments for the Write command.*/
        struct {

            /** Bytes written.*/
            unsigned int bytesWritten;

        } outputWrite;

        /** Input arguments for the Read command.*/
        struct {

            /** Buffer address.*/
            unsigned int bufferAddr;
            /** Buffer size.*/
            unsigned int bufferSize;
            /** Memory offset.*/
            unsigned int memoryOffset;

        } inputRead;

        /** Output arguments for the Read command.*/
        struct {

            /** Bytes read.*/
            unsigned int bytesRead;

        } outputRead;

        /** Input arguments for the Full Erase command.*/
        // NONE

        /** Output arguments for the Full Erase command.*/
        // NONE

        /** Input arguments for the Buffer Erase command.*/
        // NONE

        /** Output arguments for the Buffer Erase command.*/
        // NONE
    } argument;
};


/*----------------------------------------------------------------------------
 *        Types
 *----------------------------------------------------------------------------*/
/** Describes a serial EEPROM device parameters. */

typedef struct _At24Desc {

    /** Device string name.*/
    const char *name;
    /** Size of device in bytes.*/
    unsigned int size;
    /** Size of one page in bytes.*/
    unsigned int pageSize;
} At24Desc;


/*----------------------------------------------------------------------------
 *        Global variables
 *----------------------------------------------------------------------------*/

/** End of program space (code + data). */
extern unsigned int _end;
extern unsigned int _sstack;

/** Depending on DYN_TRACES, dwTraceLevel is a modifable runtime variable or a define */
uint32_t dwTraceLevel;


/*----------------------------------------------------------------------------
 *        Local variables
 *----------------------------------------------------------------------------*/

/** Size of one page in the dataflash, in bytes. */
static unsigned int pageSize;
/** Size of the buffer used for read/write operations in bytes.*/
static unsigned int bufferSize;

/** Pio pins to configure.*/
static const Pin pinTwi[] = {BOARD_PINS_TWI_EEPROM};

/** TWI driver instance.*/
static Twid twid;

static unsigned int deviceAddress;

/** Array of recognized serial EEPROM chips.*/
static const At24Desc at24Devices[] = {
    {  "AT24C01",    128,   8},
    {  "AT24C02",    256,   8},
    {  "AT24C04",    512,  16},
    {  "AT24C08",   1024,  16},
    {  "AT24C16",   2048,  16},
    {  "AT24C32",   4096,  32},
    {  "AT24C64",   8192,  32},
    { "AT24C128",  16384,  64},
    { "AT24C256",  32768,  64},
    { "AT24C512",  65536, 128},
    {"AT24C1024", 131072, 256}
};


/*----------------------------------------------------------------------------
 *        Local functions
 *----------------------------------------------------------------------------*/
 /**
 * \brief Check if the actual EEPROM is different from the selected one.
 * \param pTwid  Pointer to a Twid instance.
 * \param argv  atmel eeprom device giving index.
 * \return 0 if the EEPROM is matched; otherwise returns 1.
 */
unsigned char EepromDeviceCheck(Twid *pTwid, unsigned char index)
{
    unsigned int backup, tempValue;
    unsigned int pattern = 0x691F3DC8;
    unsigned char error = 0;
    volatile unsigned int i;
    // Read origal 4 bytes at offset 0.
    if (TWID_Read(&twid, deviceAddress, 0, 2, (unsigned char *)&backup, 4, 0)) return 1;
    // Write 4 bytes pattern at offset 0.
    if (TWID_Write(&twid, deviceAddress, 0, 2, (unsigned char *)&pattern, 4, 0)) return 1;
    // Read 4 bytes at offset (size X 2) and compare with the pattern
    
    // Wait at least 10 ms
    for (i=0; i < 100000; i++);
    
    TWID_Read(&twid, deviceAddress, at24Devices[index].size, 2, (unsigned char *)&tempValue, 4, 0);
   
    if (tempValue != pattern)  error = 1;   // The real eeprom device is larger than  the given device.
    if(index > 0) {
        // Read 4 bytes at offset (size / 2) and compare with the pattern
        TWID_Read(&twid, deviceAddress, at24Devices[index - 1].size, 2, (unsigned char *)&tempValue, 4, 0);
        if (tempValue == pattern) error = 1; // wrap around, the real eeprom device is smaller than  the given device.
    }
    // Write back the origal 4 bytes at offset 0.
    TWID_Write(&twid, deviceAddress, 0, 2, (unsigned char *)&backup, 4, 0);
    return error;
}

/*----------------------------------------------------------------------------
 *         Global functions
 *----------------------------------------------------------------------------*/

/**
 * \brie Applet main entry. This function decodes received command and executes it.
 * \param argc  always 1
 * \param argv  Address of the argument area.
*/
int main(int argc, char **argv)
{
    struct _Mailbox *pMailbox = (struct _Mailbox *) argv;
    /* Communication type with SAM-BA GUI. */
    unsigned char comType;

    /* atmel eeprom device index. */
    unsigned char at24DeviceIndex;

    unsigned int bytesToWrite, bytesToRead, bufferAddr, memoryOffset, packetSize;

    volatile unsigned int i;

    /* index on read/write buffer */
    unsigned char *pBuffer;

    /* Configure pins */
    PIO_Configure(pinTwi, PIO_LISTSIZE(pinTwi));
    /* Save communication link type */
    comType = pMailbox->argument.inputInit.comType;
    // ----------------------------------------------------------
    // INIT:
    // ----------------------------------------------------------
    if (pMailbox->command == APPLET_CMD_INIT) {

#if (DYN_TRACES == 1)
        dwTraceLevel = pMailbox->argument.inputInit.traceLevel;
#endif
        TRACE_INFO("-- EEPROM Applet %s --\n\r", SAM_BA_APPLETS_VERSION);
        TRACE_INFO("-- %s\n\r", BOARD_NAME);
        TRACE_INFO("-- Compiled: %s %s --\n\r", __DATE__, __TIME__);

        /* Enable TWI peripheral clock */
        PMC_EnablePeripheral(ID_TWI0);
        /* Configure TWI */
        TWI_ConfigureMaster(BOARD_BASE_TWI_EEPROM, TWCK, BOARD_MCK);
        TWID_Initialize(&twid, BOARD_BASE_TWI_EEPROM);
        at24DeviceIndex = pMailbox->argument.inputInit.at24DeviceIndex;
        i = pMailbox->argument.inputInit.devAddr;
        if ( i == 0) {
            deviceAddress = AT24C_ADDRESS;
        } 
        else {
            if ((i & 0x000000f0) != AT24C_ADDRESS) {
                pMailbox->status = APPLET_DEV_UNKNOWN;
                TRACE_INFO("-E Incorroct Device address %x \n\r",i );
            }
            else {
                deviceAddress = pMailbox->argument.inputInit.devAddr;
            }
        }
        TRACE_INFO("-I- EEPROM device address is 0x%x\n\r", deviceAddress);
        // Check if the actual EEPROM is different from the device user selected.
        if (EepromDeviceCheck(&twid, at24DeviceIndex)) {
            pMailbox->status = APPLET_DEV_UNKNOWN;
            pMailbox->argument.outputInit.bufferSize = 0;
            pMailbox->argument.outputInit.memorySize = 0;
            TRACE_INFO("\tDevice Unknown \n\r");
        }
        else {
            // Get device parameters
            pageSize = at24Devices[at24DeviceIndex].pageSize;
            bufferSize = BUFFER_NB_PAGE * pageSize;

            pMailbox->argument.outputInit.bufferAddress = (unsigned int) &_end;
            pMailbox->argument.outputInit.bufferSize = bufferSize;
            pMailbox->argument.outputInit.memorySize = at24Devices[at24DeviceIndex].size;
            TRACE_INFO("\t%s deviceAddress:0x%x pageSize : 0x%x bufferAddr : 0x%x\n\r",
                   at24Devices[at24DeviceIndex].name, deviceAddress, pageSize, pMailbox->argument.outputInit.bufferAddress);
            pMailbox->status = APPLET_SUCCESS;
        }
    }

    // ----------------------------------------------------------
    // WRITE:
    // ----------------------------------------------------------
    else if (pMailbox->command == APPLET_CMD_WRITE) {

        memoryOffset = pMailbox->argument.inputWrite.memoryOffset;
        bufferAddr   = pMailbox->argument.inputWrite.bufferAddr;
        bytesToWrite = pMailbox->argument.inputWrite.bufferSize;
        TRACE_INFO("WRITE at offset: 0x%x buffer at : 0x%x of: 0x%x Bytes\n\r",
               memoryOffset, bufferAddr, bytesToWrite);

        pBuffer = (unsigned char *) bufferAddr;
        if ((memoryOffset % pageSize) != 0) {

            packetSize  = min (pageSize - (memoryOffset % pageSize), bytesToWrite) ;
            // Write the page contents
            TWID_Write(&twid, deviceAddress, memoryOffset, 2,  pBuffer, packetSize, 0);

            // Wait at least 10 ms
            for (i=0; i < 100000; i++);

            bytesToWrite -= packetSize;
            pBuffer += packetSize;
            memoryOffset += packetSize;
        }

        // If it remains more than one page to write
        while (bytesToWrite >= pageSize) {
            // Write the page contents
            TWID_Write(&twid, deviceAddress, memoryOffset, 2, pBuffer, pageSize, 0);

            // Wait at least 10 ms
            for (i=0; i < 100000; i++);
            pBuffer += pageSize;
            memoryOffset += pageSize;
            bytesToWrite -= pageSize;
        }

        // Write remaining data
        if (bytesToWrite > 0) {
            // Write the page contents
            TWID_Write(&twid, deviceAddress, memoryOffset, 2, pBuffer, bytesToWrite, 0);
            // Wait at least 10 ms
            for (i=0; i < 100000; i++);
            // No more bytes to write
            bytesToWrite = 0;
        }

        TRACE_INFO("WRITE return byte written : 0x%x Bytes\n\r",
               pMailbox->argument.inputWrite.bufferSize - bytesToWrite);

        pMailbox->argument.outputWrite.bytesWritten = pMailbox->argument.inputWrite.bufferSize - bytesToWrite;
        pMailbox->status = APPLET_SUCCESS;
    }

    // ----------------------------------------------------------
    // READ:
    // ----------------------------------------------------------
    else if (pMailbox->command == APPLET_CMD_READ) {

        memoryOffset = pMailbox->argument.inputRead.memoryOffset;
        bufferAddr   = pMailbox->argument.inputRead.bufferAddr;
        bytesToRead  = pMailbox->argument.inputRead.bufferSize;

        TRACE_INFO("READ at offset: 0x%x buffer at : 0x%x of: 0x%x Bytes\n\r",
               memoryOffset, bufferAddr, bytesToRead);

        pBuffer = (unsigned char *) bufferAddr;
        // Read the contents
        TWID_Read(&twid, deviceAddress, memoryOffset, 2, pBuffer, bytesToRead, 0);
        TRACE_INFO("READ return byte read : 0x%x Bytes\n\r",  pMailbox->argument.inputRead.bufferSize);
        pMailbox->argument.outputRead.bytesRead = pMailbox->argument.inputRead.bufferSize;
        pMailbox->status = APPLET_SUCCESS;
    }

    // Acknowledge the end of command
    TRACE_INFO("\tEnd of applet (command : %x --- status : %x)\n\r", pMailbox->command, pMailbox->status);

    // Notify the host application of the end of the command processing
    pMailbox->command = ~(pMailbox->command);
    /* Send ACK character */
    if (comType == DBGU_COM_TYPE) {
         /* Wait for the transmitter to be ready */
        while ( (DBGU->DBGU_SR & DBGU_SR_TXEMPTY) == 0 ) ;
        /* Send character */
         DBGU->DBGU_THR= 0x06 ;
    }
    return 0;
}

