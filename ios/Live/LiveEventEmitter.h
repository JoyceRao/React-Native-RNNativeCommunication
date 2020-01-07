//
//  LiveEventEmitter.h
//  RJ_RNCommunication
//
//  Created by Channel-Joyce on 2020/1/3.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import <React/RCTEventEmitter.h>



@interface LiveEventEmitter : RCTEventEmitter <RCTBridgeModule>
///给js端发送事件
- (void)sendMessageToRN: (NSDictionary *)dict eventName:(NSString *)name;
@end


