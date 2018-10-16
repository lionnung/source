// OLE_MFCDlg.h : header file
//

#if !defined(AFX_OLE_MFCDLG_H__E686D73D_1227_470D_ABAF_A92CEEC8D462__INCLUDED_)
#define AFX_OLE_MFCDLG_H__E686D73D_1227_470D_ABAF_A92CEEC8D462__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

class COLE_MFCDlgAutoProxy;
class IAT91BootDLL;

/////////////////////////////////////////////////////////////////////////////
// COLE_MFCDlg dialog

class COLE_MFCDlg : public CDialog
{
	DECLARE_DYNAMIC(COLE_MFCDlg);
	friend class COLE_MFCDlgAutoProxy;

// Construction
public:
	COLE_MFCDlg(CWnd* pParent = NULL);	// standard constructor
	virtual ~COLE_MFCDlg();

	IAT91BootDLL *m_pAT91BootDLL;

// Dialog Data
	//{{AFX_DATA(COLE_MFCDlg)
	enum { IDD = IDD_OLE_MFC_DIALOG };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(COLE_MFCDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	COLE_MFCDlgAutoProxy* m_pAutoProxy;
	HICON m_hIcon;

	BOOL CanExit();

	// Generated message map functions
	//{{AFX_MSG(COLE_MFCDlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg void OnClose();
	virtual void OnOK();
	virtual void OnCancel();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_OLE_MFCDLG_H__E686D73D_1227_470D_ABAF_A92CEEC8D462__INCLUDED_)
