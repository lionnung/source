//---------------------------------------------------------------------------
#include <vcl.h>
#include <stdio.h>
#pragma hdrstop

#include "main.h"
#include "uciacomport.hpp"
//---------------------------------------------------------------------------
#pragma package(smart_init)
#pragma resource "*.dfm"

TfrmXTEA *frmXTEA;

#define KEY_LEN  16
#define PASS_LEN 8
#define ROUNDS   64

TCiaComPort *Comm;
unsigned char key[KEY_LEN];
unsigned char pass[PASS_LEN];
unsigned char buf[20];
//---------------------------------------------------------------------------
__fastcall TfrmXTEA::TfrmXTEA(TComponent* Owner)
	: TForm(Owner)
{
}
//---------------------------------------------------------------------------
void TfrmXTEA::xtea_encipher(unsigned int num_rounds, unsigned long *v, unsigned long *k)
{
	unsigned long v0 = v[0], v1 = v[1], i;
	unsigned long sum = 0, delta = 0x9E3779B9;

	for (i = 0; i < num_rounds; i++)
	{
		v0 += (((v1 << 4) ^ (v1 >> 5)) + v1) ^ (sum + k[sum & 3]);
		sum += delta;
		v1 += (((v0 << 4) ^ (v0 >> 5)) + v0) ^ (sum + k[(sum >> 11) & 3]);
	}
	v[0] = v0; v[1] = v1;
}
//---------------------------------------------------------------------------
void TfrmXTEA::xtea_decipher(unsigned int num_rounds, unsigned long *v, unsigned long *k)
{
	unsigned long v0 = v[0], v1 = v[1], i;
	unsigned long delta = 0x9E3779B9, sum = delta * num_rounds;

	for (i = 0; i < num_rounds; i++)
	{
		v1 -= (((v0 << 4) ^ (v0 >> 5)) + v0) ^ (sum + k[(sum >> 11) & 3]);
		sum -= delta;
		v0 -= (((v1 << 4) ^ (v1 >> 5)) + v1) ^ (sum + k[sum & 3]);
	}
	v[0] = v0; v[1] = v1;
}
//---------------------------------------------------------------------------
void __fastcall TfrmXTEA::OnData(TObject *Sender)
{
	unsigned long *skey;
	unsigned long *spass;
	char da[100];
	char tmp[5];

	if (!query)
	{
		Comm->PurgeRx();
		return;
	}
	else
		query = false;

	Comm->Receive(buf, PASS_LEN);

	strcpy(da, "Encipher: ");
	for (int i = 0; i < PASS_LEN; i++) {
		sprintf(tmp, "%02x \x0", buf[i]);
		strcat(da, tmp);
	}
	memResult->Lines->Add(da);

	skey = (unsigned long *) key;
	spass = (unsigned long *) buf;
	xtea_decipher(ROUNDS, spass, skey);

	strcpy(da, "Decipher: ");
	for (int i = 0; i < PASS_LEN; i++) {
		sprintf(tmp, "%02x \x0", buf[i]);
		strcat(da, tmp);
	}
	memResult->Lines->Add(da);

	if (!strncmp(buf, edtQry->Text.c_str(), PASS_LEN))
		MessageBox(Handle, "PASS !!!", "xtea", MB_ICONINFORMATION | MB_OK);
	else
		MessageBox(Handle, "FAIL !!!", "xtea", MB_ICONERROR | MB_OK);
}
//---------------------------------------------------------------------------
void __fastcall TfrmXTEA::FormCreate(TObject *Sender)
{
	query = false;
	Comm = new TCiaComPort(NULL);
	Comm->OnDataAvailable = OnData;
	Comm->Port = 1;
	Comm->Baudrate = 9600;
	Comm->Parity = ptNone;
	Comm->ByteSize = 8;
	Comm->StopBits = sbOne;
	Comm->PortOpen();
	Comm->PurgeRx();
}
//---------------------------------------------------------------------------
void __fastcall TfrmXTEA::FormClose(TObject *Sender, TCloseAction &Action)
{
	//Comm->PortClose();
	//delete Comm;
}
//---------------------------------------------------------------------------
void __fastcall TfrmXTEA::btnSetKeyClick(TObject *Sender)
{
	memcpy(key, edtKey->Text.c_str(), KEY_LEN);
	memcpy(buf, "\x1bKJE", 4);
	memcpy(&buf[4], key, KEY_LEN);
	Comm->Send(buf, 4 + KEY_LEN);
}
//---------------------------------------------------------------------------
void __fastcall TfrmXTEA::btnQryClick(TObject *Sender)
{
	char da[100];
	char tmp[5];

	query = true;
	memcpy(pass, edtQry->Text.c_str(), PASS_LEN);
	memcpy(buf, "\x1bKJF", 4);
	memcpy(&buf[4], pass, PASS_LEN);
	Comm->Send(buf, 4 + PASS_LEN);

	strcpy(da, "Query: ");
	for (int i = 0; i < PASS_LEN; i++) {
		sprintf(tmp, "%02x \x0", buf[4 + i]);
		strcat(da, tmp);
	}
	memResult->Lines->Add(da);
}
//---------------------------------------------------------------------------
void __fastcall TfrmXTEA::btnFixClick(TObject *Sender)
{
	memcpy(key, edtK->Text.c_str(), KEY_LEN);
}
//---------------------------------------------------------------------------

