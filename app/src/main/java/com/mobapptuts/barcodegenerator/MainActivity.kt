package com.mobapptuts.barcodegenerator

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlForm.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                val focusedView = currentFocus
                if (focusedView != null) {
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE)
                    if (inputMethodManager is InputMethodManager) {
                        inputMethodManager.hideSoftInputFromWindow(focusedView.windowToken, 0)
                    }
                }
                val text = textView.text.toString()
                val bitMatrix = createBitmatrix(text)
                val pixels = setBitmapPixels(bitMatrix)
                val bitmap = encodeBitmap(pixels, bitMatrix.width, bitMatrix.height)
                barcodeImageView.setImageBitmap(bitmap)
            }
            true
        }
    }

    private fun createBitmatrix(urlString: String) =
            MultiFormatWriter().encode(urlString, BarcodeFormat.QR_CODE, barcodeImageView.width, barcodeImageView.height)

    private fun setBitmapPixels(bitMatrix: BitMatrix): IntArray {
        val pixels = IntArray(bitMatrix.width * bitMatrix.height)

        for (y in 0 until bitMatrix.height) {
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width)
                pixels[offset + x] = if (bitMatrix.get(x , y)) Color.BLACK else Color.WHITE
        }
        return pixels
    }

    private fun encodeBitmap(pixels: IntArray, width: Int, height: Int) : Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }
}
