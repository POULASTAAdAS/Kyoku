import os
import binascii
import base64

fingerprint = os.environ["appFingerPirnt"]
print("android:apk-key-hash:  " + base64.urlsafe_b64encode(
    binascii.a2b_hex(fingerprint.replace(':', ''))
).decode('utf8').replace('=', ''))
