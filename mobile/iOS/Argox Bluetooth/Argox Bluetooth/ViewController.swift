//
//  ViewController.swift
//  demoapp
//
//  Created by Argox on 27/07/2017.
//  Copyright © 2017 Argox. All rights reserved.
//

import UIKit

extension NSNotification.Name {
    static let UpdateLabel = NSNotification.Name(rawValue: "UpdateLabelNotification")
}

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    var deviceBT: SimpleBluetoothIO?
    var info = [["Logo", "Barcode"],
    //var info = [["Logo.prn", "mm.prn", "SZPL.prn", "SBPL.prn", "Purchase.prn"],
                ["Block"]]
    var idxPath: Int = 0
    var idxRow: Int = 0
    let screenSize: CGRect = UIScreen.main.bounds

    @IBOutlet weak var lblStatus: UILabel!
    @IBOutlet weak var tvCMD: UITextView!
    @IBOutlet weak var tvInfo: UITextView!
    @IBOutlet weak var btnScan: UIButton!
    @IBOutlet weak var btnClear: UIButton!
    @IBOutlet weak var btnInfo: UIButton!
    @IBOutlet weak var btnSendCMD: UIButton!
    @IBOutlet weak var btnSendFile: UIButton!
    
    var altController: UIAlertController!
    var tbvFile: UITableView!
    
    @IBAction func btnScan(_ sender: Any) {
        deviceBT?.scanDevice()
    }
    
    @IBAction func btnClear(_ sender: Any) {
        tvInfo.text = ""
    }
    
    @IBAction func btnInfo(_ sender: Any) {
        if let info = deviceBT?.getInfo() {
            tvInfo.text = info + tvInfo.text
        }
    }
    
    @IBAction func btnSendCMD(_ sender: Any) {
        var buff = [UInt8](tvCMD.text.data(using: .nonLossyASCII)!)
        buff = buff.map {$0 == 0x24 ? 0x1B : $0}
        deviceBT?.writeData(buff: buff, len: buff.count)
    }
    
    @IBAction func btnSendFile(_ sender: Any) {
        altController = UIAlertController(title: "Select File\n\n\n\n\n\n\n\n", message: nil, preferredStyle: .alert)
        let canlAction = UIAlertAction(title: "Cancel", style: .default, handler: {(action: UIAlertAction!) -> Void in print("Press Cancel")})
        altController.addAction(canlAction)

        // Fix me
        var len: CGFloat
        switch screenSize.width {
        case 0...320:
            len = 50
            
        case 321...375:
            len = 105
            
        case 376...414:
            len = 144
            
        case 415...768:
            len = 498
            
        case 769...834:
            len = 564
            
        case 835...1024:
            len = 754
            
        default:
            len = 144
        }
        
        tbvFile = UITableView(frame: CGRect(x: 0, y: 55, width: screenSize.width - len, height: 181), style: .grouped)
        tbvFile.register(UITableViewCell.self, forCellReuseIdentifier: "Cell")
        tbvFile.delegate = self
        tbvFile.dataSource = self
        tbvFile.separatorStyle = .singleLine
        tbvFile.separatorInset = UIEdgeInsetsMake(0, 20, 0, 20)
        tbvFile.allowsSelection = true
        tbvFile.allowsMultipleSelection = false
        
        altController.view.addSubview(tbvFile)
        self.present(altController, animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        shadowButton(btnScan)
        shadowButton(btnClear)
        shadowButton(btnInfo)
        shadowButton(btnSendCMD)
        shadowButton(btnSendFile)
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(hideKeyboard(_ :)))
        tap.cancelsTouchesInView = false
        self.view.addGestureRecognizer(tap)
        
        NotificationCenter.default.addObserver(self, selector: #selector(updateLabel(_ :)), name: .UpdateLabel, object: nil)
        deviceBT = SimpleBluetoothIOMFi()
        //NotificationCenter.default.post(name: .UpdateLabel, object: nil, userInfo: ["name": "FILE"])
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func shadowButton(_ button: UIButton) {
        button.layer.shadowColor = UIColor.black.cgColor
        button.layer.shadowOffset = CGSize(width: 5, height: 5)
        button.layer.shadowRadius = 5
        button.layer.shadowOpacity = 1.0
    }
    
    @objc func hideKeyboard(_ tap: UITapGestureRecognizer) {
        self.tvCMD.resignFirstResponder()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return info[section].count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tbvFile.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as UITableViewCell
        
        if let myLabel = cell.textLabel {
            myLabel.text = "\(info[indexPath.section][indexPath.row])"
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        idxPath = indexPath.section
        idxRow = indexPath.row
        print("At: \(info[indexPath.section][indexPath.row])")
        altController.dismiss(animated: true, completion: nil)
        NotificationCenter.default.post(name: .UpdateLabel, object: nil, userInfo: ["name": "FILE"])
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        let title = section == 0 ? "ZPL" : "EPL"
        return title
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return info.count
    }
    
    @objc func updateLabel(_ notification: NSNotification) {
        if let userInfo = notification.userInfo?["name"] as? String {
            switch userInfo {
            case "CON":
                lblStatus.text = "Connected"
                lblStatus.textColor = .blue
                
            case "DISCON":
                lblStatus.text = "Disconnected"
                lblStatus.textColor = .red
                
            case "RESP":
                if let resp = deviceBT?.getResponse() {
                    tvInfo.text = resp + tvInfo.text
                }
                
            case "FILE":
                if lblStatus.text != "Connected" {
                    return
                }
                
                guard let fileURL = Bundle.main.url(forResource: info[idxPath][idxRow], withExtension: nil) else {
                    return
                }
                
                /*do {
                    let data = try String(contentsOf: fileURL, encoding: .nonLossyASCII)
                    //print(data)
                    deviceBT?.write(data)
                    tvInfo.text = "\(data.lengthOfBytes(using: .nonLossyASCII)) bytes written\r\n\r\n" + tvInfo.text
                }
                catch {
                    print("Open Fail")
                }*/
                
                if let stream = InputStream(url: fileURL) {
                    var buff = [UInt8](repeating: 0, count: 8192)
                    var count = 0
                    
                    stream.open()
                    while true {
                        let len = stream.read(&buff, maxLength: buff.count)
                        deviceBT?.writeData(buff: buff, len: len)
                        count += len
                        if len < buff.count {
                            break
                        }
                    }
                    stream.close()
                    tvInfo.text = "\(count) bytes written\r\n\r\n" + tvInfo.text
                }
                
            default:
                break
            }
        }
    }

}
