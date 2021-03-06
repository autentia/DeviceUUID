DeviceUUID
==========

This app display different 'unique device ID' using different methods.

In Android, there exist several 'Android IDs'.

GFS ID (Google Service Framework Android ID)
A 64-bit number (as a 16 digit hex string) that is randomly generated on the device's first GSF login and should remain constant for the lifetime of the device. (The value will change if a factory reset is performed on the device.)
- Ex: 4121147f8714e99b

android.os.Build.SERIAL
A hardware serial number, if available. Usually this value is 'null' or 'unknown'
- Ex: unknown

Settings.Secure.ANDROID_ID
A 64-bit number (as a 16 digit hex string) that is randomly generated on the device's first boot and should remain constant for the lifetime of the device. (The value may change if a factory reset is performed on the device.)
It is only available for Android >= 2.3
- Ex: 212ec38cg8d83ede

IMEI / MEID /ESN (for device whith Telefony and new device models)
A 64-bit number (as a 16 digit hex string), that represents the IMEI for GSM and the MEID or ESN for CDMA phones. This is null if device ID is not available, usually if the device is not a telephone.
- Ex: 202141497137588

UUID (Universal Unique Identifier Generated by this application)
A 8-4-4-4-12 digit hex string, that represents a Universal Unique IDentifier for the device.
- Ex: 00000000-361b-6678-40c0-ec6a0099c586
