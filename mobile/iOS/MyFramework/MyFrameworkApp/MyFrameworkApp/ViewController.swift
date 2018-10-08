//
//  ViewController.swift
//  MyFrameworkApp
//
//  Created by Argox on 2018/4/12.
//  Copyright © 2018 Argox. All rights reserved.
//

import UIKit
import MyFramework

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        MyFramework.shared.hello()
        MyFramework.shared.hello_func()
        
        do {
            try MyFramework.shared.throw_error()
        //} catch let e as ErrorKind {
        //    print("Error: \(e)")
        } catch ErrorKind.invalidParameter1 {
            print("ErrorKind.invalidParameter1")
        } catch {
            print("Other error")
        }
        
        do {
            try MyFramework.shared.throw_error2()
        //} catch let err as NSError where err.code == ErrorKind.invalidParameter2.rawValue {
        //    print("ErrorKind.invalidParameter2")
        } catch let err as NSError {
            print("Domain: \(err.domain), Code: \(err.code), Message: \(err.localizedDescription)")
        }
        
        do {
            try MyFramework.shared.throw_error3("invalid Parameter 3 *")
        } catch let err as NSError {
            print("Domain: \(err.domain), Code: \(err.code), Message: \(err.localizedDescription)")
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}
