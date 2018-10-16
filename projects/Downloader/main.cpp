//---------------------------------------------------------------------------
#include <vcl.h>
#pragma hdrstop

#include "main.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"
TForm1 *Form1;

#define SIZE (1024 * 1024 * 10)

typedef __stdcall int (*_Z_CreatePort)(int nPortType, int nPort, LPCTSTR filename);
typedef __stdcall int (*_Z_WriteData)(int IsImmediate, LPCTSTR pbuf, DWORD length);
typedef __stdcall void (*_Z_ClosePrn)(void);
typedef __stdcall int (*_Z_GetUSBBufferLen)(void);
typedef __stdcall int (*_Z_GetUSBDeviceInfo)(int nPort, char *pDeviceName, int *pDeviceNameLen,\
	char *pDevicePath, int *pDevicePathLen);

_Z_CreatePort Z_CreatePort;
_Z_WriteData Z_WriteData;
_Z_ClosePrn Z_ClosePrn;
_Z_GetUSBBufferLen Z_GetUSBBufferLen;
_Z_GetUSBDeviceInfo Z_GetUSBDeviceInfo;

HINSTANCE hInstLibrary;
AnsiString str;
int nPortType;
int nPort;

BYTE buffer[SIZE];
HANDLE hRead;
DWORD read;
//---------------------------------------------------------------------------
__fastcall TForm1::TForm1(TComponent* Owner)
	: TForm(Owner)
{
#if 1
	hInstLibrary = LoadLibrary("Winpplz.dll");
	if (hInstLibrary == NULL)
	{
		Application->MessageBox(L"Winpplz.dll load failed.", L"Error", MB_ICONERROR);
		return;
	}

	Z_CreatePort = (_Z_CreatePort) GetProcAddress(hInstLibrary, "Z_CreatePort");
	if (Z_CreatePort == NULL) {
		FreeLibrary(hInstLibrary);
		return;
	}

	Z_WriteData = (_Z_WriteData) GetProcAddress(hInstLibrary, "Z_WriteData");
	if (Z_WriteData == NULL) {
		FreeLibrary(hInstLibrary);
		return;
	}

	Z_ClosePrn = (_Z_ClosePrn) GetProcAddress(hInstLibrary, "Z_ClosePrn");
	if (Z_ClosePrn == NULL) {
		FreeLibrary(hInstLibrary);
		return;
	}

	Z_GetUSBBufferLen = (_Z_GetUSBBufferLen) GetProcAddress(hInstLibrary, "Z_GetUSBBufferLen");
	if (Z_GetUSBBufferLen == NULL) {
		FreeLibrary(hInstLibrary);
		return;
	}

	Z_GetUSBDeviceInfo = (_Z_GetUSBDeviceInfo) GetProcAddress(hInstLibrary, "Z_GetUSBDeviceInfo");
	if (Z_GetUSBDeviceInfo == NULL) {
		FreeLibrary(hInstLibrary);
		return;
	}
#endif

#if 0
	hInstLibrary = LoadLibrary("Winpplb.dll");
	if (hInstLibrary == NULL)
	{
		Application->MessageBox(L"Winpplb.dll load failed.", L"Error", MB_ICONERROR);
		return;
	}

	Z_CreatePort = (_Z_CreatePort) GetProcAddress(hInstLibrary, "B_CreatePort");
	if (Z_CreatePort == NULL) {
		FreeLibrary(hInstLibrary);
		return;
	}

	Z_WriteData = (_Z_WriteData) GetProcAddress(hInstLibrary, "B_WriteData");
	if (Z_WriteData == NULL) {
		FreeLibrary(hInstLibrary);
		return;
	}

	Z_ClosePrn = (_Z_ClosePrn) GetProcAddress(hInstLibrary, "B_ClosePrn");
	if (Z_ClosePrn == NULL) {
		FreeLibrary(hInstLibrary);
		return;
	}

	Z_GetUSBBufferLen = (_Z_GetUSBBufferLen) GetProcAddress(hInstLibrary, "B_GetUSBBufferLen");
	if (Z_GetUSBBufferLen == NULL) {
		FreeLibrary(hInstLibrary);
		return;
	}

	Z_GetUSBDeviceInfo = (_Z_GetUSBDeviceInfo) GetProcAddress(hInstLibrary, "B_GetUSBDeviceInfo");
	if (Z_GetUSBDeviceInfo == NULL) {
		FreeLibrary(hInstLibrary);
		return;
	}
#endif
}
//---------------------------------------------------------------------------
void __fastcall TForm1::Button1Click(TObject *Sender)
{
	OpenDialog1->Execute(NULL);
	Edit1->Text = OpenDialog1->FileName;
}
//---------------------------------------------------------------------------
void __fastcall TForm1::Button2Click(TObject *Sender)
{
	if (Edit1->Text.IsEmpty()) {
		Application->MessageBox(L"File not selected.", L"Error", MB_ICONERROR);
		return;
	}

#if 0
	{
		char buf1[128]={0}, buf2[128]={0};
		int nLen1=128, nLen2=128, nlen;
		nlen = Z_GetUSBBufferLen() + 1;
		if (nlen > 1) {
		  Z_GetUSBDeviceInfo(1, buf1, &nLen1, buf2, &nLen2);
		  //Z_CreatePrn(12, buf2); //or Z_CreatePort(6, 0, buf2);
		}
	}
#endif

	if (Button2->Caption == "START") {
		{
			nPortType = 4;
			nPort = Edit4->Text.ToInt();
		}

		str = Edit1->Text;
		hRead = CreateFile(str.c_str(), GENERIC_READ, FILE_SHARE_READ, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);
		if (hRead == INVALID_HANDLE_VALUE) {
			Application->MessageBox(L"File open fail!!!", L"Error", MB_ICONERROR);
			return;
		}
		ReadFile(hRead, buffer, SIZE, &read, NULL);

		Z_CreatePort(nPortType, nPort, str.c_str());
		Z_WriteData(1, buffer, read);
		Z_ClosePrn();
	}
}
//---------------------------------------------------------------------------
void __fastcall TForm1::FormClose(TObject *Sender, TCloseAction &Action)
{
	if (hInstLibrary != NULL) {
		FreeLibrary(hInstLibrary);
	}
}
//---------------------------------------------------------------------------

