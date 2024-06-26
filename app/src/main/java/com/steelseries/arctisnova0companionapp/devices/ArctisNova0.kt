package com.steelseries.arctisnova0companionapp.devices

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class ArctisNova0 {
    private val TAG = "ArctisNova0"

    private var isConnected = false
    private var micVolume = 0x0A

    companion object {
        val GET_FW_VERSION = 0x10
        val GET_MIC_VOLUME = 0x11
        val SET_MIC_VOLUME = 0x12
    }

    // This function initializes the connection to the device
    fun initializeConnection() {
        runBlocking {
            delay(2000)
            isConnected = true
        }
    }

    // This is the public function that is used to communicate with a device
    // It takes in a bytearray which needs to contain the command in which to call
    // which must be in the first byte of the array.  The rest of the array will be
    // parameters to the device command if applicable.
    fun deviceCommunication(commandBytes: ByteArray): ByteArray {
        // Check length of command
        if (commandBytes.isEmpty() || !isConnected) {
            return byteArrayOf()
        }

        // Get first byte, which is the command
        val command = commandBytes[0]

        return when (command.toInt()) {
            GET_FW_VERSION -> getFWVersion()
            GET_MIC_VOLUME -> getMicVolume()
            SET_MIC_VOLUME -> setMicVolume(commandBytes[1])
            else -> {
                byteArrayOf()
            }
        }
    }

    // Returns a byte array with the ASCII string for firmware version
    // Byte 0-11: Headset FW Version (ASCII)
    private fun getFWVersion(): ByteArray {
        Log.i(TAG, "Returning FW version for Arctis Nova 0")
        return byteArrayOf(
            0x30,
            0x30,
            0x30,
            0x30,
            0x30,
            0x30,
            0x30,
            0x30,
            0x30,
            0x2E,
            0x33,
            0x30
        )
    }

    // Gets the Mic volume
    // 0x00 - 0
    // 0x01 - 1
    // 0x02 - 2
    // 0x03 - 3
    // 0x04 - 4
    // 0x05 - 5
    // 0x06 - 6
    // 0x07 - 7
    // 0x08 - 8
    // 0x09 - 9
    // 0x0A - 10
    private fun getMicVolume(): ByteArray {
        Log.i(TAG, "Getting microphone volume on Arctis Nova 0: " + micVolume)
        return byteArrayOf(micVolume.toByte())
    }

    // Sets the Mic volume
    // 0x00 - 0
    // 0x01 - 1
    // 0x02 - 2
    // 0x03 - 3
    // 0x04 - 4
    // 0x05 - 5
    // 0x06 - 6
    // 0x07 - 7
    // 0x08 - 8
    // 0x09 - 9
    // 0x0A - 10
    private fun setMicVolume(volume: Byte): ByteArray {
        Log.i(TAG, "Setting microphone volume on Arctis Nova 0 to " + volume.toInt().toString())
        micVolume = volume.toInt()
        return byteArrayOf()
    }
}