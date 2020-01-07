//
//  LiveModule.m
//  RJ_RNCommunication
//
//  Created by Channel-Joyce on 2020/1/3.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import "LiveModule.h"
#import "ViewController.h"
#import "AppDelegate.h"

static const NSString *PROMISE_VALUE = @"小明同学";
static const NSString *CALL_VALUE = @"小王同学";
static const NSString *ERROR = @"错误：非1";

@interface LiveModule()
@end

@implementation LiveModule

RCT_EXPORT_MODULE();

///直接调用原生方法
RCT_EXPORT_METHOD(pushLiveViewController: (NSString *)str) {
  ///rn和原生桥接是异步的，所以需要对UI操作必须在主线程。
  dispatch_async(dispatch_get_main_queue(), ^{
    UINavigationController *nav = (UINavigationController *)AppDelegate.shareAppDelegate.window.rootViewController;
    ViewController *vc = [[ViewController alloc] init];
    [nav presentViewController:vc animated:false completion:nil];
  });
}

///callback回调
RCT_EXPORT_METHOD(findEvents: (RCTResponseSenderBlock)callback) {
  callback(@[[NSNull null], CALL_VALUE]);
}

///promise回调
RCT_EXPORT_METHOD(liveValue: (BOOL)valueI
                  findEventsWithResolver: (RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  
  if (valueI == YES) {
    resolve(PROMISE_VALUE);
  }else {
    NSError *error = nil;
    reject(@"error", @"This is error!", error);
  }
}



@end
