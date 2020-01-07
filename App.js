/* eslint-disable react-native/no-inline-styles */
/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  Button,
  NativeModules,
  NativeEventEmitter,
  Platform,
} from 'react-native';

import {
  Header,
  LearnMoreLinks,
  Colors,
  DebugInstructions,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

const liveModule = NativeModules.LiveModule;
class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      touchText: 'touch',
      callText: 'callback',
      promiseText: 'promise',
    };
  }

  componentDidMount() {
    if (Platform.OS === 'ios') {
      // eslint-disable-next-line prettier/prettier
      const eventEmitter = new NativeEventEmitter(NativeModules.LiveEventEmitter);
      this.listener = eventEmitter.addListener('EventName', value => {
        this.setState({
          touchText: value.message,
        });
      });
    } else {
      const eventEmitter = new NativeEventEmitter(NativeModules.LiveModule);
      this.listener = eventEmitter.addListener('EventName', value => {
        this.setState({
          touchText: value.message,
        });
      });
    }
  }

  componentWillUnmount() {
    this.listener && this.listener.remove();
  }

  _clickCallBackTouch() {
    liveModule.findEvents((error, value) => {
      this.setState({
        callText: value,
      });
    });
  }

  _clickTouch() {
    liveModule.pushLiveViewController('xxx');
  }

  _clickPromiseTouch() {
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
  }

  _buttonView(name, text, click) {
    return (
      <View
        style={{
          flexDirection: 'row',
          padding: 15,
          justifyContent: 'center',
          alignItems: 'center',
          backgroundColor: 'gray',
          marginBottom: 20,
        }}>
        <Text>{text}</Text>
        <Button title={name} onPress={click} />
      </View>
    );
  }

  render() {
    return (
      <>
        <StatusBar barStyle="dark-content" />
        <SafeAreaView>
          <ScrollView
            contentInsetAdjustmentBehavior="automatic"
            style={styles.scrollView}>
            {this._buttonView('callbackButton', this.state.callText, () =>
              this._clickCallBackTouch(),
            )}
            {this._buttonView('promiseButton', this.state.promiseText, () =>
              this._clickPromiseTouch(),
            )}
            {this._buttonView('presentButton', this.state.touchText, () =>
              this._clickTouch(),
            )}
          </ScrollView>
        </SafeAreaView>
      </>
    );
  }
}

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: Colors.lighter,
  },
  engine: {
    position: 'absolute',
    right: 0,
  },
  body: {
    backgroundColor: Colors.white,
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: Colors.black,
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
    color: Colors.dark,
  },
  highlight: {
    fontWeight: '700',
  },
  footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: '600',
    padding: 4,
    paddingRight: 12,
    textAlign: 'right',
  },
});

export default App;
