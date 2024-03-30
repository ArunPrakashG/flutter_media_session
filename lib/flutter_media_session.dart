import 'package:flutter/services.dart';

class FlutterMediaSession {
  static final FlutterMediaSession _singleton = FlutterMediaSession._internal();

  factory FlutterMediaSession() {
    return _singleton;
  }

  FlutterMediaSession._internal();

  static FlutterMediaSession get instance => _singleton;

  final MethodChannel _channel = const MethodChannel(
    'com.example.media_session/session',
  );

  Future<bool> initialize() async {
    if (await isReady()) {
      return true;
    }

    return await _channel.invokeMethod('initialize');
  }

  Future<bool> isReady() async {
    return await _channel.invokeMethod('isReady');
  }

  Future<bool> isMusicActive() async {
    return await _channel.invokeMethod('isMusicActive');
  }

  Future<bool> pause() async {
    return await _channel.invokeMethod('pausePlayback');
  }

  Future<bool> play() async {
    return await _channel.invokeMethod('resumePlayback');
  }
}
