// Borland C++ Builder
// Copyright (c) 1995, 2005 by Borland Software Corporation
// All rights reserved

// (DO NOT EDIT: machine generated header) 'Uciacomport.pas' rev: 10.00

#ifndef UciacomportHPP
#define UciacomportHPP

#pragma delphiheader begin
#pragma option push
#pragma option -w-      // All warnings off
#pragma option -Vx      // Zero-length empty class member functions
#pragma pack(push,8)
#include <System.hpp>	// Pascal unit
#include <Sysinit.hpp>	// Pascal unit
#include <Windows.hpp>	// Pascal unit
#include <Classes.hpp>	// Pascal unit
#include <Messages.hpp>	// Pascal unit
#include <Sysutils.hpp>	// Pascal unit

//-- user supplied -----------------------------------------------------------

namespace Uciacomport
{
//-- type declarations -------------------------------------------------------
typedef void __fastcall (__closure *TPortSendProgressEvent)(System::TObject* Sender, int Count);

typedef void __fastcall (__closure *TPortLineEvent)(System::TObject* Sender, bool State);

typedef void __fastcall (__closure *TPortErrEvent)(System::TObject* Sender, unsigned Error);

typedef void __fastcall (__closure *TPortExceptionEvent)(System::TObject* Sender, Sysutils::Exception* E);

class DELPHICLASS ECiaComPort;
class PASCALIMPLEMENTATION ECiaComPort : public Sysutils::Exception 
{
	typedef Sysutils::Exception inherited;
	
public:
	#pragma option push -w-inl
	/* Exception.Create */ inline __fastcall ECiaComPort(const AnsiString Msg) : Sysutils::Exception(Msg) { }
	#pragma option pop
	#pragma option push -w-inl
	/* Exception.CreateFmt */ inline __fastcall ECiaComPort(const AnsiString Msg, System::TVarRec const * Args, const int Args_Size) : Sysutils::Exception(Msg, Args, Args_Size) { }
	#pragma option pop
	#pragma option push -w-inl
	/* Exception.CreateRes */ inline __fastcall ECiaComPort(int Ident)/* overload */ : Sysutils::Exception(Ident) { }
	#pragma option pop
	#pragma option push -w-inl
	/* Exception.CreateResFmt */ inline __fastcall ECiaComPort(int Ident, System::TVarRec const * Args, const int Args_Size)/* overload */ : Sysutils::Exception(Ident, Args, Args_Size) { }
	#pragma option pop
	#pragma option push -w-inl
	/* Exception.CreateHelp */ inline __fastcall ECiaComPort(const AnsiString Msg, int AHelpContext) : Sysutils::Exception(Msg, AHelpContext) { }
	#pragma option pop
	#pragma option push -w-inl
	/* Exception.CreateFmtHelp */ inline __fastcall ECiaComPort(const AnsiString Msg, System::TVarRec const * Args, const int Args_Size, int AHelpContext) : Sysutils::Exception(Msg, Args, Args_Size, AHelpContext) { }
	#pragma option pop
	#pragma option push -w-inl
	/* Exception.CreateResHelp */ inline __fastcall ECiaComPort(int Ident, int AHelpContext)/* overload */ : Sysutils::Exception(Ident, AHelpContext) { }
	#pragma option pop
	#pragma option push -w-inl
	/* Exception.CreateResFmtHelp */ inline __fastcall ECiaComPort(System::PResStringRec ResStringRec, System::TVarRec const * Args, const int Args_Size, int AHelpContext)/* overload */ : Sysutils::Exception(ResStringRec, Args, Args_Size, AHelpContext) { }
	#pragma option pop
	
public:
	#pragma option push -w-inl
	/* TObject.Destroy */ inline __fastcall virtual ~ECiaComPort(void) { }
	#pragma option pop
	
};


#pragma option push -b-
enum TStopBits { sbOne, sbOne_5, sbTwo };
#pragma option pop

#pragma option push -b-
enum TParity { ptNone, ptOdd, ptEven, ptMark, ptSpace };
#pragma option pop

class DELPHICLASS TCiaTimer;
class PASCALIMPLEMENTATION TCiaTimer : public System::TObject 
{
	typedef System::TObject inherited;
	
private:
	unsigned FInterval;
	HWND FHandle;
	bool FEnabled;
	void __fastcall UpdateTimer(void);
	void __fastcall SetEnabled(bool Value);
	void __fastcall SetInterval(unsigned Value);
	
public:
	__fastcall virtual ~TCiaTimer(void);
	__property bool Enabled = {read=FEnabled, write=SetEnabled, default=1};
	__property unsigned Interval = {read=FInterval, write=SetInterval, default=1000};
	__property HWND Handle = {read=FHandle, write=FHandle, nodefault};
public:
	#pragma option push -w-inl
	/* TObject.Create */ inline __fastcall TCiaTimer(void) : System::TObject() { }
	#pragma option pop
	
};


class DELPHICLASS TCiaCustomBuffer;
class PASCALIMPLEMENTATION TCiaCustomBuffer : public System::TObject 
{
	typedef System::TObject inherited;
	
private:
	char *FData;
	unsigned FCapacity;
	int FReadPtr;
	int FWritePtr;
	
public:
	__fastcall virtual ~TCiaCustomBuffer(void);
	void __fastcall Grow(unsigned Count);
public:
	#pragma option push -w-inl
	/* TObject.Create */ inline __fastcall TCiaCustomBuffer(void) : System::TObject() { }
	#pragma option pop
	
};


class DELPHICLASS TCiaCommBuffer;
class PASCALIMPLEMENTATION TCiaCommBuffer : public TCiaCustomBuffer 
{
	typedef TCiaCustomBuffer inherited;
	
private:
	int FLineEndPtr;
public:
	#pragma option push -w-inl
	/* TCiaCustomBuffer.Destroy */ inline __fastcall virtual ~TCiaCommBuffer(void) { }
	#pragma option pop
	
public:
	#pragma option push -w-inl
	/* TObject.Create */ inline __fastcall TCiaCommBuffer(void) : TCiaCustomBuffer() { }
	#pragma option pop
	
};


class DELPHICLASS TCiaTxBuffer;
class PASCALIMPLEMENTATION TCiaTxBuffer : public TCiaCustomBuffer 
{
	typedef TCiaCustomBuffer inherited;
	
private:
	int FCount;
	
public:
	void __fastcall Clear(void);
	void __fastcall Empty(void);
	void __fastcall Write(void * Data, int Len);
	bool __fastcall Read(void * &Data, int &Len);
	__property int Count = {read=FCount, nodefault};
public:
	#pragma option push -w-inl
	/* TCiaCustomBuffer.Destroy */ inline __fastcall virtual ~TCiaTxBuffer(void) { }
	#pragma option pop
	
public:
	#pragma option push -w-inl
	/* TObject.Create */ inline __fastcall TCiaTxBuffer(void) : TCiaCustomBuffer() { }
	#pragma option pop
	
};


class DELPHICLASS TCiaCommThread;
class DELPHICLASS TCiaComPort;
#pragma option push -b-
enum TCiaPortOptions { EventCtsDsr, EventError };
#pragma option pop

typedef Set<TCiaPortOptions, EventCtsDsr, EventError>  TPortOptions;

class DELPHICLASS TFlowCtrl;
#pragma option push -b-
enum TDtrControl { dtrDisable, dtrEnable, dtrHandshake };
#pragma option pop

#pragma option push -b-
enum TRtsControl { rtsDisable, rtsEnable, rtsHandshake, rtsToggle };
#pragma option pop

class PASCALIMPLEMENTATION TFlowCtrl : public Classes::TPersistent 
{
	typedef Classes::TPersistent inherited;
	
private:
	int FFlags;
	TCiaComPort* FComPort;
	TDtrControl FRxDtrControl;
	TRtsControl FRxRtsControl;
	bool FRxDsrSensivity;
	bool FTxContinueXoff;
	bool FTxCtsFlow;
	bool FTxDsrFlow;
	bool FXonOff;
	__fastcall TFlowCtrl(TCiaComPort* Port);
	void __fastcall SetRxDtrControl(TDtrControl Value);
	void __fastcall SetRxRtsControl(TRtsControl Value);
	void __fastcall SetRxDsrSensivity(bool Value);
	void __fastcall SetTxContinueXoff(bool Value);
	void __fastcall SetTxCtsFlow(bool Value);
	void __fastcall SetTxDsrFlow(bool Value);
	void __fastcall SetXonOff(bool Value);
	void __fastcall ChangeCommState(void);
	
__published:
	__property bool RxDsrSensivity = {read=FRxDsrSensivity, write=SetRxDsrSensivity, nodefault};
	__property TDtrControl RxDtrControl = {read=FRxDtrControl, write=SetRxDtrControl, nodefault};
	__property TRtsControl RxRtsControl = {read=FRxRtsControl, write=SetRxRtsControl, nodefault};
	__property bool TxContinueXoff = {read=FTxContinueXoff, write=SetTxContinueXoff, nodefault};
	__property bool TxCtsFlow = {read=FTxCtsFlow, write=SetTxCtsFlow, nodefault};
	__property bool TxDsrFlow = {read=FTxDsrFlow, write=SetTxDsrFlow, nodefault};
	__property bool XonXoff = {read=FXonOff, write=SetXonOff, nodefault};
public:
	#pragma option push -w-inl
	/* TPersistent.Destroy */ inline __fastcall virtual ~TFlowCtrl(void) { }
	#pragma option pop
	
public:
	#pragma option push -w-inl
	/* TObject.Create */ inline __fastcall TFlowCtrl(void) : Classes::TPersistent() { }
	#pragma option pop
	
};


class PASCALIMPLEMENTATION TCiaComPort : public Classes::TComponent 
{
	typedef Classes::TComponent inherited;
	
private:
	int FSendCount;
	bool FSending;
	TCiaTxBuffer* FTxBuf;
	bool FDestroying;
	unsigned FHandle;
	TPortOptions FPortOptions;
	int FRxThresholdAmount;
	bool FRxThresholdUse;
	TFlowCtrl* FFlowCtrl;
	bool FLineMode;
	AnsiString FLineEnd;
	int FRxTimeout;
	TCiaTimer* FTimeoutTimer;
	int FBaudrate;
	Byte FByteSize;
	TStopBits FStopbits;
	TParity FParity;
	AnsiString FVersion;
	bool FOpen;
	int FPort;
	unsigned FRxBuffer;
	unsigned FXOffLimit;
	unsigned FXOnLimit;
	unsigned FTxBuffer;
	TCiaCommThread* FCommThread;
	Classes::TNotifyEvent FOnDataAvailable;
	Classes::TNotifyEvent FOnDataSent;
	TPortLineEvent FOnCtsChanged;
	TPortLineEvent FOnDsrChanged;
	TPortErrEvent FOnError;
	TPortExceptionEvent FOnBGException;
	_OVERLAPPED OverlapTx;
	bool FTerminated;
	TPortSendProgressEvent FOnSendProgress;
	MESSAGE void __fastcall InternalDataSent(Messages::TMessage &Msg);
	MESSAGE void __fastcall WMTimer(Messages::TMessage &Msg);
	void __fastcall WMResetTimeoutTimer(void);
	unsigned __fastcall GetRxCount(void);
	void __fastcall ClosePort(void);
	void __fastcall OpenPort(void);
	void __fastcall SetOpen(bool Value);
	void __fastcall SetVersion(AnsiString Value);
	void __fastcall SetBaudRate(int Value);
	void __fastcall SetByteSize(Byte Value);
	void __fastcall SetParity(TParity Value);
	void __fastcall SetStopBits(TStopBits Value);
	void __fastcall SetRxBuffer(unsigned Value);
	void __fastcall SetLineMode(bool Value);
	void __fastcall SetRxTimeout(int Value);
	void __fastcall SetRxThresholdAmount(int Value);
	void __fastcall SetRxThresholdUse(bool Value);
	void __fastcall WndProc(Messages::TMessage &MsgRec);
	unsigned __fastcall InternalSend(void * Buffer, int Len);
	
public:
	__fastcall virtual TCiaComPort(Classes::TComponent* AOwner);
	__fastcall virtual ~TCiaComPort(void);
	bool __fastcall Accessible(void);
	void __fastcall ClearTxBuf(void);
	void __fastcall CommFunction(unsigned Value);
	void __fastcall GetAvailablePorts(Classes::TStrings* PortList);
	int __fastcall GetFreeOutBuf(void);
	bool __fastcall PortOpen(void);
	bool __fastcall PortClose(void);
	void __fastcall PurgeRx(void);
	void __fastcall PurgeTx(void);
	unsigned __fastcall Send(void * Buffer, int Len);
	void __fastcall SendStr(const AnsiString Tx);
	unsigned __fastcall Receive(void * Buffer, int Len);
	AnsiString __fastcall ReceiveStr();
	void __fastcall CloseDelayed(void);
	void __fastcall Release(void);
	bool __fastcall ProcessMessage(void);
	void __fastcall ProcessMessages(void);
	void __fastcall MessageLoop(void);
	__property bool Open = {read=FOpen, write=SetOpen, nodefault};
	__property unsigned RxCount = {read=GetRxCount, nodefault};
	__property bool Terminated = {read=FTerminated, nodefault};
	
__published:
	__property int Baudrate = {read=FBaudrate, write=SetBaudRate, nodefault};
	__property Byte ByteSize = {read=FByteSize, write=SetByteSize, nodefault};
	__property TFlowCtrl* FlowCtrl = {read=FFlowCtrl, write=FFlowCtrl};
	__property AnsiString LineEnd = {read=FLineEnd, write=FLineEnd};
	__property bool LineMode = {read=FLineMode, write=SetLineMode, nodefault};
	__property TParity Parity = {read=FParity, write=SetParity, nodefault};
	__property int Port = {read=FPort, write=FPort, nodefault};
	__property TPortOptions PortOptions = {read=FPortOptions, write=FPortOptions, nodefault};
	__property unsigned RxBuffer = {read=FRxBuffer, write=SetRxBuffer, nodefault};
	__property int RxTimeout = {read=FRxTimeout, write=SetRxTimeout, nodefault};
	__property int RxThresholdAmount = {read=FRxThresholdAmount, write=SetRxThresholdAmount, nodefault};
	__property bool RxThresholdUse = {read=FRxThresholdUse, write=SetRxThresholdUse, nodefault};
	__property TStopBits StopBits = {read=FStopbits, write=SetStopBits, nodefault};
	__property unsigned TxBuffer = {read=FTxBuffer, write=FTxBuffer, nodefault};
	__property AnsiString Version = {read=FVersion, write=SetVersion};
	__property TPortExceptionEvent OnBGException = {read=FOnBGException, write=FOnBGException};
	__property Classes::TNotifyEvent OnDataAvailable = {read=FOnDataAvailable, write=FOnDataAvailable};
	__property Classes::TNotifyEvent OnDataSent = {read=FOnDataSent, write=FOnDataSent};
	__property TPortLineEvent OnCtsChanged = {read=FOnCtsChanged, write=FOnCtsChanged};
	__property TPortLineEvent OnDsrChanged = {read=FOnDsrChanged, write=FOnDsrChanged};
	__property TPortErrEvent OnError = {read=FOnError, write=FOnError};
	__property TPortSendProgressEvent OnSendProgress = {read=FOnSendProgress, write=FOnSendProgress};
};


class PASCALIMPLEMENTATION TCiaCommThread : public Classes::TThread 
{
	typedef Classes::TThread inherited;
	
private:
	unsigned FModemStatus;
	TCiaComPort* FCiaComPort;
	unsigned FEventMask;
	unsigned FRxCount;
	unsigned FInternalRxCount;
	TCiaCommBuffer* FRcvBuffer;
	void __fastcall PortEvent(void);
	void __fastcall InternalReceive(void);
	bool __fastcall CheckLineEnd(char * P);
	
public:
	unsigned ComHandle;
	unsigned CloseEvent;
	__fastcall TCiaCommThread(bool CreateSuspended);
	virtual void __fastcall Execute(void);
public:
	#pragma option push -w-inl
	/* TThread.Destroy */ inline __fastcall virtual ~TCiaCommThread(void) { }
	#pragma option pop
	
};


//-- var, const, procedure ---------------------------------------------------
#define CIA_COMMVERSION "1.15"
static const Word WM_CLOSEPORT = 0x401;
static const Word WM_RELEASEPORT = 0x402;
static const Word WM_DATAAVAILABLE = 0x403;
static const Word WM_DATASENT = 0x404;
static const Word WM_CTSCHANGED = 0x405;
static const Word WM_DSRCHANGED = 0x406;
static const Word WM_PORTERROR = 0x407;
static const Word WM_RESET_RX_TIMOUT = 0x408;
extern PACKAGE void __fastcall CiaEnumPorts(Classes::TStrings* PortList);
extern PACKAGE void __fastcall CiaEnumAccessiblePorts(Classes::TStrings* PortList);

}	/* namespace Uciacomport */
using namespace Uciacomport;
#pragma pack(pop)
#pragma option pop

#pragma delphiheader end.
//-- end unit ----------------------------------------------------------------
#endif	// Uciacomport
