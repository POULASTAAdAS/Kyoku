import os
import binascii
import base64

fingerprint = "D6:13:E1:E5:06:A5:38:B8:32:9A:04:95:BF:1B:F9:55:42:71:A4:A6:4F:34:50:29:AA:CF:D1:1C:AC:91:6D:60"
print("android:apk-key-hash:  " + base64.urlsafe_b64encode(
    binascii.a2b_hex(fingerprint.replace(':', ''))
).decode('utf8').replace('=', ''))
