//
//  SimpleBluetoothIO.swift
//  demoapp
//
//  Created by Argox on 27/07/2017.
//  Copyright © 2017 Argox. All rights reserved.
//

import Foundation
import ExternalAccessory

let queueName = "com.argox.mfi.bluetoothapp"

protocol SimpleBluetoothIO {
    func scanDevice()
    func getInfo() -> String
    func writeData(buff: [UInt8], len: Int)
    func getResponse() -> String
}

struct Info {
    var name: String
    var manufacturer: String
    var modelNumber: String
    var serialNumber: String
    var firmwareRevision: String
    var hardwareRevision: String
    var protocolStrings: [String]
    
    init() {
        name = ""
        manufacturer = ""
        modelNumber = ""
        serialNumber = ""
        firmwareRevision = ""
        hardwareRevision = ""
        protocolStrings = []
    }
}

class SimpleBluetoothIOMFi: NSObject, StreamDelegate, SimpleBluetoothIO {
    var manager: EAAccessoryManager?
    var accessory: EAAccessory?
    var session: EASession?
    var info = Info()
    var response: String = ""
    var writeBuffer = Array<Data>()
    
    override init() {
        super.init()
        
        manager = EAAccessoryManager.shared()
        manager?.registerForLocalNotifications()
        
        NotificationCenter.default.addObserver(self, selector: #selector(deviceConnected(_ :)), name: .EAAccessoryDidConnect, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(deviceDisconnected(_ :)), name: .EAAccessoryDidDisconnect, object: nil)
        
        if let accs = manager?.connectedAccessories {
            for acc in accs {
                print("Name: \(acc.name)")
                for proto in acc.protocolStrings {
                    print("Protocol: \(proto)")
                }
                NotificationCenter.default.post(name: .UpdateLabel, object: nil, userInfo: ["name": "CON"])
                
                setInfo(acc: acc)
                accessory = acc
                open()
                break // get first one, just for demo
            }
        }
    }

    func scanDevice() {
        manager?.showBluetoothAccessoryPicker(withNameFilter: nil, completion: nil)
    }
    
    func setInfo(acc: EAAccessory) {
        info.name = acc.name
        info.manufacturer = acc.manufacturer
        info.modelNumber = acc.modelNumber
        info.serialNumber = acc.serialNumber
        info.firmwareRevision = acc.firmwareRevision
        info.hardwareRevision = acc.hardwareRevision
        info.protocolStrings = acc.protocolStrings
    }
    
    func getInfo() -> String {
        var protocolString: String = ""
        
        for proto in info.protocolStrings {
            protocolString += "Protocol: " + proto + "\r\n"
        }

        return "Name: " + info.name + "\r\n" +
               "Manufacturer: " + info.manufacturer + "\r\n" +
               "Model Number: " + info.modelNumber + "\r\n" +
               "Serial Number: " + info.serialNumber + "\r\n" +
               "Firmware Revision: " + info.firmwareRevision + "\r\n" +
               "Hardware Revision: " + info.hardwareRevision + "\r\n" +
               protocolString + "\r\n"
    }
    
    private func open() {
        session = EASession(accessory: accessory!, forProtocol: "com.argox.datapath")
        session?.outputStream?.delegate = self
        session?.outputStream?.schedule(in: RunLoop.main, forMode: RunLoopMode.commonModes)
        session?.outputStream?.open()
        session?.inputStream?.delegate = self
        session?.inputStream?.schedule(in: RunLoop.main, forMode: RunLoopMode.commonModes)
        session?.inputStream?.open()
    }
    
    private func close() {
        session?.outputStream?.close()
        session?.outputStream?.remove(from: RunLoop.main, forMode: RunLoopMode.defaultRunLoopMode)
        session?.outputStream?.delegate = nil
        session?.inputStream?.close()
        session?.inputStream?.remove(from: RunLoop.main, forMode: RunLoopMode.defaultRunLoopMode)
        session?.inputStream?.delegate = nil
    }
    
    func writeData() {
        if writeBuffer.count < 1 {
            response = "Finished\r\n"
            NotificationCenter.default.post(name: .UpdateLabel, object: nil, userInfo: ["name": "RESP"])
            return
        }
        
        if session?.outputStream?.hasSpaceAvailable == false {
            return
        }
        
        var data = writeBuffer.remove(at: 0)
        let buff = Array(data)
        
        if let bytesWritten = session?.outputStream?.write(buff, maxLength: buff.count) {
            if bytesWritten != buff.count {
                data = data.advanced(by: bytesWritten)
                writeBuffer.insert(data, at: 0)
            }
        }
    }
    
    func writeData(buff: [UInt8], len: Int) {
        let data = Data(bytes: buff, count: len)
        writeBuffer.append(data)
        writeData()
    }
    
    func getResponse() -> String {
        return response + "\r\n"
    }
    
    func stream(_ aStream: Stream, handle eventCode: Stream.Event) {
        switch (eventCode) {
        case Stream.Event.errorOccurred:
            print("errorOccurred")
            
        case Stream.Event.endEncountered:
            print("endEncountered")
            
        case Stream.Event.hasBytesAvailable:
            print("hasBytesAvailable")
            
            var buff = [UInt8](repeating: 0, count: 4096)
            let inputStream = aStream as? InputStream
            
            response = ""
            while inputStream?.hasBytesAvailable != false {
                let len = inputStream!.read(&buff, maxLength: buff.count)
                if len > 0 {
                    if let output = NSString(bytes: &buff, length: len, encoding: String.Encoding.nonLossyASCII.rawValue) {
                        print(output)
                        response += output as String
                        NotificationCenter.default.post(name: .UpdateLabel, object: nil, userInfo: ["name": "RESP"])
                    }
                }
            }
            
        case Stream.Event.openCompleted:
            print("openCompleted")
            
        case Stream.Event.hasSpaceAvailable:
            print("hasSpaceAvailable")
            writeData()
            
        default:
            print("Unknow stream event")
        }
    }
    
    @objc func deviceConnected(_ notification: NSNotification) {
        print("deviceConnected")
        NotificationCenter.default.post(name: .UpdateLabel, object: nil, userInfo: ["name": "CON"])
        
        if let acc = notification.userInfo?[EAAccessoryKey] as? EAAccessory {
            setInfo(acc: acc)
            accessory = acc
            open()
        }
    }
    
    @objc func deviceDisconnected(_ notification: NSNotification) {
        print("deviceDisconnected")
        NotificationCenter.default.post(name: .UpdateLabel, object: nil, userInfo: ["name": "DISCON"])
        
        close()
        session = nil
        accessory = nil
        info = Info()
    }
}
