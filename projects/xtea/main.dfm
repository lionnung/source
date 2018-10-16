object frmXTEA: TfrmXTEA
  Left = 0
  Top = 0
  Caption = 'xtea'
  ClientHeight = 206
  ClientWidth = 339
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  OnClose = FormClose
  OnCreate = FormCreate
  PixelsPerInch = 96
  TextHeight = 13
  object btnSetKey: TButton
    Left = 8
    Top = 6
    Width = 75
    Height = 25
    Caption = 'Set Key'
    TabOrder = 0
    OnClick = btnSetKeyClick
  end
  object edtKey: TEdit
    Left = 89
    Top = 8
    Width = 242
    Height = 21
    TabOrder = 1
  end
  object btnQry: TButton
    Left = 8
    Top = 39
    Width = 75
    Height = 25
    Caption = 'Query'
    TabOrder = 2
    OnClick = btnQryClick
  end
  object edtQry: TEdit
    Left = 89
    Top = 41
    Width = 242
    Height = 21
    TabOrder = 3
  end
  object btnFix: TButton
    Left = 8
    Top = 175
    Width = 75
    Height = 25
    Caption = 'Key'
    TabOrder = 5
    OnClick = btnFixClick
  end
  object edtK: TEdit
    Left = 89
    Top = 177
    Width = 242
    Height = 21
    TabOrder = 6
  end
  object memResult: TMemo
    Left = 8
    Top = 70
    Width = 323
    Height = 97
    TabOrder = 4
  end
end
