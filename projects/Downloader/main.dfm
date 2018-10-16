object Form1: TForm1
  Left = 0
  Top = 0
  BorderIcons = [biSystemMenu, biMinimize]
  BorderStyle = bsSingle
  Caption = 'Downloader'
  ClientHeight = 180
  ClientWidth = 480
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  OnClose = FormClose
  PixelsPerInch = 96
  TextHeight = 13
  object Edit1: TEdit
    Left = 8
    Top = 8
    Width = 381
    Height = 21
    TabOrder = 0
  end
  object Button1: TButton
    Left = 395
    Top = 6
    Width = 75
    Height = 25
    Caption = 'File...'
    TabOrder = 1
    OnClick = Button1Click
  end
  object GroupBox1: TGroupBox
    Left = 10
    Top = 35
    Width = 462
    Height = 62
    Caption = 'Port Setting'
    TabOrder = 2
    object RadioButton3: TRadioButton
      Left = 16
      Top = 24
      Width = 113
      Height = 17
      Caption = 'USB'
      Checked = True
      TabOrder = 0
      TabStop = True
    end
    object Edit4: TEdit
      Left = 64
      Top = 22
      Width = 121
      Height = 21
      TabOrder = 1
      Text = '1'
    end
  end
  object Button2: TButton
    Left = 397
    Top = 114
    Width = 75
    Height = 59
    Caption = 'START'
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clBlack
    Font.Height = -11
    Font.Name = 'Tahoma'
    Font.Style = []
    ParentFont = False
    TabOrder = 3
    OnClick = Button2Click
  end
  object OpenDialog1: TOpenDialog
    Left = 32
    Top = 120
  end
end
