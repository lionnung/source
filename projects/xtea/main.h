//---------------------------------------------------------------------------

#ifndef mainH
#define mainH
//---------------------------------------------------------------------------
#include <Classes.hpp>
#include <Controls.hpp>
#include <StdCtrls.hpp>
#include <Forms.hpp>
//---------------------------------------------------------------------------
class TfrmXTEA : public TForm
{
__published:	// IDE-managed Components
	TButton *btnSetKey;
	TEdit *edtKey;
	TButton *btnQry;
	TEdit *edtQry;
	TButton *btnFix;
	TEdit *edtK;
	TMemo *memResult;
	void __fastcall FormCreate(TObject *Sender);
	void __fastcall FormClose(TObject *Sender, TCloseAction &Action);
	void __fastcall btnSetKeyClick(TObject *Sender);
	void __fastcall btnQryClick(TObject *Sender);
	void __fastcall btnFixClick(TObject *Sender);
private:	// User declarations
    bool query;
	void xtea_encipher(unsigned int num_rounds, unsigned long *v, unsigned long *k);
	void xtea_decipher(unsigned int num_rounds, unsigned long *v, unsigned long *k);
	void __fastcall OnData(TObject *Sender);
public:		// User declarations
	__fastcall TfrmXTEA(TComponent* Owner);
};
//---------------------------------------------------------------------------
extern PACKAGE TfrmXTEA *frmXTEA;
//---------------------------------------------------------------------------
#endif
