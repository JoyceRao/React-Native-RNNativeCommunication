- **一、IOS通信——直接调用，Callback，Promise**
     你可以在JavaScript通过NativeModules调用原生组件，原生方法，通过NativeEventEmitter进行添加事件。
     在ReactNative中，一个原生模块就是通过“RCTBridgeModule”协议的 Objective-C 类，为了实现RCTBridgeModule协议，你的类需要包含RCT_EXPORT_MODULE()宏，这个宏也可以添加一个参数用来指定在 JavaScript 中访问这个模块的名字。如果你不指定，默认就会使用这个 Objective-C 类的名字。
    声明通过RCT_EXPORT_METHOD()宏来实现要给JavaScript导出的方法。

`RCT_EXPORT_METHOD` 支持所有标准 JSON 类型，包括：

*   string (`NSString`)
*   number (`NSInteger`, `float`, `double`, `CGFloat`, `NSNumber`)
*   boolean (`BOOL`, `NSNumber`)
*   array (`NSArray`) 可包含本列表中任意类型
*   object (`NSDictionary`) 可包含 string 类型的键和本列表中任意类型的值
*   function (`RCTResponseSenderBlock`)

除此以外，任何`RCTConvert`类支持的的类型也都可以使用(参见[`RCTConvert`](https://github.com/facebook/react-native/blob/master/React/Base/RCTConvert.h)了解更多信息)。`RCTConvert`还提供了一系列辅助函数，用来接收一个 JSON 值并转换到原生 Objective-C 类型或类。

**下面看代码：**
原生实现：
xxx.h
```
#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
///添加RCTBridgeModule协议
@interface LiveModule : NSObject<RCTBridgeModule>

@end
```
xxx.m   ——实现导出给JavaScript的方法。包含Callback和Promise回调
```
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
```
**JavaScript通过NativeModules调用原生方法**

```
const liveModule = NativeModules.LiveModule;
///直接调用
liveModule.pushLiveViewController('xxx');

//callback方式
liveModule.findEvents((error, value) => {
      this.setState({
        callText: value,
      });
    });

//promise方式
liveModule
      .liveValue(true)
      .then(value => {
        this.setState({
          promiseText: value,
        });
      })
      .catch(error => {
        console.log(error);
      });
```

**原生给JavaScript发送事件**
最好的方法是继承RCTEventEmitter，实现suppportEvents方法并调用self sendEventWithName:。
```
@interface LiveEventEmitter : RCTEventEmitter <RCTBridgeModule>
///给js端发送事件
- (void)sendMessageToRN: (NSDictionary *)dict eventName:(NSString *)name;
@end
```

```
@interface LiveEventEmitter()

@property(nonatomic, strong)NSArray * nameArrays;
@end

@implementation LiveEventEmitter
RCT_EXPORT_MODULE();

/// 重写方法，添加事件名
-(NSArray<NSString *> *)supportedEvents {
  return self.nameArrays;
}
//重写方法，单例获取bridge。如果不重写bridge为nil
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

```
**JavaScript订阅事件**
```
//在componentDidMount()中订阅事件
const eventEmitter = new NativeEventEmitter(NativeModules.LiveEventEmitter);
      this.listener = eventEmitter.addListener('EventName', value => {
        this.setState({
          touchText: value.message,
        });
      });
```

- **一、Android通信——直接调用，Callback，Promise**

**JavaScript通信原生的模块**   
 一个原生模块是一个继承了ReactContextBaseJavaModule的 Java 类，它可以实现一些 JavaScript 所需的功能。ReactContextBaseJavaModule要求派生类实现getName方法。这个函数用于返回一个字符串名字，这个名字在 JavaScript 端标记这个模块。一个可选的方法getContants返回了需要导出给 JavaScript 使用的常量。
    要导出一个方法给 JavaScript 使用，Java 方法需要使用注解@ReactMethod，方法的返回类型必须为void。React Native 的跨语言访问是异步进行的。
```
public class LiveModule extends ReactContextBaseJavaModule {

/**
     *原生通过回调callback的形式返回值
     * @param callback
     * @return
     */
    @ReactMethod
    public void findEvents(Callback successCallback) {
        successCallback.invoke("1", CALL_VALUE);
    }

    /**
     *promise的形式回调
     * @param promise
     * @return
     */
    @ReactMethod
    public void liveValue(boolean isOne, Promise promise) {
        if (isOne == true) {
            promise.resolve(PROMISE_VALUE);
        }else {
            promise.reject(ERROR, new Exception());
        }
    }

    @ReactMethod
    public void pushLiveViewController(String name) {
        Intent intent = new Intent(getCurrentActivity(), OtherActivity.class);
        Objects.requireNonNull(getCurrentActivity()).startActivity(intent);

    }

@NonNull
    @Override
    public String getName() {
        return "LiveModule";
    }

    /**
     *optional
     * @return a map of constants this module exports to JS. Supports JSON types.
     */
    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        return super.getConstants();
    }
}
```
**参数类型**
Boolean -> Bool
Integer -> Number
Double -> Number
Float -> Number
String -> String
Callback -> function
ReadableMap -> Object
ReadableArray -> Array

**注册模块**
在 Java 这边要做的最后一件事就是注册这个模块。我们需要在应用的 Package 类的createNativeModules方法中添加这个模块。如果模块没有被注册，它也无法在 JavaScript 中被访问到。
```
public class LivePackage implements ReactPackage {
/**
     * @param reactContext react application context that can be used to create modules
     * @return list of native modules to register with the newly created catalyst instance
     */
    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        List<NativeModule> nativeModules = new ArrayList<>();
        this.liveModule = new LiveModule(reactContext);
        //注册这个模块
        nativeModules.add(liveModule);
        return nativeModules;
    }

    /**
     * @param reactContext
     * @return a list of view managers that should be registered with {@link UIManagerModule}
     */
    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
```
**给JavaScript发送事件**
原生模块可以在没有被调用的情况下往 JavaScript 发送事件通知。最简单的办法就是通过RCTDeviceEventEmitter。
 ```
//发送事件给JavaScript, 注意：javaScript支持的map类型，推荐使用WritableMap
public void sendEvent(String eventName,
                           @Nullable WritableMap params) {

        this.reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
    }
```
**JavaScript端与IOS同样调用**