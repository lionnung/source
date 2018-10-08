//
//  ViewController.m
//  MyFrameworkAppObjC
//
//  Created by Argox on 2018/4/12.
//  Copyright © 2018 Argox. All rights reserved.
//

#import "ViewController.h"
#import <MyFramework/MyFramework-Swift.h>

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    MyFramework* myFramework = [[MyFramework alloc] init];
    [myFramework hello];
    [myFramework hello_func];
    
    NSError* err = nil;
    [myFramework throw_error: &err];
    NSLog(@"Domain: %@, Code: %ld, Message: %@", err.domain, (long) err.code, err.localizedDescription);
    
    [myFramework throw_error2: &err];
    NSLog(@"Domain: %@, Code: %ld, Message: %@", err.domain, (long) err.code, err.localizedDescription);
    
    [myFramework throw_error3: @"invalid Parameter 3 *" error: &err];
    NSLog(@"Domain: %@, Code: %ld, Message: %@", err.domain, (long) err.code, err.localizedDescription);
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
