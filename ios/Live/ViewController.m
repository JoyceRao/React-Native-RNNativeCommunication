//
//  ViewController.m
//  RJ_RNCommunication
//
//  Created by Channel-Joyce on 2020/1/3.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import "ViewController.h"
#import "LiveEventEmitter.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)dissMissVS:(id)sender {
  
  NSDictionary *dict = [NSDictionary dictionaryWithObject:@"123456" forKey:@"message"];
  
  LiveEventEmitter *event = [[LiveEventEmitter alloc] init];
  [event sendEventWithName:@"EventName" body:dict];
  
  [self.presentingViewController dismissViewControllerAnimated:true completion:nil];
}
@end
