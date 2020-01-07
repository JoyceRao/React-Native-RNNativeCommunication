//
//  LiveEventEmitter.m
//  RJ_RNCommunication
//
//  Created by Channel-Joyce on 2020/1/3.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import "LiveEventEmitter.h"

@interface LiveEventEmitter()

@property(nonatomic, strong)NSArray * nameArrays;
@end

@implementation LiveEventEmitter
RCT_EXPORT_MODULE();

/// 重写方法，添加事件名
-(NSArray<NSString *> *)supportedEvents {
  return self.nameArrays;
}
//重写方法，单例获取bridge。
+ (id)allocWithZone:(struct _NSZone *)zone {
  static LiveEventEmitter *live = nil;
  static dispatch_once_t onceToken;
  dispatch_once(&onceToken, ^{
    live = [super allocWithZone:zone];
  });
  
  return live;
}

///发送事件给js
- (void)sendMessageToRN: (NSDictionary *)dict eventName:(NSString *)name {
  [self sendEventWithName:name body:dict];
}

- (NSArray *)nameArrays {
  if (_nameArrays == nil) {
    _nameArrays = @[@"EventName"];
  }
  return _nameArrays;
}

@end
