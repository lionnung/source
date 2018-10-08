//
//  MyFramework.swift
//  MyFramework
//
//  Created by Argox on 2018/4/12.
//  Copyright © 2018 Argox. All rights reserved.
//

public let errorDomain = "Exception"

public extension NSError {
    public class func make(message: String = "Error", code: Int = ErrorKind.invalidParameter0.rawValue) -> NSError {
        return NSError(
            domain: errorDomain,
            code: code,
            userInfo: [NSLocalizedDescriptionKey: message]
        )
    }
    
    public class func invalidParameter2() -> NSError {
        return make(message: "Invalid Parameter 2", code: ErrorKind.invalidParameter2.rawValue)
    }
    
    public class func invalidParameter3(_ strName: String) -> NSError {
        return make(message: strName, code: ErrorKind.invalidParameter3.rawValue)
    }
}

@objc
public enum ErrorKind: Int, Error {
    case invalidParameter0
    case invalidParameter1
    case invalidParameter2
    case invalidParameter3
}

@objc
open class MyFramework: NSObject {
    public static let shared = MyFramework()
    
    @objc
    open func hello() {
        print("Hello from MyFramework!!!")
    }

    @objc
    open func hello_func() {
        my_hello_func()
    }
    
    @objc(throw_error:)
    open func throw_error() throws {
        throw ErrorKind.invalidParameter1
    }
    
    @objc(throw_error2:)
    open func throw_error2() throws {
        throw NSError.invalidParameter2()
    }

    @objc
    open func throw_error3(_ strName: String) throws {
        throw NSError.invalidParameter3(strName)
    }
}

func my_hello_func() {
    print("Hello function from MyFramework!!!")
}
